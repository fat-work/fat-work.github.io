<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="所属案件" prop="case_id">
                            <el-select v-model="queryPanel.case_id" placeholder="请选择所属案件" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="采集探针" prop="probe_id">
                            <el-select v-model="queryPanel.probe_id" placeholder="请选择采集探针" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in probe_idDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="证据名称" prop="evidence_name">
                            <el-input v-model="queryPanel.evidence_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="证据类型" prop="evidenceType">
                            <el-select v-model="queryPanel.evidenceType" placeholder="请选择证据类型" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in evidenceTypeDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="设备类型" prop="deviceType">
                            <el-select v-model="queryPanel.deviceType" placeholder="请选择设备类型" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in deviceTypeDatas" :key="item.code"></el-option>
                            </el-select>
                </el-form-item>
                <el-form-item label="设备信息" prop="device_info">
                            <el-input v-model="queryPanel.device_info" clearable></el-input>
                </el-form-item>
                <el-form-item label="采集时间" prop="collect_time">
                    <el-date-picker type="daterange" start-placeholder="开始日期" end-placeholder="结束日期"  v-model="queryPanel.collect_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
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
                        <el-form-item label="所属案件" prop="case_id">
                            <el-select v-model="form.case_id" placeholder="请选择所属案件" filterable clearable @change="case_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in case_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="采集探针" prop="probe_id">
                            <el-select v-model="form.probe_id" placeholder="请选择采集探针" filterable clearable @change="probe_idChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in probe_idDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="证据编号" prop="evidence_no">
                            <el-input v-model="form.evidence_no" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="证据名称" prop="evidence_name">
                            <el-input v-model="form.evidence_name" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="证据类型" prop="evidenceType">
                            <el-select v-model="form.evidenceType" placeholder="请选择证据类型" filterable clearable @change="evidenceTypeChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in evidenceTypeDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="设备类型" prop="deviceType">
                            <el-select v-model="form.deviceType" placeholder="请选择设备类型" filterable clearable @change="deviceTypeChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in deviceTypeDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="设备信息" prop="device_info">
                            <el-input v-model="form.device_info" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="采集时间" prop="collect_time">
                    <el-date-picker type="date" v-model="form.collect_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="文件大小(字节)" prop="file_size">
                            <el-input v-model="form.file_size" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="文件路径" prop="file_path">
                            <el-input v-model="form.file_path" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="文件哈希值" prop="file_hash">
                            <el-input v-model="form.file_hash" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="内容摘要" prop="content_abstract">
                            <el-input v-model="form.content_abstract" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="重要性" prop="importance">
                            <el-select v-model="form.importance" placeholder="请选择重要性" filterable clearable @change="importanceChange">
                                <el-option :label="item.title" :value="item.code" v-for="item in importanceDatas" :key="item.code"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="关联人物" prop="relatedPersons">
                            <el-select v-model="formRelatedPersons" placeholder="请选择关联人物" multiple filterable clearable @change="relatedPersonsChange">
                                <el-option :label="item.title" :value="item.code+''" v-for="item in relatedPersonsDatas" :key="item.code"></el-option>
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
                        <el-table-column prop="probe_idName" show-overflow-tooltip label="采集探针"  width="180">
                        </el-table-column>
                        <el-table-column prop="evidence_no" show-overflow-tooltip label="证据编号"  width="180">
                        </el-table-column>
                        <el-table-column prop="evidence_name" show-overflow-tooltip label="证据名称"  width="180">
                        </el-table-column>
                        <el-table-column prop="evidenceTypeName" show-overflow-tooltip label="证据类型"  width="180">
                        </el-table-column>
                        <el-table-column prop="deviceTypeName" show-overflow-tooltip label="设备类型"  width="180">
                        </el-table-column>
                        <el-table-column prop="device_info" show-overflow-tooltip label="设备信息"  width="180">
                        </el-table-column>
                        <el-table-column prop="collect_time" show-overflow-tooltip label="采集时间"  width="180">
                        </el-table-column>
                        <el-table-column prop="file_size" show-overflow-tooltip label="文件大小(字节)"  width="180">
                        </el-table-column>
                        <el-table-column prop="file_path" show-overflow-tooltip label="文件路径"  width="180">
                        </el-table-column>
                        <el-table-column prop="file_hash" show-overflow-tooltip label="文件哈希值"  width="180">
                        </el-table-column>
                        <el-table-column prop="content_abstract" show-overflow-tooltip label="内容摘要"  width="180">
                        </el-table-column>
                        <el-table-column prop="importanceName" show-overflow-tooltip label="重要性"  width="180">
                        </el-table-column>
                        <el-table-column prop="relatedPersonsName" show-overflow-tooltip label="关联人物" >
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
                    evidence_name:'',
                    evidenceType:'',
                    deviceType:'',
                    device_info:'',
                    collect_time:[],
                    importance:'',
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
                evidenceTypeDatas: [],
                deviceTypeDatas: [],
                importanceDatas: [],
                relatedPersonsDatas: [],
                formRelatedPersons: [],
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
                        let url = "evidenceInfo/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "evidenceInfo/update";
                            message = '修改成功';
                        }
                        that.form.relatedPersons=that.formRelatedPersons.join(',');
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
                    evidence_name:that.queryPanel.evidence_name,
                    evidenceType:that.queryPanel.evidenceType,
                    deviceType:that.queryPanel.deviceType,
                    device_info:that.queryPanel.device_info,
                    collect_timeStart:that.queryPanel.collect_time!=null && that.queryPanel.collect_time.length>0?that.queryPanel.collect_time[0]:'',
                    collect_timeEnd:that.queryPanel.collect_time!=null && that.queryPanel.collect_time.length>0?that.queryPanel.collect_time[1]:'',
                    importance:that.queryPanel.importance,
                };
                ApiManager.post('evidenceInfo/listByCondition', data).then((res) =>{
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
                    evidence_name:that.queryPanel.evidence_name,
                    evidenceType:that.queryPanel.evidenceType,
                    deviceType:that.queryPanel.deviceType,
                    device_info:that.queryPanel.device_info,
                    collect_timeStart:that.queryPanel.collect_time!=null && that.queryPanel.collect_time.length>0?that.queryPanel.collect_time[0]:'',
                    collect_timeEnd:that.queryPanel.collect_time!=null && that.queryPanel.collect_time.length>0?that.queryPanel.collect_time[1]:'',
                    importance:that.queryPanel.importance,
                };
                ApiManager.download('evidenceInfo/exportExcel', data).then((res) =>{
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
                    ApiManager.get('evidenceInfo/delete', data).then((res) =>{
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
                ApiManager.get('evidenceInfo/detail', data).then((res) =>{
                    if (res.message == "SUCCESS") {
                        that.form = res.data;
                        that.formRelatedPersons=that.form.relatedPersons.split(',').map(function(data){return data})
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
                    ApiManager.get('evidenceInfo/deleteAll', data).then((res) =>{
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
            evidenceTypeChange: function(val){
                if (val===''){
                    this.form.evidenceType = null
                }
            },
            deviceTypeChange: function(val){
                if (val===''){
                    this.form.deviceType = null
                }
            },
            importanceChange: function(val){
                if (val===''){
                    this.form.importance = null
                }
            },
            relatedPersonsChange: function(val){
                if (val===''){
                    this.form.relatedPersons = null
                }
            },
            getCase_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceInfo/getCase_idDatas', data).then((res) =>{
                    that.case_idDatas = res.data;

                });
            },
            getProbe_idDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceInfo/getProbe_idDatas', data).then((res) =>{
                    that.probe_idDatas = res.data;

                });
            },
            getEvidenceTypeDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceInfo/getEvidenceTypeDatas', data).then((res) =>{
                    that.evidenceTypeDatas = res.data;

                });
            },
            getDeviceTypeDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceInfo/getDeviceTypeDatas', data).then((res) =>{
                    that.deviceTypeDatas = res.data;

                });
            },
            getImportanceDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceInfo/getImportanceDatas', data).then((res) =>{
                    that.importanceDatas = res.data;

                });
            },
            getRelatedPersonsDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('evidenceInfo/getRelatedPersonsDatas', data).then((res) =>{
                    that.relatedPersonsDatas = res.data;

                });
            },
        },
        created() {
            this.handleQuery();
            this.getCase_idDatas();
            this.getProbe_idDatas();
            this.getEvidenceTypeDatas();
            this.getDeviceTypeDatas();
            this.getImportanceDatas();
            this.getRelatedPersonsDatas();
        }
    }
</script>


