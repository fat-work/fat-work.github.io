<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="所属案件" prop="case_id">
                            <el-select v-model="queryPanel.case_id" placeholder="请选择所属案件" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="证据" prop="evidence_id">
                            <el-select v-model="queryPanel.evidence_id" placeholder="请选择证据" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in evidence_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="关联证据" prop="related_evidence_id">
                            <el-select v-model="queryPanel.related_evidence_id" placeholder="请选择关联证据" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in related_evidence_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="关系类型" prop="relation_type">
                            <el-select v-model="queryPanel.relation_type" placeholder="请选择关系类型" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in relation_typeDatas" :key="item.code"></el-option>
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
                        <el-form-item label="证据" prop="evidence_id">
                            <el-select v-model="form.evidence_id" placeholder="请选择证据" filterable clearable @change="evidence_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in evidence_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="关联证据" prop="related_evidence_id">
                            <el-select v-model="form.related_evidence_id" placeholder="请选择关联证据" filterable clearable @change="related_evidence_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in related_evidence_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="关系类型" prop="relation_type">
                            <el-select v-model="form.relation_type" placeholder="请选择关系类型" filterable clearable @change="relation_typeChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in relation_typeDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="关系描述" prop="relation_desc">
                            <el-input v-model="form.relation_desc" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="关联置信度(%)" prop="confidence">
                            <el-input v-model="form.confidence" clearable></el-input>
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
                        <el-table-column prop="evidence_idName" show-overflow-tooltip label="证据"  width="180">
                        </el-table-column>
                        <el-table-column prop="related_evidence_idName" show-overflow-tooltip label="关联证据"  width="180">
                        </el-table-column>
                        <el-table-column prop="relation_typeName" show-overflow-tooltip label="关系类型"  width="180">
                        </el-table-column>
                        <el-table-column prop="relation_desc" show-overflow-tooltip label="关系描述"  width="180">
                        </el-table-column>
                        <el-table-column prop="confidence" show-overflow-tooltip label="关联置信度(%)" >
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
                    evidence_id:'',
                    related_evidence_id:'',
                    relation_type:'',
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                case_idDatas: [],
                evidence_idDatas: [],
                related_evidence_idDatas: [],
                relation_typeDatas: [],
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
                        let url = "evidenceRelation/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "evidenceRelation/update";
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
                    evidence_id:that.queryPanel.evidence_id,
                    related_evidence_id:that.queryPanel.related_evidence_id,
                    relation_type:that.queryPanel.relation_type,
                };
                ApiManager.post('evidenceRelation/listByCondition', data).then((res) =>{
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
                    evidence_id:that.queryPanel.evidence_id,
                    related_evidence_id:that.queryPanel.related_evidence_id,
                    relation_type:that.queryPanel.relation_type,
                };
                ApiManager.download('evidenceRelation/exportExcel', data).then((res) =>{
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
                    ApiManager.get('evidenceRelation/delete', data).then((res) =>{
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
                ApiManager.get('evidenceRelation/detail', data).then((res) =>{
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
                    ApiManager.get('evidenceRelation/deleteAll', data).then((res) =>{
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
            evidence_idChange: function(val){
                if (val===''){
                    this.form.evidence_id = null
                }
            },
            related_evidence_idChange: function(val){
                if (val===''){
                    this.form.related_evidence_id = null
                }
            },
            relation_typeChange: function(val){
                if (val===''){
                    this.form.relation_type = null
                }
            },
            getCase_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceRelation/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getEvidence_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceRelation/getEvidence_idDatas', data).then((res) =>{
                    that.evidence_idDatas = res.data;

                });
            },
            getRelated_evidence_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceRelation/getRelated_evidence_idDatas', data).then((res) =>{
                    that.related_evidence_idDatas = res.data;

                });
            },
            getRelation_typeDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceRelation/getRelation_typeDatas', data).then((res) =>{
                    that.relation_typeDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getEvidence_idDatas();
            this.getRelated_evidence_idDatas();
            this.getRelation_typeDatas();
        }
    }
</script>


