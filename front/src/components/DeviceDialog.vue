<template>
  <el-dialog
      v-model="dialogVisible"
      :title="mode === 'add' ? '添加设备' : '编辑设备'"
      width="600px"
      @close="handleClose"
  >
    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="设备名称" prop="deviceName">
        <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
      </el-form-item>

      <el-form-item label="设备类型" prop="deviceType">
        <el-select v-model="form.deviceType" placeholder="请选择设备类型">
          <el-option label="摄像头" value="camera" />
          <el-option label="温度传感器" value="temperature" />
          <el-option label="湿度传感器" value="humidity" />
          <el-option label="声音监测" value="audio" />
          <el-option label="移动监测" value="motion" />
        </el-select>
      </el-form-item>

      <el-form-item label="序列号" prop="serialNumber">
        <el-input v-model="form.serialNumber" placeholder="请输入设备序列号" />
      </el-form-item>

      <el-form-item label="位置" prop="location">
        <el-input v-model="form.location" placeholder="请输入设备位置" />
      </el-form-item>

      <el-form-item label="IP地址" prop="ipAddress">
        <el-input v-model="form.ipAddress" placeholder="请输入IP地址" />
      </el-form-item>

      <el-form-item label="设备状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio label="online">在线</el-radio>
          <el-radio label="offline">离线</el-radio>
          <el-radio label="fault">故障</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

interface Props {
  modelValue: boolean
  deviceData?: any
  mode: 'add' | 'edit'
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  deviceName: '',
  deviceType: '',
  serialNumber: '',
  location: '',
  ipAddress: '',
  status: 'online'
})

const rules: FormRules = {
  deviceName: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 50, message: '设备名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  deviceType: [
    { required: true, message: '请选择设备类型', trigger: 'change' }
  ],
  serialNumber: [
    { required: true, message: '请输入序列号', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入设备位置', trigger: 'blur' }
  ],
  ipAddress: [
    { pattern: /^(\d{1,3}\.){3}\d{1,3}$/, message: '请输入正确的IP地址', trigger: 'blur' }
  ]
}

// 对话框显示状态
const dialogVisible = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
  if (val && props.deviceData) {
    Object.assign(form, props.deviceData)
  } else if (val) {
    // 重置表单
    Object.assign(form, {
      deviceName: '',
      deviceType: '',
      serialNumber: '',
      location: '',
      ipAddress: '',
      status: 'online'
    })
  }
})

watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
  formRef.value?.clearValidate()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  const valid = await formRef.value.validate()
  if (!valid) return

  loading.value = true
  try {
    // 调用API
    // if (props.mode === 'add') {
    //   await ajax.post('/api/devices', form)
    // } else {
    //   await ajax.put(`/api/devices/${props.deviceData.id}`, form)
    // }

    ElMessage.success(props.mode === 'add' ? '添加成功' : '编辑成功')
    emit('success')
    handleClose()
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    loading.value = false
  }
}
</script>