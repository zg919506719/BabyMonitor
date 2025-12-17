package comp3200.UI;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.babymonitor.R;

import java.util.concurrent.Executors;

import comp3200.Controllers.Mouse;
import comp3200.Network.HidUtil;
import comp3200.Tracker;
import comp3200.UI.Components.ConnectionView;
import comp3200.UI.Components.Touchpad;

public class MouseScreen extends AppCompatActivity implements ControllerScreen {
    String TAG = "MouseScreen";
    Mouse mouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tracker.changeContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mouse = (Mouse) HidUtil.controller;
        setContentView(R.layout.mouse);
        setUpUI();
        setUpConnectionUI();

        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String countStr = ((AppCompatEditText) findViewById(R.id.etInputCount)).getText().toString();
                int count = Integer.parseInt(countStr);

                String timeStr = ((AppCompatEditText) findViewById(R.id.etInputTime)).getText().toString();
                int time = Integer.parseInt(timeStr);

                Button leftClick = findViewById(R.id.left_mouse_button);

                try {
                    Executors.callable(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < count; i++) {
                                int randomCount = (int) (Math.random() * 10);
                                Log.i(TAG, "run: randomCount" + randomCount);
                                SystemClock.sleep(1000L * (randomCount + time));
                                Log.i(TAG, "run: 第一次点击");
                                SystemClock.sleep(1000L * randomCount);
                                Log.i(TAG, "run: 第二次点击");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        leftClick.performClick();
                                    }
                                });
                            }
                        }
                    }).call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    ConnectionView connectionView;

    void setUpConnectionUI() {
        connectionView = findViewById(R.id.mouse_connection);
        mouse.setConnectionListener(c -> {
            connectionView.connectionChanged(c);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setUpUI() {
        Button leftClick = findViewById(R.id.left_mouse_button);
        leftClick.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { //might need to add ACTION_CANCEL
                mouse.click(0, 1);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mouse.click(0, 0);
            }
            Log.i(TAG, "setUpUI: 点击了");
            return false;
        });

        Button rightClick = findViewById(R.id.right_mouse_button);
        rightClick.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mouse.click(1, 1);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mouse.click(1, 0);
            }
            return false;
        });

        Touchpad touchpad = findViewById(R.id.touchpad);
        touchpad.setOnMoveListener(new Touchpad.OnMoveListener() {
            @Override
            public void onMove(int x, int y, boolean lt) {
                mouse.move(x, y, lt);
            }

            @Override
            public void onCLick(boolean lClick, boolean rClick) {
                mouse.click(0, lClick ? 1 : 0);
                mouse.click(1, rClick ? 1 : 0);
            }

            @Override
            public void onScroll(int vScroll, int hScroll, boolean lt) {
                mouse.scroll(vScroll, hScroll, lt);
            }

            @Override
            public void onZoom(int zoom, boolean lt) {
                mouse.zoom(zoom, lt);
            }
        });

    }


}
