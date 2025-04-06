<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>电子数据智能取证平台</h2>
      <div class="header-subtitle">基于案件信息的智能问答系统</div>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(message, index) in messages" :key="index" :class="['message', message.role]">
        <div class="avatar">
          <el-avatar :icon="message.role === 'user' ? UserFilled : Service" :size="40" 
                    :class="message.role === 'assistant' ? 'assistant-avatar' : 'user-avatar'" />
        </div>
        <div class="content">
          <div v-if="message.role === 'assistant' && index === messages.length - 1 && isTyping" class="typing-text">
            {{ message.content }}
          </div>
          <div v-else-if="message.role === 'assistant'" v-html="formatMessage(message.content)"></div>
          <div v-else>{{ message.content }}</div>
        </div>
      </div>
      <div v-if="isLoading && !isTyping" class="message assistant">
        <div class="avatar">
          <el-avatar :icon="Service" :size="40" class="assistant-avatar" />
        </div>
        <div class="content">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="chat-input">
      <el-input
        v-model="userInput"
        type="textarea"
        :rows="3"
        placeholder="请输入您关于案件的问题..."
        resize="none"
        @keydown.enter.prevent="sendMessage"
      />
      <div class="button-group">
        <el-button @click="clearChat" :disabled="isLoading" plain>
          <el-icon><Delete /></el-icon>
          清空对话
        </el-button>
        <el-button type="primary" :disabled="isLoading || !userInput.trim()" @click="sendMessage">
          <el-icon><Position /></el-icon>
          发送
        </el-button>
      </div>
    </div>
    
    <el-drawer
      v-model="drawerVisible"
      title="案件信息"
      direction="rtl"
      size="30%"
    >
      <div class="case-info-container">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="办理中案件" name="active">
            <el-table :data="activeCases" style="width: 100%">
              <el-table-column prop="id" label="案件编号" width="120" />
              <el-table-column prop="name" label="案件名称" />
              <el-table-column prop="date" label="立案日期" width="100" />
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button type="primary" link @click="selectCase(scope.row)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="已结案件" name="closed">
            <el-table :data="closedCases" style="width: 100%">
              <el-table-column prop="id" label="案件编号" width="120" />
              <el-table-column prop="name" label="案件名称" />
              <el-table-column prop="date" label="结案日期" width="100" />
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button type="primary" link @click="selectCase(scope.row)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>
    
    <div class="floating-button">
      <el-button type="primary" circle @click="drawerVisible = true">
        <el-icon><Folder /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { UserFilled, Service, Delete, Position, Folder } from '@element-plus/icons-vue'
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import { ElMessage, ElMessageBox } from 'element-plus'

// 配置marked使用highlight.js进行代码高亮
marked.setOptions({
  highlight: function (code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      return hljs.highlight(code, { language: lang }).value
    }
    return hljs.highlightAuto(code).value
  },
  breaks: true
})

const userInput = ref('')
const messages = ref([
  {
    role: 'assistant',
    content: '您好！我是电子数据智能取证平台，可以帮助您分析案件信息、解读电子证据、提供调查建议。请问您需要了解哪个案件的信息？您可以点击右下角的文件夹图标查看当前案件列表。'
  }
])
const isLoading = ref(false)
const isTyping = ref(false) // 是否正在打字中
const messagesContainer = ref(null)
const drawerVisible = ref(false)
const activeTab = ref('active')

// 案件数据
const activeCases = ref([
  { id: 'DZJZ20240001', name: '张某网络诈骗案', date: '2024-01-15', status: '调查中' },
  { id: 'DZJZ20240012', name: '某公司数据泄露案', date: '2024-02-28', status: '证据收集' },
  { id: 'DZJZ20240025', name: '网络黑客入侵案', date: '2024-03-10', status: '分析中' },
  { id: 'DZJZ20240036', name: '电信诈骗团伙案', date: '2024-04-05', status: '报告生成' }
])

const closedCases = ref([
  { id: 'DZJZ20240048', name: '内部数据窃取案', date: '2024-05-30', status: '已结案' },
  { id: 'DZJZ20240052', name: '网络勒索软件案', date: '2024-06-15', status: '已结案' },
  { id: 'DZJZ20240061', name: '信用卡欺诈案', date: '2024-07-22', status: '已结案' }
])

// 选择案件
const selectCase = (caseData) => {
  userInput.value = `查询案件${caseData.id}`
  drawerVisible.value = false
}

// 格式化消息内容，支持Markdown
const formatMessage = (content) => {
  return marked(content)
}

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

// 清空对话
const clearChat = () => {
  ElMessageBox.confirm('确定要清空所有对话记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    messages.value = [{
      role: 'assistant',
      content: '您好！我是电子数据智能取证平台，可以帮助您分析案件信息、解读电子证据、提供调查建议。请问您需要了解哪个案件的信息？您可以点击右下角的文件夹图标查看当前案件列表。'
    }]
    ElMessage({
      type: 'success',
      message: '对话已清空'
    })
  }).catch(() => {})
}

// 模拟流式输出
const streamResponse = async (response) => {
  isTyping.value = true // 开始打字效果
  
  const assistantMessage = {
    role: 'assistant',
    content: ''
  }
  messages.value.push(assistantMessage)
  
  // 模拟流式输出，每次添加一个字符
  const chars = response.split('')
  for (let i = 0; i < chars.length; i++) {
    // 使用Vue的响应式API来确保更新被检测到
    assistantMessage.content += chars[i]
    // 强制更新messages数组，触发视图更新
    messages.value = [...messages.value]
    await new Promise(resolve => setTimeout(resolve, 30)) // 稍微增加延迟，使效果更明显
    scrollToBottom()
  }
  
  // 打字完成后，等待一小段时间再将打字状态设为false
  // 这样可以确保最后一个字符也能被看到
  await new Promise(resolve => setTimeout(resolve, 300))
  isTyping.value = false // 结束打字效果
  
  // 打字完成后，重新渲染为Markdown
  // 使用Vue的响应式API来确保更新被检测到
  const lastIndex = messages.value.length - 1
  messages.value[lastIndex] = {
    ...messages.value[lastIndex],
    content: response
  }
  // 强制更新messages数组，触发视图更新
  messages.value = [...messages.value]
}

// 发送消息
const sendMessage = async () => {
  if (!userInput.value.trim() || isLoading.value) return
  
  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: userInput.value
  })
  
  const question = userInput.value
  userInput.value = ''
  scrollToBottom()
  
  // 模拟API请求
  isLoading.value = true
  try {
    // 这里应该替换为实际的API调用
    // const response = await yourApiService.sendMessage(question)
    
    // 模拟API响应延迟
    await new Promise(resolve => setTimeout(resolve, 200))
    
    // 模拟响应内容
    const mockResponse = generateMockResponse(question)
    
    // 流式输出响应
    await streamResponse(mockResponse)
  } catch (error) {
    console.error('发送消息失败:', error)
    messages.value.push({
      role: 'assistant',
      content: '抱歉，发生了错误，请稍后再试。可能是网络连接问题或服务器暂时不可用。'
    })
  } finally {
    isLoading.value = false
    scrollToBottom()
  }
}

// 生成模拟响应（实际项目中应替换为真实API调用）
const generateMockResponse = (question) => {
  if (question.includes('你好') || question.includes('hi') || question.includes('hello')) {
    return '您好！我是电子数据智能取证平台，很高兴为您服务。我可以帮助您分析案件信息、解读电子证据、提供调查建议和生成报告。请告诉我您需要查询的案件编号或者直接描述您的问题，我将尽力协助您。';
  } else if (question.includes('案件') && (question.includes('列表') || question.includes('查看'))) {
    return `根据系统记录，当前有以下办理中案件：

| 案件编号 | 案件名称 | 立案日期 | 主办人 | 状态 |
|---------|---------|---------|-------|------|
| DZJZ20240001 | 张某网络诈骗案 | 2024-01-15 | 李警官 | 调查中 |
| DZJZ20240012 | 某公司数据泄露案 | 2024-02-28 | 王警官 | 证据收集 |
| DZJZ20240025 | 网络黑客入侵案 | 2024-03-10 | 赵警官 | 分析中 |
| DZJZ20240036 | 电信诈骗团伙案 | 2024-04-05 | 刘警官 | 报告生成 |
| DZJZ20240048 | 内部数据窃取案 | 2024-05-12 | 陈警官 | 已结案 |

您可以通过输入案件编号获取详细信息，例如："查询案件DZJZ20240001"`;
  } else if (question.includes('DZJZ20240001') || (question.includes('张') && question.includes('诈骗'))) {
    return `### 案件详情：张某网络诈骗案 (DZJZ20240001)

**基本信息：**
- 立案日期：2024年1月15日
- 案件类型：网络诈骗
- 主办人：李警官
- 当前状态：调查中
- 涉案金额：约58.6万元

**案件摘要：**
嫌疑人张某（男，32岁）涉嫌通过网络平台发布虚假投资信息，诱导被害人进行投资，共计骗取20余名被害人资金58.6万元。嫌疑人使用多个银行账户和数字钱包进行资金转移，试图掩盖资金流向。

**电子证据清单：**
1. 嫌疑人手机通讯记录（已提取）
2. 社交媒体聊天记录（已提取）
3. 银行交易流水（已提取）
4. 虚假投资平台网站备份（已提取）
5. 嫌疑人电脑硬盘镜像（分析中）
6. 云存储账户数据（待获取）

**最新进展：**
数据分析团队已完成对嫌疑人手机和社交媒体数据的初步分析，发现其与3名可能的共犯有频繁联系。银行流水分析显示资金主要流向了5个不同的账户，目前正在追踪这些账户的最终受益人。

**下一步工作建议：**
1. 完成电脑硬盘数据的深度分析
2. 申请获取云存储账户的访问权限
3. 对可能的共犯进行背景调查
4. 构建完整的资金流转图谱

需要了解更多信息或有具体问题，请继续提问。`;
  } else if (question.includes('DZJZ20240012') || (question.includes('公司') && question.includes('泄露'))) {
    return `### 案件详情：某公司数据泄露案 (DZJZ20240012)

**基本信息：**
- 立案日期：2024年2月28日
- 案件类型：数据泄露/商业间谍
- 主办人：王警官
- 当前状态：证据收集
- 涉及数据：客户信息约3万条，技术文档15份

**案件摘要：**
u某科技公司报案称公司核心客户数据和技术文档被泄露，怀疑是内部员工所为。初步调查显示，数据泄露发生在过去三个月内，泄露的信息已在暗网上出现，对公司造成重大商业损失和声誉影响。

**电子证据清单：**
1. 公司内部网络访问日志（已提取）
2. 员工电脑取证镜像（5台，已提取）
3. 服务器访问记录（已提取）
4. 可疑员工邮件通信记录（分析中）
5. 云存储访问记录（分析中）
6. 暗网数据样本（已获取）

**最新进展：**
通过分析网络日志和服务器访问记录，已锁定3名可疑员工。其中一名员工在数据泄露前一周有异常的数据下载行为。暗网数据样本与公司内部数据格式完全匹配，确认为泄露数据。

**下一步工作建议：**
1. 完成可疑员工电脑的深度取证分析
2. 调取可疑员工的通讯记录和社交媒体数据
3. 分析可疑员工的资金流向
4. 进行涉案员工访谈

需要了解更多信息或有具体问题，请继续提问。`;
  } else if (question.includes('DZJZ20240012') || (question.includes('公司') && question.includes('泄露'))) {
    return `### 案件详情：某公司数据泄露案 (DZJZ20240012)

**基本信息：**
- 立案日期：2024年2月28日
- 案件类型：数据泄露/商业间谍
- 主办人：王警官
- 当前状态：证据收集
- 涉及数据：客户信息约3万条，技术文档15份

**案件摘要：**
u某科技公司报案称公司核心客户数据和技术文档被泄露，怀疑是内部员工所为。初步调查显示，数据泄露发生在过去三个月内，泄露的信息已在暗网上出现，对公司造成重大商业损失和声誉影响。

**电子证据清单：**
1. 公司内部网络访问日志（已提取）
2. 员工电脑取证镜像（5台，已提取）
3. 服务器访问记录（已提取）
4. 可疑员工邮件通信记录（分析中）
5. 云存储访问记录（分析中）
6. 暗网数据样本（已获取）

**最新进展：**
通过分析网络日志和服务器访问记录，已锁定3名可疑员工。其中一名员工在数据泄露前一周有异常的数据下载行为。暗网数据样本与公司内部数据格式完全匹配，确认为泄露数据。

**下一步工作建议：**
1. 完成可疑员工电脑的深度取证分析
2. 调取可疑员工的通讯记录和社交媒体数据
3. 分析可疑员工的资金流向
4. 进行涉案员工访谈

需要了解更多信息或有具体问题，请继续提问。`;
  } else if (question.includes('DZJZ20240012') || (question.includes('公司') && question.includes('泄露'))) {
    return `### 案件详情：某公司数据泄露案 (DZJZ20240012)

**基本信息：**
- 立案日期：2024年2月28日
- 案件类型：数据泄露/商业间谍
- 主办人：王警官
- 当前状态：证据收集
- 涉及数据：客户信息约3万条，技术文档15份

**案件摘要：**
u某科技公司报案称公司核心客户数据和技术文档被泄露，怀疑是内部员工所为。初步调查显示，数据泄露发生在过去三个月内，泄露的信息已在暗网上出现，对公司造成重大商业损失和声誉影响。

**电子证据清单：**
1. 公司内部网络访问日志（已提取）
2. 员工电脑取证镜像（5台，已提取）
3. 服务器访问记录（已提取）
4. 可疑员工邮件通信记录（分析中）
5. 云存储访问记录（分析中）
6. 暗网数据样本（已获取）

**最新进展：**
通过分析网络日志和服务器访问记录，已锁定3名可疑员工。其中一名员工在数据泄露前一周有异常的数据下载行为。暗网数据样本与公司内部数据格式完全匹配，确认为泄露数据。

**下一步工作建议：**
1. 完成可疑员工电脑的深度取证分析
2. 调取可疑员工的通讯记录和社交媒体数据
3. 分析可疑员工的资金流向
4. 进行涉案员工访谈

需要了解更多信息或有具体问题，请继续提问。`;
} else if (question.includes('DZJZ20240012') || (question.includes('公司') && question.includes('泄露'))) {
    return `### 案件详情：某公司数据泄露案 (DZJZ20240012)

**基本信息：**
- 立案日期：2024年2月28日
- 案件类型：数据泄露/商业间谍
- 主办人：王警官
- 当前状态：证据收集
- 涉及数据：客户信息约3万条，技术文档15份

**案件摘要：**
u某科技公司报案称公司核心客户数据和技术文档被泄露，怀疑是内部员工所为。初步调查显示，数据泄露发生在过去三个月内，泄露的信息已在暗网上出现，对公司造成重大商业损失和声誉影响。

**电子证据清单：**
1. 公司内部网络访问日志（已提取）
2. 员工电脑取证镜像（5台，已提取）
3. 服务器访问记录（已提取）
4. 可疑员工邮件通信记录（分析中）
5. 云存储访问记录（分析中）
6. 暗网数据样本（已获取）

**最新进展：**
通过分析网络日志和服务器访问记录，已锁定3名可疑员工。其中一名员工在数据泄露前一周有异常的数据下载行为。暗网数据样本与公司内部数据格式完全匹配，确认为泄露数据。

**下一步工作建议：**
1. 完成可疑员工电脑的深度取证分析
2. 调取可疑员工的通讯记录和社交媒体数据
3. 分析可疑员工的资金流向
4. 进行涉案员工访谈

需要了解更多信息或有具体问题，请继续提问。`;
  } else if (question.includes('DZJZ20240025') || (question.includes('黑客') && question.includes('入侵'))) {
    return `### 案件详情：网络黑客入侵案 (DZJZ20240025)

**基本信息：**
- 立案日期：2024年3月10日
- 案件类型：网络安全入侵
- 主办人：赵警官
- 当前状态：分析中
- 影响范围：3台核心服务器，约50个用户账户

**案件摘要：**
某政府部门网络系统遭到黑客攻击，入侵者获取了部分内部文件和用户账户信息。系统日志显示入侵者使用了高级持续性威胁(APT)攻击手段，可能与境外黑客组织有关。入侵持续了约72小时才被发现。

**电子证据清单：**
1. 服务器系统日志（已提取）
2. 网络流量捕获数据（已提取）
3. 恶意软件样本（已提取，2个）
4. 入侵痕迹分析报告（编写中）
5. 受影响文件清单（已生成）
6. 可疑IP地址列表（已整理）

**最新进展：**
初步分析表明攻击者首先通过钓鱼邮件植入了远程访问木马，然后利用系统漏洞提升权限。已发现攻击者使用的两个命令控制(C2)服务器IP地址，正在与国际网络安全机构合作追踪源头。

**下一步工作建议：**
1. 完成恶意软件的深度分析和特征提取
2. 建立完整的攻击链路图
3. 加强系统安全防护，修补漏洞
4. 与相关部门共享威胁情报

需要了解更多信息或有具体问题，请继续提问。`;
  } else if (question.includes('DZJZ20240036') || (question.includes('电信') && question.includes('诈骗'))) {
    return `### 案件详情：电信诈骗团伙案 (DZJZ20240036)

**基本信息：**
- 立案日期：2024年4月5日
- 案件类型：电信网络诈骗
- 主办人：刘警官
- 当前状态：报告生成
- 涉案金额：约320万元

**案件摘要：**
一个跨省电信诈骗团伙以"冒充公检法"方式实施诈骗，受害人超过50人。该团伙组织严密，分工明确，包括话务组、技术组和资金转移组。通过分析电子证据，已基本摸清团伙结构和作案手法。

**电子证据清单：**
1. 嫌疑人手机通讯记录（15部，已提取）
2. 诈骗话术剧本（已提取）
3. 虚拟专用网络(VPN)使用记录（已提取）
4. 资金流水记录（已提取）
5. 团伙内部通讯工具聊天记录（已提取）
6. 作案地点网络设备日志（已提取）

**最新进展：**
通过大数据分析，已确认该团伙与另外两起类似案件有关联。目前已抓获主要嫌疑人8名，冻结涉案账户23个，追回赃款约150万元。取证分析工作已基本完成，正在编写技术分析报告。

**下一步工作建议：**
1. 完成电子证据技术分析报告
2. 协助追查在逃人员
3. 继续追踪剩余赃款流向
4. 提取典型作案手法，用于预防宣传

需要了解更多信息或有具体问题，请继续提问。`;
  } else if (question.includes('DZJZ20240048') || (question.includes('内部') && question.includes('窃取'))) {
    return `### 案件详情：内部数据窃取案 (DZJZ20240048)

**基本信息：**
- 立案日期：2024年5月12日
- 结案日期：2024年5月30日
- 案件类型：内部数据窃取
- 主办人：陈警官
- 当前状态：已结案
- 涉及数据：研发资料和客户名单

**案件摘要：**
某制造企业研发部门员工李某在离职前窃取公司核心技术资料和客户名单，并试图带到竞争对手公司。公司IT部门发现异常数据传输行为后报案。通过电子取证确认了窃取行为和证据。

**电子证据清单：**
1. 嫌疑人工作电脑镜像（已分析）
2. 公司邮件服务器日志（已分析）
3. 网络传输记录（已分析）
4. 嫌疑人U盘取证（已分析）
5. 门禁系统记录（已分析）
6. 监控录像（已分析）

**案件结论：**
电子取证分析确认李某在离职前两周内，通过公司邮箱将技术文档发送至个人邮箱，并使用U盘拷贝了客户资料。所有窃取的数据已被追回，未造成实际泄露。李某已承认违法行为，接受相关处罚。

**经验总结：**
1. 企业应加强离职流程中的数据安全管控
2. 建立敏感数据传输监控机制
3. 实施文件加密和访问权限分级
4. 定期进行内部安全意识培训

案件已结案，如需了解更多细节，请继续提问。`;
  } else if (question.includes('DZJZ20240052') || (question.includes('勒索') && question.includes('软件'))) {
    return `### 案件详情：网络勒索软件案 (DZJZ20240052)

**基本信息：**
- 立案日期：2024年5月20日
- 结案日期：2024年6月15日
- 案件类型：勒索软件攻击
- 主办人：林警官
- 当前状态：已结案
- 影响范围：某医院信息系统，约200台计算机

**案件摘要：**
某市级医院遭遇勒索软件攻击，导致患者信息系统和医疗设备控制系统部分瘫痪。攻击者要求支付50比特币作为赎金。医院立即报案并配合调查，同时启动应急恢复程序。

**电子证据清单：**
1. 勒索软件样本（已分析）
2. 受感染系统镜像（已分析）
3. 网络流量日志（已分析）
4. 勒索通知和支付指令（已提取）
5. 系统备份恢复记录（已整理）
6. 比特币钱包地址追踪（已完成）

**案件结论：**
通过分析确认这是一起"REvil"变种勒索软件攻击。攻击者通过医院一名员工打开的钓鱼邮件附件植入恶意程序。未支付赎金，通过备份系统成功恢复了大部分数据。已协助医院修补安全漏洞并加强防护措施。

**经验总结：**
1. 医疗机构应建立更严格的邮件安全过滤系统
2. 关键系统需实施网络隔离和访问控制
3. 建立完善的数据备份和灾难恢复机制
4. 定期进行安全意识培训和应急演练

案件已结案，如需了解更多细节，请继续提问。`;
  } else if (question.includes('DZJZ20240061') || (question.includes('信用卡') && question.includes('欺诈'))) {
    return `### 案件详情：信用卡欺诈案 (DZJZ20240061)

**基本信息：**
- 立案日期：2024年6月10日
- 结案日期：2024年7月22日
- 案件类型：信用卡欺诈
- 主办人：张警官
- 当前状态：已结案
- 涉案金额：约85万元

**案件摘要：**
一个信用卡欺诈团伙通过网上购物平台实施欺诈，使用盗刷的信用卡信息购买高价值商品后转售。受害人遍布全国多个省份，涉及30多家银行的信用卡账户。

**电子证据清单：**
1. 嫌疑人电子设备取证（已完成）
2. 网购平台交易记录（已分析）
3. 银行交易流水（已分析）
4. 社交媒体通讯记录（已提取）
5. 物流配送信息（已整理）
6. 信用卡信息获取渠道分析（已完成）

**案件结论：**
通过电子数据分析，确认该团伙通过暗网购买被盗信用卡信息，然后利用虚假身份在多个电商平台下单购买高价值商品。商品通过特定物流渠道送至空置房屋后转售变现。已抓获团伙成员5名，追回赃款约60万元。

**经验总结：**
1. 电商平台应加强异常交易监测机制
2. 银行需完善信用卡交易风险预警系统
3. 加强公民个人信息保护意识教育
4. 建立跨平台信息共享机制，提高欺诈识别能力

案件已结案，如需了解更多细节，请继续提问。`;
  } else {
    return `抱歉，我没有找到与您问题相关的案件信息。您可以尝试以下方式提问：

1. 直接输入案件编号，例如："查询案件DZJZ20240001"
2. 描述案件关键词，例如："张某网络诈骗案"
3. 查看案件列表，输入："查看案件列表"

或者您可以告诉我您想了解的具体问题，我会尽力协助您。`;
  }
}

onMounted(() => {
  scrollToBottom()
})
</script>
<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 90vh;
  background-color: #f5f7fa;
  position: relative;
}

.chat-header {
  padding: 15px 20px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  text-align: center;
  z-index: 10;
}

.header-subtitle {
  color: #909399;
  font-size: 14px;
  margin-top: 5px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  scroll-behavior: smooth;
}

.message {
  display: flex;
  margin-bottom: 20px;
  align-items: flex-start;
}

.user {
  flex-direction: row-reverse;
}

.avatar {
  flex-shrink: 0;
  margin: 0 12px;
}

.assistant-avatar {
  background-color: #409eff;
  color: #fff;
}

.user-avatar {
  background-color: #67c23a;
  color: #fff;
}

.content {
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  line-height: 1.6;
}

.assistant .content {
  background-color: #fff;
  border-radius: 0 8px 8px 8px;
}

.user .content {
  background-color: #ecf5ff;
  border-radius: 8px 0 8px 8px;
  text-align: right;
}

/* 打字机文本样式 */
.typing-text {
  white-space: pre-wrap;
  word-break: break-word;
}

/* 打字机效果的光标样式 */
.typing-text::after {
  content: '|';
  animation: cursor-blink 1s step-end infinite;
  margin-left: 2px;
  font-weight: bold;
}

@keyframes cursor-blink {
  from, to { opacity: 0; }
  50% { opacity: 1; }
}

/* 打字指示器样式 */
.typing-indicator {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 8px 0;
}

.typing-indicator span {
  height: 8px;
  width: 8px;
  margin: 0 2px;
  background-color: #bbb;
  border-radius: 50%;
  display: inline-block;
  animation: typing-bounce 1.4s infinite ease-in-out both;
}

.typing-indicator span:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes typing-bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.chat-input {
  display: flex;
  flex-direction: column;
  padding: 15px 20px;
  background-color: #fff;
  box-shadow: 0 -2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.floating-button {
  position: fixed;
  right: 20px;
  bottom: 100px;
  z-index: 100;
}

.case-info-container {
  padding: 15px;
}

/* 代码块样式优化 */
:deep(pre) {
  background-color: #f8f8f8;
  border-radius: 4px;
  padding: 12px;
  overflow-x: auto;
}

:deep(code) {
  font-family: Consolas, Monaco, 'Andale Mono', monospace;
  font-size: 14px;
}

/* 表格样式优化 */
:deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin: 15px 0;
}

:deep(th), :deep(td) {
  border: 1px solid #dcdfe6;
  padding: 8px 12px;
  text-align: left;
}

:deep(th) {
  background-color: #f5f7fa;
}

:deep(blockquote) {
  border-left: 4px solid #409eff;
  margin: 10px 0;
  padding: 10px 15px;
  background-color: #ecf5ff;
  color: #606266;
}

:deep(ol){
    margin-left: 16px !important;
}

:deep(ul){
    margin-left: 16px !important;
}

</style>