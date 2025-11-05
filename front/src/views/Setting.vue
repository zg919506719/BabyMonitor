<template>
  <div class="settings-container">
    <el-card class="settings-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">系统设置</span>
        </div>
      </template>

      <el-form :model="settingsForm" label-width="120px" class="settings-form">
        <!-- 基本设置 -->
        <el-divider content-position="left">基本设置</el-divider>

        <el-form-item label="系统名称">
          <el-input v-model="settingsForm.systemName" placeholder="请输入系统名称" />
        </el-form-item>

        <el-form-item label="系统版本">
          <el-tag type="info">v1.0.0</el-tag>
        </el-form-item>

        <el-form-item label="语言设置">
          <el-radio-group v-model="settingsForm.language">
            <el-radio label="zh-CN">中文</el-radio>
            <el-radio label="en-US">English</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="主题设置">
          <el-radio-group v-model="settingsForm.theme">
            <el-radio label="light">浅色主题</el-radio>
            <el-radio label="dark">深色主题</el-radio>
            <el-radio label="auto">跟随系统</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 通知设置 -->
        <el-divider content-position="left">通知设置</el-divider>

        <el-form-item label="邮件通知">
          <el-switch v-model="settingsForm.emailNotify" />
          <span class="setting-desc">重要系统通知将通过邮件发送</span>
        </el-form-item>

        <el-form-item label="短信通知">
          <el-switch v-model="settingsForm.smsNotify" />
          <span class="setting-desc">紧急告警将通过短信通知</span>
        </el-form-item>

        <el-form-item label="推送通知">
          <el-switch v-model="settingsForm.pushNotify" />
          <span class="setting-desc">实时消息推送</span>
        </el-form-item>

        <!-- 安全设置 -->
        <el-divider content-position="left">安全设置</el-divider>

        <el-form-item label="自动登出">
          <el-input-number
              v-model="settingsForm.autoLogout"
              :min="5"
              :max="120"
              controls-position="right"
          />
          <span class="setting-desc">分钟无操作自动登出</span>
        </el-form-item>

        <el-form-item label="密码强度">
          <el-select v-model="settingsForm.passwordStrength" placeholder="请选择密码强度">
            <el-option label="低" value="low" />
            <el-option label="中" value="medium" />
            <el-option label="高" value="high" />
          </el-select>
        </el-form-item>

        <!-- 数据设置 -->
        <el-divider content-position="left">数据设置</el-divider>

        <el-form-item label="数据备份">
          <el-select v-model="settingsForm.backupFrequency" placeholder="请选择备份频率">
            <el-option label="每天" value="daily" />
            <el-option label="每周" value="weekly" />
            <el-option label="每月" value="monthly" />
          </el-select>
        </el-form-item>

        <el-form-item label="数据保留">
          <el-input-number
              v-model="settingsForm.dataRetention"
              :min="1"
              :max="365"
              controls-position="right"
          />
          <span class="setting-desc">天，超过自动清理</span>
        </el-form-item>

        <!-- 操作按钮 -->
        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="loading">
            <el-icon><Check /></el-icon>
            保存设置
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            恢复默认
          </el-button>
          <el-button @click="handleClearCache">
            <el-icon><Delete /></el-icon>
            清除缓存
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 快捷操作卡片 -->
    <div class="quick-actions">
      <el-card class="action-card">
        <template #header>
          <div class="card-header">
            <span class="header-title">快捷操作</span>
          </div>
        </template>

        <div class="action-buttons">
          <el-button type="primary" @click="handleBackup">
            <el-icon><Download /></el-icon>
            立即备份
          </el-button>
          <el-button @click="handleCheckUpdate">
            <el-icon><Upload /></el-icon>
            检查更新
          </el-button>
          <el-button @click="handleSystemInfo">
            <el-icon><InfoFilled /></el-icon>
            系统信息
          </el-button>
          <el-button @click="handleRestart">
            <el-icon><RefreshRight /></el-icon>
            重启服务
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Check, Refresh, Delete, Download, Upload, InfoFilled, RefreshRight } from '@element-plus/icons-vue'

// 设置表单数据
const settingsForm = reactive({
  systemName: 'BabyMonitor 婴儿监控系统',
  language: 'zh-CN',
  theme: 'light',
  emailNotify: true,
  smsNotify: false,
  pushNotify: true,
  autoLogout: 30,
  passwordStrength: 'medium',
  backupFrequency: 'daily',
  dataRetention: 90
})

const loading = ref(false)

// 保存设置
const handleSave = async () => {
  loading.value = true
  try {
    // 模拟API调用
    // await ajax.put('/api/settings', settingsForm)

    // 模拟保存延迟
    await new Promise(resolve => setTimeout(resolve, 1000))

    ElMessage.success('设置保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    loading.value = false
  }
}

// 恢复默认设置
const handleReset = async () => {
  try {
    await ElMessageBox.confirm(
        '确定要恢复默认设置吗？当前设置将会丢失。',
        '恢复确认',
        {
          type: 'warning',
          confirmButtonText: '确定恢复',
          cancelButtonText: '取消'
        }
    )

    Object.assign(settingsForm, {
      systemName: 'BabyMonitor 婴儿监控系统',
      language: 'zh-CN',
      theme: 'light',
      emailNotify: true,
      smsNotify: false,
      pushNotify: true,
      autoLogout: 30,
      passwordStrength: 'medium',
      backupFrequency: 'daily',
      dataRetention: 90
    })

    ElMessage.success('已恢复默认设置')
  } catch (error) {
    // 用户取消
  }
}

// 清除缓存
const handleClearCache = async () => {
  try {
    await ElMessageBox.confirm(
        '确定要清除系统缓存吗？',
        '清除确认',
        {
          type: 'warning',
          confirmButtonText: '确定清除',
          cancelButtonText: '取消'
        }
    )

    // 模拟清除缓存
    await new Promise(resolve => setTimeout(resolve, 500))

    ElMessage.success('缓存清除成功')
  } catch (error) {
    // 用户取消
  }
}

// 立即备份
const handleBackup = async () => {
  try {
    await ElMessageBox.confirm(
        '确定要立即备份系统数据吗？',
        '备份确认',
        {
          confirmButtonText: '开始备份',
          cancelButtonText: '取消'
        }
    )

    // 模拟备份过程
    ElMessage.info('备份任务已开始，请稍后...')

    setTimeout(() => {
      ElMessage.success('数据备份完成')
    }, 2000)

  } catch (error) {
    // 用户取消
  }
}

// 检查更新
const handleCheckUpdate = () => {
  ElMessage.info('当前已是最新版本 v1.0.0')
}

// 系统信息
const handleSystemInfo = () => {
  ElMessageBox.alert(
      `
    <div style="text-align: left;">
      <p><strong>系统版本：</strong> v1.0.0</p>
      <p><strong>运行环境：</strong> Node.js + Vue 3 + Element Plus</p>
      <p><strong>数据库：</strong> MySQL 8.0</p>
      <p><strong>服务器：</strong> Docker + Nginx</p>
      <p><strong>最后更新：</strong> 2024-01-20</p>
    </div>
    `,
      '系统信息',
      {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      }
  )
}

// 重启服务
const handleRestart = async () => {
  try {
    await ElMessageBox.confirm(
        '确定要重启系统服务吗？重启期间系统将暂时不可用。',
        '重启确认',
        {
          type: 'warning',
          confirmButtonText: '确定重启',
          cancelButtonText: '取消'
        }
    )

    ElMessage.info('系统重启中，请稍后...')

    // 模拟重启过程
    setTimeout(() => {
      ElMessage.success('系统重启完成')
    }, 3000)

  } catch (error) {
    // 用户取消
  }
}

// 加载设置
const loadSettings = async () => {
  try {
    // 模拟从API加载设置
    // const response = await ajax.get('/api/settings')
    // Object.assign(settingsForm, response.data)
  } catch (error) {
    console.error('加载设置失败:', error)
  }
}

// 生命周期
onMounted(() => {
  loadSettings()
})
</script>

<style scoped>
.settings-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.settings-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.settings-form {
  max-width: 600px;
}

.setting-desc {
  margin-left: 10px;
  font-size: 12px;
  color: #909399;
}

.quick-actions {
  margin-top: 20px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

:deep(.el-divider__text) {
  background-color: #f5f7fa;
  font-weight: 500;
}

:deep(.el-form-item) {
  margin-bottom: 22px;
}

:deep(.el-input-number) {
  width: 120px;
}
</style>