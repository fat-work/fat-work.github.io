<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="所属案件" prop="case_id">
                            <el-select v-model="queryPanel.case_id" placeholder="请选择所属案件" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="探针" prop="probe_id">
                            <el-select v-model="queryPanel.probe_id" placeholder="请选择探针" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in probe_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="统计时间" prop="stat_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.stat_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
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
                        <el-form-item label="所属案件" prop="case_id">
                            <el-select v-model="form.case_id" placeholder="请选择所属案件" filterable clearable @change="case_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="探针" prop="probe_id">
                            <el-select v-model="form.probe_id" placeholder="请选择探针" filterable clearable @change="probe_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in probe_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="统计时间" prop="stat_time">
                    <el-date-picker type="date" v-model="form.stat_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="采集数量" prop="collect_count">
                            <el-input v-model="form.collect_count" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="采集大小(字节)" prop="collect_size">
                            <el-input v-model="form.collect_size" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="采集速度(KB/s)" prop="collect_speed">
                            <el-input v-model="form.collect_speed" clearable></el-input>
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
                        <el-table-column prop="case_idName" show-overflow-tooltip label="所属案件"  width="180">
                        </el-table-column>
                        <el-table-column prop="probe_idName" show-overflow-tooltip label="探针"  width="180">
                        </el-table-column>
                        <el-table-column prop="stat_time" show-overflow-tooltip label="统计时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="collect_count" show-overflow-tooltip label="采集数量"  width="180">
                        </el-table-column>
                        <el-table-column prop="collect_size" show-overflow-tooltip label="采集大小(字节)"  width="180">
                        </el-table-column>
                        <el-table-column prop="collect_speed" show-overflow-tooltip label="采集速度(KB/s)" >
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
                    case_id:'',
                    probe_id:'',
                    stat_time:[],
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                case_idDatas: [],
                probe_idDatas: [],
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
                        let url = "evidenceCollectionStats/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "evidenceCollectionStats/update";
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
                    case_id:that.queryPanel.case_id,
                    probe_id:that.queryPanel.probe_id,
                    stat_timeStart:that.queryPanel.stat_time!=null && that.queryPanel.stat_time.length>0?that.queryPanel.stat_time[0]:'',
                    stat_timeEnd:that.queryPanel.stat_time!=null && that.queryPanel.stat_time.length>0?that.queryPanel.stat_time[1]:'',
                };
                ApiManager.post('evidenceCollectionStats/listByCondition', data).then((res) =>{
                    that.tableData = res.data.list;
                    that.rowTotal = res.data.total;
                })


            },
                
			handleExport: function() {
                let that = this;
                let data = {
                    page:this.currentPage,
                    size:this.pageSize,
                    case_id:that.queryPanel.case_id,
                    probe_id:that.queryPanel.probe_id,
                    stat_timeStart:that.queryPanel.stat_time!=null && that.queryPanel.stat_time.length>0?that.queryPanel.stat_time[0]:'',
                    stat_timeEnd:that.queryPanel.stat_time!=null && that.queryPanel.stat_time.length>0?that.queryPanel.stat_time[1]:'',
                };
                ApiManager.download('evidenceCollectionStats/exportExcel', data).then((res) =>{
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
                    ApiManager.get('evidenceCollectionStats/delete', data).then((res) =>{
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
                ApiManager.get('evidenceCollectionStats/detail', data).then((res) =>{
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
                    ApiManager.get('evidenceCollectionStats/deleteAll', data).then((res) =>{
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
            case_idChange: function(val){
                if (val===''){
                    this.form.case_id = null
                }
            },
            probe_idChange: function(val){
                if (val===''){
                    this.form.probe_id = null
                }
            },
            getCase_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceCollectionStats/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getProbe_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceCollectionStats/getProbe_idDatas', data).then((res) =>{
                    that.probe_idDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getProbe_idDatas();
        }
    }
</script>


