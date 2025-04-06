<template>
  <div class="message-center">
    <el-card class="message-card">
      <template #header>
        <div class="card-header">
          <span>通知中心</span>
          <div class="header-actions">
            <el-button type="primary" size="small" @click="markAllAsRead">全部已读</el-button>
            <el-button type="danger" size="small" @click="clearAllMessages">清空通知</el-button>
          </div>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="全部消息" name="all">
          <message-list :messages="allMessages" @read="markAsRead" @delete="deleteMessage" />
        </el-tab-pane>
        <el-tab-pane label="预警通知" name="warning">
          <message-list :messages="warningMessages" @read="markAsRead" @delete="deleteMessage" />
        </el-tab-pane>
        <el-tab-pane label="任务提醒" name="task">
          <message-list :messages="taskMessages" @read="markAsRead" @delete="deleteMessage" />
        </el-tab-pane>
      </el-tabs>
      
      <div class="pagination-container" v-if="totalMessages > 0">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="totalMessages"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        />
      </div>
      
      <el-empty v-if="currentMessages.length === 0" description="暂无消息" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import MessageList from './components/MessageList.vue'

// 消息类型
const MESSAGE_TYPES = {
  WARNING: 'warning', // 预警通知
  TASK: 'task' // 任务提醒
}

// 预警子类型
const WARNING_SUBTYPES = {
  PROBE_OFFLINE: 'probe_offline', // 探针离线
  EVIDENCE_STALLED: 'evidence_stalled', // 证据采集停滞
  HIGH_RISK: 'high_risk' // 高风险行为检测
}

// 任务子类型
const TASK_SUBTYPES = {
  REPORT_COMPLETED: 'report_completed', // 报告生成完成
  TASK_ASSIGNED: 'task_assigned' // 协同任务分配
}

// 状态管理
const activeTab = ref('all')
const messages = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const websocket = ref(null)

// 计算属性
const allMessages = computed(() => {
  return messages.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
})

const warningMessages = computed(() => {
  return messages.value
    .filter(msg => msg.type === MESSAGE_TYPES.WARNING)
    .slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
})

const taskMessages = computed(() => {
  return messages.value
    .filter(msg => msg.type === MESSAGE_TYPES.TASK)
    .slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
})

const currentMessages = computed(() => {
  switch (activeTab.value) {
    case 'warning':
      return warningMessages.value
    case 'task':
      return taskMessages.value
    default:
      return allMessages.value
  }
})

const totalMessages = computed(() => {
  switch (activeTab.value) {
    case 'warning':
      return messages.value.filter(msg => msg.type === MESSAGE_TYPES.WARNING).length
    case 'task':
      return messages.value.filter(msg => msg.type === MESSAGE_TYPES.TASK).length
    default:
      return messages.value.length
  }
})

// 方法
const fetchMessages = async () => {
  try {
    // 这里替换为实际的API调用
    // const response = await api.getMessages()
    // messages.value = response.data
    
    // 模拟数据
    messages.value = [
      {
        id: 1,
        type: MESSAGE_TYPES.WARNING,
        subtype: WARNING_SUBTYPES.PROBE_OFFLINE,
        title: '探针离线预警',
        content: '探针ID: PRB001已离线超过30分钟，请检查设备状态',
        timestamp: new Date().getTime() - 3600000,
        read: false
      },
      {
        id: 2,
        type: MESSAGE_TYPES.WARNING,
        subtype: WARNING_SUBTYPES.EVIDENCE_STALLED,
        title: '证据采集停滞预警',
        content: '案件A12345的证据采集任务已停滞2小时，请及时处理',
        timestamp: new Date().getTime() - 7200000,
        read: false
      },
      {
        id: 3,
        type: MESSAGE_TYPES.WARNING,
        subtype: WARNING_SUBTYPES.HIGH_RISK,
        title: '高风险行为检测',
        content: '系统检测到用户user123执行了敏感文件批量删除操作',
        timestamp: new Date().getTime() - 1800000,
        read: true
      },
      {
        id: 4,
        type: MESSAGE_TYPES.TASK,
        subtype: TASK_SUBTYPES.REPORT_COMPLETED,
        title: '报告生成完成',
        content: '案件B78901的分析报告已生成完成，请查看',
        timestamp: new Date().getTime() - 900000,
        read: false
      },
      {
        id: 5,
        type: MESSAGE_TYPES.TASK,
        subtype: TASK_SUBTYPES.TASK_ASSIGNED,
        title: '协同任务分配',
        content: '您有一个新的协同分析任务，请尽快处理',
        timestamp: new Date().getTime() - 300000,
        read: false
      }
    ]
  } catch (error) {
    console.error('获取消息失败:', error)
    ElMessage.error('获取消息失败，请稍后重试')
  }
}

const initWebSocket = () => {
  // 实际开发中替换为真实的WebSocket服务地址
  const wsUrl = 'ws://your-websocket-server/messages'
  
  websocket.value = new WebSocket(wsUrl)
  
  websocket.value.onopen = () => {
    console.log('WebSocket连接已建立')
  }
  
  websocket.value.onmessage = (event) => {
    const newMessage = JSON.parse(event.data)
    handleNewMessage(newMessage)
  }
  
  websocket.value.onerror = (error) => {
    console.error('WebSocket错误:', error)
  }
  
  websocket.value.onclose = () => {
    console.log('WebSocket连接已关闭')
    // 可以在这里添加重连逻辑
  }
}

const handleNewMessage = (newMessage) => {
  messages.value.unshift(newMessage)
  
  // 根据消息类型显示不同的通知
  let type = 'info'
  if (newMessage.type === MESSAGE_TYPES.WARNING) {
    type = 'warning'
  } else if (newMessage.type === MESSAGE_TYPES.TASK) {
    type = 'success'
  }
  
  ElMessage({
    type,
    message: newMessage.title,
    duration: 5000,
    showClose: true
  })
}

const markAsRead = (messageId) => {
  const index = messages.value.findIndex(msg => msg.id === messageId)
  if (index !== -1) {
    messages.value[index].read = true
    
    // 实际开发中应该调用API更新消息状态
    // api.markMessageAsRead(messageId)
  }
}

const markAllAsRead = () => {
  ElMessageBox.confirm('确定将所有消息标记为已读？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    messages.value.forEach(msg => {
      msg.read = true
    })
    
    // 实际开发中应该调用API更新所有消息状态
    // api.markAllMessagesAsRead()
    
    ElMessage.success('所有消息已标记为已读')
  }).catch(() => {})
}

const deleteMessage = (messageId) => {
  const index = messages.value.findIndex(msg => msg.id === messageId)
  if (index !== -1) {
    messages.value.splice(index, 1)
    
    // 实际开发中应该调用API删除消息
    // api.deleteMessage(messageId)
  }
}

const clearAllMessages = () => {
  ElMessageBox.confirm('确定清空所有通知？此操作不可恢复', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    messages.value = []
    
    // 实际开发中应该调用API清空消息
    // api.clearAllMessages()
    
    ElMessage.success('所有通知已清空')
  }).catch(() => {})
}

const handlePageChange = (page) => {
  currentPage.value = page
}

// 生命周期钩子
onMounted(() => {
  fetchMessages()
  initWebSocket()
})

onUnmounted(() => {
  if (websocket.value) {
    websocket.value.close()
  }
})
</script>

<style scoped>
.message-center {
  padding: 20px;
}

.message-card {
  width: 100%;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>