<template>
  <div style="display: flex; padding: 100px; align-item: center;">
    <el-button size="small" type="primary" icon="UploadFilled" @click="void (0)">
      点击事件
    </el-button>
 
    <el-button size="small" type="primary" plain @click="void (0)">
      <el-icon :size="18">
        <UploadFilled />
      </el-icon>
      <span>点击事件</span>
    </el-button>
 
    <el-button size="small" type="primary" circle>
      <el-icon :size="18">
        <UploadFilled />
      </el-icon>
    </el-button>
 
    <el-icon :size="20" style="color: #409eff; cursor: pointer" @click="void (0)">
      <UploadFilled />
    </el-icon>
  </div>
</template>
 
<script>

</script>
