<template>
  <div class="case-analysis-container">
    <el-card class="case-header">
      <template #header>
        <div class="card-header">
          <h2>{{ caseInfo.title }}</h2>
          <el-tag type="primary">{{ caseInfo.caseNumber }}</el-tag>
          <el-tag type="success">{{ caseInfo.status }}</el-tag>
        </div>
      </template>
      <div class="case-basic-info">
        <div class="info-item">
          <span class="label">立案时间：</span>
          <span>{{ caseInfo.createTime }}</span>
        </div>
        <div class="info-item">
          <span class="label">主办人：</span>
          <span>{{ caseInfo.handler }}</span>
        </div>
        <div class="info-item">
          <span class="label">案件类型：</span>
          <span>{{ caseInfo.type }}</span>
        </div>
      </div>
    </el-card>

    <!-- 1. 案情信息 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>案情信息</h3>
        </div>
      </template>
      <div class="case-info-content">
        <div class="case-description">
          <h4>案情概述</h4>
          <p>{{ caseInfo.description }}</p>
        </div>
        <div class="case-timeline">
          <h4>案情发展时间线</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(event, index) in caseInfo.timeline"
              :key="index"
              :timestamp="event.time"
              :type="event.type"
              :color="event.color"
            >
              {{ event.content }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-card>

    <!-- 2. 人物画像 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>人物画像</h3>
          <el-select v-model="selectedPerson" placeholder="选择人物" @change="handlePersonChange">
            <el-option
              v-for="person in persons"
              :key="person.id"
              :label="person.name"
              :value="person.id"
            />
          </el-select>
        </div>
      </template>
      <div v-if="currentPerson" class="person-profile">
        <div class="person-basic">
          <el-avatar :size="100" :src="currentPerson.avatar"></el-avatar>
          <div class="person-info">
            <h4>{{ currentPerson.name }}</h4>
            <p>{{ currentPerson.description }}</p>
            <div class="person-tags">
              <el-tag v-for="(tag, index) in currentPerson.tags" :key="index" :type="tag.type" class="person-tag">
                {{ tag.name }}
              </el-tag>
            </div>
          </div>
        </div>
        <div class="person-evidences">
          <h4>相关证据</h4>
          <el-collapse>
            <el-collapse-item v-for="(evidence, index) in currentPerson.evidences" :key="index" :title="evidence.title">
              <div class="evidence-content">
                <p>{{ evidence.description }}</p>
                <div class="evidence-files">
                  <el-button v-for="(file, fIndex) in evidence.files" :key="fIndex" type="primary" link @click="viewFile(file)">
                    {{ file.name }}
                  </el-button>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
      <el-empty v-else description="请选择人物"></el-empty>
    </el-card>

    <!-- 3. 人物活动轨迹 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>人物活动轨迹</h3>
          <el-select v-model="selectedPersonTrack" placeholder="选择人物" @change="loadPersonTrack">
            <el-option
              v-for="person in persons"
              :key="person.id"
              :label="person.name"
              :value="person.id"
            />
          </el-select>
        </div>
      </template>
      <div class="track-container">
        <div id="track-map" class="track-map"></div>
        <div class="track-timeline">
            <el-slider
            v-model="trackTimelineValue"
            :min="0"
            :max="trackTimeline.length > 0 ? trackTimeline.length - 1 : 0"
            :format-tooltip="formatTrackTime"
            @change="updateTrackMap"
          ></el-slider>
          <div class="track-time-display">
            <span>{{ currentTrackTime }}</span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 4. 人物组织关系 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>人物组织关系</h3>
        </div>
      </template>
      <div id="relation-graph" class="relation-graph"></div>
      <div class="relation-legend">
        <div class="legend-item">
          <div class="legend-color" style="background-color: #409EFF;"></div>
          <span>嫌疑人</span>
        </div>
        <div class="legend-item">
          <div class="legend-color" style="background-color: #67C23A;"></div>
          <span>证人</span>
        </div>
        <div class="legend-item">
          <div class="legend-color" style="background-color: #E6A23C;"></div>
          <span>受害人</span>
        </div>
      </div>
    </el-card>

    <!-- 5. 人物行为分析 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>人物行为分析</h3>
          <el-select v-model="selectedPersonBehavior" placeholder="选择人物" @change="loadPersonBehavior">
            <el-option
              v-for="person in persons"
              :key="person.id"
              :label="person.name"
              :value="person.id"
            />
          </el-select>
        </div>
      </template>
      <div v-if="currentPersonBehavior" class="behavior-analysis">
        <div class="behavior-charts">
          <div id="behavior-chart-1" class="behavior-chart"></div>
          <div id="behavior-chart-2" class="behavior-chart"></div>
        </div>
        <div class="behavior-details">
          <h4>行为特征分析</h4>
          <el-descriptions :column="1" border>
            <el-descriptions-item v-for="(item, index) in currentPersonBehavior.features" :key="index" :label="item.label">
              {{ item.value }}
            </el-descriptions-item>
          </el-descriptions>
          <h4>行为模式</h4>
          <el-table :data="currentPersonBehavior.patterns" style="width: 100%">
            <el-table-column prop="type" label="模式类型" width="180" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="confidence" label="置信度" width="100">
              <template #default="scope">
                <el-progress :percentage="scope.row.confidence" :color="getConfidenceColor(scope.row.confidence)" />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <el-empty v-else description="请选择人物"></el-empty>
    </el-card>

    <!-- 6. 资产情况 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>资产情况</h3>
        </div>
      </template>
      <div class="assets-container">
        <div class="assets-summary">
          <el-row :gutter="20">
            <el-col :span="8" v-for="(asset, index) in assetsSummary" :key="index">
              <el-card shadow="hover" class="asset-card">
                <div class="asset-icon">
                  <el-icon><component :is="asset.icon"></component></el-icon>
                </div>
                <div class="asset-info">
                  <div class="asset-name">{{ asset.name }}</div>
                  <div class="asset-value">{{ asset.value }}</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
        <div class="assets-flow">
          <h4>资产流动情况</h4>
          <div id="assets-flow-chart" class="assets-flow-chart"></div>
        </div>
      </div>
    </el-card>

    <!-- 7. 异常行为预警 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>异常行为预警</h3>
        </div>
      </template>
      <div class="warning-container">
        <el-table :data="warningList" style="width: 100%">
          <el-table-column type="expand">
            <template #default="props">
              <div class="warning-detail">
                <p>{{ props.row.detail }}</p>
                <div class="warning-actions">
                  <el-button type="primary" size="small" @click="handleWarning(props.row, 'confirm')">确认预警</el-button>
                  <el-button type="info" size="small" @click="handleWarning(props.row, 'ignore')">忽略</el-button>
                  <el-button type="danger" size="small" @click="handleWarning(props.row, 'report')">上报</el-button>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="时间" width="180" />
          <el-table-column prop="person" label="相关人员" width="120" />
          <el-table-column prop="type" label="预警类型" width="120">
            <template #default="scope">
              <el-tag :type="getWarningTypeTag(scope.row.type)">{{ scope.row.type }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="预警内容" />
          <el-table-column prop="level" label="预警等级" width="100">
            <template #default="scope">
              <el-rate
                v-model="scope.row.level"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}"
              />
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getWarningStatusTag(scope.row.status)">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <!-- 8. 案件分析报告 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>案件分析报告</h3>
        </div>
      </template>
      <div class="report-container">
        <div class="report-preview">
          <div class="report-header">
            <h4>{{ caseInfo.title }} - 电子数据取证分析报告</h4>
            <div class="report-meta">
              <p><strong>报告编号：</strong>{{ reportInfo.reportId }}</p>
              <p><strong>生成时间：</strong>{{ reportInfo.generateTime }}</p>
              <p><strong>取证人员：</strong>{{ reportInfo.investigator }}</p>
              <p><strong>数据哈希值：</strong>{{ reportInfo.dataHash }}</p>
            </div>
          </div>
          <el-divider />
          <div class="report-content">
            <div class="report-section">
              <h5>一、案件概述</h5>
              <p>{{ caseInfo.description }}</p>
            </div>
            <div class="report-section">
              <h5>二、取证过程</h5>
              <p>{{ reportInfo.process }}</p>
            </div>
            <div class="report-section">
              <h5>三、证据清单</h5>
              <el-table :data="reportInfo.evidenceList" style="width: 100%">
                <el-table-column prop="name" label="证据名称" width="180" />
                <el-table-column prop="type" label="证据类型" width="120" />
                <el-table-column prop="source" label="来源" width="150" />
                <el-table-column prop="hash" label="哈希值" width="280" />
                <el-table-column prop="time" label="获取时间" />
              </el-table>
            </div>
            <div class="report-section">
              <h5>四、分析结论</h5>
              <p>{{ reportInfo.conclusion }}</p>
            </div>
          </div>
          <el-divider />
          <div class="report-footer">
            <p><strong>报告生成时间戳：</strong>{{ reportInfo.timestamp }}</p>
            <p><strong>报告完整性校验码：</strong>{{ reportInfo.reportHash }}</p>
          </div>
        </div>
        <div class="report-actions">
          <div class="action-group">
            <h5>报告格式</h5>
            <el-radio-group v-model="reportFormat">
              <el-radio label="pdf">PDF</el-radio>
              <el-radio label="word">Word</el-radio>
              <el-radio label="html">HTML</el-radio>
            </el-radio-group>
          </div>
          <div class="action-group">
            <h5>报告语言</h5>
            <el-radio-group v-model="reportLanguage">
              <el-radio label="zh">中文</el-radio>
              <el-radio label="en">英文</el-radio>
            </el-radio-group>
          </div>
          <div class="action-group">
            <h5>报告操作</h5>
            <el-button type="primary" @click="generateReport">生成报告</el-button>
            <el-button type="success" @click="previewReport">预览</el-button>
            <el-button @click="downloadReport">下载</el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 9. 关联证据拓扑 -->
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <h3>关联证据拓扑</h3>
          <div class="topology-controls">
            <el-radio-group v-model="topologyView" size="small">
              <el-radio-button label="all">全部</el-radio-button>
              <el-radio-button label="person">人物</el-radio-button>
              <el-radio-button label="evidence">证据</el-radio-button>
              <el-radio-button label="asset">资产</el-radio-button>
            </el-radio-group>
            <el-select v-model="topologyFocus" placeholder="选择焦点" clearable @change="updateTopologyFocus">
              <el-option-group label="人物">
                <el-option
                  v-for="person in persons"
                  :key="'person-' + person.id"
                  :label="person.name"
                  :value="'person-' + person.id"
                />
              </el-option-group>
              <el-option-group label="证据">
                <el-option
                  v-for="evidence in topologyEvidences"
                  :key="'evidence-' + evidence.id"
                  :label="evidence.name"
                  :value="'evidence-' + evidence.id"
                />
              </el-option-group>
              <el-option-group label="资产">
                <el-option
                  v-for="asset in topologyAssets"
                  :key="'asset-' + asset.id"
                  :label="asset.name"
                  :value="'asset-' + asset.id"
                />
              </el-option-group>
            </el-select>
          </div>
        </div>
      </template>
      <div class="topology-container">
        <div id="topology-graph" class="topology-graph"></div>
        <div v-if="selectedNode" class="topology-detail">
          <el-card shadow="never">
            <template #header>
              <div class="detail-header">
                <h4>{{ selectedNode.name }}</h4>
                <el-tag :type="getNodeTypeTag(selectedNode.type)">{{ getNodeTypeLabel(selectedNode.type) }}</el-tag>
              </div>
            </template>
            <div class="detail-content">
              <template v-if="selectedNode.type === 'person'">
                <el-descriptions :column="1" border>
                  <el-descriptions-item label="角色">{{ selectedNode.role }}</el-descriptions-item>
                  <el-descriptions-item label="描述">{{ selectedNode.description }}</el-descriptions-item>
                  <el-descriptions-item label="关联人物数">{{ selectedNode.relatedPersons.length }}</el-descriptions-item>
                  <el-descriptions-item label="关联证据数">{{ selectedNode.relatedEvidences.length }}</el-descriptions-item>
                  <el-descriptions-item label="关联资产数">{{ selectedNode.relatedAssets.length }}</el-descriptions-item>
                </el-descriptions>
                <div class="detail-actions">
                  <el-button type="primary" size="small" @click="viewPersonDetail(selectedNode.id)">查看详情</el-button>
                  <el-button type="success" size="small" @click="viewPersonTrack(selectedNode.id)">查看轨迹</el-button>
                </div>
              </template>
              <template v-else-if="selectedNode.type === 'evidence'">
                <el-descriptions :column="1" border>
                  <el-descriptions-item label="证据类型">{{ selectedNode.evidenceType }}</el-descriptions-item>
                  <el-descriptions-item label="获取时间">{{ selectedNode.collectTime }}</el-descriptions-item>
                  <el-descriptions-item label="哈希值">{{ selectedNode.hash }}</el-descriptions-item>
                  <el-descriptions-item label="描述">{{ selectedNode.description }}</el-descriptions-item>
                </el-descriptions>
                <div class="detail-actions">
                  <el-button type="primary" size="small" @click="viewEvidenceDetail(selectedNode.id)">查看详情</el-button>
                  <el-button type="success" size="small" @click="downloadEvidence(selectedNode.id)">下载证据</el-button>
                </div>
              </template>
              <template v-else-if="selectedNode.type === 'asset'">
                <el-descriptions :column="1" border>
                  <el-descriptions-item label="资产类型">{{ selectedNode.assetType }}</el-descriptions-item>
                  <el-descriptions-item label="价值">{{ selectedNode.value }}</el-descriptions-item>
                  <el-descriptions-item label="来源">{{ selectedNode.source }}</el-descriptions-item>
                  <el-descriptions-item label="描述">{{ selectedNode.description }}</el-descriptions-item>
                </el-descriptions>
                <div class="detail-actions">
                  <el-button type="primary" size="small" @click="viewAssetDetail(selectedNode.id)">查看详情</el-button>
                  <el-button type="success" size="small" @click="viewAssetFlow(selectedNode.id)">资金流向</el-button>
                </div>
              </template>
            </div>
          </el-card>
        </div>
      </div>
    </el-card>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import * as echarts from 'echarts'
import 'echarts/extension/bmap/bmap'
import { ElMessage } from 'element-plus'

// 案件基本信息
const caseInfo = ref({
  title: '',
//   title: '张某网络诈骗案',
  caseNumber: 'DZJZ20240001',
  status: '侦查中',
  createTime: '2024-01-15',
  handler: '李警官',
  type: '电信网络诈骗',
  description: '2024年1月，多名受害人报案称被"投资理财"诈骗，涉案金额达500余万元。经查，犯罪嫌疑人以高额回报为诱饵，通过网络平台进行诈骗活动。目前已抓获主要犯罪嫌疑人3名，冻结涉案资金200余万元。',
  timeline: [
    { time: '2024-01-05', content: '首名受害人王某报案', type: 'primary', color: '#409EFF' },
    { time: '2024-01-10', content: '陆续有多名受害人报案', type: 'primary', color: '#409EFF' },
    { time: '2024-01-15', content: '立案侦查', type: 'success', color: '#67C23A' },
    { time: '2024-01-20', content: '锁定主要嫌疑人', type: 'warning', color: '#E6A23C' },
    { time: '2024-02-05', content: '抓获嫌疑人李某', type: 'danger', color: '#F56C6C' },
    { time: '2024-02-10', content: '抓获嫌疑人张某、赵某', type: 'danger', color: '#F56C6C' },
    { time: '2024-02-15', content: '冻结涉案资金200万元', type: 'info', color: '#909399' },
    { time: '2024-03-01', content: '案件侦查进行中', type: 'info', color: '#909399' }
  ]
})

// 人物数据
const persons = ref([
  {
    id: 1,
    name: '李某',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    description: '主要犯罪嫌疑人，男，35岁，负责组织策划诈骗活动，分配任务，统筹资金流向。',
    tags: [
      { name: '主犯', type: 'danger' },
      { name: '有前科', type: 'warning' },
      { name: '团伙头目', type: 'primary' }
    ],
    evidences: [
      {
        title: '银行流水记录',
        description: '2022年12月至2024年2月期间，李某银行账户收到大量转账，金额合计约300万元。',
        files: [
          { name: '银行流水明细.pdf', url: '#' },
          { name: '资金流向分析.xlsx', url: '#' }
        ]
      },
      {
        title: '通讯记录',
        description: '李某与其他嫌疑人频繁联系，通话记录显示在案发前一周内有超过50次通话。',
        files: [
          { name: '通话记录.pdf', url: '#' },
          { name: '短信记录.pdf', url: '#' }
        ]
      },
      {
        title: '网络活动记录',
        description: '李某使用多个社交媒体账号联系受害人，发布虚假投资信息。',
        files: [
          { name: '社交媒体截图.zip', url: '#' },
          { name: '聊天记录.pdf', url: '#' }
        ]
      }
    ]
  },
  {
    id: 2,
    name: '张某',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    description: '犯罪嫌疑人，男，28岁，负责网络技术支持，搭建虚假投资平台，管理受害人信息。',
    tags: [
      { name: '从犯', type: 'warning' },
      { name: '技术支持', type: 'info' }
    ],
    evidences: [
      {
        title: '电子设备',
        description: '在张某住所查获笔记本电脑2台，手机3部，内含虚假投资平台源代码及受害人信息。',
        files: [
          { name: '电子设备清单.pdf', url: '#' },
          { name: '数据分析报告.pdf', url: '#' }
        ]
      },
      {
        title: '网站源代码',
        description: '张某开发的虚假投资平台源代码，包含后台管理系统和数据库。',
        files: [
          { name: '源代码分析.pdf', url: '#' },
          { name: '网站截图.zip', url: '#' }
        ]
      }
    ]
  },
  {
    id: 3,
    name: '赵某',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    description: '犯罪嫌疑人，女，32岁，负责与受害人联系，诱导受害人投资，提供虚假投资咨询。',
    tags: [
      { name: '从犯', type: 'warning' },
      { name: '话务员', type: 'info' }
    ],
    evidences: [
      {
        title: '话术脚本',
        description: '在赵某住所查获话术脚本，详细记录了如何诱导受害人投资的话术技巧。',
        files: [
          { name: '话术脚本.pdf', url: '#' },
          { name: '培训材料.pdf', url: '#' }
        ]
      },
      {
        title: '通讯记录',
        description: '赵某与多名受害人的通话记录，时长累计超过100小时。',
        files: [
          { name: '通话记录分析.pdf', url: '#' }
        ]
      }
    ]
  }
])

// 人物画像相关
const selectedPerson = ref('')
const currentPerson = computed(() => {
  if (!selectedPerson.value) return null
  return persons.value.find(p => p.id === selectedPerson.value)
})

const handlePersonChange = (personId) => {
  selectedPerson.value = personId
}

const viewFile = (file) => {
  console.log('查看文件:', file)
  // 实际项目中可以打开文件或下载文件
  ElMessage.success(`正在打开文件: ${file.name}`)
}

// 人物轨迹相关
const selectedPersonTrack = ref('')
const trackTimeline = ref([])
const trackTimelineValue = ref(0)
const currentTrackTime = ref('')
let trackMap = null
let bmapLoaded = ref(false)

// 加载百度地图API
const loadBMapScript = () => {
  if (document.getElementById('bmapScript')) {
    bmapLoaded.value = true
    return Promise.resolve()
  }
  
  return new Promise((resolve, reject) => {
    const script = document.createElement('script')
    script.id = 'bmapScript'
    script.type = 'text/javascript'
    script.src = 'https://api.map.baidu.com/api?v=3.0&ak='+baiduMapKey+'&callback=initBMapCallback'
    script.onerror = reject
    document.head.appendChild(script)
    
    window.initBMapCallback = () => {
      // 百度地图加载完成后，再加载ECharts的百度地图扩展
      import('echarts/extension/bmap/bmap').then(() => {
        bmapLoaded.value = true
        resolve()
      }).catch(reject)
    }
  })
}

const loadPersonTrack = (personId) => {
  // 确保百度地图API已加载
  if (!bmapLoaded.value) {
    loadBMapScript().then(() => {
      loadPersonTrackData(personId)
    }).catch(err => {
      console.error('加载百度地图API失败:', err)
      ElMessage.error('加载地图失败，请刷新页面重试')
    })
  } else {
    loadPersonTrackData(personId)
  }
}

const loadPersonTrackData = (personId) => {
  // 模拟加载人物轨迹数据
  const tracks = [
    {
      personId: 1,
      tracks: [
        { time: '2024-01-01 08:30', location: [116.404, 39.915], address: '北京市东城区某小区' },
        { time: '2024-01-01 10:15', location: [116.415, 39.920], address: '北京市朝阳区某咖啡厅' },
        { time: '2024-01-01 12:30', location: [116.425, 39.918], address: '北京市朝阳区某餐厅' },
        { time: '2024-01-01 14:45', location: [116.435, 39.912], address: '北京市朝阳区某写字楼' },
        { time: '2024-01-01 18:20', location: [116.410, 39.905], address: '北京市西城区某商场' },
        { time: '2024-01-01 20:30', location: [116.404, 39.915], address: '北京市东城区某小区' }
      ]
    },
    {
      personId: 2,
      tracks: [
        { time: '2024-01-01 09:00', location: [116.380, 39.900], address: '北京市西城区某小区' },
        { time: '2024-01-01 10:30', location: [116.435, 39.912], address: '北京市朝阳区某写字楼' },
        { time: '2024-01-01 13:00', location: [116.425, 39.918], address: '北京市朝阳区某餐厅' },
        { time: '2024-01-01 15:30', location: [116.435, 39.912], address: '北京市朝阳区某写字楼' },
        { time: '2024-01-01 19:00', location: [116.380, 39.900], address: '北京市西城区某小区' }
      ]
    },
    {
      personId: 3,
      tracks: [
        { time: '2024-01-01 08:45', location: [116.350, 39.930], address: '北京市海淀区某小区' },
        { time: '2024-01-01 10:00', location: [116.435, 39.912], address: '北京市朝阳区某写字楼' },
        { time: '2024-01-01 12:15', location: [116.425, 39.918], address: '北京市朝阳区某餐厅' },
        { time: '2024-01-01 14:30', location: [116.435, 39.912], address: '北京市朝阳区某写字楼' },
        { time: '2024-01-01 18:45', location: [116.350, 39.930], address: '北京市海淀区某小区' }
      ]
    }
  ]

  const personTrack = tracks.find(t => t.personId === personId)
  if (personTrack && personTrack.tracks.length > 0) {
    trackTimeline.value = personTrack.tracks
    trackTimelineValue.value = 0
    currentTrackTime.value = personTrack.tracks[0].time
    initTrackMap()
    updateTrackMap(0)
  } else {
    // Handle case when no tracks are found
    trackTimeline.value = []
    trackTimelineValue.value = 0
    currentTrackTime.value = ''
    initTrackMap()
    // Clear the map or show a message
    if (trackMap) {
      trackMap.setOption({
        bmap: {
          center: [116.404, 39.915],
          zoom: 12
        },
        series: []
      })
    }
  }
}

const formatTrackTime = (index) => {
  if (trackTimeline.value[index]) {
    return trackTimeline.value[index].time
  }
  return ''
}

const initTrackMap = () => {
  if (!trackMap) {
    trackMap = echarts.init(document.getElementById('track-map'))
  }
}

const updateTrackMap = (index) => {
  if (!trackMap || !trackTimeline.value.length) return

  const currentTrack = trackTimeline.value[index]
  currentTrackTime.value = currentTrack.time

  // 构建轨迹路径
  const lines = []
  const points = []
  
  for (let i = 0; i <= index; i++) {
    const point = trackTimeline.value[i]
    points.push({
      name: point.time,
      value: [...point.location, i === index ? 100 : 1, point.address]
    })
    
    if (i > 0) {
      lines.push([
        trackTimeline.value[i-1].location,
        point.location
      ])
    }
  }

  const option = {
    bmap: {
      center: currentTrack.location,
      zoom: 14,
      roam: true,
      mapStyle: {
        styleJson: [
          {
            'featureType': 'water',
            'elementType': 'all',
            'stylers': { 'color': '#d1d1d1' }
          }
        ]
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: params => {
        if (params.seriesType === 'scatter') {
          return `${params.value[4]}<br/>时间: ${params.name}`
        }
        return params.name
      }
    },
    series: [
      {
        type: 'scatter',
        coordinateSystem: 'bmap',
        data: points,
        symbolSize: val => val[3],
        label: {
          show: true,
          formatter: '{b}',
          position: 'right'
        },
        itemStyle: {
          color: '#409EFF'
        },
        emphasis: {
          label: {
            show: true
          }
        }
      },
      {
        type: 'lines',
        coordinateSystem: 'bmap',
        data: lines.map(line => ({
          coords: line
        })),
        lineStyle: {
          color: '#409EFF',
          width: 2,
          curveness: 0.2
        },
        effect: {
          show: true,
          period: 6,
          trailLength: 0.7,
          color: '#fff',
          symbolSize: 3
        }
      }
    ]
  }

  trackMap.setOption(option)
}

// 人物行为分析相关
const selectedPersonBehavior = ref('')
const currentPersonBehavior = ref(null)
let behaviorChart1 = null
let behaviorChart2 = null

const loadPersonBehavior = (personId) => {
  // 模拟加载人物行为分析数据
  const behaviors = [
    {
      personId: 1,
      features: [
        { label: '活动频率', value: '高频率，每日活跃时间超过12小时' },
        { label: '社交网络', value: '广泛，与案件相关人员联系密切' },
        { label: '资金流动', value: '频繁，短期内大额资金进出' },
        { label: '行为特征', value: '组织协调能力强，决策果断，警惕性高' }
      ],
      patterns: [
        { type: '资金转移', description: '通过多个账户快速转移资金，规避监管', confidence: 95 },
        { type: '组织协调', description: '定期组织团队会议，分配任务，检查进度', confidence: 90 },
        { type: '身份隐藏', description: '使用多个虚假身份，频繁更换通讯工具', confidence: 85 },
        { type: '目标筛选', description: '针对特定人群精准诈骗，提高成功率', confidence: 80 }
      ],
      chartData1: {
        categories: ['通讯频次', '资金流动', '网络活动', '社交关系', '位置变动'],
        values: [95, 90, 85, 80, 70]
      },
      chartData2: {
        times: ['2022-12', '2024-01', '2024-02'],
        values: [
          [30, 45, 20],
          [25, 50, 15],
          [20, 40, 30]
        ],
        types: ['通讯活动', '网络活动', '资金活动']
      }
    },
    {
      personId: 2,
      features: [
        { label: '活动频率', value: '中等频率，工作时间活跃' },
        { label: '社交网络', value: '有限，主要与团队成员联系' },
        { label: '资金流动', value: '较少，主要接收固定金额' },
        { label: '行为特征', value: '技术能力强，行为隐蔽，较少外出' }
      ],
      patterns: [
        { type: '技术支持', description: '负责维护虚假投资平台，处理技术问题', confidence: 95 },
        { type: '数据管理', description: '收集整理受害人信息，分析投资行为', confidence: 90 },
        { type: '安全防护', description: '使用VPN和加密通讯，规避监控', confidence: 85 }
      ],
      chartData1: {
        categories: ['通讯频次', '资金流动', '网络活动', '社交关系', '位置变动'],
        values: [60, 50, 95, 40, 30]
      },
      chartData2: {
        times: ['2022-12', '2024-01', '2024-02'],
        values: [
          [15, 20, 10],
          [50, 60, 45],
          [10, 15, 5]
        ],
        types: ['通讯活动', '网络活动', '资金活动']
      }
    },
    {
      personId: 3,
      features: [
        { label: '活动频率', value: '高频率，主要在白天活跃' },
        { label: '社交网络', value: '广泛，与大量潜在受害人联系' },
        { label: '资金流动', value: '中等，定期收取佣金' },
        { label: '行为特征', value: '沟通能力强，善于说服，情绪稳定' }
      ],
      patterns: [
        { type: '话术诱导', description: '使用专业话术诱导受害人投资', confidence: 95 },
        { type: '客户维护', description: '定期联系受害人，维持信任关系', confidence: 90 },
        { type: '情绪控制', description: '应对受害人质疑，安抚不满情绪', confidence: 85 }
      ],
      chartData1: {
        categories: ['通讯频次', '资金流动', '网络活动', '社交关系', '位置变动'],
        values: [95, 60, 70, 85, 50]
      },
      chartData2: {
        times: ['2022-12', '2024-01', '2024-02'],
        values: [
          [60, 75, 40],
          [30, 35, 25],
          [15, 20, 10]
        ],
        types: ['通讯活动', '网络活动', '资金活动']
      }
    }
  ]

  const personBehavior = behaviors.find(b => b.personId === personId)
  if (personBehavior) {
    currentPersonBehavior.value = personBehavior
    nextTick(() => {
      initBehaviorCharts()
    })
  } else {
    currentPersonBehavior.value = null
  }
}

const initBehaviorCharts = () => {
  if (!currentPersonBehavior.value) return

  // 雷达图 - 行为特征
  if (!behaviorChart1) {
    behaviorChart1 = echarts.init(document.getElementById('behavior-chart-1'))
  }
  
  const radarOption = {
    title: {
      text: '行为特征雷达图'
    },
    tooltip: {},
    radar: {
      indicator: currentPersonBehavior.value.chartData1.categories.map(category => ({
        name: category,
        max: 100
      }))
    },
    series: [{
      name: '行为特征',
      type: 'radar',
      data: [
        {
          value: currentPersonBehavior.value.chartData1.values,
          name: '特征指数',
          areaStyle: {
            color: 'rgba(64, 158, 255, 0.6)'
          }
        }
      ]
    }]
  }
  behaviorChart1.setOption(radarOption)

  // 柱状图 - 行为趋势
  if (!behaviorChart2) {
    behaviorChart2 = echarts.init(document.getElementById('behavior-chart-2'))
  }
  
  const barOption = {
    title: {
      text: '行为趋势分析'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: currentPersonBehavior.value.chartData2.types
    },
    xAxis: {
      type: 'category',
      data: currentPersonBehavior.value.chartData2.times
    },
    yAxis: {
      type: 'value'
    },
    series: currentPersonBehavior.value.chartData2.types.map((type, index) => ({
      name: type,
      type: 'bar',
      data: currentPersonBehavior.value.chartData2.values[index]
    }))
  }
  behaviorChart2.setOption(barOption)
}

const getConfidenceColor = (confidence) => {
  if (confidence >= 90) return '#67C23A'
  if (confidence >= 70) return '#E6A23C'
  return '#F56C6C'
}

// 人物组织关系图
let relationGraph = null

const initRelationGraph = () => {
  if (!relationGraph) {
    relationGraph = echarts.init(document.getElementById('relation-graph'))
  }
  
  const option = {
    title: {
      text: '案件人物关系网络'
    },
    tooltip: {},
    animationDurationUpdate: 1500,
    animationEasingUpdate: 'quinticInOut',
    series: [
      {
        type: 'graph',
        layout: 'force',
        symbolSize: 50,
        roam: true,
        label: {
          show: true
        },
        edgeSymbol: ['circle', 'arrow'],
        edgeSymbolSize: [4, 10],
        edgeLabel: {
          fontSize: 12
        },
        data: [
          {
            name: '李某',
            symbolSize: 70,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '张某',
            symbolSize: 50,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '赵某',
            symbolSize: 50,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '王某',
            symbolSize: 40,
            itemStyle: {
              color: '#E6A23C'
            }
          },
          {
            name: '刘某',
            symbolSize: 40,
            itemStyle: {
              color: '#E6A23C'
            }
          },
          {
            name: '陈某',
            symbolSize: 40,
            itemStyle: {
              color: '#E6A23C'
            }
          },
          {
            name: '周某',
            symbolSize: 30,
            itemStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '吴某',
            symbolSize: 30,
            itemStyle: {
              color: '#67C23A'
            }
          }
        ],
        links: [
          {
            source: '李某',
            target: '张某',
            label: {
              show: true,
              formatter: '上下级'
            }
          },
          {
            source: '李某',
            target: '赵某',
            label: {
              show: true,
              formatter: '上下级'
            }
          },
          {
            source: '张某',
            target: '赵某',
            label: {
              show: true,
              formatter: '同事'
            }
          },
          {
            source: '赵某',
            target: '王某',
            label: {
              show: true,
              formatter: '诈骗'
            }
          },
          {
            source: '赵某',
            target: '刘某',
            label: {
              show: true,
              formatter: '诈骗'
            }
          },
          {
            source: '赵某',
            target: '陈某',
            label: {
              show: true,
              formatter: '诈骗'
            }
          },
          {
            source: '李某',
            target: '周某',
            label: {
              show: true,
              formatter: '朋友'
            }
          },
          {
            source: '李某',
            target: '吴某',
            label: {
              show: true,
              formatter: '朋友'
            }
          }
        ],
        lineStyle: {
          opacity: 0.9,
          width: 2,
          curveness: 0
        }
      }
    ]
  }
  
  relationGraph.setOption(option)
}

// 资产情况
const assetsSummary = ref([
  { name: '现金资产', value: '¥2,500,000', icon: 'Money' },
  { name: '银行账户', value: '12个', icon: 'CreditCard' },
  { name: '房产', value: '3套', icon: 'House' },
  { name: '车辆', value: '2辆', icon: 'Van' },
  { name: '公司股份', value: '2家', icon: 'OfficeBuilding' },
  { name: '其他资产', value: '¥800,000', icon: 'Goods' }
])

let assetsFlowChart = null

const initAssetsFlowChart = () => {
  if (!assetsFlowChart) {
    assetsFlowChart = echarts.init(document.getElementById('assets-flow-chart'))
  }
  
  const option = {
    title: {
      text: '资金流向分析'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    series: [
      {
        name: '资金来源',
        type: 'sankey',
        layout: 'none',
        emphasis: {
          focus: 'adjacency'
        },
        data: [
          { name: '受害人A' },
          { name: '受害人B' },
          { name: '受害人C' },
          { name: '受害人D' },
          { name: '李某账户' },
          { name: '张某账户' },
          { name: '赵某账户' },
          { name: '境外账户A' },
          { name: '境外账户B' },
          { name: '现金提取' },
          { name: '房产购买' },
          { name: '车辆购买' },
          { name: '奢侈品' }
        ],
        links: [
          { source: '受害人A', target: '李某账户', value: 1000000 },
          { source: '受害人B', target: '李某账户', value: 800000 },
          { source: '受害人C', target: '张某账户', value: 600000 },
          { source: '受害人D', target: '赵某账户', value: 500000 },
          { source: '李某账户', target: '境外账户A', value: 1200000 },
          { source: '李某账户', target: '现金提取', value: 300000 },
          { source: '张某账户', target: '境外账户B', value: 400000 },
          { source: '张某账户', target: '李某账户', value: 200000 },
          { source: '赵某账户', target: '李某账户', value: 300000 },
          { source: '赵某账户', target: '现金提取', value: 200000 },
          { source: '境外账户A', target: '房产购买', value: 800000 },
          { source: '境外账户A', target: '车辆购买', value: 400000 },
          { source: '境外账户B', target: '奢侈品', value: 400000 }
        ]
      }
    ]
  }
  
  assetsFlowChart.setOption(option)
}

// 异常行为预警
const warningList = ref([
  {
    time: '2024-03-05 14:30',
    person: '李某',
    type: '资金异常',
    title: '短时间内大额资金转移',
    detail: '检测到李某账户在2小时内向多个境外账户转移资金共计100万元，交易行为异常。',
    level: 5,
    status: '未处理'
  },
  {
    time: '2024-03-04 10:15',
    person: '张某',
    type: '位置异常',
    title: '频繁更换活动地点',
    detail: '张某在过去24小时内更换了5个不同活动地点，行为轨迹异常。',
    level: 4,
    status: '已确认'
  },
    {
    time: '2024-03-03 16:45',
    person: '赵某',
    type: '通讯异常',
    title: '使用多个未登记手机号',
    detail: '赵某在过去一周内使用了3个未在系统中登记的手机号码进行通讯，疑似规避监控。',
    level: 3,
    status: '已忽略'
  },
  {
    time: '2024-03-02 09:20',
    person: '李某',
    type: '网络异常',
    title: '访问加密通讯网站',
    detail: '李某频繁访问多个加密通讯网站，疑似进行秘密联络。',
    level: 4,
    status: '已上报'
  },
  {
    time: '2024-03-01 11:30',
    person: '张某',
    type: '行为异常',
    title: '频繁删除数据',
    detail: '张某在过去24小时内频繁删除电脑和手机中的数据，疑似销毁证据。',
    level: 5,
    status: '已确认'
  }
])

const getWarningTypeTag = (type) => {
  const typeMap = {
    '资金异常': 'danger',
    '位置异常': 'warning',
    '通讯异常': 'info',
    '网络异常': 'primary',
    '行为异常': 'danger'
  }
  return typeMap[type] || 'info'
}

const getWarningStatusTag = (status) => {
  const statusMap = {
    '未处理': 'info',
    '已确认': 'success',
    '已忽略': 'warning',
    '已上报': 'danger'
  }
  return statusMap[status] || 'info'
}

const handleWarning = (warning, action) => {
  console.log('处理预警:', warning, '操作:', action)
  
  // 根据操作更新预警状态
  if (action === 'confirm') {
    warning.status = '已确认'
    ElMessage.success('预警已确认')
  } else if (action === 'ignore') {
    warning.status = '已忽略'
    ElMessage.info('预警已忽略')
  } else if (action === 'report') {
    warning.status = '已上报'
    ElMessage.warning('预警已上报')
  }
}

// 8. 案件分析报告相关
const reportInfo = ref({
  reportId: 'DXWL20240001-R001',
  generateTime: '2024-03-10 15:30:45',
  investigator: '张警官',
  dataHash: 'e7c6f4e3d2b1a0z9y8x7w6v5u4t3s2r1q0p9o8n7m6l5k4j3h2g1f0',
  timestamp: '2024-03-10T15:30:45.123Z',
  reportHash: 'a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6',
  process: '本案电子数据取证过程严格遵循《电子数据取证规范》，采用专业取证设备和软件工具，对涉案人员的电子设备、通讯记录、网络活动和资金流向等进行全面采集和分析。取证过程全程录像，并由两名以上取证人员在场见证，确保证据的合法性和完整性。',
  conclusion: '经过对本案电子数据的全面分析，认定犯罪嫌疑人李某、张某、赵某组成诈骗团伙，通过网络平台实施投资理财诈骗。李某作为主要组织者，负责统筹安排；张某负责技术支持，开发和维护虚假投资平台；赵某负责与受害人联系，诱导其投资。三人分工明确，相互配合，共同实施诈骗活动，涉案金额达500余万元。',
  evidenceList: [
    { name: '李某手机数据', type: '电子设备', source: '现场扣押', hash: 'a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6', time: '2024-02-05 10:30' },
    { name: '张某电脑数据', type: '电子设备', source: '现场扣押', hash: 'b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7', time: '2024-02-10 09:15' },
    { name: '虚假投资平台源代码', type: '电子数据', source: '张某电脑', hash: 'c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8', time: '2024-02-12 14:20' },
    { name: '银行流水记录', type: '金融数据', source: '银行提供', hash: 'd4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9', time: '2024-02-15 11:45' },
    { name: '通讯记录', type: '通讯数据', source: '运营商提供', hash: 'e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0', time: '2024-02-18 16:30' }
  ]
})

const reportFormat = ref('pdf')
const reportLanguage = ref('zh')

const generateReport = () => {
  ElMessage.success(`正在生成${reportLanguage.value === 'zh' ? '中文' : '英文'}${reportFormat.value.toUpperCase()}格式报告...`)
  setTimeout(() => {
    ElMessage.success('报告生成成功！')
  }, 1500)
}

const previewReport = () => {
  ElMessage.info('正在打开报告预览...')
}

const downloadReport = () => {
  ElMessage.success(`正在下载${reportLanguage.value === 'zh' ? '中文' : '英文'}${reportFormat.value.toUpperCase()}格式报告...`)
}


// 9. 关联证据拓扑相关
const topologyView = ref('all')
const topologyFocus = ref('')
const selectedNode = ref(null)
let topologyGraph = null

// 拓扑图数据
const topologyEvidences = ref([
  { id: 1, name: '李某手机数据', evidenceType: '电子设备', collectTime: '2024-02-05 10:30', hash: 'a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6', description: '从李某处扣押的iPhone 13手机，内含大量与案件相关的通讯记录、照片和文档。' },
  { id: 2, name: '张某电脑数据', evidenceType: '电子设备', collectTime: '2024-02-10 09:15', hash: 'b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7', description: '从张某处扣押的笔记本电脑，内含虚假投资平台源代码及受害人信息数据库。' },
  { id: 3, name: '虚假投资平台源代码', evidenceType: '电子数据', collectTime: '2024-02-12 14:20', hash: 'c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8', description: '张某开发的虚假投资平台源代码，包含后台管理系统和数据库。' },
  { id: 4, name: '银行流水记录', evidenceType: '金融数据', collectTime: '2024-02-15 11:45', hash: 'd4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9', description: '涉案人员的银行账户流水记录，显示资金往来情况。' },
  { id: 5, name: '通讯记录', evidenceType: '通讯数据', collectTime: '2024-02-18 16:30', hash: 'e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0', description: '涉案人员的通话记录、短信记录等通讯数据。' }
])

const topologyAssets = ref([
  { id: 1, name: '李某银行账户', assetType: '银行账户', value: '¥1,500,000', source: '犯罪所得', description: '李某名下的主要银行账户，接收大部分诈骗资金。' },
  { id: 2, name: '张某银行账户', assetType: '银行账户', value: '¥600,000', source: '犯罪所得', description: '张某名下的银行账户，主要用于接收技术服务费。' },
  { id: 3, name: '赵某银行账户', assetType: '银行账户', value: '¥400,000', source: '犯罪所得', description: '赵某名下的银行账户，主要用于接收佣金。' },
  { id: 4, name: '北京市朝阳区房产', assetType: '房产', value: '¥8,000,000', source: '部分犯罪所得', description: '李某名下的房产，位于北京市朝阳区，部分使用诈骗资金支付。' },
  { id: 5, name: '奔驰轿车', assetType: '车辆', value: '¥800,000', source: '犯罪所得', description: '李某名下的奔驰轿车，使用诈骗资金全额购买。' }
])

// 拓扑图节点和关系数据
const getTopologyData = () => {
  // 基础节点数据
  const nodes = []
  const links = []
  
  // 根据视图类型筛选节点
  if (topologyView.value === 'all' || topologyView.value === 'person') {
    persons.value.forEach(person => {
      nodes.push({
        id: `person-${person.id}`,
        name: person.name,
        symbolSize: 50,
        category: 0,
        type: 'person',
        role: person.tags[0]?.name || '未知角色',
        description: person.description,
        relatedPersons: [1, 2, 3].filter(id => id !== person.id).map(id => `person-${id}`),
        relatedEvidences: [1, 2, 3, 4, 5].map(id => `evidence-${id}`),
        relatedAssets: [1, 2, 3, 4, 5].map(id => `asset-${id}`)
      })
    })
  }
  
  if (topologyView.value === 'all' || topologyView.value === 'evidence') {
    topologyEvidences.value.forEach(evidence => {
      nodes.push({
        id: `evidence-${evidence.id}`,
        name: evidence.name,
        symbolSize: 40,
        category: 1,
        type: 'evidence',
        evidenceType: evidence.evidenceType,
        collectTime: evidence.collectTime,
        hash: evidence.hash,
        description: evidence.description
      })
    })
  }
  
  if (topologyView.value === 'all' || topologyView.value === 'asset') {
    topologyAssets.value.forEach(asset => {
      nodes.push({
        id: `asset-${asset.id}`,
        name: asset.name,
        symbolSize: 40,
        category: 2,
        type: 'asset',
        assetType: asset.assetType,
        value: asset.value,
        source: asset.source,
        description: asset.description
      })
    })
  }
  
  // 生成关系连接
  // 人物与人物关系
  if (topologyView.value === 'all' || topologyView.value === 'person') {
    links.push(
      { source: 'person-1', target: 'person-2', value: '上下级' },
      { source: 'person-1', target: 'person-3', value: '上下级' },
      { source: 'person-2', target: 'person-3', value: '同事' }
    )
  }
  
  // 人物与证据关系
  if ((topologyView.value === 'all' || topologyView.value === 'person' || topologyView.value === 'evidence') && 
      nodes.some(n => n.type === 'person') && nodes.some(n => n.type === 'evidence')) {
    links.push(
      { source: 'person-1', target: 'evidence-1', value: '持有' },
      { source: 'person-1', target: 'evidence-4', value: '关联' },
      { source: 'person-1', target: 'evidence-5', value: '关联' },
      { source: 'person-2', target: 'evidence-2', value: '持有' },
      { source: 'person-2', target: 'evidence-3', value: '开发' },
      { source: 'person-2', target: 'evidence-5', value: '关联' },
      { source: 'person-3', target: 'evidence-5', value: '关联' }
    )
  }
  
  // 人物与资产关系
  if ((topologyView.value === 'all' || topologyView.value === 'person' || topologyView.value === 'asset') && 
      nodes.some(n => n.type === 'person') && nodes.some(n => n.type === 'asset')) {
    links.push(
      { source: 'person-1', target: 'asset-1', value: '拥有' },
      { source: 'person-1', target: 'asset-4', value: '拥有' },
      { source: 'person-1', target: 'asset-5', value: '拥有' },
      { source: 'person-2', target: 'asset-2', value: '拥有' },
      { source: 'person-3', target: 'asset-3', value: '拥有' }
    )
  }
  
  // 证据与资产关系
  if ((topologyView.value === 'all' || topologyView.value === 'evidence' || topologyView.value === 'asset') && 
      nodes.some(n => n.type === 'evidence') && nodes.some(n => n.type === 'asset')) {
    links.push(
      { source: 'evidence-4', target: 'asset-1', value: '证明' },
      { source: 'evidence-4', target: 'asset-2', value: '证明' },
      { source: 'evidence-4', target: 'asset-3', value: '证明' }
    )
  }
  
  // 如果有焦点，筛选与焦点相关的节点和连接
  if (topologyFocus.value) {
    const relatedNodeIds = new Set()
    relatedNodeIds.add(topologyFocus.value)
    
    // 找出与焦点直接相关的节点
    links.forEach(link => {
      if (link.source === topologyFocus.value) {
        relatedNodeIds.add(link.target)
      } else if (link.target === topologyFocus.value) {
        relatedNodeIds.add(link.source)
      }
    })
    
    // 筛选节点和连接
    const filteredNodes = nodes.filter(node => relatedNodeIds.has(node.id))
    const filteredLinks = links.filter(link => 
      relatedNodeIds.has(link.source) && relatedNodeIds.has(link.target)
    )
    
    return { nodes: filteredNodes, links: filteredLinks }
  }
  
  return { nodes, links }
}

const initTopologyGraph = () => {
  if (!topologyGraph) {
    topologyGraph = echarts.init(document.getElementById('topology-graph'))
    
    // 添加点击事件
    topologyGraph.on('click', params => {
      if (params.dataType === 'node') {
        const nodeId = params.data.id
        const node = getTopologyData().nodes.find(n => n.id === nodeId)
        if (node) {
          selectedNode.value = node
        }
      }
    })
  }
  
  updateTopologyGraph()
  
  // 添加窗口大小变化监听
  window.addEventListener('resize', () => {
    topologyGraph.resize()
  })
}

const updateTopologyGraph = () => {
  if (!topologyGraph) return
  
  const { nodes, links } = getTopologyData()
  
  const option = {
    title: {
      text: '案件关联证据拓扑图',
      subtext: '点击节点查看详情',
      top: 'top',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: params => {
        if (params.dataType === 'node') {
          return `${params.data.name}<br/>${getNodeTypeLabel(params.data.type)}`
        } else if (params.dataType === 'edge') {
          return params.data.value
        }
        return params.name
      }
    },
    legend: {
      data: ['人物', '证据', '资产'],
      bottom: 10
    },
    series: [
      {
        type: 'graph',
        layout: 'force',
        data: nodes,
        links: links,
        categories: [
          { name: '人物' },
          { name: '证据' },
          { name: '资产' }
        ],
        roam: true,
        label: {
          show: true,
          position: 'right'
        },
        force: {
          repulsion: 100,
          edgeLength: 100
        },
        edgeLabel: {
          show: true,
          formatter: '{c}',
          fontSize: 12
        },
        lineStyle: {
          color: 'source',
          curveness: 0.3
        },
        emphasis: {
          focus: 'adjacency',
          lineStyle: {
            width: 4
          }
        },
        itemStyle: {
          color: params => {
            const type = params.data.type
            if (type === 'person') return '#409EFF'
            if (type === 'evidence') return '#67C23A'
            if (type === 'asset') return '#E6A23C'
            return '#909399'
          }
        }
      }
    ]
  }
  
  topologyGraph.setOption(option)
}

const updateTopologyFocus = () => {
  updateTopologyGraph()
}

const getNodeTypeTag = (type) => {
  const typeMap = {
    'person': 'primary',
    'evidence': 'success',
    'asset': 'warning'
  }
  return typeMap[type] || 'info'
}

const getNodeTypeLabel = (type) => {
  const typeMap = {
    'person': '人物',
    'evidence': '证据',
    'asset': '资产'
  }
  return typeMap[type] || '未知'
}

const viewPersonDetail = (id) => {
  const personId = parseInt(id.split('-')[1])
  selectedPerson.value = personId
  ElMessage.success(`查看人物详情: ${selectedNode.value.name}`)
  // 可以跳转到人物详情页或打开详情弹窗
}

const viewPersonTrack = (id) => {
  const personId = parseInt(id.split('-')[1])
  selectedPersonTrack.value = personId
  loadPersonTrack(personId)
  ElMessage.success(`查看人物轨迹: ${selectedNode.value.name}`)
  // 可以跳转到人物轨迹页或打开轨迹弹窗
}

const viewEvidenceDetail = (id) => {
  const evidenceId = parseInt(id.split('-')[1])
  ElMessage.success(`查看证据详情: ${selectedNode.value.name}`)
  // 可以跳转到证据详情页或打开详情弹窗
}

const downloadEvidence = (id) => {
  const evidenceId = parseInt(id.split('-')[1])
  ElMessage.success(`下载证据: ${selectedNode.value.name}`)
  // 实现证据下载功能
}

const viewAssetDetail = (id) => {
  const assetId = parseInt(id.split('-')[1])
  ElMessage.success(`查看资产详情: ${selectedNode.value.name}`)
  // 可以跳转到资产详情页或打开详情弹窗
}


// 页面初始化
onMounted(() => {
  // 初始化各个图表
  nextTick(() => {
    // 初始化拓扑图
    initTopologyGraph()
    
    // 加载百度地图API
    loadBMapScript().then(() => {
      // 初始化其他图表
      initRelationGraph()
      initAssetsFlowChart()
    }).catch(err => {
      console.error('加载百度地图API失败:', err)
      // 即使百度地图加载失败，也初始化其他图表
      initRelationGraph()
      initAssetsFlowChart()
    })
  })
})

// 监听视图类型变化
watch(topologyView, () => {
  updateTopologyGraph()
})

// 监听焦点变化
watch(topologyFocus, () => {
  updateTopologyFocus()
})

// 监听窗口大小变化
window.addEventListener('resize', () => {
  if (topologyGraph) topologyGraph.resize()
  if (relationGraph) relationGraph.resize()
  if (assetsFlowChart) assetsFlowChart.resize()
  if (behaviorChart1) behaviorChart1.resize()
  if (behaviorChart2) behaviorChart2.resize()
  if (trackMap) trackMap.resize()
})

// 监听人物选择变化
watch(selectedPerson, (newVal) => {
  if (newVal) {
    // 加载人物行为分析数据
    loadPersonBehavior(newVal)
  }
})

// 监听人物轨迹时间线变化
watch(trackTimelineValue, (newVal) => {
  updateTrackMap(newVal)
})


</script>

<style scoped>
.case-analysis-container {
  padding: 20px;
}

.case-header {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-header h2, .card-header h3 {
  margin: 0;
}

.case-basic-info {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
}

.info-item {
  margin-right: 30px;
  margin-bottom: 10px;
}

.label {
  font-weight: bold;
  margin-right: 5px;
}

.analysis-card {
  margin-bottom: 20px;
}

.case-info-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.case-description h4, .case-timeline h4 {
  margin-top: 0;
  margin-bottom: 10px;
}

.person-profile {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.person-basic {
  display: flex;
  gap: 20px;
}

.person-info {
  flex: 1;
}

.person-info h4 {
  margin-top: 0;
  margin-bottom: 10px;
}

.person-tags {
  margin-top: 10px;
}

.person-tag {
  margin-right: 5px;
  margin-bottom: 5px;
}

.person-evidences h4 {
  margin-top: 0;
  margin-bottom: 10px;
}

.evidence-content {
  padding: 10px;
}

.evidence-files {
  margin-top: 10px;
}

.track-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.track-map {
  height: 500px;
  width: 100%;
}

.track-timeline {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.track-time-display {
  text-align: center;
  margin-top: 10px;
  font-size: 16px;
  font-weight: bold;
}

.relation-graph {
  height: 600px;
  width: 100%;
}

.relation-legend {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  gap: 30px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.legend-color {
  width: 15px;
  height: 15px;
  border-radius: 50%;
}

.behavior-analysis {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.behavior-charts {
  display: flex;
  gap: 20px;
}

.behavior-chart {
  height: 400px;
  flex: 1;
}

.behavior-details {
  margin-top: 20px;
}

.behavior-details h4 {
  margin-top: 0;
  margin-bottom: 10px;
}

.assets-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.assets-summary {
  margin-bottom: 20px;
}

.asset-card {
  display: flex;
  align-items: center;
  padding: 20px;
  height: 100%;
}

.asset-icon {
  font-size: 30px;
  margin-right: 15px;
  color: #409EFF;
}

.asset-name {
  font-size: 16px;
  color: #606266;
  margin-bottom: 5px;
}

.asset-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.assets-flow h4 {
  margin-top: 0;
  margin-bottom: 10px;
}

.assets-flow-chart {
  height: 500px;
  width: 100%;
}

.warning-container {
  width: 100%;
}

.warning-detail {
  padding: 10px;
}

.warning-actions {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.topology-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.topology-controls {
  display: flex;
  gap: 20px;
  align-items: center;
  margin-bottom: 20px;
}

.topology-graph {
  height: 600px;
  width: 100%;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.topology-detail {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.detail-title {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.detail-title .el-tag {
  margin-left: 10px;
}

.detail-content {
  margin-bottom: 15px;
}

.detail-actions {
  margin-top: 15px;
}

.report-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.report-controls {
  display: flex;
  gap: 20px;
  align-items: center;
  margin-bottom: 20px;
}

.report-section {
  margin-bottom: 20px;
}

.report-section h4 {
  margin-top: 0;
  margin-bottom: 10px;
}

.report-evidence-list {
  margin-top: 15px;
}

.report-actions {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .behavior-charts {
    flex-direction: column;
  }
  
  .behavior-chart {
    height: 300px;
  }
  
  .person-basic {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}
</style>