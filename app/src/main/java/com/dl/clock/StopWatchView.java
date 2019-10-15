package com.dl.clock;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StopWatchView extends LinearLayout {

    private int tenMSecs = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private TimerTask showTimeTask = null;

    private TextView tvHour,tvMin,tvSec,tvMSec;
    private Button btnStart,btnResume,btnReset,btnPause,btnLap;
    private ListView lvTimeList;
    private ArrayAdapter<String> adapter;

    private static final int MSG_WHAT_SHOW_TIME = 1;

    public StopWatchView(Context context) {
        super(context);
    }

    public StopWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StopWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvHour = findViewById(R.id.timeHour);
        tvHour.setText("0");
        tvMin = findViewById(R.id.timeMin);
        tvMin.setText("0");
        tvSec = findViewById(R.id.timeSec);
        tvSec.setText("0");
        tvMSec = findViewById(R.id.timeMSec);
        tvMSec.setText("0");

        btnLap = findViewById(R.id.btnSWLap);
        btnLap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adapter.insert(String.format("%d:%d:%d.%d",
                        tenMSecs/100/60/60,tenMSecs/100/60%60,
                        tenMSecs/100%60,tenMSecs%100), 0);
            }
        });
        btnPause = findViewById(R.id.btnSWPause);
        btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stopTimer();

                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.GONE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });
        btnReset = findViewById(R.id.btnSWReset);
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stopTimer();
                tenMSecs = 0;
                adapter.clear();

                btnLap.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnReset.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });
        btnResume = findViewById(R.id.btnSWResume);
        btnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startTimer();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.GONE);
                btnLap.setVisibility(View.VISIBLE);
            }
        });
        btnStart = findViewById(R.id.btnSWStart);
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimer();

                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.VISIBLE);
            }
        });
        btnLap.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);

        lvTimeList= findViewById(R.id.lvWatchTimeList);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        lvTimeList.setAdapter(adapter);

        showTimeTask = new TimerTask() {

            @Override
            public void run() {
                hander.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }
        };
        timer.schedule(showTimeTask, 200, 200);

    }

    private void startTimer(){
        if (timerTask==null) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    tenMSecs++;
                }
            };
            timer.schedule(timerTask, 10, 10);
        }
    }

    private void stopTimer(){
        if (timerTask!=null) {
            timerTask.cancel();
            timerTask=null;
        }
    }

    private Handler hander = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(tenMSecs/100/60/60+"");
                    tvMin.setText(tenMSecs/100/60%60+"");
                    tvSec.setText(tenMSecs/100%60+"");
                    tvMSec.setText(tenMSecs%100+"");
                    break;
                default:
                    break;
            }
        };
    };

    public void onDestory() {
        timer.cancel();
    }


}
