<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-row :gutter="20">
                    <el-col :span="6">
                        <div class="system-name">{{title}}</div>
                    </el-col>
                    <el-col :span="12" >
                        <el-menu  mode="horizontal" class="el-menu-demo" :default-active="activeIndex2"
                            background-color="#409EFF"
                            text-color="#fff"
                            active-text-color="#ffd04b"
                        >
                            <el-menu-item v-for="item,index in horizontalMenu" :key="index" :index="item.key+''" @click="handleSelect(item.key, item.keyPath)" >{{ item.title }}</el-menu-item>
                        </el-menu>
                    </el-col>
                    <el-col :span="6" class="user-info">
                        <el-dropdown>
                            <span class="el-dropdown-link" >
                                {{ userRealName }}
                                <el-icon class="el-icon--right">
                                    <arrow-down />
                                </el-icon>
                            </span>
                            <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item @click.native="changePassword">修改密码</el-dropdown-item>

                                <el-dropdown-item divided @click.native="logout">退出</el-dropdown-item>
                            </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </el-col>
                </el-row>
            </el-header>
            <el-container>
                <el-aside width="200px" v-if="showAsideMenu">
                    <el-scrollbar>
                        <el-menu :default-openeds="['1', '2']">
                            <el-sub-menu index="2">
                                <template #title>
                                    <el-icon><Monitor /></el-icon>业务功能
                                </template>
                                <el-menu-item-group>
                                    <el-menu-item v-for="item,index in busiMenus" :key="index" :index="item.id+''" @click="handleNav(item.id, item.url)">{{ item.title }}</el-menu-item>
                                </el-menu-item-group>
                            </el-sub-menu>
                            <el-sub-menu index="1">
                                <template #title>
                                    <el-icon>
                                        <setting />
                                    </el-icon>基础功能
                                </template>
                                <el-menu-item-group>
                                    <el-menu-item v-for="item,index in baseMenus" :key="index" :index="item.id+''" @click="handleNav('1', item.url)">{{ item.title }}</el-menu-item>
                                </el-menu-item-group>
                            </el-sub-menu>
                            

                        </el-menu>
                    </el-scrollbar>
                </el-aside>
                <el-main>
                    <!-- <keep-alive>
                        <router-view />
                    </keep-alive> -->

                    <router-view v-slot="{ Component }">
                        <keep-alive>
                            <component :is="Component" />
                        </keep-alive>
                    </router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>



<script setup lang="ts">
import { ref,onMounted  } from 'vue';
import { useRouter } from 'vue-router';
const isCollapse = ref(false);
console.log(window.sessionStorage.getItem('userRealName'));
const userRealName = ref(window.sessionStorage.getItem('userRealName'));
const router = useRouter();
import ApiManager from '@/api/index';

const title= import.meta.env.VITE_APP_TITLE;
const showAsideMenu = ref(false)

const activeIndex2 = ref('2')
const horizontalMenu = [
    {"key":'1',"keyPath":"busi/caseInfoManage","title":"案件"},
    {"key":'5',"keyPath":"busi/probeInfoManage","title":"设备"},
    {"key":'2',"keyPath":"chat/index","title":"AI助手"},
    {"key":'3',"keyPath":"screen","title":"数据看板"},
    {"key":'4',"keyPath":"busi/caseInfoManage","title":"管理"},
    {"key":'6',"keyPath":"message/index","title":"消息中心"},


]
const handleSelect = (key: string, keyPath: string[]) => {
    showAsideMenu.value= false
    if(key=='3'){
        const url = router.resolve({
            path: keyPath,
        });
        window.open(url.href);
    }else if(key=='4'){
        showAsideMenu.value= true
        keyPath = keyPath.replace('/', '-')
        router.push({ path: keyPath });
    }else{
        keyPath = keyPath.replace('/', '-')
        router.push({ path: keyPath });
    }
}

onMounted(() => {
    // 获取菜单信息
    getMenus();
})

 const menus = ref([]),baseMenus = ref([]),busiMenus = ref([]),devMenus = ref([]);
// 获取菜单信息
const getMenus = () => {
    let data = {
        id: window.sessionStorage.getItem("userId")
    };
    ApiManager.post('/getMenus',data).then(res => {
        console.log(res);
        menus.value = res.data.list;
        
        baseMenus.value = menus.value.filter(item => item.moduleId === 5);
        busiMenus.value = menus.value.filter(item => item.moduleId === 6);

        handleSelect('2', 'chat/index');
    })
}
// 点击菜单
const handleNav = (key: string, keyPath: string) => {
    console.log(key, keyPath);
    keyPath = keyPath.replace('/', '-')
    router.push({ path: keyPath });
};
// 修改密码
const changePassword = () => {
    router.push({ path: '/changePassword' });
};

// 退出
const logout = () => {
    window.sessionStorage.removeItem('userId');
    window.sessionStorage.removeItem('userRealName');
    window.sessionStorage.removeItem('password');
    router.push({ path: '/login' });
};
</script>

<style scoped>
.common-layout {
    width: 100vw;
    height: 100vh;
}

.el-header {
    background-color: #409EFF;
}

.el-aside {
    background-color: #fff;
    height: calc(100vh - 60px);
}

.el-main {
    background-color: #F2F3F5;
}

.system-name {
    color: #fff;
    font-size: 20px;
    font-weight: bold;
    line-height: 60px;
}

.user-info {
    text-align: right;
    line-height: 60px;
    color: #fff;
    font-size: 20px;
    font-weight: bold;
}

.el-dropdown-link {
    cursor: pointer;
    color: #fff;
    display: flex;
  align-items: center;
  line-height: 60px;
  font-size: 20px;
  font-weight: bold;
}

.el-menu-demo{

}
</style>