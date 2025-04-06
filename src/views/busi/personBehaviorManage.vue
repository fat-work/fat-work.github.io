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
                <el-form-item label="行为时间" prop="behavior_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.behavior_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                </el-form-item>
                <el-form-item label="行为类型" prop="behavior_type">
                            <el-select v-model="queryPanel.behavior_type" placeholder="请选择行为类型" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in behavior_typeDatas" :key="item.code"></el-option>
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
                        <el-form-item label="行为时间" prop="behavior_time">
                    <el-date-picker type="date" v-model="form.behavior_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="行为类型" prop="behavior_type">
                            <el-select v-model="form.behavior_type" placeholder="请选择行为类型" filterable clearable @change="behavior_typeChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in behavior_typeDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="行为描述" prop="behavior_desc">
                    <el-input type="textarea" :rows="2" v-model="form.behavior_desc"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="风险等级" prop="riskLevel">
                            <el-select v-model="form.riskLevel" placeholder="请选择风险等级" filterable clearable @change="riskLevelChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in riskLevelDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="支持证据" prop="supportingEvidence">
                            <el-select v-model="formSupportingEvidence" placeholder="请选择支持证据" multiple filterable clearable @change="supportingEvidenceChange">
                                <el-option :label="item.title" :value="item.code+''" v-for="item in supportingEvidenceDatas" :key="item.code"></el-option>
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
                        <el-table-column prop="case_idName" show-overflow-tooltip label="所属案件"  width="180">
                        </el-table-column>
                        <el-table-column prop="person_idName" show-overflow-tooltip label="人物"  width="180">
                        </el-table-column>
                        <el-table-column prop="behavior_time" show-overflow-tooltip label="行为时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="behavior_typeName" show-overflow-tooltip label="行为类型"  width="180">
                        </el-table-column>
                        <el-table-column prop="behavior_desc" show-overflow-tooltip label="行为描述"  width="180">
                        </el-table-column>
                        <el-table-column prop="riskLevelName" show-overflow-tooltip label="风险等级"  width="180">
                        </el-table-column>
                        <el-table-column prop="supportingEvidenceName" show-overflow-tooltip label="支持证据" >
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
                    behavior_time:[],
                    behavior_type:'',
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
                behavior_typeDatas: [],
                riskLevelDatas: [],
                supportingEvidenceDatas: [],
                formSupportingEvidence: [],
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
                        let url = "personBehavior/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "personBehavior/update";
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
                    behavior_timeStart:that.queryPanel.behavior_time!=null && that.queryPanel.behavior_time.length>0?that.queryPanel.behavior_time[0]:'',
                    behavior_timeEnd:that.queryPanel.behavior_time!=null && that.queryPanel.behavior_time.length>0?that.queryPanel.behavior_time[1]:'',
                    behavior_type:that.queryPanel.behavior_type,
                };
                ApiManager.post('personBehavior/listByCondition', data).then((res) =>{
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
                    behavior_timeStart:that.queryPanel.behavior_time!=null && that.queryPanel.behavior_time.length>0?that.queryPanel.behavior_time[0]:'',
                    behavior_timeEnd:that.queryPanel.behavior_time!=null && that.queryPanel.behavior_time.length>0?that.queryPanel.behavior_time[1]:'',
                    behavior_type:that.queryPanel.behavior_type,
                };
                ApiManager.download('personBehavior/exportExcel', data).then((res) =>{
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
                    ApiManager.get('personBehavior/delete', data).then((res) =>{
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
                ApiManager.get('personBehavior/detail', data).then((res) =>{
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
                    ApiManager.get('personBehavior/deleteAll', data).then((res) =>{
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
            behavior_typeChange: function(val){
                if (val===''){
                    this.form.behavior_type = null
                }
            },
            riskLevelChange: function(val){
                if (val===''){
                    this.form.riskLevel = null
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
                ApiManager.post('personBehavior/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getPerson_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('personBehavior/getPerson_idDatas', data).then((res) =>{
                    that.person_idDatas = res.data;

                });
            },
            getBehavior_typeDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('personBehavior/getBehavior_typeDatas', data).then((res) =>{
                    that.behavior_typeDatas = res.data;

                });
            },
            getRiskLevelDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('personBehavior/getRiskLevelDatas', data).then((res) =>{
                    that.riskLevelDatas = res.data;

                });
            },
            getSupportingEvidenceDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('personBehavior/getSupportingEvidenceDatas', data).then((res) =>{
                    that.supportingEvidenceDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getPerson_idDatas();
            this.getBehavior_typeDatas();
            this.getRiskLevelDatas();
            this.getSupportingEvidenceDatas();
        }
    }
</script>


