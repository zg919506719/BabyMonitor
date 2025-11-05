<template>
  <div class="order-list-container">
    <!-- 筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="filterForm" :inline="true" class="filter-form">
        <el-form-item label="订单号">
          <el-input
              v-model="filterForm.orderNo"
              placeholder="请输入订单号"
              clearable
              @clear="handleFilter"
          />
        </el-form-item>

        <el-form-item label="用户ID">
          <el-input
              v-model="filterForm.userId"
              placeholder="请输入用户ID"
              clearable
              @clear="handleFilter"
          />
        </el-form-item>

        <el-form-item label="用户名">
          <el-input
              v-model="filterForm.username"
              placeholder="请输入用户名"
              clearable
              @clear="handleFilter"
          />
        </el-form-item>

        <el-form-item label="VIP套餐">
          <el-select
              v-model="filterForm.vipPlan"
              placeholder="请选择VIP套餐"
              clearable
              @clear="handleFilter"
          >
            <el-option label="月卡" value="monthly" />
            <el-option label="季卡" value="quarterly" />
            <el-option label="年卡" value="yearly" />
            <el-option label="终身卡" value="lifetime" />
          </el-select>
        </el-form-item>

        <el-form-item label="支付状态">
          <el-select
              v-model="filterForm.payStatus"
              placeholder="请选择支付状态"
              clearable
              @clear="handleFilter"
          >
            <el-option label="待支付" value="pending" />
            <el-option label="支付成功" value="paid" />
            <el-option label="支付失败" value="failed" />
            <el-option label="已退款" value="refunded" />
          </el-select>
        </el-form-item>

        <el-form-item label="支付方式">
          <el-select
              v-model="filterForm.paymentMethod"
              placeholder="请选择支付方式"
              clearable
              @clear="handleFilter"
          >
            <el-option label="微信支付" value="wechat" />
            <el-option label="支付宝" value="alipay" />
            <el-option label="银行卡" value="bank" />
          </el-select>
        </el-form-item>

        <el-form-item label="创建时间">
          <el-date-picker
              v-model="filterForm.createTime"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              @change="handleFilter"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleFilter" :loading="loading">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ stats.totalAmount }}</div>
          <div class="stat-label">总金额(元)</div>
        </div>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ stats.todayAmount }}</div>
          <div class="stat-label">今日金额(元)</div>
        </div>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ stats.paidCount }}</div>
          <div class="stat-label">成功订单</div>
        </div>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ stats.pendingCount }}</div>
          <div class="stat-label">待支付</div>
        </div>
      </el-card>
    </div>

    <!-- 操作按钮 -->
    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="table-header">
          <span class="header-title">VIP充值订单</span>
          <div>
            <el-button @click="handleExport">
              <el-icon><Download /></el-icon>
              导出订单
            </el-button>
            <el-button @click="handleRefresh">
              <el-icon><RefreshRight /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </template>

      <!-- 订单表格 -->
      <el-table
          :data="orderList"
          v-loading="loading"
          stripe
          style="width: 100%"
      >
        <el-table-column prop="orderNo" label="订单号" width="200">
          <template #default="{ row }">
            <div class="order-no">
              <el-tag size="small" type="info">{{ row.orderNo }}</el-tag>
              <el-button type="text" @click="handleCopyOrderNo(row.orderNo)">
                <el-icon><CopyDocument /></el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>

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

        <el-table-column prop="vipPlan" label="VIP套餐" width="120">
          <template #default="{ row }">
            <el-tag :type="getPlanTagType(row.vipPlan)" effect="light">
              {{ getPlanName(row.vipPlan) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template #default="{ row }">
            <span class="amount">¥{{ row.amount }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="originalAmount" label="原价" width="120" align="right">
          <template #default="{ row }">
            <span class="original-amount">¥{{ row.originalAmount }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="discount" label="折扣" width="100">
          <template #default="{ row }">
            <span class="discount">{{ row.discount }}折</span>
          </template>
        </el-table-column>

        <el-table-column prop="paymentMethod" label="支付方式" width="100">
          <template #default="{ row }">
            <el-tag :type="getPaymentTagType(row.paymentMethod)">
              {{ getPaymentMethodName(row.paymentMethod) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="payStatus" label="支付状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.payStatus)" effect="dark">
              {{ getStatusName(row.payStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column prop="payTime" label="支付时间" width="180">
          <template #default="{ row }">
            {{ row.payTime ? formatDateTime(row.payTime) : '-' }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
                size="small"
                type="primary"
                link
                @click="handleView(row)"
            >
              详情
            </el-button>
            <el-button
                v-if="row.payStatus === 'paid'"
                size="small"
                type="warning"
                link
                @click="handleRefund(row)"
            >
              退款
            </el-button>
            <el-button
                v-if="row.payStatus === 'pending'"
                size="small"
                type="success"
                link
                @click="handleManualPaid(row)"
            >
              标记已支付
            </el-button>
            <el-button
                size="small"
                type="danger"
                link
                @click="handleDelete(row)"
            >
              删除
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

    <!-- 订单详情对话框 -->
    <order-detail-dialog
        v-model="detailDialogVisible"
        :order-data="currentOrder"
        @refresh="fetchOrderList"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Download, RefreshRight, CopyDocument } from '@element-plus/icons-vue'
import OrderDetailDialog from '../components/OrderDetailDialog.vue'
import { useClipboard } from '@vueuse/core'

// 订单数据类型
interface VipOrder {
  id: number
  orderNo: string
  userId: number
  username: string
  userAvatar: string
  vipPlan: string
  amount: number
  originalAmount: number
  discount: number
  paymentMethod: string
  payStatus: 'pending' | 'paid' | 'failed' | 'refunded'
  createTime: string
  payTime: string
  expireTime: string
  transactionId?: string
}

// 筛选表单
const filterForm = reactive({
  orderNo: '',
  userId: '',
  username: '',
  vipPlan: '',
  payStatus: '',
  paymentMethod: '',
  createTime: []
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 统计数据
const stats = reactive({
  totalAmount: 0,
  todayAmount: 0,
  paidCount: 0,
  pendingCount: 0
})

// 响应式数据
const orderList = ref<VipOrder[]>([])
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentOrder = ref<VipOrder | null>(null)

const { copy } = useClipboard()

// 获取订单列表
const fetchOrderList = async () => {
  loading.value = true
  try {
    // 这里调用你的API
    const params = {
      ...filterForm,
      page: pagination.current,
      size: pagination.size,
      startTime: filterForm.createTime?.[0],
      endTime: filterForm.createTime?.[1]
    }

    // 模拟API调用
    // const response = await ajax.get('/api/vip-orders', params)
    // orderList.value = response.data.list
    // pagination.total = response.data.total
    // stats.totalAmount = response.data.stats.totalAmount
    // ...

    // 模拟数据
    orderList.value = [
      {
        id: 1,
        orderNo: 'VIP202401200001',
        userId: 1001,
        username: '用户张三',
        userAvatar: '',
        vipPlan: 'monthly',
        amount: 29.9,
        originalAmount: 39.9,
        discount: 7.5,
        paymentMethod: 'wechat',
        payStatus: 'paid',
        createTime: '2024-01-20 10:30:25',
        payTime: '2024-01-20 10:31:00',
        expireTime: '2024-02-20 10:30:25',
        transactionId: '4200001981202401201234567890'
      },
      {
        id: 2,
        orderNo: 'VIP202401200002',
        userId: 1002,
        username: '用户李四',
        userAvatar: '',
        vipPlan: 'yearly',
        amount: 299,
        originalAmount: 399,
        discount: 7.5,
        paymentMethod: 'alipay',
        payStatus: 'pending',
        createTime: '2024-01-20 11:15:30',
        payTime: '',
        expireTime: '',
        transactionId: ''
      },
      {
        id: 3,
        orderNo: 'VIP202401200003',
        userId: 1003,
        username: '用户王五',
        userAvatar: '',
        vipPlan: 'quarterly',
        amount: 79.9,
        originalAmount: 99.9,
        discount: 8,
        paymentMethod: 'bank',
        payStatus: 'failed',
        createTime: '2024-01-20 14:20:15',
        payTime: '',
        expireTime: '',
        transactionId: ''
      },
      {
        id: 4,
        orderNo: 'VIP202401190001',
        userId: 1004,
        username: '用户赵六',
        userAvatar: '',
        vipPlan: 'lifetime',
        amount: 999,
        originalAmount: 1299,
        discount: 7.7,
        paymentMethod: 'wechat',
        payStatus: 'refunded',
        createTime: '2024-01-19 16:45:20',
        payTime: '2024-01-19 16:46:00',
        expireTime: '',
        transactionId: '4200001981202401199876543210'
      }
    ]
    pagination.total = 4

    // 模拟统计数据
    stats.totalAmount = 1407.8
    stats.todayAmount = 408.9
    stats.paidCount = 2
    stats.pendingCount = 1

  } catch (error) {
    ElMessage.error('获取订单列表失败')
    console.error('Error fetching orders:', error)
  } finally {
    loading.value = false
  }
}

// 获取统计数据
const fetchStats = async () => {
  try {
    // const response = await ajax.get('/api/vip-orders/stats')
    // Object.assign(stats, response.data)
  } catch (error) {
    console.error('Error fetching stats:', error)
  }
}

// 筛选
const handleFilter = () => {
  pagination.current = 1
  fetchOrderList()
  fetchStats()
}

// 重置筛选
const handleReset = () => {
  Object.assign(filterForm, {
    orderNo: '',
    userId: '',
    username: '',
    vipPlan: '',
    payStatus: '',
    paymentMethod: '',
    createTime: []
  })
  pagination.current = 1
  fetchOrderList()
  fetchStats()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  fetchOrderList()
}

// 页码变化
const handleCurrentChange = (page: number) => {
  pagination.current = page
  fetchOrderList()
}

// 查看详情
const handleView = (order: VipOrder) => {
  currentOrder.value = order
  detailDialogVisible.value = true
}

// 复制订单号
const handleCopyOrderNo = async (orderNo: string) => {
  try {
    await copy(orderNo)
    ElMessage.success('订单号已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

// 退款
const handleRefund = async (order: VipOrder) => {
  try {
    await ElMessageBox.confirm(
        `确定要对订单 "${order.orderNo}" 进行退款吗？`,
        '退款确认',
        {
          type: 'warning',
          confirmButtonText: '确定退款',
          cancelButtonText: '取消'
        }
    )

    // await ajax.post(`/api/vip-orders/${order.id}/refund`)
    ElMessage.success('退款申请已提交')
    fetchOrderList()
    fetchStats()
  } catch (error) {
    // 用户取消
  }
}

// 手动标记已支付
const handleManualPaid = async (order: VipOrder) => {
  try {
    await ElMessageBox.confirm(
        `确定要手动标记订单 "${order.orderNo}" 为已支付吗？`,
        '标记确认',
        {
          type: 'warning',
          confirmButtonText: '确定标记',
          cancelButtonText: '取消'
        }
    )

    // await ajax.put(`/api/vip-orders/${order.id}/manual-paid`)
    ElMessage.success('标记成功')
    fetchOrderList()
    fetchStats()
  } catch (error) {
    // 用户取消
  }
}

// 删除订单
const handleDelete = async (order: VipOrder) => {
  try {
    await ElMessageBox.confirm(
        `确定删除订单 "${order.orderNo}" 吗？`,
        '删除确认',
        {
          type: 'warning',
          confirmButtonText: '确定删除',
          cancelButtonText: '取消'
        }
    )

    // await ajax.delete(`/api/vip-orders/${order.id}`)
    ElMessage.success('删除成功')
    fetchOrderList()
    fetchStats()
  } catch (error) {
    // 用户取消删除
  }
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 刷新
const handleRefresh = () => {
  fetchOrderList()
  fetchStats()
}

// 工具函数
const getPlanTagType = (plan: string) => {
  const types: { [key: string]: string } = {
    monthly: 'primary',
    quarterly: 'success',
    yearly: 'warning',
    lifetime: 'danger'
  }
  return types[plan] || 'info'
}

const getPlanName = (plan: string) => {
  const names: { [key: string]: string } = {
    monthly: '月卡',
    quarterly: '季卡',
    yearly: '年卡',
    lifetime: '终身卡'
  }
  return names[plan] || plan
}

const getPaymentTagType = (method: string) => {
  const types: { [key: string]: string } = {
    wechat: 'success',
    alipay: 'primary',
    bank: 'warning'
  }
  return types[method] || 'info'
}

const getPaymentMethodName = (method: string) => {
  const names: { [key: string]: string } = {
    wechat: '微信',
    alipay: '支付宝',
    bank: '银行卡'
  }
  return names[method] || method
}

const getStatusTagType = (status: string) => {
  const types: { [key: string]: string } = {
    pending: 'warning',
    paid: 'success',
    failed: 'danger',
    refunded: 'info'
  }
  return types[status] || 'info'
}

const getStatusName = (status: string) => {
  const names: { [key: string]: string } = {
    pending: '待支付',
    paid: '支付成功',
    failed: '支付失败',
    refunded: '已退款'
  }
  return names[status] || status
}

const formatDateTime = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  fetchOrderList()
  fetchStats()
})
</script>

<style scoped>
.order-list-container {
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

.stats-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card {
  text-align: center;
}

.stat-item {
  padding: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
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

.order-no {
  display: flex;
  align-items: center;
  gap: 8px;
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

.amount {
  font-weight: bold;
  color: #F56C6C;
}

.original-amount {
  text-decoration: line-through;
  color: #909399;
  font-size: 12px;
}

.discount {
  color: #67C23A;
  font-weight: 500;
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