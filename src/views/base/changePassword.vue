<template>
    <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="原密码">
            <el-input v-model="form.oldPassword"></el-input>
        </el-form-item>

        <el-form-item label="新的密码">
            <el-input v-model="form.npassword"></el-input>
        </el-form-item>

        <el-form-item label="密码确认">
            <el-input v-model="form.ppassword"></el-input>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="handleSave">{{ saveBtnTitle }}</el-button>
        </el-form-item>
        
    </el-form>
</template>
<script>
import { ApiManager } from '@/api/index'
export default {
    data() {
        return {
            form: {
                oldPassword: '',
                npassword: '',
                ppassword: ''
            },
            userId: window.sessionStorage.getItem('userId'),
            saveBtnTitle: '保存',
            visible: false
        }
    },
    methods: {
        handleSave() {
            let that = this;
            let data = that.form
            data.userId = that.userId;
            let url = "user/changePassword";
            let message = '密码修改成功';
            if (!this.validatePwd()) {
                return
            }
            ApiManager.post(url, data).then((res)=> {
                console.log(res)
                if (res.code == '200') {
                    that.$notify({
                        title: '成功',
                        message: message,
                        type: 'success'
                    });
                } else {
                    console.log(res)
                    that.$notify({
                        title: '失败',
                        message: res.message,
                        type: 'error'
                    });
                }
            })
        },
        validatePwd() {
            let that = this;
            // 校验密码的复杂性，至少有字母，数字
            // if (!/^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,}$/.test(that.form.npassword)) {
            //     that.$notify({
            //         title: '提示',
            //         message: '密码必须包含字母和数字，且长度大于6位',
            //         type: 'danger'
            //     });
            //     return false
            // }
            if (that.form.npassword != that.form.ppassword) {
                that.$notify({
                    title: '提示',
                    message: '您两次输入的密码不一致',
                    type: 'danger'
                });
                return false
            }
            return true
        }
    }
}
</script>