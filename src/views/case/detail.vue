<template>
  <div class="case-detail-container">
    <!-- 1. 案件基本信息卡片 -->
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <h3>案件基本信息</h3>
        </div>
      </template>
      <div class="case-info">
        <div class="info-item">
          <span class="label">案件编号：</span>
          <span class="value">{{ caseInfo.caseNumber }}</span>
        </div>
        <div class="info-item">
          <span class="label">案件地点：</span>
          <span class="value">{{ caseInfo.location }}</span>
        </div>
        <div class="info-item">
          <span class="label">部署设备数量：</span>
          <span class="value">{{ caseInfo.deviceCount }}</span>
        </div>
        <div class="info-item">
          <span class="label">证据数量：</span>
          <span class="value">{{ caseInfo.evidenceCount }}</span>
        </div>
        <div class="info-item">
          <span class="label">违法分子数量：</span>
          <span class="value">{{ caseInfo.suspectCount }}</span>
        </div>
      </div>
    </el-card>

    <!-- 2. 探针设备部署地图 -->
    <el-card class="map-card">
      <template #header>
        <div class="card-header">
          <h3>探针设备部署地点</h3>
          <!-- <el-switch
            v-model="mapMode3D"
            active-text="3D模式"
            inactive-text="2D模式"
            @change="toggleMapMode"
          /> -->
        </div>
      </template>
      <div class="map-container" ref="mapContainer"></div>
      
      <!-- 当前选中设备的详细信息 -->
      <div v-if="selectedDevice" class="selected-device-info">
        <h4>设备详情</h4>
        <div class="info-item">
          <span class="label">设备ID：</span>
          <span class="value">{{ selectedDevice.id }}</span>
        </div>
        <div class="info-item">
          <span class="label">设备名称：</span>
          <span class="value">{{ selectedDevice.name }}</span>
        </div>
        <div class="info-item">
          <span class="label">部署位置：</span>
          <span class="value">{{ selectedDevice.location }}</span>
        </div>
        <div class="info-item">
          <span class="label">采集证据数：</span>
          <span class="value">{{ selectedDevice.evidenceCount }}</span>
        </div>
        <div class="info-item">
          <span class="label">运行状态：</span>
          <span class="value" :class="selectedDevice.status === '正常' ? 'status-normal' : 'status-warning'">
            {{ selectedDevice.status }}
          </span>
        </div>
      </div>
    </el-card>

    <!-- 3. 探针设备电子证据采集速度 -->
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <h3>探针设备电子证据采集速度</h3>
        </div>
      </template>
      <div class="chart-container" ref="speedChartContainer"></div>
    </el-card>

    <!-- 4. 探针设备电子证据采集数量 -->
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <h3>探针设备电子证据采集数量</h3>
          <el-date-picker
            v-model="timeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="updateEvidenceCountChart"
          />
        </div>
      </template>
      <div class="chart-container" ref="countChartContainer"></div>
    </el-card>

    <!-- 5. 探针设备电子证据采集信息 -->
    <el-card class="evidence-card">
      <template #header>
        <div class="card-header">
          <h3>探针设备电子证据采集信息</h3>
        </div>
      </template>
      
      <div class="device-list">
        <el-tabs type="border-card" @tab-click="handleTabClick">
          <el-tab-pane v-for="device in devices" :key="device.id" :label="device.name">
            <div class="evidence-stats">
              <div class="stats-section">
                <h4>设备类型分布</h4>
                <div class="pie-chart" :id="`device-type-chart-${device.id}`"></div>
              </div>
              <div class="stats-section">
                <h4>证据类型分布</h4>
                <div class="pie-chart" :id="`evidence-type-chart-${device.id}`"></div>
              </div>
            </div>
            
            <div class="evidence-table">
              <h4>最近采集证据</h4>
              <el-table :data="device.recentEvidence" stripe style="width: 100%">
                <el-table-column prop="time" label="采集时间" width="180" />
                <el-table-column prop="deviceType" label="设备类型" width="120" />
                <el-table-column prop="evidenceType" label="证据类型" width="120" />
                <el-table-column prop="description" label="描述" />
                <el-table-column prop="size" label="大小" width="100" />
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted, reactive } from 'vue'
import * as echarts from 'echarts'
import 'echarts-gl'

export default {
  name: 'CaseDetail',
  setup() {
    // 地图实例
    const map = ref(null)
    const mapContainer = ref(null)
    const mapMode3D = ref(false)
    
    // 图表实例
    const speedChart = ref(null)
    const countChart = ref(null)
    const speedChartContainer = ref(null)
    const countChartContainer = ref(null)
    
    // 时间范围选择
    const timeRange = ref([
      new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000),
      new Date()
    ])
    
    // 选中的设备
    const selectedDevice = ref(null)
    
    // 案件基本信息
    const caseInfo = reactive({
      caseNumber: 'CASE-2024-0001',
      location: '北京市海淀区某科技园区',
      deviceCount: 5,
      evidenceCount: 1243,
      suspectCount: 8
    })
    
    // 探针设备数据
    const devices = reactive([
      {
        id: 'device-001',
        name: '探针设备-001',
        location: '科技园区主楼1层',
        position: [116.307629, 40.058359],
        status: '正常',
        evidenceCount: 324,
        collectionSpeed: [10, 15, 20, 18, 25, 30, 28],
        collectionCount: [50, 80, 120, 160, 200, 250, 300],
        deviceTypes: [
          { name: '电脑', value: 45 },
          { name: '手机', value: 30 },
          { name: '服务器', value: 15 },
          { name: '监控', value: 10 }
        ],
        evidenceTypes: [
          { name: '聊天记录', value: 35 },
          { name: '银行卡账户', value: 20 },
          { name: '网页地址', value: 25 },
          { name: '数字货币', value: 10 },
          { name: '云数据', value: 10 }
        ],
        recentEvidence: [
          { time: '2024-06-01 10:23:45', deviceType: '手机', evidenceType: '聊天记录', description: 'WeChat聊天记录', size: '2.3MB' },
          { time: '2024-06-01 10:15:22', deviceType: '电脑', evidenceType: '网页地址', description: '访问记录', size: '1.5MB' },
          { time: '2024-06-01 09:58:11', deviceType: '服务器', evidenceType: '数字货币', description: '交易记录', size: '0.8MB' }
        ]
      },
      {
        id: 'device-002',
        name: '探针设备-002',
        location: '科技园区主楼3层',
        position: [116.308629, 40.057359],
        status: '正常',
        evidenceCount: 256,
        collectionSpeed: [8, 12, 16, 20, 18, 22, 25],
        collectionCount: [40, 70, 100, 140, 180, 210, 250],
        deviceTypes: [
          { name: '电脑', value: 50 },
          { name: '手机', value: 35 },
          { name: '服务器', value: 10 },
          { name: '监控', value: 5 }
        ],
        evidenceTypes: [
          { name: '聊天记录', value: 30 },
          { name: '银行卡账户', value: 25 },
          { name: '网页地址', value: 20 },
          { name: '数字货币', value: 15 },
          { name: '云数据', value: 10 }
        ],
        recentEvidence: [
          { time: '2024-06-01 10:20:15', deviceType: '电脑', evidenceType: '银行卡账户', description: '交易记录', size: '1.8MB' },
          { time: '2024-06-01 10:10:05', deviceType: '手机', evidenceType: '聊天记录', description: 'Telegram聊天记录', size: '3.2MB' },
          { time: '2024-06-01 09:55:30', deviceType: '监控', evidenceType: '云数据', description: '监控视频', size: '15.6MB' }
        ]
      },
      {
        id: 'device-003',
        name: '探针设备-003',
        location: '科技园区副楼1层',
        position: [116.309629, 40.056359],
        status: '警告',
        evidenceCount: 198,
        collectionSpeed: [12, 15, 18, 16, 20, 22, 19],
        collectionCount: [30, 60, 90, 120, 150, 180, 195],
        deviceTypes: [
          { name: '电脑', value: 40 },
          { name: '手机', value: 40 },
          { name: '服务器', value: 15 },
          { name: '监控', value: 5 }
        ],
        evidenceTypes: [
          { name: '聊天记录', value: 40 },
          { name: '银行卡账户', value: 15 },
          { name: '网页地址', value: 20 },
          { name: '数字货币', value: 20 },
          { name: '云数据', value: 5 }
        ],
        recentEvidence: [
          { time: '2024-06-01 10:18:33', deviceType: '手机', evidenceType: '数字货币', description: '钱包信息', size: '0.5MB' },
          { time: '2024-06-01 10:05:47', deviceType: '电脑', evidenceType: '聊天记录', description: 'Discord聊天记录', size: '4.1MB' },
          { time: '2024-06-01 09:50:22', deviceType: '服务器', evidenceType: '网页地址', description: '服务器日志', size: '8.3MB' }
        ]
      },
      {
        id: 'device-004',
        name: '探针设备-004',
        location: '科技园区副楼2层',
        position: [116.310629, 40.055359],
        status: '正常',
        evidenceCount: 265,
        collectionSpeed: [15, 18, 22, 25, 23, 28, 30],
        collectionCount: [45, 75, 110, 150, 190, 230, 260],
        deviceTypes: [
          { name: '电脑', value: 35 },
          { name: '手机', value: 45 },
          { name: '服务器', value: 10 },
          { name: '监控', value: 10 }
        ],
        evidenceTypes: [
          { name: '聊天记录', value: 45 },
          { name: '银行卡账户', value: 20 },
          { name: '网页地址', value: 15 },
          { name: '数字货币', value: 10 },
          { name: '云数据', value: 10 }
        ],
        recentEvidence: [
          { time: '2024-06-01 10:21:18', deviceType: '手机', evidenceType: '聊天记录', description: 'WhatsApp聊天记录', size: '5.2MB' },
          { time: '2024-06-01 10:12:36', deviceType: '电脑', evidenceType: '银行卡账户', description: '网银记录', size: '1.2MB' },
          { time: '2024-06-01 09:59:45', deviceType: '监控', evidenceType: '云数据', description: '监控视频', size: '18.7MB' }
        ]
      },
      {
        id: 'device-005',
        name: '探针设备-005',
        location: '科技园区地下室',
        position: [116.311629, 40.054359],
        status: '正常',
        evidenceCount: 200,
        collectionSpeed: [10, 14, 18, 16, 20, 24, 22],
        collectionCount: [35, 65, 95, 125, 155, 185, 200],
        deviceTypes: [
          { name: '电脑', value: 30 },
                    { name: '手机', value: 25 },
          { name: '服务器', value: 25 },
          { name: '监控', value: 20 }
        ],
        evidenceTypes: [
          { name: '聊天记录', value: 25 },
          { name: '银行卡账户', value: 30 },
          { name: '网页地址', value: 15 },
          { name: '数字货币', value: 15 },
          { name: '云数据', value: 15 }
        ],
        recentEvidence: [
          { time: '2024-06-01 10:19:27', deviceType: '服务器', evidenceType: '银行卡账户', description: '交易数据', size: '2.7MB' },
          { time: '2024-06-01 10:08:19', deviceType: '手机', evidenceType: '数字货币', description: '钱包数据', size: '0.6MB' },
          { time: '2024-06-01 09:52:38', deviceType: '电脑', evidenceType: '网页地址', description: '浏览历史', size: '1.9MB' }
        ]
      }
    ])

    // 初始化地图
    const initMap = () => {
      try {
        // 动态加载百度地图脚本
        const loadBaiduMap = () => {
          return new Promise((resolve, reject) => {
            if (window.BMap) {
              resolve(window.BMap)
              return
            }
            
            window.onBMapCallback = function() {
              resolve(window.BMap)
            }
            
            const script = document.createElement('script')
            script.type = 'text/javascript'
            script.src = 'https://api.map.baidu.com/api?v=3.0&ak='+baiduMapKey+'&callback=onBMapCallback' // 请替换为您的百度地图API密钥
            script.onerror = reject
            document.head.appendChild(script)
          })
        }
        
        loadBaiduMap().then(BMap => {
          // 创建地图实例
          map.value = new BMap.Map(mapContainer.value)
          
          // 设置中心点和缩放级别
          const centerPoint = new BMap.Point(116.309629, 40.056359)
          map.value.centerAndZoom(centerPoint, 17)
          
          // 启用滚轮缩放和平移
          map.value.enableScrollWheelZoom()
          map.value.enableDragging()
          
          // 添加地图控件
          map.value.addControl(new BMap.NavigationControl())
          map.value.addControl(new BMap.ScaleControl())
          map.value.addControl(new BMap.OverviewMapControl())
          map.value.addControl(new BMap.MapTypeControl())
          
          // 设置地图类型
          if (mapMode3D.value) {
            map.value.setMapType(BMAP_PERSPECTIVE_MAP) // 3D模式
          } else {
            map.value.setMapType(BMAP_NORMAL_MAP) // 2D模式
          }
          
          // 添加设备标记
          devices.forEach(device => {
            const point = new BMap.Point(device.position[0], device.position[1])
            const marker = new BMap.Marker(point)
            
            // 自定义图标
            const icon = new BMap.Icon(
              device.status === '正常' ? '/images/device-normal.png' : '/images/device-warning.png',
              new BMap.Size(200, 200)
            )
            icon.setImageSize(new BMap.Size(32,32));

            marker.setIcon(icon)
            
            // 添加标题
            marker.setTitle(device.name)
            
            // 点击事件
            marker.addEventListener('click', () => {
              selectedDevice.value = device
            })
            
            // 鼠标悬停事件
            const label = new BMap.Label(`<div class="map-label">${device.name}<br/>证据数: ${device.evidenceCount}</div>`, {
              offset: new BMap.Size(20, -30),
              enableMassClear: true
            })
            label.setStyle({
              display: 'none',
              border: 'none',
              backgroundColor: 'transparent'
            })
            marker.setLabel(label)
            
            marker.addEventListener('mouseover', () => {
              label.setStyle({ display: 'block' })
            })
            
            marker.addEventListener('mouseout', () => {
              label.setStyle({ display: 'none' })
            })
            
            map.value.addOverlay(marker)
          })
        }).catch(error => {
          console.error('百度地图加载失败:', error)
        })
      } catch (e) {
        console.error('地图加载失败:', e)
      }
    }
    
    // 切换地图模式
    const toggleMapMode = () => {
      if (map.value) {
        if (mapMode3D.value) {
          map.value.setMapType(BMAP_PERSPECTIVE_MAP) // 3D模式
        } else {
          map.value.setMapType(BMAP_NORMAL_MAP) // 2D模式
        }
      }
    }
    
    // 初始化采集速度图表
    const initSpeedChart = () => {
      if (!speedChartContainer.value) return
      
      speedChart.value = echarts.init(speedChartContainer.value)
      
      const option = {
        title: {
          text: '最近7天证据采集速度(MB/s)'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: devices.map(device => device.name)
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
          data: ['6天前', '5天前', '4天前', '3天前', '2天前', '1天前', '今天']
        },
        yAxis: {
          type: 'value',
          name: 'MB/s'
        },
        series: devices.map(device => ({
          name: device.name,
          type: 'line',
          data: device.collectionSpeed,
          smooth: true,
          markPoint: {
            data: [
              { type: 'max', name: '最大值' },
              { type: 'min', name: '最小值' }
            ]
          }
        }))
      }
      
      speedChart.value.setOption(option)
    }
    
    // 初始化采集数量图表
    const initCountChart = () => {
      if (!countChartContainer.value) return
      
      countChart.value = echarts.init(countChartContainer.value)
      
      const option = {
        title: {
          text: '最近7天证据采集数量'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: devices.map(device => device.name)
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
          data: ['6天前', '5天前', '4天前', '3天前', '2天前', '1天前', '今天']
        },
        yAxis: {
          type: 'value',
          name: '证据数量'
        },
        series: devices.map(device => ({
          name: device.name,
          type: 'line',
          data: device.collectionCount,
          smooth: true,
          areaStyle: {},
          markPoint: {
            data: [
              { type: 'max', name: '最大值' }
            ]
          },
          markLine: {
            data: [
              { type: 'average', name: '平均值' }
            ]
          }
        }))
      }
      
      countChart.value.setOption(option)
    }
    
    // 更新采集数量图表
    const updateEvidenceCountChart = () => {
      // 实际应用中，这里应该根据选择的时间范围从后端获取数据
      // 这里仅做演示，使用模拟数据
      console.log('更新时间范围:', timeRange.value)
      
      // 重新初始化图表
      initCountChart()
    }
    
    // 初始化设备类型和证据类型饼图
    const initPieCharts = () => {
      devices.forEach(device => {
        // 设备类型饼图
        const deviceTypeChartElement = document.getElementById(`device-type-chart-${device.id}`)
        if (!deviceTypeChartElement) return
        
        // 检查是否已经存在图表实例，如果存在则销毁
        const existingChart = echarts.getInstanceByDom(deviceTypeChartElement)
        if (existingChart) {
          existingChart.dispose()
        }
        
        const deviceTypeChart = echarts.init(deviceTypeChartElement)
        deviceTypeChart.setOption({
          title: {
            text: '设备类型分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: device.deviceTypes.map(item => item.name)
          },
          series: [
            {
              name: '设备类型',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '18',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: device.deviceTypes
            }
          ]
        })
        
        // 证据类型饼图
        const evidenceTypeChartElement = document.getElementById(`evidence-type-chart-${device.id}`)
        if (!evidenceTypeChartElement) return
        
        // 检查是否已经存在图表实例，如果存在则销毁
        const existingEvidenceChart = echarts.getInstanceByDom(evidenceTypeChartElement)
        if (existingEvidenceChart) {
          existingEvidenceChart.dispose()
        }
        
        const evidenceTypeChart = echarts.init(evidenceTypeChartElement)
        evidenceTypeChart.setOption({
          title: {
            text: '证据类型分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: device.evidenceTypes.map(item => item.name)
          },
          series: [
            {
              name: '证据类型',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '18',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: device.evidenceTypes
            }
          ]
        })
      })
    }
    
    // 添加Tab切换事件处理函数
    const handleTabClick = (tab) => {
      // 给DOM一点时间渲染
      setTimeout(() => {
        // 重新初始化当前激活Tab的饼图
        const activeDeviceIndex = parseInt(tab.index)
        const activeDevice = devices[activeDeviceIndex]
        
        if (activeDevice) {
          // 只初始化当前激活的设备饼图
          const deviceTypeChartElement = document.getElementById(`device-type-chart-${activeDevice.id}`)
          const evidenceTypeChartElement = document.getElementById(`evidence-type-chart-${activeDevice.id}`)
          
          if (deviceTypeChartElement) {
            const existingChart = echarts.getInstanceByDom(deviceTypeChartElement)
            if (existingChart) {
              existingChart.resize()
            } else {
              const deviceTypeChart = echarts.init(deviceTypeChartElement)
              deviceTypeChart.setOption({
                // 与上面相同的配置
                title: {
                  text: '设备类型分布',
                  left: 'center'
                },
                tooltip: {
                  trigger: 'item',
                  formatter: '{a} <br/>{b}: {c} ({d}%)'
                },
                legend: {
                  orient: 'vertical',
                  left: 'left',
                  data: activeDevice.deviceTypes.map(item => item.name)
                },
                series: [
                  {
                    name: '设备类型',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    label: {
                      show: false,
                      position: 'center'
                    },
                    emphasis: {
                      label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                      }
                    },
                    labelLine: {
                      show: false
                    },
                    data: activeDevice.deviceTypes
                  }
                ]
              })
            }
          }
          
          if (evidenceTypeChartElement) {
            const existingChart = echarts.getInstanceByDom(evidenceTypeChartElement)
            if (existingChart) {
              existingChart.resize()
            } else {
              const evidenceTypeChart = echarts.init(evidenceTypeChartElement)
              evidenceTypeChart.setOption({
                // 与上面相同的配置
                title: {
                  text: '证据类型分布',
                  left: 'center'
                },
                tooltip: {
                  trigger: 'item',
                  formatter: '{a} <br/>{b}: {c} ({d}%)'
                },
                legend: {
                  orient: 'vertical',
                  left: 'left',
                  data: activeDevice.evidenceTypes.map(item => item.name)
                },
                series: [
                  {
                    name: '证据类型',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    label: {
                      show: false,
                      position: 'center'
                    },
                    emphasis: {
                      label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                      }
                    },
                    labelLine: {
                      show: false
                    },
                    data: activeDevice.evidenceTypes
                  }
                ]
              })
            }
          }
        }
      }, 50) // 短暂延迟确保DOM已更新
    }
    
    // 窗口大小变化时重新调整图表大小
    const handleResize = () => {
      if (speedChart.value) {
        speedChart.value.resize()
      }
      if (countChart.value) {
        countChart.value.resize()
      }
      
      // 重新初始化饼图
      initPieCharts()
    }
    
    onMounted(() => {
      // 初始化地图
      initMap()
      
      // 等待DOM渲染完成后初始化饼图
      setTimeout(() => {
         // 初始化图表
        initSpeedChart()
        initCountChart()

        initPieCharts()
        // 监听窗口大小变化
        window.addEventListener('resize', handleResize)
      }, 500)
      
      
      
    })
    
    return {
      mapContainer,
      speedChartContainer,
      countChartContainer,
      mapMode3D,
      toggleMapMode,
      selectedDevice,
      caseInfo,
      devices,
      timeRange,
      updateEvidenceCountChart,
      handleTabClick  // 添加这一行
    }
  }
}
</script>

<style scoped>
.case-detail-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
}

.info-card, .map-card, .chart-card, .evidence-card {
  margin-bottom: 20px;
}

.case-info {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.info-item {
  display: flex;
  align-items: center;
}

.label {
  font-weight: bold;
  margin-right: 10px;
  color: #606266;
}

.value {
  color: #303133;
}

.map-container {
  height: 500px;
  width: 100%;
  margin-bottom: 20px;
}

.selected-device-info {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  margin-top: 15px;
}

.selected-device-info h4 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #303133;
}

.status-normal {
  color: #67c23a;
}

.status-warning {
  color: #e6a23c;
}

.chart-container {
  height: 400px;
  width: 100%;
}

.evidence-stats {
  display: flex;
  margin-bottom: 20px;
}

.stats-section {
  flex: 1;
  padding: 10px;
}

.pie-chart {
  height: 300px;
  width: 100%;
  position: relative;
  overflow: hidden;
}

/* 确保Tab内容区域有足够的高度 */
.el-tabs__content {
  min-height: 400px;
}

/* 确保饼图容器在Tab切换时保持稳定 */
.evidence-stats {
  display: flex;
  margin-bottom: 20px;
  min-height: 350px;
}
</style>