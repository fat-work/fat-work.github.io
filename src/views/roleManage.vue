<template>
    <div>
        <el-form :model="queryPanel" ref="queryPanel" label-width="80px">
            <el-row class="query_panel_container">
                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model="queryPanel.roleName" clearable></el-input>
                </el-form-item>
                <el-button class="search_button"  icon="search" @click="handleQuery">查询</el-button>
                <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
            </el-row>
        </el-form>

        <el-dialog v-model="visible" title="新增或修改">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model="form.roleName" clearable></el-input>
                </el-form-item>
                <el-form-item label="菜单权限" prop="menus">
                    <el-checkbox-group v-model="formMenus">
                        <el-checkbox :label="item.id" v-for="item in menusDatas" :key="item.id" @change="menusChange">{{
                            item.title }}</el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
                <el-form-item label="备注" prop="roleMemo">
                    <el-input type="textarea" :rows="2" v-model="form.roleMemo"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="handleSave">{{ saveBtnTitle }}</el-button>
                    <el-button @click="visible = false">取消</el-button>
                </el-form-item>

            </el-form>
        </el-dialog>

        <el-row class="content_table">
            <el-col :span="24" class="table_info">
                <el-table height="100%" :data="tableData" border style="width: 100%">

                    <el-table-column prop="roleName" label="角色名称" width="180">
                    </el-table-column>
                    <el-table-column prop="roleMemo" label="备注">
                    </el-table-column>

                    <el-table-column fixed="right" label="操作" width="110">
                        <template #default="scope">
                            <el-button size="small" type="primary" @click.prevent="handleEdit(scope.$index, scope.row)" link>编辑</el-button>
                            <el-button size="small" type="danger"
                                @click.prevent="handleDelete(scope.$index, scope.row)" link>删除</el-button>
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
import { ApiManager } from '@/api/index'
export default {
    data: function () {
        return {
            visible: false,
            queryPanel: {
                roleName: '',
            },
            tableData: [],
            form: {},
            saveBtnTitle: '立即创建',
            currentPage: 1,
            pageSize: 10,
            rowTotal: 0,
            menusDatas: [],
            formMenus: [],
            rules: {
                roleName: [{ required: true, message: "请输入正确的角色名称", trigger: 'blur' }],
            }
        }
    },
    methods: {
        handleAdd: function () {
            let that = this;
            that.visible = true;
            that.form = {};
            that.formMenus = []
            that.saveBtnTitle = '立即创建'
        },
        handleSave: function () {
            let that = this;
            that.$refs["form"].validate((valid) => {
                if (valid) {
                    let data = that.form

                    let id = data.id;
                    let url = "role/add";
                    let message = '保存成功';
                    if (typeof id != "undefined") {
                        url = "role/update";
                        message = '修改成功';
                    }
                    that.form.menus = JSON.stringify(that.formMenus)
                    ApiManager.post(url, data).then((res) =>{
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
            ApiManager.get('role/detail', data).then((res) =>{
                if (res.message == "SUCCESS") {
                    that.form = res.data;
                    that.formMenus = JSON.parse(that.form.menus)
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
                queryStr: "role_name like '%" + that.queryPanel.roleName + "%'"
            };
            ApiManager.post('role/listCondition', data).then((res) =>{
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
                ApiManager.get('role/delete', data).then((res) =>{
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
        getMenusDatas: function () {
            let that = this;
            let data = {
            };
            ApiManager.post('role/getMenusDatas', data).then((res) =>{
                that.menusDatas = res.data;
            });
        },
    },
    created() {
        this.handleQuery();
        this.getMenusDatas();
    }
}
</script>