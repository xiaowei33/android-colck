package com.dl.clock;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class TimeView extends LinearLayout {
    private TextView tvTime;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTime = findViewById(R.id.tvTime);
        tvTime.setText("Hello");

        timerHandler.sendEmptyMessage(0);
    }

    private android.os.Handler timerHandler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            refreshTime();

            if (getVisibility()== View.VISIBLE) {
                timerHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility==View.VISIBLE) {
            timerHandler.sendEmptyMessage(0);
        }else{
            timerHandler.removeMessages(0);
        }
    }

    private void refreshTime(){
        Calendar c = Calendar.getInstance();

        tvTime.setText(String.format("%d:%d:%d",
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND)));
    }
}
