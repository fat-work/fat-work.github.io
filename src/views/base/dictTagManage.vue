<template>
    <div>
        <el-form :model="queryPanel" ref="queryPanel" label-width="80px">
            <el-row class="query_panel_container">
                <el-form-item label="编号" prop="code">
                    <el-input v-model="queryPanel.code" clearable></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="title">
                    <el-input v-model="queryPanel.title" clearable></el-input>
                </el-form-item>
                <el-button class="search_button" icon="search" @click="handleQuery">查询</el-button>
                <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
            </el-row>
        </el-form>

        <el-dialog v-model="visible" title="新增或修改">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="名称" prop="title">
                    <el-input v-model="form.title" @change="handlePinyin" clearable></el-input>
                </el-form-item>
                <el-form-item label="编号" prop="code">
                    <el-input v-model="form.code" clearable></el-input>
                </el-form-item>
                <el-form-item label="说明" prop="remark">
                    <el-input type="textarea" :rows="2" v-model="form.remark"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="handleSave">{{ saveBtnTitle }}</el-button>
                    <el-button @click="visible = false">取消</el-button>
                </el-form-item>

            </el-form>
        </el-dialog>

        <el-drawer title="字典项" :with-header="false" v-model="drawer" :size="drawerWidth" :direction="direction">
            <dictManage :defaultTag="tag"></dictManage>
        </el-drawer>

        <el-row class="content_table">
            <el-col :span="24" class="table_info">
                <el-table height="100%" :data="tableData" style="width: 100%">

                    <el-table-column prop="code" show-overflow-tooltip label="编号" width="180">
                        <template #default="scope">
                            <el-button type="text" @click="handleDetail(scope.$index, scope.row)">
                                {{ scope.row.code }}</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="title" show-overflow-tooltip label="名称" width="180">
                        <template #default="scope">
                            <el-button type="text" @click="handleDetail(scope.$index, scope.row)">{{
                                scope.row.title }}</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="remark" show-overflow-tooltip label="说明">
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
import dictManage from './dictManage.vue';
import { pinyin } from "pinyin-pro";
export default {
    components: {
        dictManage
    },
    data: function () {
        return {
            drawerWidth: '60%',
            visible: false,
            queryPanel: {
                code: '',
                title: '',
            },
            drawer: false,
            direction: 'rtl',
            tableData: [],
            form: {},
            saveBtnTitle: '立即创建',
            currentPage: 1,
            pageSize: 10,
            rowTotal: 0,
            multipleSelection: [],
            rules: {
            },
            tag: ''
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
                    let url = "dictTag/add";
                    let message = '保存成功';
                    if (typeof id != "undefined") {
                        url = "dictTag/update";
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
            ApiManager.get('dictTag/detail', data).then((res) => {
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
                queryStr: "code like '%" + (that.queryPanel.code == null ? "" : that.queryPanel.code) + "%'" + " and " + "title like '%" + (that.queryPanel.title == null ? "" : that.queryPanel.title) + "%'"
            };
            ApiManager.post('dictTag/listByCondition', data).then((res) => {
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
                ApiManager.get('dictTag/delete', data).then((res) => {
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
        handleDetail: function (index, row) {
            this.tag = row.code
            this.drawer = true;
        },
        handlePinyin: function () {
            console.log(this.form.title)
            let code = pinyin(this.form.title, { pattern: 'pinyin', toneType: 'none', type: 'array' }).join('');
            this.form.code = code
            // this.$set(this.form, 'code', code)
        },
    },
    created() {
        this.handleQuery();
    }
}
</script>