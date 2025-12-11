package com.babymonitor.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sakubersama.common.network.bean.AppException
import com.example.sakubersama.common.network.bean.ExceptionHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {
    //11111
    val showDialog by lazy { MutableLiveData<String>() }

    //11
    val dismissDialog by lazy { MutableLiveData<Boolean>() }

    fun <T> request(
        block: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (AppException) -> Unit = { /* 默认错误处理 */ },
        isShowDialog: Boolean = true,
    ) {
        viewModelScope.launch {
            // 显示加载
            if (isShowDialog) showDialog.postValue("加载中...")

            try {
                val result = withContext(Dispatchers.IO) { block() }
                if (isShowDialog) dismissDialog.postValue(false)
                onSuccess(result)
            } catch (e: Exception) {
                if (isShowDialog) dismissDialog.postValue(false)
                val exception = ExceptionHandle.handleException(e)
                Log.e("BaseViewModel", e.message?:"")
                onError(exception)
            }
        }
    }
}