<template>
    <div>
        <el-form :model="queryPanel" ref="queryPanel" label-width="80px">
            <el-row class="query_panel_container">
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="queryPanel.userName" clearable></el-input>
                </el-form-item>
                <el-form-item label="部门" prop="userDepaId">
                    <el-select style="width: 180px;" v-model="queryPanel.userDepaId" placeholder="请选择部门" filterable clearable @clear="()=>{this.queryPanel.userDepaId=''}">
                        <el-option :label="item.title" :value="item.id" v-for="item in userDepaIdDatas"
                            :key="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-button class="search_button" icon="search" @click="handleQuery">查询</el-button>
                <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
            </el-row>
        </el-form>

        <el-dialog v-model="visible" title="新增或修改">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="form.userName" clearable></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="userPassword">
                    <el-input v-model="form.userPassword" clearable></el-input>
                </el-form-item>
                <el-form-item label="真实姓名" prop="userRealName">
                    <el-input v-model="form.userRealName" clearable></el-input>
                </el-form-item>
                <el-form-item label="角色" prop="roleId">
                    <el-select v-model="form.roleId" placeholder="请选择角色" filterable clearable>
                        <el-option :label="item.title" :value="item.id" v-for="item in roleIdDatas"
                            :key="item.id"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="部门" prop="userDepaId">
                    <el-select v-model="form.userDepaId" placeholder="请选择部门" filterable clearable>
                        <el-option :label="item.title" :value="item.id" v-for="item in userDepaIdDatas"
                            :key="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="性别" prop="userSex">
                    <el-radio-group v-model="form.userSex">
                        <el-radio :label="item.id" v-for="item in userSexDatas" :key="item.id"
                            @change="userSexChange">{{ item.title }}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="地址" prop="userAddress">
                    <el-input v-model="form.userAddress" clearable></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="userPhone">
                    <el-input v-model="form.userPhone" clearable></el-input>
                </el-form-item>
                <el-form-item label="邮政编码" prop="userPost">
                    <el-input v-model="form.userPost" clearable></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="userEmail">
                    <el-input v-model="form.userEmail" clearable></el-input>
                </el-form-item>

                <el-form-item label="头像" prop="userAvatar">
                    <el-upload class="avatar-uploader" :action="uploadAction" :on-success="handleUserAvatarSuccess" multiple="false"
                        :on-remove="handleUserAvatarRemove" :on-preview="handlePreview" :file-list="userAvatarFileList"
                        list-type="picture">
                        <el-button size="small" type="primary" v-if="userAvatarFileList.length==0">点击上传</el-button>
                    </el-upload>
                </el-form-item>
                <!-- <el-form-item label="额外信息1" prop="userE1">
                    <el-input v-model="form.userE1" clearable></el-input>
                </el-form-item>
                <el-form-item label="额外信息2" prop="userE2">
                    <el-input v-model="form.userE2" clearable></el-input>
                </el-form-item>
                <el-form-item label="额外信息3" prop="userE3">
                    <el-input v-model="form.userE3" clearable></el-input>
                </el-form-item> -->

                <el-form-item>
                    <el-button type="primary" @click="handleSave">{{ saveBtnTitle }}</el-button>
                    <el-button @click="visible = false">取消</el-button>
                </el-form-item>

            </el-form>
        </el-dialog>

        <el-row class="content_table">
            <el-col :span="24" class="table_info">
                <el-table height="100%" :data="tableData" style="width: 100%">

                    <el-table-column prop="userName" label="用户名" width="180">
                    </el-table-column>
                    <el-table-column prop="userPassword" label="密码" width="180">
                    </el-table-column>
                    <el-table-column prop="userRealName" label="真实姓名" width="180">
                    </el-table-column>
                    <el-table-column prop="userSexName" label="性别" width="180">
                    </el-table-column>
                    <el-table-column prop="userAddress" label="地址" width="180">
                    </el-table-column>
                    <el-table-column prop="userPhone" label="电话" width="180">
                    </el-table-column>
                    <el-table-column prop="userPost" label="邮政编码" width="180">
                    </el-table-column>
                    <el-table-column prop="userEmail" label="邮箱" width="180">
                    </el-table-column>
                    <el-table-column prop="roleIdName" label="角色" width="180">
                    </el-table-column>
                    <el-table-column prop="userAvatar" label="头像" width="180">
                        <template #default="scope">
                            <image-preview :src="scope.row.userAvatar" width="50px"></image-preview>
                        </template>
                    </el-table-column>
                    <el-table-column prop="userDepaIdName" label="部门" width="180">
                    </el-table-column>
                    <!-- <el-table-column prop="userE1" label="额外信息1"  width="180">
                </el-table-column>
                <el-table-column prop="userE2" label="额外信息2"  width="180">
                </el-table-column>
                <el-table-column prop="userE3" label="额外信息3" >
                </el-table-column> -->

                    <el-table-column fixed="right" label="操作" width="110">
                        <template #default="scope">
                            <el-button size="small" type="primary" @click.prevent="handleEdit(scope.$index, scope.row)"
                                link>编辑</el-button>
                            <el-button size="small" type="danger" @click.prevent="handleDelete(scope.$index, scope.row)"
                                link>删除</el-button>
                        </template>
                    </el-table-column>

                </el-table>
            </el-col>
            <el-col :span="24" class="pagination" style="text-align: right">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-sizes="[10, 20, 30, 100]" :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="rowTotal">
                </el-pagination>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { ApiManager, uploadAction } from '@/api/index'
import ImagePreview from '@/views/components/ImagePreview.vue'
export default {
    components:{
        ImagePreview
    },
    data: function () {
        return {
            uploadAction: uploadAction,
            visible: false,
            queryPanel: {
                userName: '',
                userDepaId: '',
            },
            tableData: [],
            form: {},
            saveBtnTitle: '立即创建',
            currentPage: 1,
            pageSize: 10,
            rowTotal: 0,
            userSexDatas: [],
            roleIdDatas: [],
            userAvatarFileList: [],
            userDepaIdDatas: [],
            rules: {
                userName: [{ required: true, message: "请输入正确的登录名", trigger: 'blur' }],
                userPassword: [{ required: true, message: "请输入正确的密码", trigger: 'blur' }],
                userRealName: [{ required: true, message: "请输入正确的真实姓名", trigger: 'blur' }],
                roleId: [{ required: true, message: "请输入正确的角色", trigger: 'blur' }],
                userDepaId: [{ required: true, message: "请输入正确的部门", trigger: 'blur' }],
            }
        }
    },
    methods: {
        handleAdd: function () {
            let that = this;
            that.visible = true;
            that.form = {
                depaParentId: ''
            };
            that.saveBtnTitle = '立即创建'
        },
        handleSave: function () {
            let that = this;
            that.$refs["form"].validate((valid) => {
                if (valid) {
                    let data = that.form

                    let id = data.id;
                    let url = "user/add";
                    let message = '保存成功';
                    if (typeof id != "undefined") {
                        url = "user/update";
                        message = '修改成功';
                    }
                    that.form.userAvatar=JSON.stringify(that.userAvatarFileList)
                    ApiManager.post(url, data).then((res) => {
                        if (res.message == "SUCCESS") {
                            that.$notify({
                                title: '成功',
                                message: message,
                                type: 'success'
                            });
                            that.visible = false;
                            that.handleQuery();
                        }
                    })
                } else {
                    console.log("error submit!!");
                    return false;
                }
            })
        },
        handleEdit: function (index, row) {
            let that = this;
            let id = row.id;
            let data = {
                id: id
            };
            ApiManager.get('user/detail', data).then((res) => {
                if (res.message == "SUCCESS") {
                    that.form = res.data;
                    that.userAvatarFileList = JSON.parse(that.form.userAvatar)
                    that.visible = true;
                    that.saveBtnTitle = '立即修改'
                }
            });
        },
        handleQuery: function () {
            let that = this;
            let data = {
                page: this.currentPage,
                size: this.pageSize,
                queryStr: "user_name like '%" + that.queryPanel.userName + "%'" + " and " + "user_depa_id like '%" + that.queryPanel.userDepaId + "%'"

            };
            ApiManager.post('user/listCondition', data).then((res) => {
                that.tableData = res.data.list;
                that.rowTotal = res.data.total;
            })
        },
        handleDelete: function (index, row) {
            let that = this;
            that.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let id = row.id;
                let data = {
                    id: id
                };
                ApiManager.get('user/delete', data).then((res) => {
                    if (res.message == "SUCCESS") {
                        that.$notify({
                            title: '成功',
                            message: '删除成功',
                            type: 'success'
                        });
                        that.handleQuery();
                    }
                });
            }).catch(() => {

            });
        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.handleQuery();
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.handleQuery();
        },
        menusChange: function (val) {
            console.log(val)
        },
        handleUserAvatarSuccess(res, file) {
            this.userAvatarFileList.push(res.data)
        },
        handleUserAvatarRemove(file, fileList) {
            this.userAvatarFileList = fileList
        },
        userDepaIdChange: function (val) {
            console.log(val)
        },
        handlePreview(file) {
            var aEle = document.createElement("a");// 创建a标签
            aEle.download = file.name;// 设置下载文件的文件名
            aEle.href = file.url;// 后台返回的下载地址
            aEle.click();// 设置点击事件
        },
        getUserSexDatas: function () {
            let that = this;
            let data = {
            };
            ApiManager.post('user/getUserSexDatas', data).then((res) => {
                that.userSexDatas = res.data;
            });
        },
        getRoleIdDatas: function () {
            let that = this;
            let data = {
            };
            ApiManager.post('user/getRoleIdDatas', data).then((res) => {
                that.roleIdDatas = res.data;
            });
        },
        getUserDepaIdDatas: function () {
            let that = this;
            let data = {
            };
            ApiManager.post('user/getUserDepaIdDatas', data).then((res) => {
                that.userDepaIdDatas = res.data;
            });
        },
    },
    created() {
        this.handleQuery();
        this.getUserSexDatas();
        this.getRoleIdDatas();
        this.getUserDepaIdDatas();
    }
}
</script>