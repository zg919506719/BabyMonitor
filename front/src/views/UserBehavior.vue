<template>
  <div class="behavior-container">
    <!-- 筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="filterForm" :inline="true" class="filter-form">
        <el-form-item label="用户ID">
          <el-input
              v-model="filterForm.userId"
              placeholder="请输入用户ID"
              clearable
          />
        </el-form-item>

        <el-form-item label="用户名">
          <el-input
              v-model="filterForm.username"
              placeholder="请输入用户名"
              clearable
          />
        </el-form-item>

        <el-form-item label="行为类型">
          <el-select
              v-model="filterForm.behaviorType"
              placeholder="请选择行为类型"
              clearable
          >
            <el-option label="页面访问" value="page_view" />
            <el-option label="按钮点击" value="button_click" />
            <el-option label="功能使用" value="feature_use" />
            <el-option label="支付行为" value="payment" />
            <el-option label="分享行为" value="share" />
            <el-option label="搜索行为" value="search" />
          </el-select>
        </el-form-item>

        <el-form-item label="时间范围">
          <el-date-picker
              v-model="filterForm.timeRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleFilter">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button @click="handleExport">
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="16">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon user-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ stats.totalUsers }}</div>
                <div class="stat-label">活跃用户</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon behavior-icon">
                <el-icon><DataLine /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ stats.totalBehaviors }}</div>
                <div class="stat-label">总行为数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon pageview-icon">
                <el-icon><View /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ stats.avgPageViews }}</div>
                <div class="stat-label">人均页面浏览</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-item">
              <div class="stat-icon duration-icon">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ stats.avgDuration }}</div>
                <div class="stat-label">平均停留(分钟)</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <div class="chart-header">
              <span>行为类型分布</span>
            </div>
          </template>
          <div ref="behaviorChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <div class="chart-header">
              <span>活跃时段分析</span>
            </div>
          </template>
          <div ref="hourlyChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 行为列表 -->
    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="table-header">
          <span class="header-title">用户行为记录</span>
          <div>
            <el-button @click="handleRefresh">
              <el-icon><RefreshRight /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </template>

      <el-table
          :data="behaviorList"
          v-loading="loading"
          stripe
          style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />

        <el-table-column prop="userInfo" label="用户信息" width="180">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="32" :src="row.userAvatar" />
              <div class="user-details">
                <div class="username">{{ row.username }}</div>
                <div class="user-id">ID: {{ row.userId }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="behaviorType" label="行为类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getBehaviorTypeTag(row.behaviorType)">
              {{ getBehaviorTypeName(row.behaviorType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="page" label="页面/功能" width="200">
          <template #default="{ row }">
            <div class="page-info">
              <span class="page-name">{{ row.page }}</span>
              <div v-if="row.element" class="element-name">{{ row.element }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="description" label="行为描述" min-width="200">
          <template #default="{ row }">
            <div class="behavior-description">
              {{ row.description }}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="device" label="设备信息" width="150">
          <template #default="{ row }">
            <div class="device-info">
              <el-tag size="small">{{ row.deviceType }}</el-tag>
              <div class="browser">{{ row.browser }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="ipAddress" label="IP地址" width="130" />

        <el-table-column prop="createTime" label="发生时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button
                size="small"
                type="primary"
                link
                @click="handleViewDetail(row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 行为详情对话框 -->
    <behavior-detail-dialog
        v-model="detailDialogVisible"
        :behavior-data="currentBehavior"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Download, RefreshRight, User, DataLine, View, Clock } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import BehaviorDetailDialog from '../components/BehaviorDetailDialog.vue'

// 行为数据类型
interface UserBehavior {
  id: number
  userId: number
  username: string
  userAvatar: string
  behaviorType: string
  page: string
  element: string
  description: string
  deviceType: string
  browser: string
  ipAddress: string
  createTime: string
  extraData?: any
}

// 筛选表单
const filterForm = reactive({
  userId: '',
  username: '',
  behaviorType: '',
  timeRange: []
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 统计数据
const stats = reactive({
  totalUsers: 0,
  totalBehaviors: 0,
  avgPageViews: 0,
  avgDuration: 0
})

// 响应式数据
const behaviorList = ref<UserBehavior[]>([])
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentBehavior = ref<UserBehavior | null>(null)

// 图表引用
const behaviorChartRef = ref<HTMLElement>()
const hourlyChartRef = ref<HTMLElement>()
let behaviorChart: echarts.ECharts | null = null
let hourlyChart: echarts.ECharts | null = null

// 获取行为列表
const fetchBehaviorList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    const params = {
      ...filterForm,
      page: pagination.current,
      size: pagination.size,
      startTime: filterForm.timeRange?.[0],
      endTime: filterForm.timeRange?.[1]
    }

    // 模拟数据
    behaviorList.value = [
      {
        id: 1,
        userId: 1001,
        username: '用户张三',
        userAvatar: '',
        behaviorType: 'page_view',
        page: '/dashboard',
        element: '',
        description: '访问仪表板页面',
        deviceType: 'Desktop',
        browser: 'Chrome',
        ipAddress: '192.168.1.100',
        createTime: '2024-01-20 10:30:25'
      },
      {
        id: 2,
        userId: 1002,
        username: '用户李四',
        userAvatar: '',
        behaviorType: 'button_click',
        page: '/devices',
        element: 'add-device-btn',
        description: '点击添加设备按钮',
        deviceType: 'Mobile',
        browser: 'Safari',
        ipAddress: '192.168.1.101',
        createTime: '2024-01-20 11:15:30'
      },
      {
        id: 3,
        userId: 1001,
        username: '用户张三',
        userAvatar: '',
        behaviorType: 'feature_use',
        page: '/monitor',
        element: 'real-time-monitor',
        description: '使用实时监控功能',
        deviceType: 'Desktop',
        browser: 'Chrome',
        ipAddress: '192.168.1.100',
        createTime: '2024-01-20 14:20:15'
      },
      {
        id: 4,
        userId: 1003,
        username: '用户王五',
        userAvatar: '',
        behaviorType: 'payment',
        page: '/vip',
        element: 'buy-vip-btn',
        description: '购买VIP会员',
        deviceType: 'Tablet',
        browser: 'Firefox',
        ipAddress: '192.168.1.102',
        createTime: '2024-01-20 16:45:20'
      }
    ]
    pagination.total = 4

    // 模拟统计数据
    stats.totalUsers = 156
    stats.totalBehaviors = 2847
    stats.avgPageViews = 18.2
    stats.avgDuration = 12.5

  } catch (error) {
    ElMessage.error('获取行为数据失败')
    console.error('Error fetching behaviors:', error)
  } finally {
    loading.value = false
  }
}

// 初始化图表
const initCharts = () => {
  nextTick(() => {
    if (behaviorChartRef.value) {
      behaviorChart = echarts.init(behaviorChartRef.value)
      const behaviorOption = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center'
        },
        series: [
          {
            name: '行为类型',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 18,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1048, name: '页面访问' },
              { value: 735, name: '按钮点击' },
              { value: 580, name: '功能使用' },
              { value: 300, name: '支付行为' },
              { value: 184, name: '其他行为' }
            ]
          }
        ]
      }
      behaviorChart.setOption(behaviorOption)
    }

    if (hourlyChartRef.value) {
      hourlyChart = echarts.init(hourlyChartRef.value)
      const hourlyOption = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['00', '02', '04', '06', '08', '10', '12', '14', '16', '18', '20', '22']
        },
        yAxis: {
          type: 'value',
          name: '行为次数'
        },
        series: [
          {
            name: '行为活跃度',
            type: 'line',
            smooth: true,
            data: [120, 82, 91, 154, 362, 580, 782, 864, 735, 620, 485, 290],
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(64, 158, 255, 0.3)'
                }, {
                  offset: 1, color: 'rgba(64, 158, 255, 0.1)'
                }]
              }
            },
            lineStyle: {
              width: 3
            }
          }
        ]
      }
      hourlyChart.setOption(hourlyOption)
    }
  })
}

// 窗口大小变化时重绘图表
const handleResize = () => {
  behaviorChart?.resize()
  hourlyChart?.resize()
}

// 筛选
const handleFilter = () => {
  pagination.current = 1
  fetchBehaviorList()
}

// 重置筛选
const handleReset = () => {
  Object.assign(filterForm, {
    userId: '',
    username: '',
    behaviorType: '',
    timeRange: []
  })
  pagination.current = 1
  fetchBehaviorList()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  fetchBehaviorList()
}

// 页码变化
const handleCurrentChange = (page: number) => {
  pagination.current = page
  fetchBehaviorList()
}

// 查看详情
const handleViewDetail = (behavior: UserBehavior) => {
  currentBehavior.value = behavior
  detailDialogVisible.value = true
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 刷新
const handleRefresh = () => {
  fetchBehaviorList()
}

// 工具函数
const getBehaviorTypeTag = (type: string) => {
  const types: { [key: string]: string } = {
    page_view: 'primary',
    button_click: 'success',
    feature_use: 'warning',
    payment: 'danger',
    share: 'info',
    search: ''
  }
  return types[type] || 'info'
}

const getBehaviorTypeName = (type: string) => {
  const names: { [key: string]: string } = {
    page_view: '页面访问',
    button_click: '按钮点击',
    feature_use: '功能使用',
    payment: '支付行为',
    share: '分享行为',
    search: '搜索行为'
  }
  return names[type] || type
}

const formatDateTime = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  fetchBehaviorList()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  behaviorChart?.dispose()
  hourlyChart?.dispose()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.behavior-container {
  padding: 20px;
}

.filter-card {
  margin-bottom: 16px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.stats-overview {
  margin-bottom: 16px;
}

.stat-card {
  height: 100%;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 10px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.user-icon { background: #409EFF; }
.behavior-icon { background: #67C23A; }
.pageview-icon { background: #E6A23C; }
.duration-icon { background: #F56C6C; }

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.chart-row {
  margin-bottom: 16px;
}

.chart-header {
  font-weight: 600;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: 500;
  margin-bottom: 2px;
}

.user-id {
  font-size: 12px;
  color: #909399;
}

.page-info {
  display: flex;
  flex-direction: column;
}

.page-name {
  font-weight: 500;
}

.element-name {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.behavior-description {
  line-height: 1.4;
}

.device-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.browser {
  font-size: 12px;
  color: #909399;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.el-form-item) {
  margin-bottom: 0;
}
</style>