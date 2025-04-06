<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="案件编号" prop="case_no">
                            <el-input v-model="queryPanel.case_no" clearable></el-input>
                </el-form-item>
                <el-form-item label="案件名称" prop="case_name">
                            <el-input v-model="queryPanel.case_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="案件状态" prop="caseStatus">
                            <el-select v-model="queryPanel.caseStatus" placeholder="请选择案件状态" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in caseStatusDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="案件类型" prop="case_type">
                            <el-select v-model="queryPanel.case_type" placeholder="请选择案件类型" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in case_typeDatas" :key="item.code"></el-option>
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
                        <el-form-item label="案件编号" prop="case_no">
                            <el-input v-model="form.case_no" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="案件名称" prop="case_name">
                            <el-input v-model="form.case_name" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="案件描述" prop="case_desc">
                    <div style="width: 100%;height: 200px;">
                        <quill-editor ref="case_descQuillEditor"  v-model="form.case_desc"/>
                    </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="案件地点" prop="location">
                            <el-input v-model="form.location" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="案件开始时间" prop="start_time">
                    <el-date-picker type="date" v-model="form.start_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="案件结束时间" prop="end_time">
                    <el-date-picker type="date" v-model="form.end_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="案件状态" prop="caseStatus">
				<el-radio-group v-model="form.caseStatus">
					<el-radio :label="item.code" v-for="item in caseStatusDatas" :key="item.code" @change="caseStatusChange">{{item.title}}</el-radio>
				</el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="负责人" prop="create_user">
                            <el-select v-model="form.create_user" placeholder="请选择负责人" filterable clearable @change="create_userChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in create_userDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="案件类型" prop="case_type">
                            <el-select v-model="form.case_type" placeholder="请选择案件类型" filterable clearable @change="case_typeChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in case_typeDatas" :key="item.code"></el-option>
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
                        <el-table-column prop="case_no" show-overflow-tooltip label="案件编号"  width="180">
                        </el-table-column>
                        <el-table-column prop="case_name" show-overflow-tooltip label="案件名称"  width="180">
                        </el-table-column>
                        <el-table-column prop="case_desc" show-overflow-tooltip label="案件描述"  width="180">
                            <template #default="scope">
                                <div v-html="scope.row.case_desc" ></div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="location" show-overflow-tooltip label="案件地点"  width="180">
                        </el-table-column>
                        <el-table-column prop="start_time" show-overflow-tooltip label="案件开始时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="end_time" show-overflow-tooltip label="案件结束时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="caseStatusName" show-overflow-tooltip label="案件状态"  width="180">
                        </el-table-column>
                        <el-table-column prop="create_userName" show-overflow-tooltip label="负责人"  width="180">
                        </el-table-column>
                        <el-table-column prop="case_typeName" show-overflow-tooltip label="案件类型" >
                        </el-table-column>

                <el-table-column fixed="right" label="操作" width="240">
                    <template #default="scope">
                        <el-button class="updateBtnCls" size="small" type="success" @click="handleDetail(scope.$index, scope.row)" link>案件详情</el-button>
                        <el-button class="updateBtnCls" size="small" type="primary" @click="handleIndex(scope.$index, scope.row)" link>案件分析</el-button>
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

    <el-drawer
        v-model="drawer"
        title="案件详情"
        :direction="direction"
        size="80%"
    >
    <CaseDetail></CaseDetail>
  </el-drawer>

  <el-drawer
        v-model="drawer1"
        title="分析"
        :direction="direction"
        size="80%"
    >
    <CaseIndex></CaseIndex>
  </el-drawer>

</template>

<script>
    import { ApiManager, uploadAction } from '@/api/index'
    import  CaseDetail  from '@/views/case/detail.vue'
    import  CaseIndex from '@/views/case/index.vue'

    export default {
        components:{
            CaseDetail,CaseIndex
        },
        data: function() {
            return {
                uploadAction: uploadAction,
                visible: false,
                queryPanel: {
                    case_no:'',
                    case_name:'',
                    caseStatus:'',
                    case_type:'',
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                caseStatusDatas: [],
                create_userDatas: [],
                case_typeDatas: [],
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
                },
                direction:'rtl',
                drawer: false,
                drawer1:false
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
                        let url = "caseInfo/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "caseInfo/update";
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
                    case_no:that.queryPanel.case_no,
                    case_name:that.queryPanel.case_name,
                    caseStatus:that.queryPanel.caseStatus,
                    case_type:that.queryPanel.case_type,
                };
                ApiManager.post('caseInfo/listByCondition', data).then((res) =>{
                    that.tableData = res.data.list;
                    that.rowTotal = res.data.total;
                })


            },
                
			handleExport: function() {
                let that = this;
                let data = {
                    page:this.currentPage,
                    size:this.pageSize,
                    case_no:that.queryPanel.case_no,
                    case_name:that.queryPanel.case_name,
                    caseStatus:that.queryPanel.caseStatus,
                    case_type:that.queryPanel.case_type,
                };
                ApiManager.download('caseInfo/exportExcel', data).then((res) =>{
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
                    ApiManager.get('caseInfo/delete', data).then((res) =>{
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
                ApiManager.get('caseInfo/detail', data).then((res) =>{
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
                    ApiManager.get('caseInfo/deleteAll', data).then((res) =>{
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
            caseStatusChange: function(val){
                if (val===''){
                    this.form.caseStatus = null
                }
            },
            create_userChange: function(val){
                if (val===''){
                    this.form.create_user = null
                }
            },
            case_typeChange: function(val){
                if (val===''){
                    this.form.case_type = null
                }
            },
            getCaseStatusDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('caseInfo/getCaseStatusDatas', data).then((res) =>{
                    that.caseStatusDatas = res.data;

                });
            },
            getCreate_userDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('caseInfo/getCreate_userDatas', data).then((res) =>{
                    that.create_userDatas = res.data;

                });
            },
            getCase_typeDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('caseInfo/getCase_typeDatas', data).then((res) =>{
                    that.case_typeDatas = res.data;

                });
            },
            handleDetail: function(index, row) {
                this.drawer = true
            },
            handleIndex: function(index, row) {
                this.drawer1 = true
            },
        },
        created() {
            this.handleQuery();
            this.getCaseStatusDatas();
            this.getCreate_userDatas();
            this.getCase_typeDatas();
        }
    }
</script>


