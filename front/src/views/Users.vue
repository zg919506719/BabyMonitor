<template>
  <div class="user-list-container">
    <!-- 筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="filterForm" :inline="true" class="filter-form">
        <el-form-item label="用户名">
          <el-input
              v-model="filterForm.username"
              placeholder="请输入用户名"
              clearable
              @clear="handleFilter"
          />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input
              v-model="filterForm.email"
              placeholder="请输入邮箱"
              clearable
              @clear="handleFilter"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-select
              v-model="filterForm.status"
              placeholder="请选择状态"
              clearable
              @clear="handleFilter"
          >
            <el-option label="启用" value="active" />
            <el-option label="禁用" value="inactive" />
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

    <!-- 操作按钮 -->
    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="table-header">
          <span class="header-title">用户列表</span>
          <div>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新增用户
            </el-button>
            <el-button @click="handleExport">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
          </div>
        </div>
      </template>

      <!-- 用户表格 -->
      <el-table
          :data="userList"
          v-loading="loading"
          stripe
          style="width: 100%"
      >
        <el-table-column type="selection" width="55" />

        <el-table-column prop="id" label="ID" width="80" />

        <el-table-column prop="username" label="用户名" min-width="120">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :size="32" :src="row.avatar" />
              <span class="username">{{ row.username }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="email" label="邮箱" min-width="180" />

        <el-table-column prop="phone" label="手机号" width="130" />

        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)">
              {{ getRoleName(row.role) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch
                v-model="row.status"
                :active-value="'active'"
                :inactive-value="'inactive'"
                @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
                size="small"
                type="primary"
                link
                @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
                size="small"
                type="success"
                link
                @click="handleView(row)"
            >
              查看
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

    <!-- 新增/编辑对话框 -->
    <user-dialog
        v-model="dialogVisible"
        :user-data="currentUser"
        :mode="dialogMode"
        @success="handleDialogSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'
import UserDialog from '../components/UserDialog.vue'

// 用户数据类型
interface User {
  id: number
  username: string
  email: string
  phone: string
  avatar: string
  role: string
  status: 'active' | 'inactive'
  createTime: string
}

// 筛选表单
const filterForm = reactive({
  username: '',
  email: '',
  status: '',
  createTime: []
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 响应式数据
const userList = ref<User[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const currentUser = ref<User | null>(null)

// 获取用户列表
const fetchUserList = async () => {
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
    // const response = await ajax.get('/api/users', params)
    // userList.value = response.data.list
    // pagination.total = response.data.total

    // 模拟数据
    userList.value = [
      {
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      },{
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      },{
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      },{
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      },{
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      },{
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      },{
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      },{
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      },{
        id: 1,
        username: 'admin',
        email: 'admin@example.com',
        phone: '13800138000',
        avatar: '',
        role: 'admin',
        status: 'active',
        createTime: '2024-01-15 10:30:00'
      },
      {
        id: 2,
        username: 'user001',
        email: 'user001@example.com',
        phone: '13900139000',
        avatar: '',
        role: 'user',
        status: 'active',
        createTime: '2024-01-16 14:20:00'
      }
    ]
    pagination.total = userList.value.length

  } catch (error) {
    ElMessage.error('获取用户列表失败')
    console.error('Error fetching users:', error)
  } finally {
    loading.value = false
  }
}

// 筛选
const handleFilter = () => {
  pagination.current = 1
  fetchUserList()
}

// 重置筛选
const handleReset = () => {
  Object.assign(filterForm, {
    username: '',
    email: '',
    status: '',
    createTime: []
  })
  pagination.current = 1
  fetchUserList()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  fetchUserList()
}

// 页码变化
const handleCurrentChange = (page: number) => {
  pagination.current = page
  fetchUserList()
}

// 状态切换
const handleStatusChange = async (user: User) => {
  try {
    // await ajax.put(`/api/users/${user.id}/status`, { status: user.status })
    ElMessage.success(`用户 ${user.status === 'active' ? '启用' : '禁用'} 成功`)
  } catch (error) {
    // 操作失败，恢复原状态
    user.status = user.status === 'active' ? 'inactive' : 'active'
    ElMessage.error('操作失败')
  }
}

// 新增用户
const handleAdd = () => {
  dialogMode.value = 'add'
  currentUser.value = null
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (user: User) => {
  dialogMode.value = 'edit'
  currentUser.value = { ...user }
  dialogVisible.value = true
}

// 查看用户
const handleView = (user: User) => {
  ElMessage.info(`查看用户: ${user.username}`)
  // 这里可以跳转到用户详情页
}

// 删除用户
const handleDelete = async (user: User) => {
  try {
    await ElMessageBox.confirm(
        `确定删除用户 "${user.username}" 吗？`,
        '删除确认',
        {
          type: 'warning',
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }
    )

    // await ajax.delete(`/api/users/${user.id}`)
    ElMessage.success('删除成功')
    fetchUserList()
  } catch (error) {
    // 用户取消删除
  }
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 对话框成功回调
const handleDialogSuccess = () => {
  dialogVisible.value = false
  fetchUserList()
}

// 工具函数
const getRoleType = (role: string) => {
  const types: { [key: string]: string } = {
    admin: 'danger',
    user: 'success',
    guest: 'info'
  }
  return types[role] || 'info'
}

const getRoleName = (role: string) => {
  const names: { [key: string]: string } = {
    admin: '管理员',
    user: '普通用户',
    guest: '访客'
  }
  return names[role] || role
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  fetchUserList()
})
</script>

<style scoped>
.user-list-container {
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

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.username {
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