<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="所属案件" prop="caseID">
                            <el-select v-model="queryPanel.caseID" placeholder="请选择所属案件" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in caseIDDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="事件标题" prop="event_title">
                            <el-input v-model="queryPanel.event_title" clearable></el-input>
                </el-form-item>
                <el-form-item label="重要性" prop="importance">
                            <el-select v-model="queryPanel.importance" placeholder="请选择重要性" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in importanceDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
            <el-form-item>
                <el-button id="queryBtnId" icon="search" @click="handleQuery">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button id="addBtnId" type="primary" icon="plus" @click="handleAdd">新增</el-button>
                <el-button id="deleteMoreBtnId" type="danger" icon="delete" @click="handleDeleteMore">批量删除</el-button>
                <el-button id="exportBtnId" type="success" icon="download" @click="handleExport">导出</el-button>
            </el-form-item>
        </el-row>
    </el-form>


    <el-dialog v-model="visible" title="新增或修改" fullscreen class="dialog_info">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
            <el-row :gutter="10">
                    <el-col :span="24">
                        <el-form-item label="所属案件" prop="caseID">
                            <el-select v-model="form.caseID" placeholder="请选择所属案件" filterable clearable @change="caseIDChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in caseIDDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="事件时间" prop="event_time">
                    <el-date-picker type="date" v-model="form.event_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="事件标题" prop="event_title">
                            <el-input v-model="form.event_title" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="事件描述" prop="event_desc">
                    <div style="width: 100%;height: 200px;">
                        <quill-editor ref="event_descQuillEditor"  v-model="form.event_desc"/>
                    </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="重要性" prop="importance">
                            <el-select v-model="form.importance" placeholder="请选择重要性" filterable clearable @change="importanceChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in importanceDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
            </el-row>
            <el-form-item class="form_btn_info">
                <el-button id="saveBtnId" type="primary" @click="handleSave">{{saveBtnTitle}}</el-button>
                <el-button id="cancelBtnId" @click="visible = false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>

    <el-row  class="content_table">
        <el-col :span="24" class="table_info">
            <el-table id="dataTableId" :data="tableData"  border style="width: 100%" @selection-change="handleSelectionChange" >
                <el-table-column type="selection" width="40">
                </el-table-column>
                        <el-table-column prop="caseIDName" show-overflow-tooltip label="所属案件"  width="180">
                        </el-table-column>
                        <el-table-column prop="event_time" show-overflow-tooltip label="事件时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="event_title" show-overflow-tooltip label="事件标题"  width="180">
                        </el-table-column>
                        <el-table-column prop="event_desc" show-overflow-tooltip label="事件描述"  width="180">
                            <template #default="scope">
                                <div v-html="scope.row.event_desc" ></div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="importanceName" show-overflow-tooltip label="重要性" >
                        </el-table-column>

                <el-table-column fixed="right" label="操作" width="110">
                    <template #default="scope">
                        <el-button class="updateBtnCls" size="small" @click="handleEdit(scope.$index, scope.row)" link>修改</el-button>
                        <el-button class="deleteBtnCls" size="small" type="danger" @click="handleDelete(scope.$index, scope.row)" link>删除</el-button>
                    </template>
                </el-table-column>

            </el-table>
        </el-col>
        <el-col :span="24" class="pagination" style="text-align: right">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 30, 100]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="rowTotal">
            </el-pagination>
        </el-col>
    </el-row>
</template>

<script>
    import { ApiManager, uploadAction } from '@/api/index'

    export default {
        data: function() {
            return {
                uploadAction: uploadAction,
                visible: false,
                queryPanel: {
                    caseID:'',
                    event_title:'',
                    importance:'',
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                caseIDDatas: [],
                importanceDatas: [],
                editorOption:{
                    placeholder: '请在这里输入',
                    theme: 'snow',
                    modules: {
                        history: {
                            delay: 1000,
                            maxStack: 50,
                            userOnly: false
                        },
                        toolbar: {
                            container: toolbarOptions,
                        }
                    }
                },
                rules: {
                }
            }
        },
        methods: {
            handleAdd: function() {
                let that = this;
                that.visible = true;
                that.form = {};
                that.saveBtnTitle = '立即创建'
            },
            handleSelectionChange:function(val){
                this.multipleSelection = val;
            },
                
			handleSave: function() {
                let that = this;
                that.$refs["form"].validate((valid) => {
                    if (valid) {
                        let data = that.form

                        let id = data.id;
                        let url = "caseTimeline/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "caseTimeline/update";
                            message = '修改成功';
                        }
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
                    }else{
                        console.log("error submit!!");
                        return false;
                    }
                })
            },
                
			handleQuery: function() {
                let that = this;

                let data = {
                    page: this.currentPage,
                    size: this.pageSize,
                    caseID:that.queryPanel.caseID,
                    event_title:that.queryPanel.event_title,
                    importance:that.queryPanel.importance,
                };
                ApiManager.post('caseTimeline/listByCondition', data).then((res) =>{
                    that.tableData = res.data.list;
                    that.rowTotal = res.data.total;
                })


            },
                
			handleExport: function() {
                let that = this;
                let data = {
                    page:this.currentPage,
                    size:this.pageSize,
                    caseID:that.queryPanel.caseID,
                    event_title:that.queryPanel.event_title,
                    importance:that.queryPanel.importance,
                };
                ApiManager.download('caseTimeline/exportExcel', data).then((res) =>{
                })
            },
                
			handleDelete: function(index, row) {
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
                    ApiManager.get('caseTimeline/delete', data).then((res) =>{
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
                
			handleEdit: function(index, row) {
                let that = this;
                let id = row.id;
                let data = {
                    id: id
                };
                ApiManager.get('caseTimeline/detail', data).then((res) =>{
                    if (res.message == "SUCCESS") {
                        that.form = res.data;
                        that.visible = true;
                        that.saveBtnTitle = '立即修改'
                    }
                });
            },
                
			handleDeleteMore:function(){
                let ids = []
                this.multipleSelection.forEach((obj) => {
                  ids.push(obj.id);
                });
                let idsStr = ids.join(",")

                let that = this;
                that.$confirm('此操作将永久删除记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let data = {
                        id: idsStr
                    };
                    ApiManager.get('caseTimeline/deleteAll', data).then((res) =>{
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
            caseIDChange: function(val){
                if (val===''){
                    this.form.caseID = null
                }
            },
            importanceChange: function(val){
                if (val===''){
                    this.form.importance = null
                }
            },
            getCaseIDDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('caseTimeline/getCaseIDDatas', data).then((res) =>{
                    that.caseIDDatas = res.data;

                });
            },
            getImportanceDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('caseTimeline/getImportanceDatas', data).then((res) =>{
                    that.importanceDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCaseIDDatas();
            this.getImportanceDatas();
        }
    }
</script>


