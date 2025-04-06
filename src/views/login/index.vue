<template>
    <div class="login-container">
        <div class="login-box">
            <h1 class="system-name">{{ title }}</h1>
            <el-form :model="loginForm" :label-position="labelPosition" :size="size" ref="formRef"
                @submit.native.prevent="handleLogin">
                <el-form-item label="" prop="userName">
                    <el-input v-model="loginForm.userName" prefix-icon="User">
                    </el-input>
                </el-form-item>
                <el-form-item label="" prop="password">
                    <el-input v-model="loginForm.password" :type="showPassword ? 'text' : 'password'"
                        prefix-icon="Lock">
                        <template #append>
                            <el-icon @click="showPassword = !showPassword">
                                <el-button :icon="showPassword ? 'View' : 'Hide'" />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-button class="login-button" type="primary"
                    native-type="submit">登&nbsp;&nbsp;&nbsp;&nbsp;录</el-button>
            </el-form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { addDynamicRoutes } from '@/router'; // 根据实际路径引入
const router = useRouter()
import { ApiManager } from '@/api/index'
import { ElNotification } from 'element-plus'
import { useMenuInfoStore } from '@/store/useMenuInfoStore'

const title= import.meta.env.VITE_APP_TITLE;

// 设置表单的样式
const labelPosition = ref<FormProps['labelPosition']>('right')
const size = ref<ComponentSize>('large')
// 表单数据
const loginForm = reactive({
    userName: 'admin',
    password: '1'
});
// 是否显示密码
const showPassword = ref(false);

// 登录
const handleLogin = () => {
    console.log('登录信息', loginForm);
    let params = {
        queryStr: "user_name ='" + loginForm.userName + "' and user_password ='" + loginForm.password + "'"
    };
    ApiManager.post('/login', params).then((res: any) => {
        console.log('登录信息', res);
        if (res.code == "200") {
            let obj = res.data.user;
            window.sessionStorage.setItem("userId", obj.id);
            window.sessionStorage.setItem("userRealName", obj.userRealName);
            window.sessionStorage.setItem("password", obj.userPassword);
            const menusStore = useMenuInfoStore()
            menusStore.setMenus(res.data.menuList)
            
            addDynamicRoutes(res.data.menuList)
            router.push({path: '/'})
            
            
        }else{
            ElNotification({
                title: '错误',
                message: res.message,
                type: 'error',
            })
        }
    })
};

</script>
<style scoped>
@keyframes gradientAnimation {
    0% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }

    100% {
        background-position: 0% 50%;
    }
}

.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: linear-gradient(125deg, #f3ec78, #af4261, #86e3ce, #f1c40f, #fc5c7d, #6a82fb, #ff9a9e, #fad0c4);
    /* 多彩渐变背景 */
    background-size: 400%;
    animation: gradientAnimation 15s ease infinite;
}


.login-box {
    background: white;
    padding: 40px 30px;
    border-radius: 10px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    width: 300px;
    /* 调整宽度 */
}

.system-name {
    text-align: center;
    margin-bottom: 20px;
    font-size: 28px;
    /* 调整字体大小 */
}

.el-form-item {
    margin-bottom: 20px;
    /* 增加输入框之间的间距 */
}

.login-button {
    margin-top: 20px;
    height: 40px;
    font-size: 18px;
    width: 100%;
}
</style>
