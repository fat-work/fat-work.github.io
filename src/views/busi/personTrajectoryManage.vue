<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="所属案件" prop="case_id">
                            <el-select v-model="queryPanel.case_id" placeholder="请选择所属案件" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="人物" prop="person_id">
                            <el-select v-model="queryPanel.person_id" placeholder="请选择人物" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in person_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="轨迹时间" prop="trajectory_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.trajectory_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
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
                        <el-form-item label="人物" prop="person_id">
                            <el-select v-model="form.person_id" placeholder="请选择人物" filterable clearable @change="person_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in person_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="位置描述" prop="location">
                    <div style="width: 100%;height: 200px;">
                        <quill-editor ref="locationQuillEditor"  v-model="form.location"/>
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
                        <el-form-item label="轨迹时间" prop="trajectory_time">
                    <el-date-picker type="date" v-model="form.trajectory_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="支持证据" prop="supportingEvidence">
                            <el-select v-model="form.SupportingEvidence" placeholder="请选择支持证据" multiple filterable clearable @change="supportingEvidenceChange">
                                <el-option :label="item.title" :value="item.code+''" v-for="item in supportingEvidenceDatas" :key="item.code"></el-option>
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
                        <el-table-column prop="person_idName" show-overflow-tooltip label="人物"  width="180">
                        </el-table-column>
                        <el-table-column prop="location" show-overflow-tooltip label="位置描述"  width="180">
                            <template #default="scope">
                                <div v-html="scope.row.location" ></div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="longitude" show-overflow-tooltip label="经度"  width="180">
                        </el-table-column>
                        <el-table-column prop="latitude" show-overflow-tooltip label="纬度"  width="180">
                        </el-table-column>
                        <el-table-column prop="trajectory_time" show-overflow-tooltip label="轨迹时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="supportingEvidenceName" show-overflow-tooltip label="支持证据"  width="180">
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
                    person_id:'',
                    trajectory_time:[],
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                case_idDatas: [],
                person_idDatas: [],
                supportingEvidenceDatas: [],
                formSupportingEvidence: [],
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
                that.formSupportingEvidence=[]
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
                        let url = "personTrajectory/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "personTrajectory/update";
                            message = '修改成功';
                        }
                        that.form.supportingEvidence=that.formSupportingEvidence.join(',');
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
                    person_id:that.queryPanel.person_id,
                    trajectory_timeStart:that.queryPanel.trajectory_time!=null && that.queryPanel.trajectory_time.length>0?that.queryPanel.trajectory_time[0]:'',
                    trajectory_timeEnd:that.queryPanel.trajectory_time!=null && that.queryPanel.trajectory_time.length>0?that.queryPanel.trajectory_time[1]:'',
                };
                ApiManager.post('personTrajectory/listByCondition', data).then((res) =>{
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
                    person_id:that.queryPanel.person_id,
                    trajectory_timeStart:that.queryPanel.trajectory_time!=null && that.queryPanel.trajectory_time.length>0?that.queryPanel.trajectory_time[0]:'',
                    trajectory_timeEnd:that.queryPanel.trajectory_time!=null && that.queryPanel.trajectory_time.length>0?that.queryPanel.trajectory_time[1]:'',
                };
                ApiManager.download('personTrajectory/exportExcel', data).then((res) =>{
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
                    ApiManager.get('personTrajectory/delete', data).then((res) =>{
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
                ApiManager.get('personTrajectory/detail', data).then((res) =>{
                    if (res.message == "SUCCESS") {
                        that.form = res.data;
                        that.formSupportingEvidence=that.form.supportingEvidence.split(',').map(function(data){return data})
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
                    ApiManager.get('personTrajectory/deleteAll', data).then((res) =>{
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
            person_idChange: function(val){
                if (val===''){
                    this.form.person_id = null
                }
            },
            supportingEvidenceChange: function(val){
                if (val===''){
                    this.form.supportingEvidence = null
                }
            },
            getCase_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('personTrajectory/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getPerson_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('personTrajectory/getPerson_idDatas', data).then((res) =>{
                    that.person_idDatas = res.data;

                });
            },
            getSupportingEvidenceDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('personTrajectory/getSupportingEvidenceDatas', data).then((res) =>{
                    that.supportingEvidenceDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getPerson_idDatas();
            this.getSupportingEvidenceDatas();
        }
    }
</script>


