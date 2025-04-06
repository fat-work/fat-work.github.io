<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="所属案件" prop="case_id">
                            <el-select v-model="queryPanel.case_id" placeholder="请选择所属案件" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="报告编号" prop="report_no">
                            <el-input v-model="queryPanel.report_no" clearable></el-input>
                </el-form-item>
                <el-form-item label="报告标题" prop="report_title">
                            <el-input v-model="queryPanel.report_title" clearable></el-input>
                </el-form-item>
                <el-form-item label="报告类型" prop="reportType">
                            <el-select v-model="queryPanel.reportType" placeholder="请选择报告类型" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in reportTypeDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="生成时间" prop="generate_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.generate_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                </el-form-item>
                <el-form-item label="生成人" prop="generate_user_id">
                            <el-select v-model="queryPanel.generate_user_id" placeholder="请选择生成人" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in generate_user_idDatas" :key="item.code"></el-option>
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
                        <el-form-item label="报告编号" prop="report_no">
                            <el-input v-model="form.report_no" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="报告标题" prop="report_title">
                            <el-input v-model="form.report_title" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="报告类型" prop="reportType">
                            <el-select v-model="form.reportType" placeholder="请选择报告类型" filterable clearable @change="reportTypeChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in reportTypeDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="报告内容" prop="report_content">
                    <div style="width: 100%;height: 200px;">
                        <quill-editor ref="report_contentQuillEditor"  v-model="form.report_content"/>
                    </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="报告文件" prop="report_file_path">
                <el-upload
                        class="avatar-uploader"
                        :action="uploadAction"
                        :on-success="handleReport_file_pathSuccess"
                        :on-remove="handleReport_file_pathRemove"
                        :on-preview="handlePreview"
                        :file-list="report_file_pathFileList"
                        list-type="picture">
                    <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="生成时间" prop="generate_time">
                    <el-date-picker type="date" v-model="form.generate_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="生成人" prop="generate_user_id">
                            <el-select v-model="form.generate_user_id" placeholder="请选择生成人" filterable clearable @change="generate_user_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in generate_user_idDatas" :key="item.code"></el-option>
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
                        <el-table-column prop="report_no" show-overflow-tooltip label="报告编号"  width="180">
                        </el-table-column>
                        <el-table-column prop="report_title" show-overflow-tooltip label="报告标题"  width="180">
                        </el-table-column>
                        <el-table-column prop="reportTypeName" show-overflow-tooltip label="报告类型"  width="180">
                        </el-table-column>
                        <el-table-column prop="report_content" show-overflow-tooltip label="报告内容"  width="180">
                            <template #default="scope">
                                <div v-html="scope.row.report_content" ></div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="report_file_path" show-overflow-tooltip label="报告文件"  width="180">
                        </el-table-column>
                        <el-table-column prop="generate_time" show-overflow-tooltip label="生成时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="generate_user_idName" show-overflow-tooltip label="生成人"  width="180">
                        </el-table-column>
                        <el-table-column prop="relatedEvidenceName" show-overflow-tooltip label="关联证据" >
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
                    report_no:'',
                    report_title:'',
                    reportType:'',
                    generate_time:[],
                    generate_user_id:'',
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                case_idDatas: [],
                reportTypeDatas: [],
                report_file_pathFileList: [],
                generate_user_idDatas: [],
                relatedEvidenceDatas: [],
                formRelatedEvidence: [],
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
                that.report_file_pathFileList=[]
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
                        let url = "reportInfo/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "reportInfo/update";
                            message = '修改成功';
                        }
                        that.form.report_file_path=JSON.stringify(that.report_file_pathFileList)
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
                    report_no:that.queryPanel.report_no,
                    report_title:that.queryPanel.report_title,
                    reportType:that.queryPanel.reportType,
                    generate_timeStart:that.queryPanel.generate_time!=null && that.queryPanel.generate_time.length>0?that.queryPanel.generate_time[0]:'',
                    generate_timeEnd:that.queryPanel.generate_time!=null && that.queryPanel.generate_time.length>0?that.queryPanel.generate_time[1]:'',
                    generate_user_id:that.queryPanel.generate_user_id,
                };
                ApiManager.post('reportInfo/listByCondition', data).then((res) =>{
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
                    report_no:that.queryPanel.report_no,
                    report_title:that.queryPanel.report_title,
                    reportType:that.queryPanel.reportType,
                    generate_timeStart:that.queryPanel.generate_time!=null && that.queryPanel.generate_time.length>0?that.queryPanel.generate_time[0]:'',
                    generate_timeEnd:that.queryPanel.generate_time!=null && that.queryPanel.generate_time.length>0?that.queryPanel.generate_time[1]:'',
                    generate_user_id:that.queryPanel.generate_user_id,
                };
                ApiManager.download('reportInfo/exportExcel', data).then((res) =>{
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
                    ApiManager.get('reportInfo/delete', data).then((res) =>{
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
                ApiManager.get('reportInfo/detail', data).then((res) =>{
                    if (res.message == "SUCCESS") {
                        that.form = res.data;
                        that.report_file_pathFileList = JSON.parse(that.form.report_file_path)
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
                    ApiManager.get('reportInfo/deleteAll', data).then((res) =>{
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
            reportTypeChange: function(val){
                if (val===''){
                    this.form.reportType = null
                }
            },
            handleReport_file_pathSuccess(res, file) {
                this.report_file_pathFileList.push(res.data)
            },
            handleReport_file_pathRemove(file,fileList) {
                this.report_file_pathFileList = fileList
            },
            generate_user_idChange: function(val){
                if (val===''){
                    this.form.generate_user_id = null
                }
            },
            relatedEvidenceChange: function(val){
                if (val===''){
                    this.form.relatedEvidence = null
                }
            },
            handlePreview(file) {
                var aEle = document.createElement("a");// 创建a标签
                aEle.download = file.name;// 设置下载文件的文件名
                aEle.href = file.url;// 后台返回的下载地址
                aEle.click();// 设置点击事件
            },
            getCase_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('reportInfo/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getReportTypeDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('reportInfo/getReportTypeDatas', data).then((res) =>{
                    that.reportTypeDatas = res.data;

                });
            },
            getGenerate_user_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('reportInfo/getGenerate_user_idDatas', data).then((res) =>{
                    that.generate_user_idDatas = res.data;

                });
            },
            getRelatedEvidenceDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('reportInfo/getRelatedEvidenceDatas', data).then((res) =>{
                    that.relatedEvidenceDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getReportTypeDatas();
            this.getGenerate_user_idDatas();
            this.getRelatedEvidenceDatas();
        }
    }
</script>


