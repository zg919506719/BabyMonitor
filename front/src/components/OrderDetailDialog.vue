<template>
  <el-dialog
      v-model="dialogVisible"
      title="订单详情"
      width="700px"
      @close="handleClose"
  >
    <el-descriptions :column="2" border v-loading="loading">
      <el-descriptions-item label="订单号">
        {{ orderData?.orderNo }}
      </el-descriptions-item>

      <el-descriptions-item label="用户信息">
        <div class="user-info">
          <el-avatar :size="32" :src="orderData?.userAvatar" />
          <span class="username">{{ orderData?.username }}</span>
          <span class="user-id">(ID: {{ orderData?.userId }})</span>
        </div>
      </el-descriptions-item>

      <el-descriptions-item label="VIP套餐">
        <el-tag :type="getPlanTagType(orderData?.vipPlan)">
          {{ getPlanName(orderData?.vipPlan) }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="支付金额">
        <span class="amount">¥{{ orderData?.amount }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="原价">
        <span class="original-amount">¥{{ orderData?.originalAmount }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="折扣">
        <span class="discount">{{ orderData?.discount }}折</span>
      </el-descriptions-item>

      <el-descriptions-item label="支付方式">
        <el-tag :type="getPaymentTagType(orderData?.paymentMethod)">
          {{ getPaymentMethodName(orderData?.paymentMethod) }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="支付状态">
        <el-tag :type="getStatusTagType(orderData?.payStatus)" effect="dark">
          {{ getStatusName(orderData?.payStatus) }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="创建时间">
        {{ formatDateTime(orderData?.createTime) }}
      </el-descriptions-item>

      <el-descriptions-item label="支付时间">
        {{ orderData?.payTime ? formatDateTime(orderData.payTime) : '-' }}
      </el-descriptions-item>

      <el-descriptions-item label="到期时间" :span="2">
        {{ orderData?.expireTime ? formatDateTime(orderData.expireTime) : '-' }}
      </el-descriptions-item>

      <el-descriptions-item label="交易号" :span="2">
        {{ orderData?.transactionId || '-' }}
      </el-descriptions-item>
    </el-descriptions>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button
            v-if="orderData?.payStatus === 'paid'"
            type="warning"
            @click="handleRefund"
        >
          退款
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

interface Props {
  modelValue: boolean
  orderData?: any
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'refresh'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const loading = ref(false)
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

// 退款
const handleRefund = async () => {
  if (!props.orderData) return

  try {
    await ElMessageBox.confirm(
        `确定要对订单 "${props.orderData.orderNo}" 进行退款吗？`,
        '退款确认',
        {
          type: 'warning',
          confirmButtonText: '确定退款',
          cancelButtonText: '取消'
        }
    )

    // await ajax.post(`/api/vip-orders/${props.orderData.id}/refund`)
    ElMessage.success('退款申请已提交')
    emit('refresh')
    handleClose()
  } catch (error) {
    // 用户取消
  }
}

// 工具函数
const getPlanTagType = (plan?: string) => {
  const types: { [key: string]: string } = {
    monthly: 'primary',
    quarterly: 'success',
    yearly: 'warning',
    lifetime: 'danger'
  }
  return types[plan || ''] || 'info'
}

const getPlanName = (plan?: string) => {
  const names: { [key: string]: string } = {
    monthly: '月卡',
    quarterly: '季卡',
    yearly: '年卡',
    lifetime: '终身卡'
  }
  return names[plan || ''] || plan
}

const getPaymentTagType = (method?: string) => {
  const types: { [key: string]: string } = {
    wechat: 'success',
    alipay: 'primary',
    bank: 'warning'
  }
  return types[method || ''] || 'info'
}

const getPaymentMethodName = (method?: string) => {
  const names: { [key: string]: string } = {
    wechat: '微信支付',
    alipay: '支付宝',
    bank: '银行卡'
  }
  return names[method || ''] || method
}

const getStatusTagType = (status?: string) => {
  const types: { [key: string]: string } = {
    pending: 'warning',
    paid: 'success',
    failed: 'danger',
    refunded: 'info'
  }
  return types[status || ''] || 'info'
}

const getStatusName = (status?: string) => {
  const names: { [key: string]: string } = {
    pending: '待支付',
    paid: '支付成功',
    failed: '支付失败',
    refunded: '已退款'
  }
  return names[status || ''] || status
}

const formatDateTime = (dateString?: string) => {
  return dateString ? new Date(dateString).toLocaleString('zh-CN') : '-'
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

.amount {
  font-weight: bold;
  color: #F56C6C;
}

.original-amount {
  text-decoration: line-through;
  color: #909399;
}

.discount {
  color: #67C23A;
  font-weight: 500;
}
</style>