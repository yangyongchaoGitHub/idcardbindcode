package com.dataexpo.cbi.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dataexpo.cbi.BascActivity;
import com.dataexpo.cbi.MyApplication;
import com.dataexpo.cbi.pojo.NetResult;
import com.dataexpo.cbi.pojo.PdaUserInfo;
import com.dataexpo.cbi.retrofitInf.ApiService;
import com.idata.fastscandemo.R;
import com.idata.ise.scanner.decoder.CamDecodeAPI;
import com.idata.ise.scanner.decoder.DecodeResult;
import com.idata.ise.scanner.decoder.DecodeResultListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.dataexpo.cbi.common.Utils.INPUT_SUCCESS;
import static com.dataexpo.cbi.common.Utils.checkInput;

public class ScanCode extends BascActivity implements DecodeResultListener, View.OnClickListener {
    private final String TAG = ScanCode.class.getSimpleName();

    private EditText et_qrcode;
    private TextView tv_query;
    private TextView tv_title;
    private ImageButton btn_input_login_back;
    private ImageView iv_success;

    private Context mContext;

    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    private SoundPool soundPool;

    Retrofit mRetrofit;

    private int scaning = 0;

    private String expo_id;
    private String expoName;

    private final int SHOW_INIT = 0;
    private final int SHOW_ING = 1;
    private final int SHOW_WORKING = 2;

    //显示成功控制
    private volatile int showInt = SHOW_INIT;
    private volatile long showStartTime = System.currentTimeMillis();
    private volatile int running = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_scan_code);
        initView();
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        soundMap.put(1, soundPool.load(mContext, R.raw.rescan, 1)); //播放的声音文件
        initData();
        mRetrofit = MyApplication.getmRetrofit();
        //摄像机设置回调
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            expo_id = bundle.getString("Expo_id");
            expoName = bundle.getString("expo_name");
        }
        Log.i(TAG, "expo_id:" + expo_id);
        CamDecodeAPI.getInstance(mContext)
                .SetOnDecodeListener(this);
        tv_title.setText(expoName);

        running = 0;
        TimerThread timerThread = new TimerThread();
        timerThread.start();
    }

    private void initData() {

    }

    private void initView() {
        et_qrcode = findViewById(R.id.et_qrcode);
        tv_query = findViewById(R.id.tv_login_query);
        btn_input_login_back = findViewById(R.id.btn_input_login_back);
        tv_title = findViewById(R.id.tv_input_login_title);
        iv_success = findViewById(R.id.iv_success);

        tv_query.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //确认按钮按下
        if (600==keyCode||601==keyCode||602==keyCode) {
            scaning = 1;
            CamDecodeAPI.getInstance(mContext).ScanBarcode(mContext);
        }

        return super.onKeyDown(keyCode, event);
    }

    //摄像头返回
    @Override
    public void onDecodeResult(DecodeResult decodeResult) {
        scaning = 0;
        //restCardValue();
        if (null != decodeResult){
            //扫码完成返回
            //manager.playSoundAndVibrate(true, false);
            String code = new String(decodeResult.getBarcodeData());

            if (INPUT_SUCCESS == checkInput(code)) {
                et_qrcode.setText(code);

                //进行查询
                queryUserInfo(code);
            } else {
                Toast.makeText(this, "扫描内容异常", Toast.LENGTH_SHORT).show();
            }
        }else {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_query:
                if (!"".equals(et_qrcode.getText().toString())) {
                    queryUserInfo(et_qrcode.getText().toString());
                } else {
                    Toast.makeText(mContext, "请先扫码", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void queryUserInfo(String code) {
        if (showInt == SHOW_WORKING) {
            return;
        }
        showInt = SHOW_WORKING;
        ApiService apiService = mRetrofit.create(ApiService.class);

        Call<NetResult<PdaUserInfo>> call = apiService.queryUserInfo(Integer.parseInt(expo_id), code);

        call.enqueue(new Callback<NetResult<PdaUserInfo>>() {
            @Override
            public void onResponse(Call<NetResult<PdaUserInfo>> call, Response<NetResult<PdaUserInfo>> response) {
                final NetResult<PdaUserInfo> result = response.body();
                if (result == null) {
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PdaUserInfo userInfo = result.getData();

                        if (userInfo == null || userInfo.getUid() == null) {
                            //根据二维码查询到的数据是空的, 跳转到绑定界面
                            Toast.makeText(mContext, "人员未注册！！！", Toast.LENGTH_SHORT).show();

                        } else if (userInfo.getIdcard() == null ||
                                "".equals(userInfo.getIdcard())){
                            //有用户数据，但是没有绑定身份证
                            //跳转到绑定身份证界面
                            Intent intent = new Intent();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("userData", userInfo);
                            intent.putExtras(bundle);
                            intent.setClass(mContext, BindIdCard.class);

                            startActivityForResult(intent, 0);
                        } else {
                            // 有数据，身份证也绑定了
                            //提示直接进场
                            Toast.makeText(mContext, "欢迎光临", Toast.LENGTH_SHORT).show();
                            showStartTime = System.currentTimeMillis();
                            iv_success.setVisibility(View.VISIBLE);
                            et_qrcode.setText("");
                        }
                        et_qrcode.setText("");
                        et_qrcode.requestFocus();
                        showInt = SHOW_ING;
                    }
                });
            }

            @Override
            public void onFailure(Call<NetResult<PdaUserInfo>> call, Throwable t) {
                Log.i(TAG, "onFailure" + t.toString());
                showInt = SHOW_ING;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_qrcode.setText("");
                        et_qrcode.requestFocus();
                        Toast.makeText(mContext, "查询数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult " + requestCode + " | " +resultCode);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String position = data.getStringExtra("position");
            if ("1".equals(position)) {
                showStartTime = System.currentTimeMillis();
                iv_success.setVisibility(View.VISIBLE);
            }
        }
    }

    class TimerThread extends Thread {
        @Override
        public void run() {
            while (running == 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //如果在显示，那么就在3秒钟后回到等待界面
                if (showInt == SHOW_ING) {
                    if (System.currentTimeMillis() - showStartTime > 2000) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_success.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
            running = 2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", + running + " onResume");
        if (running == 2) {
            running = 0;
            TimerThread timerThread = new TimerThread();
            timerThread.start();
        }
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop!!!!!!!!!!!!");
        running = 1;
        super.onStop();
    }
}
