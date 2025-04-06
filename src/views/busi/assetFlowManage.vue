<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="所属案件" prop="case_id">
                            <el-select v-model="queryPanel.case_id" placeholder="请选择所属案件" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="资产" prop="asset_id">
                            <el-select v-model="queryPanel.asset_id" placeholder="请选择资产" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in asset_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="流动时间" prop="flow_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.flow_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                </el-form-item>
                <el-form-item label="流动类型" prop="flowType">
                            <el-select v-model="queryPanel.flowType" placeholder="请选择流动类型" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in flowTypeDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="关联资产" prop="related_asset_id">
                            <el-select v-model="queryPanel.related_asset_id" placeholder="请选择关联资产" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in related_asset_idDatas" :key="item.code"></el-option>
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
                        <el-form-item label="资产" prop="asset_id">
                            <el-select v-model="form.asset_id" placeholder="请选择资产" filterable clearable @change="asset_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in asset_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="流动时间" prop="flow_time">
                    <el-date-picker type="date" v-model="form.flow_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="流动类型" prop="flowType">
                            <el-select v-model="form.flowType" placeholder="请选择流动类型" filterable clearable @change="flowTypeChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in flowTypeDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="流动金额" prop="flow_amount">
                            <el-input v-model="form.flow_amount" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="货币类型" prop="currency">
                            <el-select v-model="form.currency" placeholder="请选择货币类型" filterable clearable @change="currencyChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in currencyDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="来源人物" prop="from_person_id">
                            <el-select v-model="form.from_person_id" placeholder="请选择来源人物" filterable clearable @change="from_person_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in from_person_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="目标人物" prop="to_person_id">
                            <el-select v-model="form.to_person_id" placeholder="请选择目标人物" filterable clearable @change="to_person_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in to_person_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="关联资产" prop="related_asset_id">
                            <el-select v-model="form.related_asset_id" placeholder="请选择关联资产" filterable clearable @change="related_asset_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in related_asset_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="流动描述" prop="flow_desc">
                    <div style="width: 100%;height: 200px;">
                        <quill-editor ref="flow_descQuillEditor"  v-model="form.flow_desc"/>
                    </div>
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
                        <el-table-column prop="asset_idName" show-overflow-tooltip label="资产"  width="180">
                        </el-table-column>
                        <el-table-column prop="flow_time" show-overflow-tooltip label="流动时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="flowTypeName" show-overflow-tooltip label="流动类型"  width="180">
                        </el-table-column>
                        <el-table-column prop="flow_amount" show-overflow-tooltip label="流动金额"  width="180">
                        </el-table-column>
                        <el-table-column prop="currencyName" show-overflow-tooltip label="货币类型"  width="180">
                        </el-table-column>
                        <el-table-column prop="from_person_idName" show-overflow-tooltip label="来源人物"  width="180">
                        </el-table-column>
                        <el-table-column prop="to_person_idName" show-overflow-tooltip label="目标人物"  width="180">
                        </el-table-column>
                        <el-table-column prop="related_asset_idName" show-overflow-tooltip label="关联资产"  width="180">
                        </el-table-column>
                        <el-table-column prop="flow_desc" show-overflow-tooltip label="流动描述"  width="180">
                            <template #default="scope">
                                <div v-html="scope.row.flow_desc" ></div>
                            </template>
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
                    asset_id:'',
                    flow_time:[],
                    flowType:'',
                    related_asset_id:'',
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                case_idDatas: [],
                asset_idDatas: [],
                flowTypeDatas: [],
                currencyDatas: [],
                from_person_idDatas: [],
                to_person_idDatas: [],
                related_asset_idDatas: [],
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
                        let url = "assetFlow/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "assetFlow/update";
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
                    asset_id:that.queryPanel.asset_id,
                    flow_timeStart:that.queryPanel.flow_time!=null && that.queryPanel.flow_time.length>0?that.queryPanel.flow_time[0]:'',
                    flow_timeEnd:that.queryPanel.flow_time!=null && that.queryPanel.flow_time.length>0?that.queryPanel.flow_time[1]:'',
                    flowType:that.queryPanel.flowType,
                    related_asset_id:that.queryPanel.related_asset_id,
                };
                ApiManager.post('assetFlow/listByCondition', data).then((res) =>{
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
                    asset_id:that.queryPanel.asset_id,
                    flow_timeStart:that.queryPanel.flow_time!=null && that.queryPanel.flow_time.length>0?that.queryPanel.flow_time[0]:'',
                    flow_timeEnd:that.queryPanel.flow_time!=null && that.queryPanel.flow_time.length>0?that.queryPanel.flow_time[1]:'',
                    flowType:that.queryPanel.flowType,
                    related_asset_id:that.queryPanel.related_asset_id,
                };
                ApiManager.download('assetFlow/exportExcel', data).then((res) =>{
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
                    ApiManager.get('assetFlow/delete', data).then((res) =>{
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
                ApiManager.get('assetFlow/detail', data).then((res) =>{
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
                    ApiManager.get('assetFlow/deleteAll', data).then((res) =>{
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
            asset_idChange: function(val){
                if (val===''){
                    this.form.asset_id = null
                }
            },
            flowTypeChange: function(val){
                if (val===''){
                    this.form.flowType = null
                }
            },
            currencyChange: function(val){
                if (val===''){
                    this.form.currency = null
                }
            },
            from_person_idChange: function(val){
                if (val===''){
                    this.form.from_person_id = null
                }
            },
            to_person_idChange: function(val){
                if (val===''){
                    this.form.to_person_id = null
                }
            },
            related_asset_idChange: function(val){
                if (val===''){
                    this.form.related_asset_id = null
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
                ApiManager.post('assetFlow/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getAsset_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('assetFlow/getAsset_idDatas', data).then((res) =>{
                    that.asset_idDatas = res.data;

                });
            },
            getFlowTypeDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('assetFlow/getFlowTypeDatas', data).then((res) =>{
                    that.flowTypeDatas = res.data;

                });
            },
            getCurrencyDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('assetFlow/getCurrencyDatas', data).then((res) =>{
                    that.currencyDatas = res.data;

                });
            },
            getFrom_person_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('assetFlow/getFrom_person_idDatas', data).then((res) =>{
                    that.from_person_idDatas = res.data;

                });
            },
            getTo_person_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('assetFlow/getTo_person_idDatas', data).then((res) =>{
                    that.to_person_idDatas = res.data;

                });
            },
            getRelated_asset_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('assetFlow/getRelated_asset_idDatas', data).then((res) =>{
                    that.related_asset_idDatas = res.data;

                });
            },
            getSupportingEvidenceDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('assetFlow/getSupportingEvidenceDatas', data).then((res) =>{
                    that.supportingEvidenceDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getAsset_idDatas();
            this.getFlowTypeDatas();
            this.getCurrencyDatas();
            this.getFrom_person_idDatas();
            this.getTo_person_idDatas();
            this.getRelated_asset_idDatas();
            this.getSupportingEvidenceDatas();
        }
    }
</script>


