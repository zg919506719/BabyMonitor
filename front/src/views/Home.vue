<template>

  <nav class="navbar">
    <router-link to="/">首页</router-link>
    |
    <router-link to="/dashboard">平台管理</router-link>
  </nav>

  <el-carousel :interval="4000" type="card" height="80vh" class="swipe">
    <el-carousel-item v-for="item in 4" :key="item" >
      <el-image :src="getImageUrl(item)" fit="cover"/>
    </el-carousel-item>
  </el-carousel>
</template>

<style scoped>
.swipe{
  margin-top: 100px;
}
.navbar {
  padding: 1rem;
  background: #f5f5f5;
  border-bottom: 1px solid #ddd;
}

.navbar a {
  margin: 0 10px;
  text-decoration: none;
  color: #333;
}

.navbar a.router-link-active {
  color: #42b983;
  font-weight: bold;
}


</style>
<script setup lang="ts">
const getImageUrl = (index: number) => {
  return `/static/page${index}.png`
}
</script>