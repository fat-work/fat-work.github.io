<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="案件ID" prop="case_id">
                            <el-select v-model="queryPanel.case_id" placeholder="请选择案件ID" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="预警标题" prop="warning_title">
                            <el-input v-model="queryPanel.warning_title" clearable></el-input>
                </el-form-item>
                <el-form-item label="预警内容" prop="warning_content">
                            <el-input v-model="queryPanel.warning_content" clearable></el-input>
                </el-form-item>
                <el-form-item label="预警级别" prop="warningLevel">
                            <el-select v-model="queryPanel.warningLevel" placeholder="请选择预警级别" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in warningLevelDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="预警类型" prop="warning_type">
                            <el-select v-model="queryPanel.warning_type" placeholder="请选择预警类型" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in warning_typeDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="预警时间" prop="warning_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.warning_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                </el-form-item>
                <el-form-item label="处理人" prop="handle_user_id">
                            <el-select v-model="queryPanel.handle_user_id" placeholder="请选择处理人" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in handle_user_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="处理时间" prop="handle_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.handle_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
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
                        <el-form-item label="案件ID" prop="case_id">
                            <el-select v-model="form.case_id" placeholder="请选择案件ID" filterable clearable @change="case_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="预警标题" prop="warning_title">
                            <el-input v-model="form.warning_title" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="预警内容" prop="warning_content">
                    <div style="width: 100%;height: 200px;">
                        <quill-editor ref="warning_contentQuillEditor"  v-model="form.warning_content"/>
                    </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="预警级别" prop="warningLevel">
                            <el-select v-model="form.warningLevel" placeholder="请选择预警级别" filterable clearable @change="warningLevelChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in warningLevelDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="预警类型" prop="warning_type">
                            <el-select v-model="form.warning_type" placeholder="请选择预警类型" filterable clearable @change="warning_typeChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in warning_typeDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="预警时间" prop="warning_time">
                    <el-date-picker type="date" v-model="form.warning_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="关联人物" prop="relatedPersons">
                            <el-select v-model="formRelatedPersons" placeholder="请选择关联人物" multiple filterable clearable @change="relatedPersonsChange">
                                <el-option :label="item.title" :value="item.code+''" v-for="item in relatedPersonsDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="关联证据" prop="relatedEvidence">
                            <el-select v-model="formRelatedEvidence" placeholder="请选择关联证据" multiple filterable clearable @change="relatedEvidenceChange">
                                <el-option :label="item.title" :value="item.code+''" v-for="item in relatedEvidenceDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="状态" prop="state">
                            <el-select v-model="form.state" placeholder="请选择状态" filterable clearable @change="stateChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in stateDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="处理人" prop="handle_user_id">
                            <el-select v-model="form.handle_user_id" placeholder="请选择处理人" filterable clearable @change="handle_user_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in handle_user_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="处理时间" prop="handle_time">
                    <el-date-picker type="date" v-model="form.handle_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="处理结果" prop="handle_result">
                    <el-input type="textarea" :rows="2" v-model="form.handle_result"></el-input>
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
                        <el-table-column prop="case_idName" show-overflow-tooltip label="案件ID"  width="180">
                        </el-table-column>
                        <el-table-column prop="warning_title" show-overflow-tooltip label="预警标题"  width="180">
                        </el-table-column>
                        <el-table-column prop="warning_content" show-overflow-tooltip label="预警内容"  width="180">
                            <template #default="scope">
                                <div v-html="scope.row.warning_content" ></div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="warningLevelName" show-overflow-tooltip label="预警级别"  width="180">
                        </el-table-column>
                        <el-table-column prop="warning_typeName" show-overflow-tooltip label="预警类型"  width="180">
                        </el-table-column>
                        <el-table-column prop="warning_time" show-overflow-tooltip label="预警时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="relatedPersonsName" show-overflow-tooltip label="关联人物"  width="180">
                        </el-table-column>
                        <el-table-column prop="relatedEvidenceName" show-overflow-tooltip label="关联证据"  width="180">
                        </el-table-column>
                        <el-table-column prop="stateName" show-overflow-tooltip label="状态"  width="180">
                        </el-table-column>
                        <el-table-column prop="handle_user_idName" show-overflow-tooltip label="处理人"  width="180">
                        </el-table-column>
                        <el-table-column prop="handle_time" show-overflow-tooltip label="处理时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="handle_result" show-overflow-tooltip label="处理结果" >
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
                    warning_title:'',
                    warning_content:'',
                    warningLevel:'',
                    warning_type:'',
                    warning_time:[],
                    handle_user_id:'',
                    handle_time:[],
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                case_idDatas: [],
                warningLevelDatas: [],
                warning_typeDatas: [],
                relatedPersonsDatas: [],
                formRelatedPersons: [],
                relatedEvidenceDatas: [],
                formRelatedEvidence: [],
                stateDatas: [],
                handle_user_idDatas: [],
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
                that.formRelatedPersons=[]
                that.formRelatedEvidence=[]
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
                        let url = "warningInfo/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "warningInfo/update";
                            message = '修改成功';
                        }
                        that.form.relatedPersons=that.formRelatedPersons.join(',');
                        that.form.relatedEvidence=that.formRelatedEvidence.join(',');
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
                    warning_title:that.queryPanel.warning_title,
                    warning_content:that.queryPanel.warning_content,
                    warningLevel:that.queryPanel.warningLevel,
                    warning_type:that.queryPanel.warning_type,
                    warning_timeStart:that.queryPanel.warning_time!=null && that.queryPanel.warning_time.length>0?that.queryPanel.warning_time[0]:'',
                    warning_timeEnd:that.queryPanel.warning_time!=null && that.queryPanel.warning_time.length>0?that.queryPanel.warning_time[1]:'',
                    handle_user_id:that.queryPanel.handle_user_id,
                    handle_timeStart:that.queryPanel.handle_time!=null && that.queryPanel.handle_time.length>0?that.queryPanel.handle_time[0]:'',
                    handle_timeEnd:that.queryPanel.handle_time!=null && that.queryPanel.handle_time.length>0?that.queryPanel.handle_time[1]:'',
                };
                ApiManager.post('warningInfo/listByCondition', data).then((res) =>{
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
                    warning_title:that.queryPanel.warning_title,
                    warning_content:that.queryPanel.warning_content,
                    warningLevel:that.queryPanel.warningLevel,
                    warning_type:that.queryPanel.warning_type,
                    warning_timeStart:that.queryPanel.warning_time!=null && that.queryPanel.warning_time.length>0?that.queryPanel.warning_time[0]:'',
                    warning_timeEnd:that.queryPanel.warning_time!=null && that.queryPanel.warning_time.length>0?that.queryPanel.warning_time[1]:'',
                    handle_user_id:that.queryPanel.handle_user_id,
                    handle_timeStart:that.queryPanel.handle_time!=null && that.queryPanel.handle_time.length>0?that.queryPanel.handle_time[0]:'',
                    handle_timeEnd:that.queryPanel.handle_time!=null && that.queryPanel.handle_time.length>0?that.queryPanel.handle_time[1]:'',
                };
                ApiManager.download('warningInfo/exportExcel', data).then((res) =>{
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
                    ApiManager.get('warningInfo/delete', data).then((res) =>{
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
                ApiManager.get('warningInfo/detail', data).then((res) =>{
                    if (res.message == "SUCCESS") {
                        that.form = res.data;
                        that.formRelatedPersons=that.form.relatedPersons.split(',').map(function(data){return data})
                        that.formRelatedEvidence=that.form.relatedEvidence.split(',').map(function(data){return data})
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
                    ApiManager.get('warningInfo/deleteAll', data).then((res) =>{
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
            warningLevelChange: function(val){
                if (val===''){
                    this.form.warningLevel = null
                }
            },
            warning_typeChange: function(val){
                if (val===''){
                    this.form.warning_type = null
                }
            },
            relatedPersonsChange: function(val){
                if (val===''){
                    this.form.relatedPersons = null
                }
            },
            relatedEvidenceChange: function(val){
                if (val===''){
                    this.form.relatedEvidence = null
                }
            },
            stateChange: function(val){
                if (val===''){
                    this.form.state = null
                }
            },
            handle_user_idChange: function(val){
                if (val===''){
                    this.form.handle_user_id = null
                }
            },
            getCase_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('warningInfo/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getWarningLevelDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('warningInfo/getWarningLevelDatas', data).then((res) =>{
                    that.warningLevelDatas = res.data;

                });
            },
            getWarning_typeDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('warningInfo/getWarning_typeDatas', data).then((res) =>{
                    that.warning_typeDatas = res.data;

                });
            },
            getRelatedPersonsDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('warningInfo/getRelatedPersonsDatas', data).then((res) =>{
                    that.relatedPersonsDatas = res.data;

                });
            },
            getRelatedEvidenceDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('warningInfo/getRelatedEvidenceDatas', data).then((res) =>{
                    that.relatedEvidenceDatas = res.data;

                });
            },
            getStateDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('warningInfo/getStateDatas', data).then((res) =>{
                    that.stateDatas = res.data;

                });
            },
            getHandle_user_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('warningInfo/getHandle_user_idDatas', data).then((res) =>{
                    that.handle_user_idDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getWarningLevelDatas();
            this.getWarning_typeDatas();
            this.getRelatedPersonsDatas();
            this.getRelatedEvidenceDatas();
            this.getStateDatas();
            this.getHandle_user_idDatas();
        }
    }
</script>


