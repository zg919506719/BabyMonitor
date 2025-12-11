package com.babymonitor.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babymonitor.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val userRepository = UserRepository()

    // 表单数据
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // UI 状态
    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        // 监听表单变化
        phoneNumber.observeForever { validateForm() }
        password.observeForever { validateForm() }
    }

    private fun validateForm() {
        val phone = phoneNumber.value.orEmpty()
        val pwd = password.value.orEmpty()
        _loginEnable.value = phone.length == 11 && pwd.length >= 6
    }

    fun login() {
        val phone = phoneNumber.value.orEmpty()
        val pwd = password.value.orEmpty()

        if (!validateInput(phone, pwd)) {
            return
        }

        _loading.value = true

        viewModelScope.launch {
            try {
                val result = userRepository.login(phone, pwd)
                _loading.value = false
                _loginSuccess.value = true
            } catch (e: Exception) {
                _loading.value = false
                _errorMessage.value = e.message ?: "登录失败，请重试"
            }
        }
    }

    private fun validateInput(phone: String, password: String): Boolean {
        if (phone.length != 11) {
            _errorMessage.value = "请输入正确的手机号"
            return false
        }
        if (password.length < 6) {
            _errorMessage.value = "密码长度不能少于6位"
            return false
        }
        return true
    }
}