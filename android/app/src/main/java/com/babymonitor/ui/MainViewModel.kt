package com.babymonitor.ui

import com.babymonitor.base.BaseViewModel

class MainViewModel : BaseViewModel() {
    fun login(token: String) {
        request(
            { },
            { response ->

            },
            onError = { exception ->

            }
        )
    }
}