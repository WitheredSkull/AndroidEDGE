package com.daniel.edge;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import com.shuanglu.edge.View.Banner.TextBanner.Model.TextBannerAdapter;

/**
 * 创建人 Daniel
 * 时间   2018/11/12.
 * 简介   xxx
 */
public class DemoJava {
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
    private TextBannerAdapter adapter;

    class a<T> extends AsyncTask<T, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(T... ts) {
            return null;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 2000);
        }
    };
}
