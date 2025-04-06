<template>
  <div class="dashboard-container">
    <div class="search-section">
      <el-card class="search-card">
        <div class="search-header">
          <h2>全局搜索</h2>
          <el-button type="primary" @click="toggleAdvancedSearch">
            {{ showAdvancedSearch ? '收起高级搜索' : '展开高级搜索' }}
          </el-button>
        </div>
        
        <!-- 基础搜索 -->
        <div class="basic-search">
          <el-input
            v-model="searchQuery"
            placeholder="搜索案件、探针编号、嫌疑人姓名、证据类型等"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
        
        <!-- 高级搜索 -->
        <div v-if="showAdvancedSearch" class="advanced-search">
          <el-form :model="advancedFilters" label-width="80px">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="时间范围">
                  <el-date-picker
                    v-model="advancedFilters.dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="地点">
                  <el-select v-model="advancedFilters.location" placeholder="选择地点" clearable>
                    <el-option v-for="item in locationOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="设备类型">
                  <el-select v-model="advancedFilters.deviceType" placeholder="选择设备类型" clearable>
                    <el-option v-for="item in deviceTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="案件类型">
                  <el-select v-model="advancedFilters.caseType" placeholder="选择案件类型" clearable>
                    <el-option v-for="item in caseTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="证据状态">
                  <el-select v-model="advancedFilters.evidenceStatus" placeholder="选择证据状态" clearable>
                    <el-option v-for="item in evidenceStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item>
                  <el-button type="primary" @click="handleSearch">搜索</el-button>
                  <el-button @click="resetFilters">重置</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-card>
    </div>
    
    <!-- 搜索结果表格 -->
    <div class="table-section">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>搜索结果</span>
            <div class="header-actions">
              <el-button type="success" @click="exportData">导出数据</el-button>
            </div>
          </div>
        </template>
        
        <el-table
          v-loading="loading"
          :data="tableData"
          border
          style="width: 100%"
          @sort-change="handleSortChange"
        >
          <el-table-column prop="caseNumber" label="案件编号" sortable />
          <el-table-column prop="probeId" label="探针编号" sortable />
          <el-table-column prop="suspectName" label="嫌疑人姓名" />
          <el-table-column prop="evidenceType" label="证据类型" />
          <el-table-column prop="location" label="地点" />
          <el-table-column prop="deviceType" label="设备类型" />
          <el-table-column prop="createTime" label="创建时间" sortable />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewDetail(scope.row)">查看</el-button>
              <el-button type="warning" size="small" @click="editItem(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 搜索相关状态
const searchQuery = ref('')
const showAdvancedSearch = ref(false)
const advancedFilters = reactive({
  dateRange: [],
  location: '',
  deviceType: '',
  caseType: '',
  evidenceStatus: ''
})

// 表格相关状态
const loading = ref(false)
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const sortBy = ref('')
const sortOrder = ref('')

// 选项数据
const locationOptions = [
  { value: '北京', label: '北京' },
  { value: '上海', label: '上海' },
  { value: '广州', label: '广州' },
  { value: '深圳', label: '深圳' }
]

const deviceTypeOptions = [
  { value: '手机', label: '手机' },
  { value: '电脑', label: '电脑' },
  { value: '平板', label: '平板' },
  { value: 'U盘', label: 'U盘' }
]

const caseTypeOptions = [
  { value: '刑事案件', label: '刑事案件' },
  { value: '民事案件', label: '民事案件' },
  { value: '行政案件', label: '行政案件' }
]

const evidenceStatusOptions = [
  { value: '已收集', label: '已收集' },
  { value: '已分析', label: '已分析' },
  { value: '已归档', label: '已归档' }
]

// 方法
const toggleAdvancedSearch = () => {
  showAdvancedSearch.value = !showAdvancedSearch.value
}

const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

const resetFilters = () => {
  searchQuery.value = ''
  Object.keys(advancedFilters).forEach(key => {
    if (Array.isArray(advancedFilters[key])) {
      advancedFilters[key] = []
    } else {
      advancedFilters[key] = ''
    }
  })
  handleSearch()
}

const handleSortChange = ({ prop, order }) => {
  sortBy.value = prop
  sortOrder.value = order === 'ascending' ? 'asc' : order === 'descending' ? 'desc' : ''
  fetchData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

const viewDetail = (row) => {
  ElMessageBox.alert(`查看案件详情：${row.caseNumber}`, '详情', {
    confirmButtonText: '确定'
  })
}

const editItem = (row) => {
  ElMessage.success(`编辑案件：${row.caseNumber}`)
}

const exportData = () => {
  ElMessage.success('数据导出成功')
}

// 获取表格数据
const fetchData = () => {
  loading.value = true
  
  // 这里应该是实际的API调用
  // 模拟API调用
  setTimeout(() => {
    // 模拟数据
    const mockData = []
    for (let i = 1; i <= 100; i++) {
      mockData.push({
        id: i,
        caseNumber: `CASE-${2024000 + i}`,
        probeId: `PROBE-${10000 + i}`,
        suspectName: `嫌疑人${i}`,
        evidenceType: i % 3 === 0 ? '电子证据' : i % 3 === 1 ? '物理证据' : '书面证据',
        location: locationOptions[i % 4].value,
        deviceType: deviceTypeOptions[i % 4].value,
        createTime: `2024-${Math.floor(i / 10) + 1}-${(i % 28) + 1}`
      })
    }
    
    // 模拟搜索过滤
    let filteredData = [...mockData]
    
    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase()
      filteredData = filteredData.filter(item => 
        item.caseNumber.toLowerCase().includes(query) ||
        item.probeId.toLowerCase().includes(query) ||
        item.suspectName.toLowerCase().includes(query) ||
        item.evidenceType.toLowerCase().includes(query)
      )
    }
    
    // 高级筛选
    if (advancedFilters.location) {
      filteredData = filteredData.filter(item => item.location === advancedFilters.location)
    }
    
    if (advancedFilters.deviceType) {
      filteredData = filteredData.filter(item => item.deviceType === advancedFilters.deviceType)
    }
    
    if (advancedFilters.dateRange && advancedFilters.dateRange.length === 2) {
      const startDate = new Date(advancedFilters.dateRange[0])
      const endDate = new Date(advancedFilters.dateRange[1])
      
      filteredData = filteredData.filter(item => {
        const itemDate = new Date(item.createTime)
        return itemDate >= startDate && itemDate <= endDate
      })
    }
    
    // 排序
    if (sortBy.value && sortOrder.value) {
      filteredData.sort((a, b) => {
        if (sortOrder.value === 'asc') {
          return a[sortBy.value] > b[sortBy.value] ? 1 : -1
        } else {
          return a[sortBy.value] < b[sortBy.value] ? 1 : -1
        }
      })
    }
    
    // 分页
    total.value = filteredData.length
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    tableData.value = filteredData.slice(start, end)
    
    loading.value = false
  }, 500)
}

// 初始化
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.search-section {
  margin-bottom: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.basic-search {
  margin-bottom: 20px;
}

.advanced-search {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>