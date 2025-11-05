<template>
  <el-container class="layout-container-demo" style="height: 100vh">
    <el-aside class="left">
      <el-scrollbar>
        <el-menu :default-openeds="['2']" :router="true">
          <el-menu-item index="/dashboard">
            <el-icon>
              <IconMenu/>
            </el-icon>
            <span>首页</span>
          </el-menu-item>

          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <message/>
              </el-icon>
              用户
            </template>
            <el-menu-item index="/dashboard/user">用户列表</el-menu-item>
            <el-menu-item index="/dashboard/order">订单列表</el-menu-item>
            <el-menu-item index="/dashboard/device">设备列表</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/dashboard/behavior">
            <el-icon>
              <setting/>
            </el-icon>
            <span>行为日志</span>
          </el-menu-item>

          <el-menu-item index="/dashboard/setting">
            <el-icon>
              <setting/>
            </el-icon>
            <span>设置</span>
          </el-menu-item>

        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px;border-bottom: 1px solid var(--el-border-color);">
        <div class="toolbar">
          <div class="crumb">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">homepage</el-breadcrumb-item>
            </el-breadcrumb>
          </div>


          <el-dropdown>
            <el-icon style="margin-right: 8px; margin-top: 1px">
              <setting/>
            </el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>View</el-dropdown-item>
                <el-dropdown-item>Add</el-dropdown-item>
                <el-dropdown-item>Delete</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <span>Tom</span>
        </div>


      </el-header>

      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script lang="ts" setup>
import {Menu as IconMenu, Message, Setting} from '@element-plus/icons-vue'
</script>

<style scoped>
.left {
  width: 20vw;
  border-right: 1px solid var(--el-border-color);
}

.crumb {
  flex: 1;
}

.layout-container-demo .el-header {
  color: var(--el-text-color-primary);
}

.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
}

.layout-container-demo .el-menu {
  border-right: none;
}

.layout-container-demo .el-main {
  padding: 0;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  height: 100%;
  width: 100%;
  right: 20px;
}
</style>
