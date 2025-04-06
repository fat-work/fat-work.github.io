<template>
  <div class="tech-screen" ref="techScreen">
    <!-- 进场动画遮罩层 -->
    <div class="entrance-mask" v-show="showEntrance">
      <div class="logo-container">
        <div class="logo-circle">
          <div class="logo-inner"></div>
        </div>
        <div class="logo-text">电子数据智能取证平台</div>
      </div>
      <div class="loading-bar">
        <div class="loading-progress" :style="{ width: loadingProgress + '%' }"></div>
      </div>
      <div class="loading-text">系统加载中 {{ loadingProgress }}%</div>
    </div>
    
    <!-- 主内容区 -->
    <div class="main-content" :class="{ 'show-content': !showEntrance }">
      <div class="header">
        <div class="title">
          <span class="title-text">电子数据智能取证平台</span>
          <div class="title-decoration"></div>
        </div>
        <div class="time">{{ currentTime }}</div>
      </div>
      
      <div class="content">
        <!-- 地图容器 -->
        <div class="map-container" ref="mapContainer"></div>
        <!-- 第一行 -->
        <div class="panel-group left-panels">
          <div class="panel case-statistics">
            <div class="panel-header">
              <div class="panel-title">案件统计</div>
              <div class="panel-decoration">
                <div class="decoration-line"></div>
                <div class="decoration-circle"></div>
              </div>
            </div>
            <div class="panel-content">
              <div class="data-cards">
                <div class="data-card">
                  <div class="data-value count-up">{{ totalCases }}</div>
                  <div class="data-label">总案件数</div>
                  <div class="data-icon">
                    <div class="rotating-circle"></div>
                  </div>
                </div>
                <div class="data-card">
                  <div class="data-value count-up">{{ activeCases }}</div>
                  <div class="data-label">进行中案件</div>
                  <div class="data-icon">
                    <div class="pulse-circle"></div>
                  </div>
                </div>
                <div class="data-card">
                  <div class="data-value count-up">{{ solvedCases }}</div>
                  <div class="data-label">已结案件</div>
                  <div class="data-icon">
                    <div class="check-icon"></div>
                  </div>
                </div>
              </div>
              <div class="chart-container" ref="caseChart"></div>
            </div>
          </div>
          
          <div class="panel probe-statistics">
            <div class="panel-header">
              <div class="panel-title">探针设备统计</div>
              <div class="panel-decoration">
                <div class="decoration-line"></div>
                <div class="decoration-circle"></div>
              </div>
            </div>
            <div class="panel-content">
              <div class="data-cards">
                <div class="data-card">
                  <div class="data-value count-up">{{ onlineProbes }}</div>
                  <div class="data-label">在线设备</div>
                  <div class="data-icon">
                    <div class="online-icon"></div>
                  </div>
                </div>
                <div class="data-card">
                  <div class="data-value count-up">{{ offlineProbes }}</div>
                  <div class="data-label">离线设备</div>
                  <div class="data-icon">
                    <div class="offline-icon"></div>
                  </div>
                </div>
              </div>
              <div class="chart-container" ref="probeChart"></div>
            </div>
          </div>

          <div class="panel asset-analysis">
            <div class="panel-header">
              <div class="panel-title">资产分析</div>
              <div class="panel-decoration">
                <div class="decoration-line"></div>
                <div class="decoration-circle"></div>
              </div>
            </div>
            <div class="panel-content">
              <div class="rotating-container">
                <div class="rotating-ring"></div>
                <div class="chart-container" ref="assetChart"></div>
              </div>
            </div>
          </div>

        </div>
        
        <!-- 第二行 -->
        <div class="panel-group right-panels">
          <div class="panel evidence-analysis">
            <div class="panel-header">
              <div class="panel-title">证据采集与分析</div>
              <div class="panel-decoration">
                <div class="decoration-line"></div>
                <div class="decoration-circle"></div>
              </div>
            </div>
            <div class="panel-content">
              <div class="data-cards">
                <div class="data-card">
                  <div class="data-value count-up">{{ totalEvidence }}</div>
                  <div class="data-label">总证据量</div>
                  <div class="data-icon">
                    <div class="evidence-icon"></div>
                  </div>
                </div>
                <div class="data-card">
                  <div class="data-value count-up">{{ analyzedEvidence }}</div>
                  <div class="data-label">已分析证据</div>
                  <div class="data-icon">
                    <div class="analysis-icon"></div>
                  </div>
                </div>
              </div>
              <div class="chart-container" ref="evidenceChart"></div>
            </div>
          </div>
          
          <div class="panel person-portrait">
            <div class="panel-header">
              <div class="panel-title">人物画像</div>
              <div class="panel-decoration">
                <div class="decoration-line"></div>
                <div class="decoration-circle"></div>
              </div>
            </div>
            <div class="panel-content">
              <div class="radar-container">
                <div class="radar-scan"></div>
                <div class="chart-container" ref="personChart"></div>
              </div>
            </div>
          </div>

          <div class="panel realtime-monitor">
            <div class="panel-header">
              <div class="panel-title">实时监控</div>
              <div class="panel-decoration">
                <div class="decoration-line"></div>
                <div class="decoration-circle"></div>
              </div>
            </div>
            <div class="panel-content">
              <div class="data-cards">
                <div class="data-card">
                  <div class="data-value count-up">{{ activeUsers }}</div>
                  <div class="data-label">活跃用户</div>
                  <div class="data-icon">
                    <div class="user-icon"></div>
                  </div>
                </div>
                <div class="data-card">
                  <div class="data-value">{{ dataTransfer }}</div>
                  <div class="data-label">数据传输量</div>
                  <div class="data-icon">
                    <div class="data-transfer-icon"></div>
                  </div>
                </div>
              </div>
              <div class="chart-container" ref="monitorChart"></div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 动态装饰元素 -->
      <div class="decoration-elements">
        <div class="corner-decoration top-left"></div>
        <div class="corner-decoration top-right"></div>
        <div class="corner-decoration bottom-left"></div>
        <div class="corner-decoration bottom-right"></div>
        <div class="floating-particles"></div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { CountUp } from 'countup.js'
export default {
  name: 'TechScreen',
  data() {
    return {
      // 进场动画相关
      showEntrance: true,
      loadingProgress: 0,
      
      // 时间显示
      currentTime: '',
      
      // 案件统计数据
      totalCases: 1256,
      activeCases: 328,
      solvedCases: 928,
      
      // 探针设备数据
      onlineProbes: 156,
      offlineProbes: 24,
      
      // 证据数据
      totalEvidence: 25689,
      analyzedEvidence: 18456,
      
      // 监控数据
      activeUsers: 42,
      dataTransfer: '1.2TB',
      
      // 图表实例
      charts: {},
      
      // 数字动画实例
      countUpInstances: {},
      // 添加地图实例
      map: null
    }
  },
  mounted() {
    // 启动进场动画
    this.startEntranceAnimation()
    
    // 初始化时间
    this.initTime()
    
    // 自动刷新时间
    setInterval(this.updateTime, 1000)
    
    // 窗口大小变化时重绘图表
    window.addEventListener('resize', this.resizeCharts)

    // 初始化百度地图
    this.$nextTick(() => {
      this.initMap()
    })
  },
  beforeDestroy() {
    clearInterval(this.timeInterval)
    clearInterval(this.dataInterval)
    window.removeEventListener('resize', this.resizeCharts)
    
    // 销毁所有图表实例
    Object.keys(this.charts).forEach(key => {
      this.charts[key].dispose()
    })
  },
  methods: {
    // 初始化百度地图
    initMap() {
      // 确保百度地图API已加载
      if (typeof BMap === 'undefined') {
        this.loadBaiduMapScript().then(() => {
          this.createMap()
        })
      } else {
        this.createMap()
      }
    },
    // 加载百度地图脚本
    loadBaiduMapScript() {
      return new Promise((resolve, reject) => {
        const script = document.createElement('script')
        script.type = 'text/javascript'
        script.src = 'https://api.map.baidu.com/api?v=3.0&ak='+baiduMapKey+'&callback=initBaiduMap'
        document.head.appendChild(script)
        
        // 全局回调函数
        window.initBaiduMap = () => {
          resolve()
        }
        
        script.onerror = reject
      })
    },
    // 创建地图实例
    createMap() {
      this.map = new BMap.Map(this.$refs.mapContainer)
      // 初始化地图，设置中心点坐标和地图级别
      const point = new BMap.Point(116.404, 39.915) // 北京市中心
      this.map.centerAndZoom(point, 12)
      // 开启鼠标滚轮缩放
      this.map.enableScrollWheelZoom(true)
      // 设置地图样式
      this.map.setMapStyle({
        styleJson: [{
    "featureType": "land",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "color": "#122B56ff"
    }
}, {
    "featureType": "water",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "color": "#113549ff"
    }
}, {
    "featureType": "green",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "color": "#0e1b30ff"
    }
}, {
    "featureType": "building",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "building",
    "elementType": "geometry.topfill",
    "stylers": {
        "color": "#113549ff"
    }
}, {
    "featureType": "building",
    "elementType": "geometry.sidefill",
    "stylers": {
        "color": "#143e56ff"
    }
}, {
    "featureType": "building",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#dadada00"
    }
}, {
    "featureType": "subwaystation",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "color": "#113549B2"
    }
}, {
    "featureType": "education",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "color": "#12223dff"
    }
}, {
    "featureType": "medical",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "color": "#12223dff"
    }
}, {
    "featureType": "scenicspots",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "color": "#12223dff"
    }
}, {
    "featureType": "highway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "weight": 4
    }
}, {
    "featureType": "highway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "highway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#fed66900"
    }
}, {
    "featureType": "highway",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "highway",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "highway",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "highway",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "arterial",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "weight": 2
    }
}, {
    "featureType": "arterial",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "arterial",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffeebb00"
    }
}, {
    "featureType": "arterial",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "arterial",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "arterial",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "local",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on",
        "weight": 1
    }
}, {
    "featureType": "local",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "local",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "local",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "local",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#979c9aff"
    }
}, {
    "featureType": "local",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffffff"
    }
}, {
    "featureType": "railway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "subway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "weight": 1
    }
}, {
    "featureType": "subway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#d8d8d8ff"
    }
}, {
    "featureType": "subway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "subway",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "subway",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#979c9aff"
    }
}, {
    "featureType": "subway",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffffff"
    }
}, {
    "featureType": "continent",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "continent",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "continent",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "continent",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "city",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "city",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "city",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "city",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "town",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "town",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "town",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#454d50ff"
    }
}, {
    "featureType": "town",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffffff"
    }
}, {
    "featureType": "road",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "poilabel",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "districtlabel",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "road",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "road",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "district",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "poilabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "poilabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "poilabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "manmade",
    "elementType": "geometry",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "districtlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffffff"
    }
}, {
    "featureType": "entertainment",
    "elementType": "geometry",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "shopping",
    "elementType": "geometry",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "nationalway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "6"
    }
}, {
    "featureType": "nationalway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "7"
    }
}, {
    "featureType": "nationalway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "8"
    }
}, {
    "featureType": "nationalway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "9"
    }
}, {
    "featureType": "nationalway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "10"
    }
}, {
    "featureType": "nationalway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "6"
    }
}, {
    "featureType": "nationalway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "7"
    }
}, {
    "featureType": "nationalway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "8"
    }
}, {
    "featureType": "nationalway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "9"
    }
}, {
    "featureType": "nationalway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "10"
    }
}, {
    "featureType": "nationalway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "6"
    }
}, {
    "featureType": "nationalway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "7"
    }
}, {
    "featureType": "nationalway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "8"
    }
}, {
    "featureType": "nationalway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "9"
    }
}, {
    "featureType": "nationalway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,10",
        "level": "10"
    }
}, {
    "featureType": "cityhighway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "6"
    }
}, {
    "featureType": "cityhighway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "7"
    }
}, {
    "featureType": "cityhighway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "8"
    }
}, {
    "featureType": "cityhighway",
    "stylers": {
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "9"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "6"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "7"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "8"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "9"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "6"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "7"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "8"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "labels",
    "stylers": {
        "visibility": "off",
        "curZoomRegionId": "0",
        "curZoomRegion": "6,9",
        "level": "9"
    }
}, {
    "featureType": "subwaylabel",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "subwaylabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "tertiarywaysign",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "tertiarywaysign",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "provincialwaysign",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "provincialwaysign",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "nationalwaysign",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "nationalwaysign",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "highwaysign",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "highwaysign",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "village",
    "elementType": "labels",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "district",
    "elementType": "labels.text",
    "stylers": {
        "fontsize": 20
    }
}, {
    "featureType": "district",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "district",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "country",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "country",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "water",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "water",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "tertiaryway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "tertiaryway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff10"
    }
}, {
    "featureType": "provincialway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "provincialway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "nationalway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "nationalway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "highway",
    "elementType": "labels.text",
    "stylers": {
        "fontsize": 20
    }
}, {
    "featureType": "nationalway",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "nationalway",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "nationalway",
    "elementType": "labels.text",
    "stylers": {
        "fontsize": 20
    }
}, {
    "featureType": "provincialway",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "provincialway",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "provincialway",
    "elementType": "labels.text",
    "stylers": {
        "fontsize": 20
    }
}, {
    "featureType": "cityhighway",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "cityhighway",
    "elementType": "labels.text",
    "stylers": {
        "fontsize": 20
    }
}, {
    "featureType": "cityhighway",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "estate",
    "elementType": "geometry",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "tertiaryway",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "tertiaryway",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "fourlevelway",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "fourlevelway",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "scenicspotsway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "scenicspotsway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "universityway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "universityway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "vacationway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "vacationway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "fourlevelway",
    "elementType": "geometry",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "fourlevelway",
    "elementType": "geometry.fill",
    "stylers": {
        "color": "#12223dff"
    }
}, {
    "featureType": "fourlevelway",
    "elementType": "geometry.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "transportationlabel",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "transportationlabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "transportationlabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "transportationlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "educationlabel",
    "elementType": "labels",
    "stylers": {
        "visibility": "on"
    }
}, {
    "featureType": "educationlabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "educationlabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "educationlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "transportation",
    "elementType": "geometry",
    "stylers": {
        "color": "#113549ff"
    }
}, {
    "featureType": "airportlabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "airportlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "scenicspotslabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "scenicspotslabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "medicallabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "medicallabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "medicallabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "scenicspotslabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "airportlabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "entertainmentlabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "entertainmentlabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "entertainmentlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "estatelabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "estatelabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "estatelabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "businesstowerlabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "businesstowerlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "businesstowerlabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "companylabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "companylabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "companylabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "governmentlabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "governmentlabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "governmentlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "restaurantlabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "restaurantlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "restaurantlabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "hotellabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "hotellabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "hotellabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "shoppinglabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "shoppinglabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "shoppinglabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "lifeservicelabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "lifeservicelabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "lifeservicelabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "carservicelabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "carservicelabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "carservicelabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "financelabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "financelabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "financelabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "otherlabel",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "otherlabel",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "otherlabel",
    "elementType": "labels.icon",
    "stylers": {
        "visibility": "off"
    }
}, {
    "featureType": "manmade",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "manmade",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "transportation",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "transportation",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "education",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "education",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "medical",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "medical",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}, {
    "featureType": "scenicspots",
    "elementType": "labels.text.fill",
    "stylers": {
        "color": "#2dc4bbff"
    }
}, {
    "featureType": "scenicspots",
    "elementType": "labels.text.stroke",
    "stylers": {
        "color": "#ffffff00"
    }
}]
      })
      
      // 添加一些标记点示例
      this.addMapMarkers()
    },

    // 添加地图标记点
    addMapMarkers() {
      // 模拟一些探针设备位置
      const locations = [
        { lng: 116.404, lat: 39.915, name: '探针设备A', status: 'online' },
        { lng: 116.415, lat: 39.905, name: '探针设备B', status: 'online' },
        { lng: 116.395, lat: 39.925, name: '探针设备C', status: 'offline' },
        { lng: 116.425, lat: 39.935, name: '探针设备D', status: 'online' },
        { lng: 116.385, lat: 39.895, name: '探针设备E', status: 'offline' }
      ]
      
      locations.forEach(location => {
        const point = new BMap.Point(location.lng, location.lat)
        const marker = new BMap.Marker(point)
        this.map.addOverlay(marker)
        
        // 自定义标记点样式
        const statusColor = location.status === 'online' ? '#00ffb4' : '#ff5a5a'
        // 创建带闪烁动画的SVG图标
        const size = new BMap.Size(20, 20);
        const icon = new BMap.Icon(
          "data:image/svg+xml;charset=utf-8," + encodeURIComponent(`
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20">
              <circle cx="10" cy="10" r="5" fill="${statusColor}" stroke="#ffffff" stroke-width="1">
                <animate attributeName="r" values="5;8;5" dur="${location.status === 'online' ? '1.5s' : '3s'}" repeatCount="indefinite" />
                <animate attributeName="opacity" values="1;0.6;1" dur="${location.status === 'online' ? '1.5s' : '3s'}" repeatCount="indefinite" />
              </circle>
              <circle cx="10" cy="10" r="2" fill="#ffffff" />
            </svg>
          `),
          size
        );
        icon.setImageSize(size);
        marker.setIcon(icon);
        
        // 添加科技感信息窗口
        const infoWindow = new BMap.InfoWindow(`
          <div style="padding: 15px; background: linear-gradient(135deg, rgba(14, 33, 71, 0.95), rgba(26, 58, 108, 0.95)); border-radius: 8px; border: 1px solid ${statusColor}; box-shadow: 0 0 15px rgba(0, 0, 0, 0.5);">
            <div style="display: flex; align-items: center; margin-bottom: 10px; border-bottom: 1px solid rgba(73, 146, 255, 0.3); padding-bottom: 8px;">
              <div style="width: 12px; height: 12px; border-radius: 50%; background-color: ${statusColor}; margin-right: 8px; box-shadow: 0 0 8px ${statusColor};"></div>
              <h4 style="margin: 0; color: #fff; font-size: 16px; text-shadow: 0 0 5px rgba(0, 255, 180, 0.5);">${location.name}</h4>
            </div>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <div>
                <p style="margin: 5px 0; color: #4992ff; font-size: 12px;">状态</p>
                <p style="margin: 0; color: ${statusColor}; font-weight: bold;">${location.status === 'online' ? '在线' : '离线'}</p>
              </div>
              <div>
                <p style="margin: 5px 0; color: #4992ff; font-size: 12px;">信号强度</p>
                <p style="margin: 0; color: #fff;">${location.status === 'online' ? Math.floor(Math.random() * 30 + 70) + '%' : 'N/A'}</p>
              </div>
              <div>
                <p style="margin: 5px 0; color: #4992ff; font-size: 12px;">最后更新</p>
                <p style="margin: 0; color: #fff;">${new Date().toLocaleTimeString()}</p>
              </div>
            </div>
            <div style="margin-top: 10px; height: 4px; background: rgba(73, 146, 255, 0.2); border-radius: 2px; overflow: hidden;">
              <div style="height: 100%; width: ${location.status === 'online' ? '100%' : '30%'}; background: linear-gradient(to right, #4992ff, ${statusColor}); border-radius: 2px;"></div>
            </div>
          </div>
        `, {
          // 自定义信息窗口样式
          width: 320,
          height: 150,
          enableMessage: false,
          enableCloseOnClick: false,
          enableAutoPan: true
        })
        
        marker.addEventListener('click', function() {
          this.openInfoWindow(infoWindow)
        })
      })
    },
    startEntranceAnimation() {
      // 模拟加载进度
      const loadingInterval = setInterval(() => {
        this.loadingProgress += Math.floor(Math.random() * 5) + 1
        
        if (this.loadingProgress >= 100) {
          this.loadingProgress = 100
          clearInterval(loadingInterval)
          
          // 加载完成后，延迟一段时间再显示主内容
          setTimeout(() => {
            this.showEntrance = false
            
            // 主内容显示后初始化图表和数据
            setTimeout(() => {
              this.initCharts()
              this.initCountUp()
              
              // 启动数据自动刷新
              setInterval(this.updateData, 30000)
            }, 500)
          }, 1000)
        }
      }, 100)
    },
    initTime() {
      this.updateTime()
    },
    updateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = (now.getMonth() + 1).toString().padStart(2, '0')
      const day = now.getDate().toString().padStart(2, '0')
      const hours = now.getHours().toString().padStart(2, '0')
      const minutes = now.getMinutes().toString().padStart(2, '0')
      const seconds = now.getSeconds().toString().padStart(2, '0')
      
      this.currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    initCountUp() {
      // 初始化数字动画
      const options = {
        duration: 2.5,
        useEasing: true,
        useGrouping: true,
        separator: ',',
        decimal: '.'
      }
      
      // 案件统计数字动画
      this.countUpInstances.totalCases = new CountUp(
        document.querySelectorAll('.case-statistics .data-value')[0],
        this.totalCases,
        options
      )
      this.countUpInstances.activeCases = new CountUp(
        document.querySelectorAll('.case-statistics .data-value')[1],
        this.activeCases,
        options
      )
      this.countUpInstances.solvedCases = new CountUp(
        document.querySelectorAll('.case-statistics .data-value')[2],
        this.solvedCases,
        options
      )
      
      // 探针设备数字动画
      this.countUpInstances.onlineProbes = new CountUp(
        document.querySelectorAll('.probe-statistics .data-value')[0],
        this.onlineProbes,
        options
      )
      this.countUpInstances.offlineProbes = new CountUp(
        document.querySelectorAll('.probe-statistics .data-value')[1],
        this.offlineProbes,
        options
      )
      
      // 证据数据数字动画
      this.countUpInstances.totalEvidence = new CountUp(
        document.querySelectorAll('.evidence-analysis .data-value')[0],
        this.totalEvidence,
        options
      )
      this.countUpInstances.analyzedEvidence = new CountUp(
        document.querySelectorAll('.evidence-analysis .data-value')[1],
        this.analyzedEvidence,
        options
      )
      
      // 监控数据数字动画
      this.countUpInstances.activeUsers = new CountUp(
        document.querySelectorAll('.realtime-monitor .data-value')[0],
        this.activeUsers,
        options
      )
      
      // 启动所有数字动画
      Object.values(this.countUpInstances).forEach(instance => {
        instance.start()
      })
    },
    initCharts() {

        // 初始化资产分析图表
      this.initAssetChart()

      // 初始化案件统计图表
      this.initCaseChart()
      
      // 初始化探针设备图表
      this.initProbeChart()
      
      // 初始化证据分析图表
      this.initEvidenceChart()
      
      // 初始化人物画像图表
      this.initPersonChart()
      
      
      
      // 初始化实时监控图表
      this.initMonitorChart()
    },
    initCaseChart() {
      const chart = echarts.init(this.$refs.caseChart)
      this.charts.caseChart = chart
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新增案件', '结案数量'],
          textStyle: {
            color: '#fff'
          },
          right: 10,
          top: 0
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月'],
          axisLine: {
            lineStyle: {
                            color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff'
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          },
          axisLabel: {
            color: '#fff'
          }
        },
        series: [
          {
            name: '新增案件',
            type: 'line',
            stack: '总量',
            data: [120, 132, 101, 134, 90, 230, 210],
            lineStyle: {
              width: 3,
              color: '#4992ff'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(73, 146, 255, 0.8)' },
                { offset: 1, color: 'rgba(73, 146, 255, 0.1)' }
              ])
            },
            symbol: 'circle',
            symbolSize: 8,
            itemStyle: {
              color: '#4992ff',
              borderColor: '#fff',
              borderWidth: 2
            }
          },
          {
            name: '结案数量',
            type: 'line',
            stack: '总量',
            data: [82, 93, 90, 93, 129, 133, 132],
            lineStyle: {
              width: 3,
              color: '#00ffb4'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(0, 255, 180, 0.8)' },
                { offset: 1, color: 'rgba(0, 255, 180, 0.1)' }
              ])
            },
            symbol: 'circle',
            symbolSize: 8,
            itemStyle: {
              color: '#00ffb4',
              borderColor: '#fff',
              borderWidth: 2
            }
          }
        ],
        animationDuration: 2000,
        animationEasing: 'bounceOut'
      }
      
      chart.setOption(option)
    },
    initProbeChart() {
      const chart = echarts.init(this.$refs.probeChart)
      this.charts.probeChart = chart
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          textStyle: {
            color: '#fff'
          }
        },
        series: [
          {
            name: '探针设备状态',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#0e2147',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold',
                color: '#fff'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: this.onlineProbes, name: '在线设备', itemStyle: { color: '#00ffb4' } },
              { value: this.offlineProbes, name: '离线设备', itemStyle: { color: '#ff5a5a' } }
            ],
            animationDelay: function (idx) {
              return idx * 200;
            }
          }
        ],
        animationDuration: 2000,
        animationEasing: 'cubicOut'
      }
      
      chart.setOption(option)
    },
    initEvidenceChart() {
      const chart = echarts.init(this.$refs.evidenceChart)
      this.charts.evidenceChart = chart
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['图片', '视频', '文档', '音频', '其他'],
          textStyle: {
            color: '#fff'
          },
          top: 0
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          },
          axisLabel: {
            color: '#fff'
          }
        },
        yAxis: {
          type: 'category',
          data: ['已分析', '待分析', '总量'],
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff'
          }
        },
        series: [
          {
            name: '图片',
            type: 'bar',
            stack: '总量',
            emphasis: {
              focus: 'series'
            },
            data: [7200, 1800, 9000],
            itemStyle: {
              color: '#4992ff'
            },
            barWidth: 20,
            animationDelay: 100
          },
          {
            name: '视频',
            type: 'bar',
            stack: '总量',
            emphasis: {
              focus: 'series'
            },
            data: [5300, 1700, 7000],
            itemStyle: {
              color: '#00ffb4'
            },
            animationDelay: 200
          },
          {
            name: '文档',
            type: 'bar',
            stack: '总量',
            emphasis: {
              focus: 'series'
            },
            data: [4200, 1800, 6000],
            itemStyle: {
              color: '#fffc00'
            },
            animationDelay: 300
          },
          {
            name: '音频',
            type: 'bar',
            stack: '总量',
            emphasis: {
              focus: 'series'
            },
            data: [1100, 900, 2000],
            itemStyle: {
              color: '#ff5a5a'
            },
            animationDelay: 400
          },
          {
            name: '其他',
            type: 'bar',
            stack: '总量',
            emphasis: {
              focus: 'series'
            },
            data: [656, 1033, 1689],
            itemStyle: {
              color: '#a454ff'
            },
            animationDelay: 500
          }
        ],
        animationDuration: 2000,
        animationEasing: 'elasticOut'
      }
      
      chart.setOption(option)
    },
    initPersonChart() {
      const chart = echarts.init(this.$refs.personChart)
      this.charts.personChart = chart
      
      const option = {
        tooltip: {},
        radar: {
          indicator: [
            { name: '社交关系', max: 100 },
            { name: '资产情况', max: 100 },
            { name: '行为特征', max: 100 },
            { name: '通讯记录', max: 100 },
            { name: '位置轨迹', max: 100 },
            { name: '网络活动', max: 100 }
          ],
          shape: 'circle',
          splitNumber: 4,
          axisName: {
            color: '#fff',
            fontSize: 12
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.2)'
            }
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: ['rgba(255, 255, 255, 0.05)', 'rgba(255, 255, 255, 0.02)']
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.2)'
            }
          }
        },
        series: [
          {
            name: '嫌疑人画像',
            type: 'radar',
            data: [
              {
                value: [85, 65, 78, 90, 82, 95],
                name: '嫌疑人A',
                areaStyle: {
                  color: new echarts.graphic.RadialGradient(0.5, 0.5, 1, [
                    { offset: 0, color: 'rgba(73, 146, 255, 0.8)' },
                    { offset: 1, color: 'rgba(73, 146, 255, 0.1)' }
                  ])
                },
                lineStyle: {
                  color: '#4992ff',
                  width: 2
                },
                symbolSize: 6,
                symbol: 'circle'
              },
              {
                value: [65, 80, 70, 60, 75, 68],
                name: '嫌疑人B',
                areaStyle: {
                  color: new echarts.graphic.RadialGradient(0.5, 0.5, 1, [
                    { offset: 0, color: 'rgba(0, 255, 180, 0.8)' },
                    { offset: 1, color: 'rgba(0, 255, 180, 0.1)' }
                  ])
                },
                lineStyle: {
                  color: '#00ffb4',
                  width: 2
                },
                symbolSize: 6,
                symbol: 'circle'
              }
            ]
          }
        ],
        animationDuration: 3000,
        animationEasing: 'cubicInOut'
      }
      
      chart.setOption(option)
    },
    initAssetChart() {
      const chart = echarts.init(this.$refs.assetChart)
      this.charts.assetChart = chart
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          data: ['银行账户', '虚拟货币', '不动产', '车辆', '股票', '其他'],
          textStyle: {
            color: '#fff'
          },
          bottom: 0
        },
        series: [
          {
            name: '资产分布',
            type: 'pie',
            radius: ['30%', '50%'],
            center: ['50%', '45%'],
            roseType: 'radius',
            itemStyle: {
              borderRadius: 5
            },
            label: {
              color: '#fff'
            },
            data: [
              { value: 40, name: '银行账户', itemStyle: { color: '#4992ff' } },
              { value: 25, name: '虚拟货币', itemStyle: { color: '#00ffb4' } },
              { value: 15, name: '不动产', itemStyle: { color: '#fffc00' } },
              { value: 10, name: '车辆', itemStyle: { color: '#ff5a5a' } },
              { value: 8, name: '股票', itemStyle: { color: '#a454ff' } },
              { value: 2, name: '其他', itemStyle: { color: '#0ebeff' } }
            ],
            animationDelay: function (idx) {
              return idx * 200;
            }
          }
        ],
        animationDuration: 2500,
        animationEasing: 'bounceOut'
      }
      console.log(option)
      chart.setOption(option)
    },
    initMonitorChart() {
      const chart = echarts.init(this.$refs.monitorChart)
      this.charts.monitorChart = chart
      
      // 生成模拟数据
      const data = []
      const now = new Date()
      
      for (let i = 0; i < 100; i++) {
        const time = new Date(now.getTime() - (100 - i) * 60000)
        data.push([
          [time.getFullYear(), time.getMonth() + 1, time.getDate()].join('/') + ' ' + 
          [time.getHours(), time.getMinutes(), time.getSeconds()].join(':'),
          Math.round(Math.random() * 100 + 50)
        ])
      }
      
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            params = params[0]
            return params.name + '<br/>' + params.value[1] + ' MB/s'
          },
          axisPointer: {
            animation: false
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'time',
          splitLine: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff'
          }
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '100%'],
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff'
          }
        },
        series: [
          {
            name: '数据传输速率',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: data,
            lineStyle: {
              width: 2,
              color: '#00ffb4'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(0, 255, 180, 0.8)' },
                { offset: 1, color: 'rgba(0, 255, 180, 0.1)' }
              ])
            }
          }
        ],
        animationDuration: 2000
      }
      
      chart.setOption(option)
      
            // 动态更新数据
      setInterval(() => {
        const now = new Date()
        const time = [
          [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/') + ' ' + 
          [now.getHours(), now.getMinutes(), now.getSeconds()].join(':'),
          Math.round(Math.random() * 100 + 50)
        ]
        
        data.shift()
        data.push(time)
        
        chart.setOption({
          series: [{
            data: data
          }]
        })
      }, 1000)
    },
    resizeCharts() {
      Object.keys(this.charts).forEach(key => {
        this.charts[key].resize()
      })
    },
    updateData() {
      // 模拟数据更新
      this.totalCases = Math.floor(Math.random() * 100) + 1200
      this.activeCases = Math.floor(Math.random() * 50) + 300
      this.solvedCases = this.totalCases - this.activeCases
      
      this.onlineProbes = Math.floor(Math.random() * 20) + 150
      this.offlineProbes = Math.floor(Math.random() * 10) + 20
      
      this.totalEvidence = Math.floor(Math.random() * 1000) + 25000
      this.analyzedEvidence = Math.floor(this.totalEvidence * (Math.random() * 0.1 + 0.7))
      
      this.activeUsers = Math.floor(Math.random() * 15) + 35
      
      const dataSize = (Math.random() * 0.5 + 1).toFixed(1)
      this.dataTransfer = `${dataSize}TB`
      
      // 更新图表数据
      this.updateProbeChart()
      
      // 更新数字动画
      this.updateCountUp()
    },
    updateProbeChart() {
      if (this.charts.probeChart) {
        this.charts.probeChart.setOption({
          series: [{
            data: [
              { value: this.onlineProbes, name: '在线设备', itemStyle: { color: '#00ffb4' } },
              { value: this.offlineProbes, name: '离线设备', itemStyle: { color: '#ff5a5a' } }
            ]
          }]
        })
      }
    },
    updateCountUp() {
      // 更新数字动画
      if (this.countUpInstances.totalCases) {
        this.countUpInstances.totalCases.update(this.totalCases)
      }
      if (this.countUpInstances.activeCases) {
        this.countUpInstances.activeCases.update(this.activeCases)
      }
      if (this.countUpInstances.solvedCases) {
        this.countUpInstances.solvedCases.update(this.solvedCases)
      }
      if (this.countUpInstances.onlineProbes) {
        this.countUpInstances.onlineProbes.update(this.onlineProbes)
      }
      if (this.countUpInstances.offlineProbes) {
        this.countUpInstances.offlineProbes.update(this.offlineProbes)
      }
      if (this.countUpInstances.totalEvidence) {
        this.countUpInstances.totalEvidence.update(this.totalEvidence)
      }
      if (this.countUpInstances.analyzedEvidence) {
        this.countUpInstances.analyzedEvidence.update(this.analyzedEvidence)
      }
      if (this.countUpInstances.activeUsers) {
        this.countUpInstances.activeUsers.update(this.activeUsers)
      }
    }
  }
}
</script>

<style scoped>
/* 基础样式 */
.tech-screen {
  width: 100%;
  height: 100vh;
  background-color: #0e2147;
  color: #fff;
  overflow: hidden;
  font-family: "Microsoft YaHei", sans-serif;
  position: relative;
  background-image: radial-gradient(circle at 50% 50%, #1a3a6c 0%, #0e2147 70%);
}

/* 进场动画样式 */
.entrance-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #0e2147;
  z-index: 100;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.logo-container {
  margin-bottom: 50px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4992ff, #00ffb4);
  display: flex;
  justify-content: center;
  align-items: center;
  animation: pulse 2s infinite;
  box-shadow: 0 0 30px rgba(73, 146, 255, 0.8);
}

.logo-inner {
  width: 90px;
  height: 90px;
  border-radius: 50%;
  background-color: #0e2147;
  position: relative;
}

.logo-inner::before, .logo-inner::after {
  content: '';
  position: absolute;
  background-color: #00ffb4;
}

.logo-inner::before {
  width: 50px;
  height: 10px;
  top: 40px;
  left: 20px;
  border-radius: 5px;
}

.logo-inner::after {
  width: 10px;
  height: 50px;
  top: 20px;
  left: 40px;
  border-radius: 5px;
}

.logo-text {
  font-size: 28px;
  font-weight: bold;
  margin-top: 20px;
  background: linear-gradient(to right, #4992ff, #00ffb4);
  -webkit-background-clip: text;
  color: transparent;
  animation: fadeIn 2s;
}

.loading-bar {
  width: 300px;
  height: 6px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 10px;
}

.loading-progress {
  height: 100%;
  background: linear-gradient(to right, #4992ff, #00ffb4);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.loading-text {
  font-size: 16px;
  color: #4992ff;
}

/* 主内容区样式 */
.main-content {
  width: 100%;
  height: 100%;
  padding: 20px;
  box-sizing: border-box;
  opacity: 0;
  transition: opacity 1s ease;
}

.show-content {
  opacity: 1;
}

.header {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-bottom: 20px;
  position: relative;
}

.title {
  position: relative;
  text-align: center;
}

.title-text {
  font-size: 48px;
  font-weight: bold;
  background: linear-gradient(to right, #4992ff, #00ffb4);
  -webkit-background-clip: text;
  color: transparent;
  text-shadow: 0 0 10px rgba(73, 146, 255, 0.5);
  position: relative;
  z-index: 1;
}

.title-decoration {
  position: absolute;
  left: -20px;
  right: -20px;
  height: 2px;
  background: linear-gradient(to right, transparent, #4992ff, #00ffb4, transparent);
  z-index: 0;
}

.time {
  position: absolute;
  right: 20px;
  font-size: 18px;
  color: #4992ff;
  padding: 5px 15px;
  border-radius: 20px;
  background-color: rgba(73, 146, 255, 0.1);
  border: 1px solid rgba(73, 146, 255, 0.3);
}

.content {
  height: calc(100% - 80px);
  position: relative;
  display: block;
}

.row {
  display: flex;
  margin-bottom: 20px;
  flex: 1;
}

.row:last-child {
  margin-bottom: 0;
}

/* 面板样式调整 */
.panel {
  flex: 0 0 32%;
  background-color: rgba(16, 42, 90, 0.7);
  border-radius: 10px;
  margin-bottom: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(73, 146, 255, 0.2);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
  animation: fadeInUp 1s;
  animation-fill-mode: both;
  backdrop-filter: blur(5px);
}

.panel::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(to right, #4992ff, #00ffb4);
}

.panel:last-child {
  margin-right: 0;
}

.panel-header {
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title {
  font-size: 18px;
  color: #4992ff;
  font-weight: bold;
  position: relative;
  padding-left: 15px;
}

.panel-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 5px;
  height: 18px;
  background: linear-gradient(to bottom, #4992ff, #00ffb4);
  border-radius: 3px;
}

.panel-decoration {
  display: flex;
  align-items: center;
}

.decoration-line {
  width: 40px;
  height: 2px;
  background: linear-gradient(to right, #4992ff, #00ffb4);
}

.decoration-circle {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #00ffb4;
  margin-left: 5px;
  animation: pulse 2s infinite;
}

.panel-content {
  flex: 1;
  padding: 0 15px 15px;
  display: flex;
  flex-direction: column;
}

.data-cards {
  display: flex;
  margin-bottom: 15px;
}

.data-card {
  flex: 1;
  background-color: rgba(16, 42, 90, 0.7);
  border-radius: 8px;
  padding: 10px;
  margin-right: 10px;
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(73, 146, 255, 0.2);
}

.data-card:last-child {
  margin-right: 0;
}

.data-value {
  font-size: 24px;
  font-weight: bold;
  color: #00ffb4;
  margin-bottom: 5px;
}

.data-label {
  font-size: 14px;
  color: #4992ff;
}

.data-icon {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chart-container {
  flex: 1;
  width: 100%;
  height: 30vh;
}

/* 动态元素样式 */
.rotating-circle {
  width: 20px;
  height: 20px;
  border: 2px solid #4992ff;
  border-top-color: #00ffb4;
  border-radius: 50%;
  animation: rotate 2s linear infinite;
}

.pulse-circle {
  width: 20px;
  height: 20px;
  background-color: rgba(0, 255, 180, 0.3);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

.check-icon {
  width: 20px;
  height: 20px;
  position: relative;
}

.check-icon::before, .check-icon::after {
  content: '';
  position: absolute;
  background-color: #00ffb4;
}

.check-icon::before {
  width: 6px;
  height: 2px;
  transform: rotate(45deg);
  left: 4px;
  top: 12px;
}

.check-icon::after {
  width: 12px;
  height: 2px;
  transform: rotate(-45deg);
  left: 7px;
  top: 10px;
}

.online-icon, .offline-icon {
  width: 20px;
  height: 20px;
  border-radius: 50%;
}

.online-icon {
  background-color: #00ffb4;
  box-shadow: 0 0 10px #00ffb4;
  animation: pulse 2s infinite;
}

.offline-icon {
  background-color: #ff5a5a;
  box-shadow: 0 0 10px #ff5a5a;
}

.evidence-icon, .analysis-icon, .user-icon, .data-transfer-icon {
  width: 20px;
  height: 20px;
  position: relative;
}

.evidence-icon::before, .evidence-icon::after {
  content: '';
  position: absolute;
  background-color: #4992ff;
}

.evidence-icon::before {
  width: 12px;
  height: 16px;
  border-radius: 2px;
  left: 4px;
  top: 2px;
}

.evidence-icon::after {
  width: 8px;
  height: 2px;
  left: 6px;
  top: 6px;
  background-color: #00ffb4;
  box-shadow: 0 4px 0 #00ffb4, 0 8px 0 #00ffb4;
}

.analysis-icon::before {
  content: '';
  position: absolute;
  width: 16px;
  height: 16px;
  border: 2px solid #4992ff;
  border-radius: 50%;
  left: 1px;
  top: 1px;
}

.analysis-icon::after {
  content: '';
  position: absolute;
  width: 6px;
  height: 6px;
  background-color: #00ffb4;
  border-radius: 50%;
  left: 7px;
  top: 7px;
}

.user-icon::before {
  content: '';
  position: absolute;
  width: 8px;
  height: 8px;
  background-color: #4992ff;
  border-radius: 50%;
  left: 6px;
  top: 2px;
}

.user-icon::after {
  content: '';
  position: absolute;
  width: 16px;
  height: 10px;
  border-radius: 8px 8px 0 0;
  background-color: #00ffb4;
  left: 2px;
  top: 10px;
}

.data-transfer-icon::before {
  content: '';
  position: absolute;
  width: 16px;
  height: 2px;
  background-color: #4992ff;
  left: 2px;
  top: 9px;
  animation: transferData 2s infinite;
}

.data-transfer-icon::after {
  content: '';
  position: absolute;
  border-style: solid;
  border-width: 4px;
  border-color: transparent #00ffb4 transparent transparent;
  left: 12px;
  top: 5px;
  animation: transferData 2s infinite;
}

/* 雷达扫描效果 */
.radar-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.radar-scan {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
  pointer-events: none;
}

.radar-scan::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  background: conic-gradient(
    rgba(0, 255, 180, 0.3) 0deg,
    transparent 6deg,
    transparent 330deg,
    rgba(0, 255, 180, 0.3) 360deg
  );
  animation: radarScan 4s linear infinite;
  border-radius: 50%;
}

/* 旋转圆环效果 */
.rotating-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.rotating-ring {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px;
  height: 200px;
  border-radius: 50%;
  border: 2px dashed rgba(73, 146, 255, 0.3);
  animation: rotate 20s linear infinite;
  pointer-events: none;
}

.rotating-ring::before, .rotating-ring::after {
  content: '';
  position: absolute;
  border-radius: 50%;
}

.rotating-ring::before {
  top: -10px;
  left: -10px;
  right: -10px;
  bottom: -10px;
  border: 1px solid rgba(0, 255, 180, 0.3);
  animation: rotate 15s linear infinite reverse;
}

.rotating-ring::after {
  top: -20px;
  left: -20px;
  right: -20px;
  bottom: -20px;
  border: 1px dashed rgba(73, 146, 255, 0.2);
  animation: rotate 30s linear infinite;
}

/* 装饰元素 */
.decoration-elements {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;
}

.corner-decoration {
  position: absolute;
  width: 100px;
  height: 100px;
  border-style: solid;
  border-color: rgba(73, 146, 255, 0.3);
  border-width: 2px;
}

.top-left {
  top: 20px;
  left: 20px;
  border-right: none;
  border-bottom: none;
}

.top-right {
  top: 20px;
  right: 20px;
  border-left: none;
  border-bottom: none;
}

.bottom-left {
  bottom: 20px;
  left: 20px;
  border-right: none;
  border-top: none;
}

.bottom-right {
  bottom: 20px;
  right: 20px;
  border-left: none;
  border-top: none;
}

.floating-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.floating-particles::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(circle at 10% 20%, rgba(73, 146, 255, 0.1) 0%, transparent 5%),
    radial-gradient(circle at 30% 70%, rgba(0, 255, 180, 0.1) 0%, transparent 5%),
    radial-gradient(circle at 50% 40%, rgba(73, 146, 255, 0.1) 0%, transparent 5%),
    radial-gradient(circle at 70% 80%, rgba(0, 255, 180, 0.1) 0%, transparent 5%),
    radial-gradient(circle at 90% 10%, rgba(73, 146, 255, 0.1) 0%, transparent 5%);
  background-size: 200% 200%;
  animation: floatingParticles 20s ease infinite;
}

/* 动画定义 */
@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes radarScan {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes transferData {
  0% {
    opacity: 0;
    transform: translateX(-10px);
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0;
    transform: translateX(5px);
  }
}

@keyframes floatingParticles {
  0% {
    background-position: 0% 0%;
  }
  25% {
    background-position: 100% 0%;
  }
  50% {
    background-position: 100% 100%;
  }
  75% {
    background-position: 0% 100%;
  }
  100% {
    background-position: 0% 0%;
  }
}

/* 响应式调整 */
@media screen and (max-width: 1400px) {
  .row {
    flex-direction: column;
  }
  
  .panel {
    margin-right: 0;
    margin-bottom: 20px;
    height: 300px;
  }
  .panel-group {
    width: 35%;
  }
  
  .panel:last-child {
    margin-bottom: 0;
  }
  
  .data-cards {
    flex-wrap: wrap;
  }
  
  .data-card {
    margin-bottom: 10px;
  }
}

/* 面板进入动画延迟 */
.case-statistics {
  animation-delay: 0.1s;
}

.probe-statistics {
  animation-delay: 0.2s;
}

.evidence-analysis {
  animation-delay: 0.3s;
}

.person-portrait {
  animation-delay: 0.4s;
}

.asset-analysis {
  animation-delay: 0.5s;
}

.realtime-monitor {
  animation-delay: 0.6s;
}
/* 地图容器样式 */
.map-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  background-color: #122953 !important;
  transform:scale(0.9);
  border-radius:10px;
  box-shadow:0 0 5px #122953, 0 0 10px #122953, 0 0 15px #122953;
}

/* 面板组样式 */
.panel-group {
  position: absolute;
  top: 0;
  width: 30%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  z-index: 1;
}

.left-panels {
  left: 20px;
}

.right-panels {
  right: 20px;
}


/*地图标题 infoWindow*/
.BMap_bubble_title{
  color:#fff;
  font-size:18px;
  /*font-weight: bold;*/
  text-align:left;
  background:transparent !important;
}

.BMap_pop .BMap_top{
  background:#3F4358 !important;
  border:0 !important;
}
.BMap_pop .BMap_center{
  width:251px !important;
  border:0 !important;
  background:#3F4358 !important;
}
.BMap_pop .BMap_bottom{
  border:0 !important;
  background:#3F4358 !important;
}

.BMap_pop div:nth-child(3){
  background:transparent !important;
}
.BMap_pop div:nth-child(3) div{
  border-radius:7px;
  background:#3F4358 !important;
  border:0 !important;
}
.BMap_pop div:nth-child(1){
  border-radius:7px 0 0 0;
  background:transparent !important;
  border:0 !important;
}
.BMap_pop div:nth-child(1) div{
  background:#3F4358 !important;
}
.BMap_pop div:nth-child(5){
  border-radius:0 0 0 7px;
  background:transparent !important;
  border:0 !important;
}
.BMap_pop div:nth-child(5) div{
  border-radius:7px;
  background:#3F4358 !important;
}
.BMap_pop div:nth-child(7){
  background:transparent !important;
}
.BMap_pop div:nth-child(7) div{
  border-radius:7px;
  background:#3F4358 !important;
}

.BMap_pop div:nth-child(8) div{
  /*border-radius:7px;*/
  background:#3F4358 !important;
}
/*窗体阴影*/
.BMap_shadow div:nth-child(5) img{
  margin-left: -1100px !important;
}

.BMap_shadow div:nth-child(4){
  width: 262px !important;
}


</style>