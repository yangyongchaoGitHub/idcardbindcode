package com.dataexpo.cbi.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dataexpo.cbi.BascActivity;
import com.idata.fastscandemo.R;
import com.idata.ise.scanner.decoder.CamDecodeAPI;
import com.idata.ise.scanner.decoder.DecodeResult;
import com.idata.ise.scanner.decoder.DecodeResultListener;

import java.util.HashMap;

public class ScanCode extends BascActivity implements DecodeResultListener, View.OnClickListener {
    private final String TAG = ScanCode.class.getSimpleName();

    private EditText et_scan;
    private TextView tv_query;
    private ImageButton btn_input_login_back;

    private long exitTime = System.currentTimeMillis();
    private int exitCount = 0;

    private Context mContext;

    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_scan_code);
        initView();
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        soundMap.put(1, soundPool.load(mContext, R.raw.rescan, 1)); //播放的声音文件
        initData();
        CamDecodeAPI.getInstance(mContext)
                .SetOnDecodeListener(this);
    }

    private void initData() {

    }

    private void initView() {
        et_scan = findViewById(R.id.et_a_code);
        tv_query = findViewById(R.id.tv_login_query);
        btn_input_login_back = findViewById(R.id.btn_input_login_back);

        tv_query.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Log.i(TAG, "onKeyDown  status: " + mStatus + " --- " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (System.currentTimeMillis() - exitTime < 2000) {
                if (++exitCount == 3) {
                    exitTime = System.currentTimeMillis();
                    ScanCode.this.finish();
                }
            } else {
                exitTime = System.currentTimeMillis();
                exitCount = 0;
            }
            return false;
        }

        //确认按钮按下
        if (600==keyCode||601==keyCode||602==keyCode) {
            CamDecodeAPI.getInstance(mContext).ScanBarcode(mContext);
        }

        return super.onKeyDown(keyCode, event);
    }

    //摄像头返回
    @Override
    public void onDecodeResult(DecodeResult decodeResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_query:
                break;
            default:
        }
    }
}
