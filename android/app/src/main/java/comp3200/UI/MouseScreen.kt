package comp3200.UI

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import com.babymonitor.R
import comp3200.Controllers.Device.connectionListener
import comp3200.Controllers.Mouse
import comp3200.Network.HidUtil
import comp3200.Tracker
import comp3200.UI.Components.ConnectionView
import comp3200.UI.Components.Touchpad
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MouseScreen : AppCompatActivity(), ControllerScreen {
    var TAG: String = "MouseScreen"
    var mouse: Mouse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Tracker.changeContext(this)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        mouse = HidUtil.controller as Mouse
        setContentView(R.layout.mouse)
        setUpUI()
        setUpConnectionUI()
        val btnStart=findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                btnStart.isEnabled=false

                val countStr =
                    (findViewById<View?>(R.id.etInputCount) as AppCompatEditText).getText()
                        .toString()
                val count = countStr.toInt()

                val timeStr = (findViewById<View?>(R.id.etInputTime) as AppCompatEditText).getText()
                    .toString()
                val time = timeStr.toInt()

                val leftClick = findViewById<Button>(R.id.left_mouse_button)

                try {
                    lifecycleScope.launch {
                        for (i in 0..<count) {
                            val randomCount = (Math.random() * 5).toInt()
                            delay(1000L * (randomCount + time))
                            Log.i(TAG, "随机数：" + randomCount+",时间间隔${(randomCount + time)}")
                            Log.i(TAG, "run: 第${i}次点击")
                            leftClick.performClick()

                            val randomCount2 = (Math.random() * 5).toInt()
                            delay(1000L * (randomCount2 + 5))
                            Log.i(TAG, "随机数2：" + randomCount2+",时间间隔${(randomCount2 + 5)}")
                            Log.i(TAG, "run: 第${i}次再次点击")

                            leftClick.performClick()
                        }
                        btnStart.isEnabled=true
                        btnStart.text="任务完成"
                    }
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        })
    }

    var connectionView: ConnectionView? = null

    fun setUpConnectionUI() {
        connectionView = findViewById<ConnectionView>(R.id.mouse_connection)
        mouse!!.setConnectionListener(connectionListener { c: Boolean ->
            connectionView!!.connectionChanged(c)
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setUpUI() {
        val leftClick = findViewById<Button>(R.id.left_mouse_button)
        leftClick.setOnClickListener {
            lifecycleScope.launch {
                mouse!!.click(1, 1)
                delay(500)
                mouse!!.click(1, 0)
            }
        }
        leftClick.setOnTouchListener(OnTouchListener { view: View?, event: MotionEvent? ->
            if (event!!.getAction() == MotionEvent.ACTION_DOWN) { //might need to add ACTION_CANCEL
                mouse!!.click(0, 1)
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mouse!!.click(0, 0)
            }
            Log.i(TAG, "setUpUI: 点击了")
            false
        })

        val rightClick = findViewById<Button>(R.id.right_mouse_button)
        rightClick.setOnTouchListener(OnTouchListener { view: View?, event: MotionEvent? ->
            if (event!!.getAction() == MotionEvent.ACTION_DOWN) {
                mouse!!.click(1, 1)
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mouse!!.click(1, 0)
            }
            false
        })

        val touchpad = findViewById<Touchpad>(R.id.touchpad)
        touchpad.setOnMoveListener(object : Touchpad.OnMoveListener {
            override fun onMove(x: Int, y: Int, lt: Boolean) {
                mouse!!.move(x, y, lt)
            }

            override fun onCLick(lClick: Boolean, rClick: Boolean) {
                mouse!!.click(0, if (lClick) 1 else 0)
                mouse!!.click(1, if (rClick) 1 else 0)
            }

            override fun onScroll(vScroll: Int, hScroll: Int, lt: Boolean) {
                mouse!!.scroll(vScroll, hScroll, lt)
            }

            override fun onZoom(zoom: Int, lt: Boolean) {
                mouse!!.zoom(zoom, lt)
            }
        })
    }
}
