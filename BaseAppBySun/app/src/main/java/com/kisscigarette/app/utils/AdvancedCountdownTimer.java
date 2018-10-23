package com.kisscigarette.app.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * 倒计时定时任务
 *
 * @author DaHui
 */
@SuppressLint("HandlerLeak")
public abstract class AdvancedCountdownTimer
{
    private static final int MSG_RUN = 1;
    private static final int MSG_PAUSE = 2;
    private int mCountdownInterval;
    private int mTotalTime;
    private int mRemainTime;
    private MHandler mHandler;

    public AdvancedCountdownTimer()
    {
        mHandler = new MHandler();
    }

    /**
     * 设置倒计时长和间隔时间
     *
     * @param millisInFuture    倒计时长 秒
     * @param countDownInterval 时间间隔 毫秒
     */
    public void setMills(int millisInFuture, int countDownInterval)
    {
        mTotalTime = millisInFuture;
        mCountdownInterval = countDownInterval;
        mRemainTime = millisInFuture;
    }

    /**
     * 取消计数
     */
    public final void cancel()
    {

        if(mHandler!=null){
            mHandler.stop();
            mHandler = null;
        }

    }

    /**
     * 恢复计数
     */
    public final void resume()
    {
        mHandler.removeMessages(MSG_PAUSE);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
    }

    /**
     * 暂停计数
     */
    public final void pause()
    {
        mHandler.removeMessages(MSG_RUN);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));
    }

    /**
     * 开始计数 线程安全
     *
     * @return
     */
    public synchronized final AdvancedCountdownTimer start()
    {
        if (mHandler == null)
        {
            mHandler = new MHandler();
        }
        if (mRemainTime <= 0)
        {
            onFinish();
            return this;
        }
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN), mCountdownInterval);
        return this;
    }

    /**
     * 计数抽象函数，选择重写
     *
     * @param millisUntilFinished 剩余秒数
     * @param percent             剩余百分比
     */
    public abstract void onTick(int millisUntilFinished, int percent);

    /**
     * 结束抽象函数，选择重写
     */
    public abstract void onFinish();

    /**
     * 计数处理器
     *
     * @author DaHui
     */
    class MHandler extends Handler
    {
        private boolean isStop = false;

        public void stop()
        {
            isStop = true;
        }

        @Override
        public void handleMessage(Message msg)
        {
            synchronized (AdvancedCountdownTimer.this)
            {
                if (!isStop)
                {
                    if (msg.what == MSG_RUN)
                    {
                        mRemainTime--;
                        if (mRemainTime <= 0)
                        {
                            onFinish();
                        } else
                        {
                            onTick(mRemainTime, Long.valueOf(100 * (mTotalTime - mRemainTime) / mTotalTime).intValue());
                            sendMessageDelayed(obtainMessage(MSG_RUN), mCountdownInterval);
                        }
                    } else if (msg.what == MSG_PAUSE)
                    {

                    }
                } else
                {
                    removeMessages(MSG_RUN);
                    removeMessages(MSG_PAUSE);
                }
            }
        }
    }
}
