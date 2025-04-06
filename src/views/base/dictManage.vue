<template>
    <div>
        <el-form :model="queryPanel" ref="queryPanel" label-width="80px">
            <el-row class="query_panel_container">
                <el-form-item label="名称" prop="title">
                    <el-input v-model="queryPanel.title" clearable></el-input>
                </el-form-item>
                <!-- <el-form-item label="组" prop="title">
                    <el-input v-model="queryPanel.tag" clearable></el-input>
                </el-form-item> -->
                <el-button class="search_button" icon="search" @click="handleQuery">查询</el-button>
                <el-button type="primary" icon="plus" @click="handleAdd">新增</el-button>
            </el-row>
        </el-form>

        <el-dialog v-model="visible" title="新增或修改" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-form-item label="名称">
                    <el-input ref="titleRef" v-model="form.title" @keyup.enter.native="handleSaveNext"
                        clearable></el-input>
                </el-form-item>
                <el-form-item label="编号">
                    <el-input v-model="form.code" clearable></el-input>
                </el-form-item>
                <el-form-item label="组">
                    <el-input v-model="form.tag" clearable></el-input>
                </el-form-item>
                <el-form-item label="父级id">
                    <el-input v-model="form.pid" clearable></el-input>
                </el-form-item>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="额外信息一">
                            <el-input v-model="form.e1" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="额外信息二">
                            <el-input v-model="form.e2" clearable></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="额外信息三">
                            <el-input v-model="form.e3" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="额外信息四">
                            <el-input v-model="form.e4" clearable></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item>
                    <el-button type="success" @click="handleSaveNext">保存并新增下一个</el-button>
                    <el-button type="primary" @click="handleSave">{{ saveBtnTitle }}</el-button>
                    <el-button @click="visible = false">取消</el-button>
                </el-form-item>

            </el-form>
        </el-dialog>

        <el-row class="content_table">
            <el-col :span="24" class="table_info">
                <el-table height="100%" :data="tableData" style="width: 100%">

                    <el-table-column prop="id" label="标识" width="50">
                    </el-table-column>
                    <el-table-column prop="title" label="名称" width="180">
                    </el-table-column>
                    <el-table-column prop="code" label="编号" width="180">
                    </el-table-column>
                    <el-table-column prop="tag" label="组" width="180">
                    </el-table-column>
                    <el-table-column prop="pid" label="父级id" width="180">
                    </el-table-column>
                    <el-table-column prop="e1" label="额外信息一" width="180">
                    </el-table-column>
                    <el-table-column prop="e2" label="额外信息二" width="180">
                    </el-table-column>
                    <el-table-column prop="e3" label="额外信息三" width="180">
                    </el-table-column>
                    <el-table-column prop="e4" label="额外信息四">
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
    props: ['defaultTag'],
    watch: {
        defaultTag: function (newValue, oldValue) {
            this.queryPanel.tag = newValue;
            this.handleQuery();
        }
    },
    data: function () {
        return {
            visible: false,
            queryPanel: {
                title: '',
                tag: ''
            },
            tableData: [],
            form: {},
            saveBtnTitle: '立即创建',
            currentPage: 1,
            pageSize: 10,
            rowTotal: 0,
            rules: []
        }
    },
    methods: {
        handleAdd: function () {
            let that = this;
            that.visible = true;
            console.log(that);
            that.form = {
                tag: that.defaultTag
            };
            that.saveBtnTitle = '立即创建'
        },
        handleSave: function () {
            let that = this;
            that.$refs["form"].validate((valid) => {
                if (valid) {
                    let data = that.form

                    let id = data.id;
                    let url = "dict/add";
                    let message = '保存成功';
                    if (typeof id != "undefined") {
                        url = "dict/update";
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
            ApiManager.get('dict/detail', data).then((res) => {
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
                queryStr: "title like '%" + that.queryPanel.title + "%'" + " and tag like '%" + that.queryPanel.tag + "%'"
            };
            ApiManager.post('dict/listCondition', data).then((res) => {
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
                ApiManager.get('dict/delete', data).then((res) => {
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
        handleSaveNext() {

            let that = this;
            let data = that.form

            let id = data.id;
            let url = "dict/add";
            let message = '保存成功';
            if (typeof id != "undefined") {
                url = "dict/update";
                message = '修改成功';
            }

            ApiManager.post(url, data).then((res) => {
                if (res.message == "SUCCESS") {
                    that.$notify({
                        title: '成功',
                        message: message,
                        type: 'success'
                    });
                    that.form = {
                        title: '',
                        tag: that.defaultTag,
                    };
                    that.$nextTick(function () {
                        that.$refs.titleRef.focus();
                    });
                    that.handleQuery();
                }
            })


        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.handleQuery();
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.handleQuery();
        },
        dictsChange: function (val) {
            console.log(val)
        },
    },
    mounted() {
        this.queryPanel.tag = this.defaultTag
        console.log(this.defaultTag);
        this.handleQuery();
    },
    created() {
        // this.handleQuery();
    }
}
</script>