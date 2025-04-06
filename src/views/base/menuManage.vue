<template>
    <div>
        <el-form :model="queryPanel" ref="queryPanel" label-width="80px">
            <el-row class="query_panel_container">
                <el-form-item label="功能名称" prop="menuTitle">
                    <el-input v-model="queryPanel.menuTitle" clearable></el-input>
                </el-form-item>
                <el-button class="search_button" icon="search" @click="handleQuery">查询</el-button>
                <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
            </el-row>
        </el-form>

        <el-dialog v-model="visible" title="新增或修改">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="功能编号" prop="menuCode">
                    <el-input v-model="form.menuCode" clearable></el-input>
                </el-form-item>
                <el-form-item label="功能名称" prop="menuTitle">
                    <el-input v-model="form.menuTitle" clearable></el-input>
                </el-form-item>
                <el-form-item label="跳转路径" prop="menuUrl">
                    <el-input v-model="form.menuUrl" clearable></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="menuMemo">
                    <el-input type="textarea" :rows="2" v-model="form.menuMemo"></el-input>
                </el-form-item>
                <el-form-item label="模块" prop="moduleId">
                    <el-select v-model="form.moduleId" placeholder="请选择模块" filterable clearable>
                        <el-option :label="item.title" :value="item.id" v-for="item in moduleIdDatas"
                            :key="item.id"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="handleSave">{{ saveBtnTitle }}</el-button>
                    <el-button @click="visible = false">取消</el-button>
                </el-form-item>

            </el-form>
        </el-dialog>

        <el-row class="content_table">
            <el-col :span="24" class="table_info">
                <el-table height="100%" :data="tableData"  style="width: 100%">

                    <el-table-column prop="menuCode" label="功能编号" width="180">
                    </el-table-column>
                    <el-table-column prop="menuTitle" label="功能名称" width="180">
                    </el-table-column>
                    <el-table-column prop="menuUrl" label="跳转路径" width="180">
                    </el-table-column>
                    <el-table-column prop="menuMemo" label="备注" width="180">
                    </el-table-column>
                    <el-table-column prop="moduleIdName" label="模块">
                    </el-table-column>

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
import { ApiManager } from '@/api/index'
export default {
    data: function () {
        return {
            visible: false,
            queryPanel: {
                menuTitle: '',
            },
            tableData: [],
            form: {},
            saveBtnTitle: '立即创建',
            currentPage: 1,
            pageSize: 10,
            rowTotal: 0,
            moduleIdDatas: [],
            rules: {
                menuTitle: [{ required: true, message: "请输入正确的功能名称", trigger: 'blur' }],
                menuUrl: [{ required: true, message: "请输入正确的跳转路径", trigger: 'blur' }],
            }
        }
    },
    methods: {
        handleAdd: function () {
            let that = this;
            that.visible = true;
            that.form = {};
            that.saveBtnTitle = '立即创建'
        },
        handleSave: function () {
            let that = this;
            that.$refs["form"].validate((valid) => {
                if (valid) {
                    let data = that.form

                    let id = data.id;
                    let url = "menu/add";
                    let message = '保存成功';
                    if (typeof id != "undefined") {
                        url = "menu/update";
                        message = '修改成功';
                    }
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
            ApiManager.get('menu/detail', data).then((res) => {
                if (res.message == "SUCCESS") {
                    that.form = res.data;
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
                queryStr: "menu_title like '%" + that.queryPanel.menuTitle + "%'" 
            };
            ApiManager.post('menu/listCondition', data).then((res) => {
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
                ApiManager.get('menu/delete', data).then((res) => {
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
        getModuleIdDatas: function () {
            let that = this;
            let data = {
            };
            ApiManager.post('menu/getModuleIdDatas', data).then((res) => {
                that.moduleIdDatas = [{"id":0,"title":"请选择"}].concat(res.data);
            });
        },
    },
    created() {
        this.handleQuery();
        this.getModuleIdDatas();
    }
}
</script>