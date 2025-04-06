import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
 
const app = createApp(App)
 
// 引入 ElementPlus 插件(npm i element-plus)
import ElementPlusPlugin from '@/plugins/element-plus'
 
// 全局注册 ElementPlus 图标组件(npm install @element-plus/icons-vue)
import * as ElementPlusIcons from '@element-plus/icons-vue'
for(const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component)
}
// 全局引入富文本
import QuillEditor from '@/components/QuillEditor/index.vue';
app.component('QuillEditor', QuillEditor);

// 全局引入图片回显
import ImagePreview from '@/components/ImagePreview/index.vue';
app.component('ImagePreview', ImagePreview);

// 引入HTTP请求工具并配置为全局方法
import axios from 'axios'
app.config.globalProperties.$axios = axios
 

app
.use(store)
.use(router)
.use(ElementPlusPlugin)
.mount('#app')
