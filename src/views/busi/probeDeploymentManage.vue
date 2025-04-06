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
                <el-form-item label="部署人员" prop="deploy_user_id">
                            <el-select v-model="queryPanel.deploy_user_id" placeholder="请选择部署人员" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in deploy_user_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="部署时间" prop="deploy_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.deploy_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                </el-form-item>
                <el-form-item label="回收时间" prop="retrieve_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.retrieve_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
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
                        <el-form-item label="部署人员" prop="deploy_user_id">
                            <el-select v-model="form.deploy_user_id" placeholder="请选择部署人员" filterable clearable @change="deploy_user_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in deploy_user_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="部署位置描述" prop="deploy_location">
                    <div style="width: 100%;height: 200px;">
                        <quill-editor ref="deploy_locationQuillEditor"  v-model="form.deploy_location"/>
                    </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="经度" prop="longitude">
                            <el-input v-model="form.longitude" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="纬度" prop="latitude">
                            <el-input v-model="form.latitude" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="部署时间" prop="deploy_time">
                    <el-date-picker type="date" v-model="form.deploy_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="回收时间" prop="retrieve_time">
                    <el-date-picker type="date" v-model="form.retrieve_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="部署状态" prop="deploymentStatus">
                            <el-select v-model="form.deploymentStatus" placeholder="请选择部署状态" filterable clearable @change="deploymentStatusChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in deploymentStatusDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="备注" prop="remark">
                    <el-input type="textarea" :rows="2" v-model="form.remark"></el-input>
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
                        <el-table-column prop="deploy_user_idName" show-overflow-tooltip label="部署人员"  width="180">
                        </el-table-column>
                        <el-table-column prop="deploy_location" show-overflow-tooltip label="部署位置描述"  width="180">
                            <template #default="scope">
                                <div v-html="scope.row.deploy_location" ></div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="longitude" show-overflow-tooltip label="经度"  width="180">
                        </el-table-column>
                        <el-table-column prop="latitude" show-overflow-tooltip label="纬度"  width="180">
                        </el-table-column>
                        <el-table-column prop="deploy_time" show-overflow-tooltip label="部署时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="retrieve_time" show-overflow-tooltip label="回收时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="deploymentStatusName" show-overflow-tooltip label="部署状态"  width="180">
                        </el-table-column>
                        <el-table-column prop="remark" show-overflow-tooltip label="备注" >
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
                    deploy_user_id:'',
                    deploy_time:[],
                    retrieve_time:[],
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
                deploy_user_idDatas: [],
                deploymentStatusDatas: [],
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
                        let url = "probeDeployment/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "probeDeployment/update";
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
                    deploy_user_id:that.queryPanel.deploy_user_id,
                    deploy_timeStart:that.queryPanel.deploy_time!=null && that.queryPanel.deploy_time.length>0?that.queryPanel.deploy_time[0]:'',
                    deploy_timeEnd:that.queryPanel.deploy_time!=null && that.queryPanel.deploy_time.length>0?that.queryPanel.deploy_time[1]:'',
                    retrieve_timeStart:that.queryPanel.retrieve_time!=null && that.queryPanel.retrieve_time.length>0?that.queryPanel.retrieve_time[0]:'',
                    retrieve_timeEnd:that.queryPanel.retrieve_time!=null && that.queryPanel.retrieve_time.length>0?that.queryPanel.retrieve_time[1]:'',
                };
                ApiManager.post('probeDeployment/listByCondition', data).then((res) =>{
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
                    deploy_user_id:that.queryPanel.deploy_user_id,
                    deploy_timeStart:that.queryPanel.deploy_time!=null && that.queryPanel.deploy_time.length>0?that.queryPanel.deploy_time[0]:'',
                    deploy_timeEnd:that.queryPanel.deploy_time!=null && that.queryPanel.deploy_time.length>0?that.queryPanel.deploy_time[1]:'',
                    retrieve_timeStart:that.queryPanel.retrieve_time!=null && that.queryPanel.retrieve_time.length>0?that.queryPanel.retrieve_time[0]:'',
                    retrieve_timeEnd:that.queryPanel.retrieve_time!=null && that.queryPanel.retrieve_time.length>0?that.queryPanel.retrieve_time[1]:'',
                };
                ApiManager.download('probeDeployment/exportExcel', data).then((res) =>{
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
                    ApiManager.get('probeDeployment/delete', data).then((res) =>{
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
                ApiManager.get('probeDeployment/detail', data).then((res) =>{
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
                    ApiManager.get('probeDeployment/deleteAll', data).then((res) =>{
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
            deploy_user_idChange: function(val){
                if (val===''){
                    this.form.deploy_user_id = null
                }
            },
            deploymentStatusChange: function(val){
                if (val===''){
                    this.form.deploymentStatus = null
                }
            },
            getCase_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('probeDeployment/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getProbe_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('probeDeployment/getProbe_idDatas', data).then((res) =>{
                    that.probe_idDatas = res.data;

                });
            },
            getDeploy_user_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('probeDeployment/getDeploy_user_idDatas', data).then((res) =>{
                    that.deploy_user_idDatas = res.data;

                });
            },
            getDeploymentStatusDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('probeDeployment/getDeploymentStatusDatas', data).then((res) =>{
                    that.deploymentStatusDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getProbe_idDatas();
            this.getDeploy_user_idDatas();
            this.getDeploymentStatusDatas();
        }
    }
</script>


