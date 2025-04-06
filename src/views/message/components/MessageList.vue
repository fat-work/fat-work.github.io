<template>
  <div class="message-list">
    <el-timeline>
      <el-timeline-item
        v-for="message in messages"
        :key="message.id"
        :type="getTimelineItemType(message)"
        :timestamp="formatTimestamp(message.timestamp)"
        :color="getTimelineItemColor(message)"
      >
        <el-card class="message-item" :class="{ 'message-unread': !message.read }">
          <div class="message-header">
            <h4 class="message-title">{{ message.title }}</h4>
            <div class="message-actions">
              <el-button
                v-if="!message.read"
                type="primary"
                size="small"
                text
                @click="handleMarkAsRead(message.id)"
              >
                标记已读
              </el-button>
              <el-button
                type="danger"
                size="small"
                text
                @click="handleDelete(message.id)"
              >
                删除
              </el-button>
            </div>
          </div>
          <p class="message-content">{{ message.content }}</p>
          <div class="message-footer">
            <el-tag size="small" :type="getTagType(message)">
              {{ getMessageTypeLabel(message) }}
            </el-tag>
          </div>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { formatDistance } from 'date-fns'
import { zhCN } from 'date-fns/locale'

const props = defineProps({
  messages: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['read', 'delete'])

// 获取时间线项目类型
const getTimelineItemType = (message) => {
  if (message.type === 'warning') {
    return 'warning'
  } else if (message.type === 'task') {
    return 'success'
  }
  return 'info'
}

// 获取时间线项目颜色
const getTimelineItemColor = (message) => {
  if (message.type === 'warning') {
    return '#E6A23C'
  } else if (message.type === 'task') {
    return '#67C23A'
  }
  return '#909399'
}

// 获取标签类型
const getTagType = (message) => {
  if (message.type === 'warning') {
    return 'warning'
  } else if (message.type === 'task') {
    return 'success'
  }
  return 'info'
}

// 获取消息类型标签
const getMessageTypeLabel = (message) => {
  if (message.type === 'warning') {
    switch (message.subtype) {
      case 'probe_offline':
        return '探针离线'
      case 'evidence_stalled':
        return '证据采集停滞'
      case 'high_risk':
        return '高风险行为'
      default:
        return '预警通知'
    }
  } else if (message.type === 'task') {
    switch (message.subtype) {
      case 'report_completed':
        return '报告完成'
      case 'task_assigned':
        return '任务分配'
      default:
        return '任务提醒'
    }
  }
  return '系统通知'
}

// 格式化时间戳
const formatTimestamp = (timestamp) => {
  return formatDistance(new Date(timestamp), new Date(), {
    addSuffix: true,
    locale: zhCN
  })
}

// 标记为已读
const handleMarkAsRead = (messageId) => {
  emit('read', messageId)
}

// 删除消息
const handleDelete = (messageId) => {
  emit('delete', messageId)
}
</script>

<style scoped>
.message-list {
  margin-top: 10px;
}

.message-item {
  margin-bottom: 10px;
  transition: all 0.3s;
}

.message-unrea {
  background-color: #f5f7fa;
}
.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.message-title {
  font-size: 16px;
  font-weight: bold;
}
.message-actions {
  display: flex;
  align-items: center;
}
.message-actions button {
  margin-left: 10px;
}
.message-content {
  font-size: 14px;
  color: #666;
}
.message-footer {
  margin-top: 10px;
}
</style>