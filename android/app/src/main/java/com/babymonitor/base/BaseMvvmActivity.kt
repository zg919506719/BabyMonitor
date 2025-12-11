package com.babymonitor.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseMvvmActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
        initView()
        initData()
    }

    @Suppress("UNCHECKED_CAST")
    private fun createBinding(): VB {
        val type = javaClass.genericSuperclass as ParameterizedType
        val bindingClass = type.actualTypeArguments[0] as Class<VB>
        val method = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java
        )
        return method.invoke(null, layoutInflater) as VB
    }

    abstract fun initView()
    abstract fun initData()

}