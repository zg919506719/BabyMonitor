<template>
  <div class="device-list-container">
    <!-- 筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="filterForm" :inline="true" class="filter-form">
        <el-form-item label="设备名称">
          <el-input
              v-model="filterForm.deviceName"
              placeholder="请输入设备名称"
              clearable
              @clear="handleFilter"
          />
        </el-form-item>

        <el-form-item label="设备类型">
          <el-select
              v-model="filterForm.deviceType"
              placeholder="请选择设备类型"
              clearable
              @clear="handleFilter"
          >
            <el-option label="摄像头" value="camera" />
            <el-option label="温度传感器" value="temperature" />
            <el-option label="湿度传感器" value="humidity" />
            <el-option label="声音监测" value="audio" />
            <el-option label="移动监测" value="motion" />
          </el-select>
        </el-form-item>

        <el-form-item label="设备状态">
          <el-select
              v-model="filterForm.status"
              placeholder="请选择设备状态"
              clearable
              @clear="handleFilter"
          >
            <el-option label="在线" value="online" />
            <el-option label="离线" value="offline" />
            <el-option label="故障" value="fault" />
          </el-select>
        </el-form-item>

        <el-form-item label="位置">
          <el-input
              v-model="filterForm.location"
              placeholder="请输入位置"
              clearable
              @clear="handleFilter"
          />
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

    <!-- 操作按钮 -->
    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="table-header">
          <span class="header-title">设备列表</span>
          <div>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              添加设备
            </el-button>
            <el-button @click="handleBatchOperate" :disabled="selectedDevices.length === 0">
              <el-icon><Operation /></el-icon>
              批量操作
            </el-button>
            <el-button @click="handleExport">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
          </div>
        </div>
      </template>

      <!-- 设备表格 -->
      <el-table
          :data="deviceList"
          v-loading="loading"
          stripe
          style="width: 100%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />

        <el-table-column prop="id" label="设备ID" width="100" />

        <el-table-column prop="deviceName" label="设备名称" min-width="150">
          <template #default="{ row }">
            <div class="device-info">
              <el-avatar :size="32" :src="getDeviceIcon(row.deviceType)" />
              <div class="device-details">
                <div class="device-name">{{ row.deviceName }}</div>
                <div class="device-sn">SN: {{ row.serialNumber }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="deviceType" label="设备类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getDeviceTypeTagType(row.deviceType)">
              {{ getDeviceTypeName(row.deviceType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" effect="dark">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="location" label="位置" width="150" />

        <el-table-column prop="ipAddress" label="IP地址" width="130" />

        <el-table-column prop="lastOnlineTime" label="最后在线" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.lastOnlineTime) }}
          </template>
        </el-table-column>

        <el-table-column prop="battery" label="电量" width="100">
          <template #default="{ row }">
            <div class="battery-info">
              <el-progress
                  :percentage="row.battery"
                  :stroke-width="8"
                  :show-text="false"
                  :color="getBatteryColor(row.battery)"
              />
              <span class="battery-text">{{ row.battery }}%</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="signal" label="信号强度" width="100">
          <template #default="{ row }">
            <div class="signal-info">
              <el-icon v-for="n in 4" :key="n" :color="n <= row.signal ? '#67C23A' : '#C0C4CC'">
                <Smoking />
              </el-icon>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button
                size="small"
                type="primary"
                link
                @click="handleView(row)"
            >
              查看
            </el-button>
            <el-button
                size="small"
                type="success"
                link
                @click="handleControl(row)"
            >
              控制
            </el-button>
            <el-button
                size="small"
                type="warning"
                link
                @click="handleEdit(row)"
            >
              编辑
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

    <!-- 设备对话框 -->
    <device-dialog
        v-model="dialogVisible"
        :device-data="currentDevice"
        :mode="dialogMode"
        @success="handleDialogSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download, Operation, Smoking } from '@element-plus/icons-vue'
import DeviceDialog from '../components/DeviceDialog.vue'

// 设备数据类型
interface Device {
  id: number
  deviceName: string
  serialNumber: string
  deviceType: string
  status: 'online' | 'offline' | 'fault'
  location: string
  ipAddress: string
  lastOnlineTime: string
  battery: number
  signal: number
  createTime: string
}

// 筛选表单
const filterForm = reactive({
  deviceName: '',
  deviceType: '',
  status: '',
  location: '',
  createTime: []
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 响应式数据
const deviceList = ref<Device[]>([])
const selectedDevices = ref<Device[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const currentDevice = ref<Device | null>(null)

// 获取设备列表
const fetchDeviceList = async () => {
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
    // const response = await ajax.get('/api/devices', params)
    // deviceList.value = response.data.list
    // pagination.total = response.data.total

    // 模拟数据
    deviceList.value = [
      {
        id: 1,
        deviceName: '婴儿房摄像头',
        serialNumber: 'CAM001',
        deviceType: 'camera',
        status: 'online',
        location: '婴儿房',
        ipAddress: '192.168.1.101',
        lastOnlineTime: '2024-01-20 14:30:25',
        battery: 85,
        signal: 4,
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        deviceName: '卧室温度传感器',
        serialNumber: 'TEMP001',
        deviceType: 'temperature',
        status: 'online',
        location: '主卧室',
        ipAddress: '192.168.1.102',
        lastOnlineTime: '2024-01-20 14:28:10',
        battery: 45,
        signal: 3,
        createTime: '2024-01-16 14:20:00'
      },
      {
        id: 3,
        deviceName: '客厅湿度监测',
        serialNumber: 'HUM001',
        deviceType: 'humidity',
        status: 'offline',
        location: '客厅',
        ipAddress: '192.168.1.103',
        lastOnlineTime: '2024-01-19 22:15:30',
        battery: 20,
        signal: 1,
        createTime: '2024-01-17 09:15:00'
      },
      {
        id: 4,
        deviceName: '婴儿床声音监测',
        serialNumber: 'AUD001',
        deviceType: 'audio',
        status: 'fault',
        location: '婴儿床',
        ipAddress: '192.168.1.104',
        lastOnlineTime: '2024-01-18 16:45:20',
        battery: 90,
        signal: 2,
        createTime: '2024-01-18 11:30:00'
      }
    ]
    pagination.total = 4

  } catch (error) {
    ElMessage.error('获取设备列表失败')
    console.error('Error fetching devices:', error)
  } finally {
    loading.value = false
  }
}

// 选择变化
const handleSelectionChange = (devices: Device[]) => {
  selectedDevices.value = devices
}

// 筛选
const handleFilter = () => {
  pagination.current = 1
  fetchDeviceList()
}

// 重置筛选
const handleReset = () => {
  Object.assign(filterForm, {
    deviceName: '',
    deviceType: '',
    status: '',
    location: '',
    createTime: []
  })
  pagination.current = 1
  fetchDeviceList()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  fetchDeviceList()
}

// 页码变化
const handleCurrentChange = (page: number) => {
  pagination.current = page
  fetchDeviceList()
}

// 添加设备
const handleAdd = () => {
  dialogMode.value = 'add'
  currentDevice.value = null
  dialogVisible.value = true
}

// 编辑设备
const handleEdit = (device: Device) => {
  dialogMode.value = 'edit'
  currentDevice.value = { ...device }
  dialogVisible.value = true
}

// 查看设备
const handleView = (device: Device) => {
  ElMessage.info(`查看设备: ${device.deviceName}`)
  // 这里可以跳转到设备详情页
}

// 控制设备
const handleControl = (device: Device) => {
  ElMessage.info(`控制设备: ${device.deviceName}`)
  // 打开设备控制面板
}

// 删除设备
const handleDelete = async (device: Device) => {
  try {
    await ElMessageBox.confirm(
        `确定删除设备 "${device.deviceName}" 吗？`,
        '删除确认',
        {
          type: 'warning',
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }
    )

    // await ajax.delete(`/api/devices/${device.id}`)
    ElMessage.success('删除成功')
    fetchDeviceList()
  } catch (error) {
    // 用户取消删除
  }
}

// 批量操作
const handleBatchOperate = () => {
  if (selectedDevices.value.length === 0) {
    ElMessage.warning('请选择要操作的设备')
    return
  }

  ElMessage.info(`批量操作 ${selectedDevices.value.length} 个设备`)
  // 实现批量操作逻辑
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 对话框成功回调
const handleDialogSuccess = () => {
  dialogVisible.value = false
  fetchDeviceList()
}

// 工具函数
const getDeviceIcon = (deviceType: string) => {
  const icons: { [key: string]: string } = {
    camera: '/static/icons/camera.png',
    temperature: '/static/icons/temperature.png',
    humidity: '/static/icons/humidity.png',
    audio: '/static/icons/audio.png',
    motion: '/static/icons/motion.png'
  }
  return icons[deviceType] || '/static/icons/device.png'
}

const getDeviceTypeTagType = (deviceType: string) => {
  const types: { [key: string]: string } = {
    camera: 'primary',
    temperature: 'success',
    humidity: 'warning',
    audio: 'info',
    motion: 'danger'
  }
  return types[deviceType] || 'info'
}

const getDeviceTypeName = (deviceType: string) => {
  const names: { [key: string]: string } = {
    camera: '摄像头',
    temperature: '温度传感器',
    humidity: '湿度传感器',
    audio: '声音监测',
    motion: '移动监测'
  }
  return names[deviceType] || deviceType
}

const getStatusTagType = (status: string) => {
  const types: { [key: string]: string } = {
    online: 'success',
    offline: 'info',
    fault: 'danger'
  }
  return types[status] || 'info'
}

const getStatusName = (status: string) => {
  const names: { [key: string]: string } = {
    online: '在线',
    offline: '离线',
    fault: '故障'
  }
  return names[status] || status
}

const getBatteryColor = (percentage: number) => {
  if (percentage > 70) return '#67C23A'
  if (percentage > 30) return '#E6A23C'
  return '#F56C6C'
}

const formatDateTime = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  fetchDeviceList()
})
</script>

<style scoped>
.device-list-container {
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

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.device-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.device-details {
  display: flex;
  flex-direction: column;
}

.device-name {
  font-weight: 500;
  margin-bottom: 2px;
}

.device-sn {
  font-size: 12px;
  color: #909399;
}

.battery-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.battery-text {
  font-size: 12px;
  color: #606266;
  min-width: 30px;
}

.signal-info {
  display: flex;
  align-items: center;
  gap: 2px;
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