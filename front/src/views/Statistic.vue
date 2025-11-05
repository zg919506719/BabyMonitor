<template>
  <el-row :gutter="16" style="margin: 50px;">
    <el-col :xs="24" :sm="12" :md="6" class="text-center mb-4" >
      <el-statistic title="已注册用户" :value="0" />
    </el-col>
    <el-col :xs="24" :sm="12" :md="6" class="text-center mb-4">
      <el-statistic :value="138">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            付费订单总数
            <el-icon style="margin-left: 4px" :size="12">
              <Male />
            </el-icon>
          </div>
        </template>
        <template #suffix>/100</template>
      </el-statistic>
    </el-col>
    <el-col :xs="24" :sm="12" :md="6" class="text-center mb-4">
      <el-statistic title="已绑定设备数" :value="outputValue" />
    </el-col>
    <el-col :xs="24" :sm="12" :md="6" class="text-center mb-4">
      <el-statistic title="行为日志总数" :value="562">
        <template #suffix>
          <el-icon style="vertical-align: -0.125em">
            <ChatLineRound />
          </el-icon>
        </template>
      </el-statistic>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useTransition } from '@vueuse/core'
import { ChatLineRound, Male } from '@element-plus/icons-vue'

const source = ref(0)
const outputValue = useTransition(source, {
  duration: 1500,
})
source.value = 172000
</script>
