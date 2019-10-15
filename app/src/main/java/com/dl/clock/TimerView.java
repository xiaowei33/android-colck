package com.dl.clock;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class TimerView extends LinearLayout {

    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;

    private int allTimerCount = 0;
    private Timer timer=new Timer();
    private TimerTask timerTask = null;
    private Button btnStart,btnPause,btnResume,btnReset;
    private EditText etHour,etMin,etSec;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stopTimer();

                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
            }
        });
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stopTimer();

                etHour.setText("0");
                etMin.setText("0");
                etSec.setText("0");

                btnReset.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });
        btnResume = findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startTimer();

                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimer();

                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });


        etHour = findViewById(R.id.etHour);
        etMin = findViewById(R.id.etMin);
        etSec = findViewById(R.id.etSec);

        etHour.setText("00");
        etHour.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value>59) {
                        etHour.setText("59");
                    }else if (value<0) {
                        etHour.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etMin.setText("00");
        etMin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value>59) {
                        etMin.setText("59");
                    }else if (value<0) {
                        etMin.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etSec.setText("00");
        etSec.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());

                    if (value>59) {
                        etSec.setText("59");
                    }else if (value<0) {
                        etSec.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);

    }

    private void checkToEnableBtnStart(){
        btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText())&&
                Integer.parseInt(etHour.getText().toString())>0)||
                (!TextUtils.isEmpty(etMin.getText())&&
                        Integer.parseInt(etMin.getText().toString())>0)||
                (!TextUtils.isEmpty(etSec.getText())&&
                        Integer.parseInt(etSec.getText().toString())>0));
    }


    private void startTimer(){
        if (timerTask==null) {
            allTimerCount = Integer.parseInt(etHour.getText().toString())*60*60+
                    Integer.parseInt(etMin.getText().toString())*60+
                    Integer.parseInt(etSec.getText().toString());
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    allTimerCount--;

                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);

                    if (allTimerCount<=0) {
                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopTimer();
                    }
                }
            };

            timer.schedule(timerTask, 1000, 1000);
        }
    }

    private void stopTimer(){
        if (timerTask!=null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_TIME_TICK:

                    int hour = allTimerCount/60/60;
                    int min = (allTimerCount/60)%60;
                    int sec = allTimerCount%60;

                    etHour.setText(hour+"");
                    etMin.setText(min+"");
                    etSec.setText(sec+"");

                    break;
                case MSG_WHAT_TIME_IS_UP:
                    new AlertDialog.Builder(getContext())
                            .setTitle("Time is up")
                            .setMessage("Time is up")
                            .setNegativeButton("Cancel", null)
                            .show();

                    btnReset.setVisibility(View.GONE);
                    btnResume.setVisibility(View.GONE);
                    btnPause.setVisibility(View.GONE);
                    btnStart.setVisibility(View.VISIBLE);

                    break;
                default:
                    break;
            }
        };
    };
}
