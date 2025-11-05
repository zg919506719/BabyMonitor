<template>
  <el-dialog
      v-model="dialogVisible"
      title="行为详情"
      width="600px"
      @close="handleClose"
  >
    <el-descriptions :column="1" border v-if="behaviorData">
      <el-descriptions-item label="行为ID">
        {{ behaviorData.id }}
      </el-descriptions-item>

      <el-descriptions-item label="用户信息">
        <div class="user-info">
          <el-avatar :size="32" :src="behaviorData.userAvatar" />
          <span class="username">{{ behaviorData.username }}</span>
          <span class="user-id">(ID: {{ behaviorData.userId }})</span>
        </div>
      </el-descriptions-item>

      <el-descriptions-item label="行为类型">
        <el-tag :type="getBehaviorTypeTag(behaviorData.behaviorType)">
          {{ getBehaviorTypeName(behaviorData.behaviorType) }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="页面地址">
        {{ behaviorData.page }}
      </el-descriptions-item>

      <el-descriptions-item label="操作元素">
        {{ behaviorData.element || '-' }}
      </el-descriptions-item>

      <el-descriptions-item label="行为描述">
        {{ behaviorData.description }}
      </el-descriptions-item>

      <el-descriptions-item label="设备信息">
        <div class="device-details">
          <div>设备类型: {{ behaviorData.deviceType }}</div>
          <div>浏览器: {{ behaviorData.browser }}</div>
        </div>
      </el-descriptions-item>

      <el-descriptions-item label="IP地址">
        {{ behaviorData.ipAddress }}
      </el-descriptions-item>

      <el-descriptions-item label="发生时间">
        {{ formatDateTime(behaviorData.createTime) }}
      </el-descriptions-item>

      <el-descriptions-item label="附加数据" v-if="behaviorData.extraData">
        <pre class="extra-data">{{ JSON.stringify(behaviorData.extraData, null, 2) }}</pre>
      </el-descriptions-item>
    </el-descriptions>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

interface Props {
  modelValue: boolean
  behaviorData?: any
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const dialogVisible = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
})

watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
}

// 工具函数
const getBehaviorTypeTag = (type: string) => {
  const types: { [key: string]: string } = {
    page_view: 'primary',
    button_click: 'success',
    feature_use: 'warning',
    payment: 'danger',
    share: 'info'
  }
  return types[type] || 'info'
}

const getBehaviorTypeName = (type: string) => {
  const names: { [key: string]: string } = {
    page_view: '页面访问',
    button_click: '按钮点击',
    feature_use: '功能使用',
    payment: '支付行为',
    share: '分享行为'
  }
  return names[type] || type
}

const formatDateTime = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}
</script>

<style scoped>
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.username {
  font-weight: 500;
}

.user-id {
  color: #909399;
  font-size: 12px;
}

.device-details {
  line-height: 1.6;
}

.extra-data {
  background: #f5f7fa;
  padding: 8px;
  border-radius: 4px;
  font-size: 12px;
  max-height: 200px;
  overflow: auto;
}
</style>