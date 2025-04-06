<template>

    <el-form  :inline="true" id="queryPanelId" :model="queryPanel" ref="queryPanel"  >
        <el-row class="query_panel_container">
                            <el-form-item label="探针编号" prop="probe_no">
                            <el-input v-model="queryPanel.probe_no" clearable></el-input>
                </el-form-item>
                <el-form-item label="探针名称" prop="probe_name">
                            <el-input v-model="queryPanel.probe_name" clearable></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="state">
                            <el-select v-model="queryPanel.state" placeholder="请选择状态" filterable clearable >
                                <el-option :label="item.title" :value="item.code" v-for="item in stateDatas" :key="item.code"></el-option>
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
                        <el-form-item label="探针编号" prop="probe_no">
                            <el-input v-model="form.probe_no" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="探针名称" prop="probe_name">
                            <el-input v-model="form.probe_name" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="探针型号" prop="probe_model">
                            <el-input v-model="form.probe_model" clearable></el-input>
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
                        <el-form-item label="IP地址" prop="ip_address">
                            <el-input v-model="form.ip_address" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="MAC地址" prop="mac_address">
                            <el-input v-model="form.mac_address" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="硬件信息" prop="hardware_info">
                            <el-input v-model="form.hardware_info" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="软件版本" prop="software_version">
                            <el-input v-model="form.software_version" clearable></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="最后在线时间" prop="last_online_time">
                    <el-date-picker type="date" v-model="form.last_online_time" value-format="YYYY-MM-DD" clearable></el-date-picker>
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
                        <el-table-column prop="probe_no" show-overflow-tooltip label="探针编号"  width="180">
                        </el-table-column>
                        <el-table-column prop="probe_name" show-overflow-tooltip label="探针名称"  width="180">
                        </el-table-column>
                        <el-table-column prop="probe_model" show-overflow-tooltip label="探针型号"  width="180">
                        </el-table-column>
                        <el-table-column prop="stateName" show-overflow-tooltip label="状态"  width="180">
                        </el-table-column>
                        <el-table-column prop="ip_address" show-overflow-tooltip label="IP地址"  width="180">
                        </el-table-column>
                        <el-table-column prop="mac_address" show-overflow-tooltip label="MAC地址"  width="180">
                        </el-table-column>
                        <el-table-column prop="hardware_info" show-overflow-tooltip label="硬件信息"  width="180">
                        </el-table-column>
                        <el-table-column prop="software_version" show-overflow-tooltip label="软件版本"  width="180">
                        </el-table-column>
                        <el-table-column prop="last_online_time" show-overflow-tooltip label="最后在线时间" >
                        </el-table-column>

                <el-table-column fixed="right" label="操作" width="160">
                    <template #default="scope">
                        <el-button class="updateBtnCls" size="small" type="success" @click="handleIndex(scope.$index, scope.row)" link>详情</el-button>
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
        title="详情"
        :direction="direction"
        size="80%"
    >
    <ProbeIndex></ProbeIndex>
  </el-drawer>
</template>

<script>
    import { ApiManager, uploadAction } from '@/api/index'
    import  ProbeIndex from '@/views/probe/index.vue'
    export default {
        components:{
            ProbeIndex
        },
        data: function() {
            return {
                uploadAction: uploadAction,
                visible: false,
                queryPanel: {
                    probe_no:'',
                    probe_name:'',
                    state:'',
                },
                tableData: [],
                form: {},
                saveBtnTitle:'立即创建',
                currentPage:1,
                pageSize: 10,
                rowTotal: 0,
                multipleSelection:[],
                stateDatas: [],
                rules: {
                },
                direction:'rtl',
                drawer:false
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
                        let url = "probeInfo/add";
                        let message = '保存成功';
                        if (typeof id != "undefined") {
                            url = "probeInfo/update";
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
                    probe_no:that.queryPanel.probe_no,
                    probe_name:that.queryPanel.probe_name,
                    state:that.queryPanel.state,
                };
                ApiManager.post('probeInfo/listByCondition', data).then((res) =>{
                    that.tableData = res.data.list;
                    that.rowTotal = res.data.total;
                })


            },
                
			handleExport: function() {
                let that = this;
                let data = {
                    page:this.currentPage,
                    size:this.pageSize,
                    probe_no:that.queryPanel.probe_no,
                    probe_name:that.queryPanel.probe_name,
                    state:that.queryPanel.state,
                };
                ApiManager.download('probeInfo/exportExcel', data).then((res) =>{
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
                    ApiManager.get('probeInfo/delete', data).then((res) =>{
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
                ApiManager.get('probeInfo/detail', data).then((res) =>{
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
                    ApiManager.get('probeInfo/deleteAll', data).then((res) =>{
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
            stateChange: function(val){
                if (val===''){
                    this.form.state = null
                }
            },
            getStateDatas: function(){
                let that = this;
                let data = {
                };
                ApiManager.post('probeInfo/getStateDatas', data).then((res) =>{
                    that.stateDatas = res.data;

                });
            },
            handleIndex:function(){
                this.drawer = true
            }
        },
        created() {
            this.handleQuery();
            this.getStateDatas();
        }
    }
</script>


