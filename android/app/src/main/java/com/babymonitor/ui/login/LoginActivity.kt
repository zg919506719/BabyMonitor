package com.babymonitor.ui.login

import android.content.Intent
import androidx.activity.viewModels
import com.babymonitor.base.BaseMvvmActivity
import com.babymonitor.databinding.ActivityLoginBinding
import com.babymonitor.ui.MainActivity
import com.babymonitor.ui.register.RegisterActivity
import com.babymonitor.utils.ToastUtils

class LoginActivity : BaseMvvmActivity<ActivityLoginBinding>() {
    val viewModel: LoginViewModel by viewModels()

    override fun initView() {
        setupClickListeners()
    }

    override fun initData() {
        setupObservers()
        checkAutoLogin()
    }

    private fun setupClickListeners() {
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }
    }

    private fun setupObservers() {
        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                navigateToMain()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                ToastUtils.show(message)
            }
        }
    }

    private fun checkAutoLogin() {
        // 简单检查是否已登录
        if (viewModel.userRepository.isLoggedIn()) {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}