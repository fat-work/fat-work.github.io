<!-- Start Generation Here -->
<template>
  <div class="not-found">
    <h1>404</h1>
    <p>抱歉，这是一个找不到的页面。</p>
    <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}
.not-found h1 {
  font-size: 5rem;
  margin-bottom: 1rem;
}
.not-found p {
  font-size: 1.5rem;
  margin-bottom: 2rem;
}
</style>
<!-- End Generation Here -->
