<template>
  <div class="probe-detail-container">
    <el-card class="probe-header">
      <div class="probe-title">
        <h2>探针设备详情</h2>
        <el-tag :type="probeStatus.type">{{ probeStatus.label }}</el-tag>
      </div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="设备编号">{{ probeInfo.id }}</el-descriptions-item>
        <el-descriptions-item label="派送人员">{{ probeInfo.assignee }}</el-descriptions-item>
        <el-descriptions-item label="部署地点">{{ probeInfo.location }}</el-descriptions-item>
        <el-descriptions-item label="部署时间">{{ formatDate(probeInfo.deployTime) }}</el-descriptions-item>
        <el-descriptions-item label="运行状态">
          <el-tag :type="probeStatus.type">{{ probeStatus.label }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="当前采集速度">{{ probeInfo.currentSpeed }} MB/s</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <div class="data-overview">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="data-card">
            <template #header>
              <div class="card-header">
                <span>采集总量</span>
              </div>
            </template>
            <div class="data-value">{{ formatDataSize(probeInfo.totalData) }}</div>
            <div class="data-desc">已采集电子证据总量</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="data-card">
            <template #header>
              <div class="card-header">
                <span>文件数量</span>
              </div>
            </template>
            <div class="data-value">{{ probeInfo.fileCount }}</div>
            <div class="data-desc">已采集文件总数</div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="data-card">
            <template #header>
              <div class="card-header">
                <span>平均速度</span>
              </div>
            </template>
            <div class="data-value">{{ probeInfo.avgSpeed }} MB/s</div>
            <div class="data-desc">采集平均速度</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span>采集速度趋势</span>
          <el-radio-group v-model="timeRange" size="small">
            <el-radio-button label="day">今日</el-radio-button>
            <el-radio-button label="week">本周</el-radio-button>
            <el-radio-button label="month">本月</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      <div ref="speedChartRef" class="chart"></div>
    </el-card>

    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span>采集数据量时间轴</span>
          <el-radio-group v-model="dataTimeRange" size="small">
            <el-radio-button label="day">今日</el-radio-button>
            <el-radio-button label="week">本周</el-radio-button>
            <el-radio-button label="month">本月</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      <div ref="dataChartRef" class="chart"></div>
    </el-card>

    <el-card class="evidence-card">
      <template #header>
        <div class="card-header">
          <span>采集证据信息</span>
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文件名"
            class="search-input"
            clearable
            @clear="handleSearch"
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </template>
      <el-table :data="filteredEvidenceList" style="width: 100%" v-loading="tableLoading">
        <el-table-column prop="fileName" label="文件名" min-width="200" show-overflow-tooltip />
        <el-table-column prop="fileType" label="文件类型" width="120">
          <template #default="scope">
            <el-tag :type="getFileTypeTag(scope.row.fileType)">{{ scope.row.fileType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="文件大小" width="120">
          <template #default="scope">
            {{ formatDataSize(scope.row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="collectTime" label="采集时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.collectTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'success' ? 'success' : 'danger'">
              {{ scope.row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" link @click="viewEvidence(scope.row)">查看</el-button>
            <el-button type="success" link @click="downloadEvidence(scope.row)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalItems"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const probeId = route.params.id || '001'

// 探针基本信息
const probeInfo = ref({
  id: probeId,
  assignee: '张三',
  location: '北京市海淀区XX大厦B座5层',
  deployTime: new Date('2024-10-01 09:00:00'),
  status: 'running', // running, offline, error
  currentSpeed: 5.6,
  avgSpeed: 4.8,
  totalData: 1024 * 1024 * 1024 * 15, // 15GB
  fileCount: 3562
})

// 探针状态
const probeStatus = computed(() => {
  const statusMap = {
    running: { label: '运行中', type: 'success' },
    offline: { label: '离线', type: 'info' },
    error: { label: '异常', type: 'danger' }
  }
  return statusMap[probeInfo.value.status] || { label: '未知', type: 'warning' }
})

// 图表相关
const speedChartRef = ref(null)
const dataChartRef = ref(null)
const timeRange = ref('day')
const dataTimeRange = ref('day')
let speedChart = null
let dataChart = null

// 证据列表相关
const evidenceList = ref([])
const searchKeyword = ref('')
const tableLoading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)

// 过滤后的证据列表
const filteredEvidenceList = computed(() => {
  if (!searchKeyword.value) {
    return evidenceList.value
  }
  return evidenceList.value.filter(item => 
    item.fileName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  if (typeof date === 'string') {
    date = new Date(date)
  }
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 格式化数据大小
const formatDataSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 获取文件类型对应的标签类型
const getFileTypeTag = (fileType) => {
  const typeMap = {
    'doc': 'primary',
    'pdf': 'success',
    'jpg': 'warning',
    'mp4': 'danger',
    'mp3': 'info'
  }
  return typeMap[fileType.toLowerCase()] || ''
}

// 初始化速度趋势图表
const initSpeedChart = () => {
  if (!speedChartRef.value) return
  
  speedChart = echarts.init(speedChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: generateTimeData(timeRange.value)
    },
    yAxis: {
      type: 'value',
      name: 'MB/s'
    },
    series: [
      {
        name: '采集速度',
        type: 'line',
        smooth: true,
        lineStyle: {
          width: 3,
          shadowColor: 'rgba(0,0,0,0.3)',
          shadowBlur: 10,
          shadowOffsetY: 8
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(128, 255, 165, 0.7)'
            },
            {
              offset: 1,
              color: 'rgba(1, 191, 236, 0.1)'
            }
          ])
        },
        emphasis: {
          focus: 'series'
        },
        data: generateRandomData(24, 1, 10)
      }
    ]
  }
  
  speedChart.setOption(option)
  
  window.addEventListener('resize', () => {
    speedChart.resize()
  })
}

// 初始化数据量时间轴图表
const initDataChart = () => {
  if (!dataChartRef.value) return
  
  dataChart = echarts.init(dataChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        return params[0].name + '<br/>' + 
               params[0].seriesName + ': ' + formatDataSize(params[0].value * 1024 * 1024)
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: generateTimeData(dataTimeRange.value)
    },
    yAxis: {
      type: 'value',
      name: '数据量(MB)'
    },
    series: [
      {
        name: '采集数据量',
        type: 'line',
        step: 'end',
        emphasis: {
          focus: 'series'
        },
        markPoint: {
          data: [
            { type: 'max', name: '最大值' },
            { type: 'min', name: '最小值' }
          ]
        },
        markArea: {
          data: [
            [{ name: '高峰期', xAxis: '12:00' }, { xAxis: '18:00' }]
          ]
        },
        data: generateCumulativeData(24, 10, 50)
      }
    ]
  }
  
  dataChart.setOption(option)
  
  window.addEventListener('resize', () => {
    dataChart.resize()
  })
}

// 生成时间数据
const generateTimeData = (range) => {
  let data = []
  if (range === 'day') {
    for (let i = 0; i < 24; i++) {
      data.push(i.toString().padStart(2, '0') + ':00')
    }
  } else if (range === 'week') {
    data = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  } else if (range === 'month') {
    for (let i = 1; i <= 30; i++) {
      data.push(i + '日')
    }
  }
  return data
}

// 生成随机数据
const generateRandomData = (count, min, max) => {
  const data = []
  for (let i = 0; i < count; i++) {
    data.push(+(min + Math.random() * (max - min)).toFixed(2))
  }
  return data
}

// 生成累积数据
const generateCumulativeData = (count, min, max) => {
  const data = []
  let sum = 0
  for (let i = 0; i < count; i++) {
    const value = +(min + Math.random() * (max - min)).toFixed(2)
    sum += value
    data.push(sum)
  }
  return data
}

// 获取证据列表
const fetchEvidenceList = async () => {
  tableLoading.value = true
  try {
    // 这里应该是实际的API调用，现在使用模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据
    const mockData = []
    const fileTypes = ['doc', 'pdf', 'jpg', 'mp4', 'mp3', 'xlsx', 'txt']
    
    for (let i = 0; i < 100; i++) {
      mockData.push({
        id: 'file_' + i,
        fileName: `证据文件_${i}_${Math.random().toString(36).substring(2, 8)}.${fileTypes[i % fileTypes.length]}`,
        fileType: fileTypes[i % fileTypes.length],
        fileSize: Math.floor(Math.random() * 1024 * 1024 * 10), // 0-10MB
        collectTime: new Date(Date.now() - Math.floor(Math.random() * 7 * 24 * 60 * 60 * 1000)),
        status: Math.random() > 0.1 ? 'success' : 'failed' // 90%成功率
      })
    }
    
    totalItems.value = mockData.length
    evidenceList.value = mockData.slice(
      (currentPage.value - 1) * pageSize.value,
      currentPage.value * pageSize.value
    )
  } catch (error) {
    console.error('获取证据列表失败:', error)
    ElMessage.error('获取证据列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchEvidenceList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchEvidenceList()
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchEvidenceList()
}

// 查看证据
const viewEvidence = (evidence) => {
  ElMessageBox.alert(
    `文件名: ${evidence.fileName}<br>
     文件类型: ${evidence.fileType}<br>
     文件大小: ${formatDataSize(evidence.fileSize)}<br>
     采集时间: ${formatDate(evidence.collectTime)}`,
    '证据详情',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '确定'
    }
  )
}

// 下载证据
const downloadEvidence = (evidence) => {
  ElMessage.success(`开始下载: ${evidence.fileName}`)
  // 实际下载逻辑
}

// 监听时间范围变化，更新图表
watch(timeRange, (newValue) => {
  if (speedChart) {
    speedChart.setOption({
      xAxis: {
        data: generateTimeData(newValue)
      },
      series: [{
        data: generateRandomData(
          newValue === 'day' ? 24 : newValue === 'week' ? 7 : 30,
          1,
          10
        )
      }]
    })
  }
})

watch(dataTimeRange, (newValue) => {
  if (dataChart) {
    dataChart.setOption({
      xAxis: {
        data: generateTimeData(newValue)
      },
      series: [{
        data: generateCumulativeData(
          newValue === 'day' ? 24 : newValue === 'week' ? 7 : 30,
          10,
          50
        )
      }]
    })
  }
})

// 页面加载时初始化
onMounted(() => {
  fetchEvidenceList()
  
  // 延迟初始化图表，确保DOM已经渲染
  setTimeout(() => {
    initSpeedChart()
    initDataChart()
  }, 100)
})
</script>

<style scoped>
.probe-detail-container {
  padding: 20px;
}

.probe-header {
  margin-bottom: 20px;
}

.probe-title {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.probe-title h2 {
  margin: 0;
  margin-right: 15px;
}

.data-overview {
  margin-bottom: 20px;
}

.data-card {
  height: 190px;
  text-align: center;
}

.data-value {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
  margin: 15px 0;
}

.data-desc {
  color: #909399;
  font-size: 14px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart {
  height: 350px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.evidence-card {
  margin-bottom: 20px;
}

.search-input {
  width: 250px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>