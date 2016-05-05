package com.overtech.lenovo.utils;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by Overtech on 16/5/5.
 */
public class SMSCodeCountDownTimer extends CountDownTimer {
    private Button button;

    public SMSCodeCountDownTimer(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public SMSCodeCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setEnabled(false);
        button.setText(millisUntilFinished / 1000 + "秒后重试");
    }

    @Override
    public void onFinish() {
        button.setEnabled(true);
        button.setText("重新获取验证码");
    }
}
