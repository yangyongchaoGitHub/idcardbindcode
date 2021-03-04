package com.dataexpo.cbi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dataexpo.cbi.BascActivity;
import com.dataexpo.cbi.MyApplication;
import com.dataexpo.cbi.pojo.NetResult;
import com.dataexpo.cbi.pojo.PdaUserInfo;
import com.dataexpo.cbi.readidcard.DynamicPermission;
import com.dataexpo.cbi.retrofitInf.ApiService;
import com.idata.fastscandemo.R;
import com.idata.ise.scanner.decoder.CamDecodeAPI;
import com.idatachina.imeasuresdk.IMeasureSDK;
import com.ivsign.android.IDCReader.IdentityCard;
import com.yishu.YSNfcCardReader.NfcCardReader;
import com.yishu.util.ByteUtil;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BindIdCard extends BascActivity implements View.OnClickListener {
    private final String TAG = BindIdCard.class.getSimpleName();
    
    private Context mContext;

    Retrofit mRetrofit;

    PdaUserInfo userInfo;

    ImageButton btn_input_login_back;
    TextView tv_user_info_name_value;
    TextView tv_user_info_idcard_value;
    TextView tv_user_info_card_name_value;
    TextView tv_bind;

    private static NfcCardReader nfcCardReaderAPI;
    private MyHandler mHandler;
    private boolean isActive = false;
    private static Intent thisIntent;
    private IMeasureSDK mIMeasureSDK;
    private DynamicPermission dynamicPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_bind_idcard);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userInfo = (PdaUserInfo) bundle.getSerializable("userData");
        }

        initView();

        initData();
        mRetrofit = MyApplication.getmRetrofit();
        //摄像机设置回调


        nfcCardReaderAPI = new NfcCardReader(mHandler,BindIdCard.this);
        mHandler = new MyHandler(BindIdCard.this);
        nfcCardReaderAPI = new NfcCardReader(mHandler,BindIdCard.this);

        //权限判断
        dynamicPermission = new DynamicPermission(BindIdCard.this, new DynamicPermission.PassPermission() {
            @Override
            public void operation() {
            }
        });
        dynamicPermission.getPermissionStart();
        mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCSTART);
        mIMeasureSDK = new IMeasureSDK(getBaseContext());
        mIMeasureSDK.init(initCallback);
    }

    private void initData() {

    }

    private void initView() {
        btn_input_login_back = findViewById(R.id.btn_input_login_back);
        tv_user_info_name_value = findViewById(R.id.tv_user_info_name_value);
        tv_user_info_idcard_value = findViewById(R.id.tv_user_info_idcard_value);
        tv_user_info_card_name_value = findViewById(R.id.tv_user_info_card_name_value);
        tv_bind = findViewById(R.id.tv_bind);

        btn_input_login_back.setOnClickListener(this);
        tv_bind.setOnClickListener(this);

        tv_user_info_name_value.setText(userInfo.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_input_login_back:

                this.finish();
                break;
            case R.id.tv_bind:
                if (!"".equals(tv_user_info_idcard_value.getText().toString())) {
                    //进行绑定
                    bindIdCardToUser();
                } else {
                    Toast.makeText(mContext, "未读取到身份证", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mHandler.getNfcInit()) {
            nfcCardReaderAPI.enabledNFCMessage();
//            thisIntent = getIntent();
//            mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCBUTTON);
//            Log.i(TAG,"onResume to MESSAGE_VALID_NFCBUTTON " );
        }

        isActive = true;

        Intent intent = getIntent();
        String action = intent.getAction();
        Log.i(TAG,"onResume " + action);

        if("android.nfc.action.TECH_DISCOVERED".equals(action)){
            Log.i(TAG,"onResume 1");
            if(thisIntent == null){
//                if(leftViewOperation != null){
//                    leftViewOperation.setMode(2);
//                }
                Log.i(TAG,"onResume 2");
                mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCSTART);
//                cardInfo.clearData();
//                main_tvContent_show.setText("");
                thisIntent = intent;
                mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCBUTTON);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("onPause","enter onPause");
        isActive = false;
    }

    private IMeasureSDK.InitCallback initCallback = new IMeasureSDK.InitCallback() {
        @Override
        public void success() {
            Log.d(TAG, "success: 上电成功");
            //Toast.makeText(getBaseContext(), "上电成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failed(int code, String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "failed: 上电失败，请使用 (T25) 款PDA进行身份证读取 "+msg, Toast.LENGTH_LONG).show();
                }
            });

            Log.d(TAG, "failed: 上电失败，"+msg);
            //Toast.makeText(getBaseContext(), "上电失败[" + msg + "]", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void disconnect() {
            //Toast.makeText(getBaseContext(), "与扫码服务断开", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "disconnect: 与扫码服务断开");
            mIMeasureSDK.reconect();
        }
    };

    private static class MyHandler extends Handler {
        private WeakReference<BindIdCard> activityWeakReference;
        //蓝牙nfc初始化之后不再初始化
        private boolean nfcInit  = false;
        private boolean btInit = false;

        public boolean getNfcInit() {
            return nfcInit;
        }

        public MyHandler(BindIdCard activity){
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final BindIdCard activity = activityWeakReference.get();
            if(activity == null){
                return;
            }

            switch (msg.what){
                case ByteUtil.MESSAGE_VALID_NFCSTART:
                    Log.i("Info","enter MESSAGE_VALID_NFCSTART");
//                    activity.hideBtBtn();
//                    activity.setCurBtName("未连接");
//                    bluetoothReaderAPI.closeBlueTooth();
//
//                    main_btPic_show.setVisibility(View.GONE);
//                    main_nfcPic_show.setVisibility(View.VISIBLE);
//                    main_tvContent_show.setText("");

                    if(nfcInit){
                        break;
                    }

                    Boolean enabledNFC = false;
                    if(activity.isActive){
                        enabledNFC = nfcCardReaderAPI.enabledNFCMessage();
                    }
                    if(enabledNFC){
                        nfcInit = true;
//                        Toast.makeText(activity,"NFC初始化成功",Toast.LENGTH_SHORT).show();
                    }else{
//                        main_btPic_show.setVisibility(View.VISIBLE);
//                        main_nfcPic_show.setVisibility(View.GONE);
                        Toast.makeText(activity,"NFC初始化失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ByteUtil.MESSAGE_VALID_BTSTART:
                    Log.i("Info","enter MESSAGE_VALID_BTSTART");
//                    main_btPic_show.setVisibility(View.VISIBLE);
//                    main_nfcPic_show.setVisibility(View.GONE);
//                    main_tvContent_show.setText("");
//
//                    if(btInit){
//                        break;
//                    }
//                    if(bluetoothReaderAPI.checkBltDevice()){
//                        btInit = true;
////                        Toast.makeText(activity,"蓝牙初始化成功",Toast.LENGTH_SHORT).show();
//                    }else{
//                        main_btPic_show.setVisibility(View.GONE);
//                        main_nfcPic_show.setVisibility(View.VISIBLE);
//                        Toast.makeText(activity,"当前设备无蓝牙或者蓝牙未开启",Toast.LENGTH_SHORT).show();
//                    }

                    break;
                case ByteUtil.BLUETOOTH_CONNECTION_SUCCESS:
                    Log.i("Info","enter BLUETOOTH_CONNECTION_SUCCESS");
//                    activity.showBtBtn();
//
////                    Toast.makeText(activity,"设备连接成功",Toast.LENGTH_SHORT).show();
//                    BluetoothDevice device = (BluetoothDevice) msg.obj;
//                    btdevice = device;
//                    main_tvContent_show.setText("");
//                    activity.setCurBtName(device.getName());
//                    new SPUtil(activity).putConnBtDevice(device.getAddress());
                    break;
                case ByteUtil.BLUETOOTH_CONNECTION_FAILED:
                    Log.i("Info","enter BLUETOOTH_CONNECTION_FAILED");
//                    activity.hideBtBtn();
//                    activity.setCurBtName("未连接");
                    Toast.makeText(activity,"设备连接失败，请重新连接",Toast.LENGTH_SHORT).show();
                    break;
                case ByteUtil.MESSAGE_VALID_NFCBUTTON:
                    Log.i("Info","enter MESSAGE_VALID_NFCBUTTON");
                    //boolean isNFC = nfcCardReaderAPI.isNFC(thisIntent);
                    boolean isNFC = true;
                    if(isNFC){
                        nfcCardReaderAPI.CreateCard(thisIntent);
                    }else{
                        Toast.makeText(activity,"获取nfc失败",Toast.LENGTH_SHORT).show();
                    }
                    thisIntent = null;
                    break;
                case ByteUtil.MESSAGE_VALID_BTBUTTON:
                    Log.i("Info","enter MESSAGE_VALID_BTBUTTON");
//                    activity.btBtnDisabled();
//                    activity.btBtnDisabled();
//                    if(activity.getMode() == 3){
//                        Log.i("Info","enter OTG register");
//                        boolean otgInit = otgCardReaderAPI.registerOTGCard();
//                        if(otgInit){
//                            otgCardReaderAPI.readCard();
//                        }
//                        else {
//                            activity.btBtnEnabled();
//                            Toast.makeText(activity,"OTG初始化失败",Toast.LENGTH_SHORT).show();
//                        }
//                    }else{
//                        bluetoothReaderAPI.readCard();
//                    }
                    break;
                case ByteUtil.READ_CARD_START:
                    Log.i("Info","enter READ_CARD_START");
                    //main_tvContent_show.setText("开始读卡，请稍后...");
                    Toast.makeText(activity,"开始读卡",Toast.LENGTH_SHORT).show();
                    break;
                case ByteUtil.READ_CARD_FAILED:
                    Log.e("Info","enter READ_CARD_FAILED");
                    //activity.btBtnEnabled();

                    /*//显示读卡时间
                    System.out.println("蓝牙读取时间："+bluetoothReaderAPI.getTime());
                    System.out.println("NFC读取时间："+nfcCardReaderAPI.getTime());*/

                    //read failed (NFC)
                    if(78 !=nfcCardReaderAPI.getErrorFlag()){
                        String message = nfcCardReaderAPI.getMessage();

                        //when mode is bluetooth,message is ""
                        if(!("".equals(message))) {
                            //main_tvContent_show.setText("");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

////TODO : delete!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                    activity.tv_bind.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            activity.tv_user_info_idcard_value.setText("452501199205250514");
//                            activity.tv_user_info_card_name_value.setText("阿徐");
//                            activity.bindIdCardToUser();
//                        }
//                    });

                    //Toast.makeText(activity,"读卡失败:"+msg.obj,Toast.LENGTH_SHORT).show();
                    //main_tvContent_show.setText("读卡失败："+msg.obj);
                    break;
                case ByteUtil.READ_CARD_SUCCESS:
                    Log.i("Info","enter READ_CARD_SUCCESS");

                    /*//显示读卡时间
                    System.out.println("蓝牙读取时间："+bluetoothReaderAPI.getTime());
                    System.out.println("NFC读取时间："+nfcCardReaderAPI.getTime());*/

//                    //read failed (NFC)
//                    if(78 !=nfcCardReaderAPI.getErrorFlag()){
//                        String message = nfcCardReaderAPI.getMessage();
//
//                        //when mode is bluetooth,message is ""
//                        if(!("".equals(message))) {
//                            main_tvContent_show.setText("");
//                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//                            break;
//                        }
//                    }

                    //main_tvContent_show.setText("读卡成功！");
                    //在初始化或者输入编码时可以更改
                    //if (activity.mStatus == activity.STATUS_INIT || activity.mStatus == activity.STATUS_INPUT_CODEORID) {
                    Toast.makeText(activity, "读卡成功", Toast.LENGTH_SHORT).show();
                    IdentityCard card = (IdentityCard) msg.obj;
                    if (card != null) {
                        final String name = card.getNameText();
                        final String sex = card.getSexText();
                        final String birthday = card.getBirthdayText();
                        final String nation = card.getMingZuText();
                        final String address = card.getAddressText();
                        final String number = card.getNumberText();
                        final String qianfa = card.getQianfaText();
                        final String effdate = card.getEffectiveDate();
                        Bitmap head = card.getImage();

                        if (head == null) {
                            Toast.makeText(activity, "头像读取失败", Toast.LENGTH_SHORT).show();
                        }

                        final Bitmap personImg = card.getImage();

                        Log.i("Info:", name + "\n" + sex + "\n" + birthday + "\n" + nation + "\n" + address + "\n" + number + "\n" + qianfa + "\n" + effdate + "\n");
                        //activity.setData(card);
                        activity.tv_bind.post(new Runnable() {
                            @Override
                            public void run() {
                                activity.tv_user_info_idcard_value.setText(number);
                                activity.tv_user_info_card_name_value.setText(name);
                                activity.bindIdCardToUser();
                            }
                        });
                    }
                    //}
                    break;
                case ByteUtil.MESSAGE_VALID_OTGSTART:
                    //activity.clearData();

                    Log.i("Info","enter MESSAGE_VALID_OTGSTART");
//                    activity.setCurBtName("未连接");
//                    bluetoothReaderAPI.closeBlueTooth();
//                    main_btPic_show.setVisibility(View.VISIBLE);
//                    main_nfcPic_show.setVisibility(View.GONE);
//                    main_tvContent_show.setText("");
//                    activity.showBtBtn();
                    break;
                case ByteUtil.BTREAD_BUTTON_ENABLED:
                    Log.i("Info","enter BTREAD_BUTTON_ENABLED");
                    //activity.btBtnEnabled();
                    break;
                default:break;
            }
        }
    }

    private void bindIdCardToUser() {
        ApiService apiService = mRetrofit.create(ApiService.class);

        Log.i(TAG, "======== " + tv_user_info_idcard_value.getText().toString());
        Call<NetResult<String>> call = apiService.bindIdCardToUser(userInfo.getUid(),
                tv_user_info_card_name_value.getText().toString(), tv_user_info_idcard_value.getText().toString()
        );

        call.enqueue(new Callback<NetResult<String>>() {
            @Override
            public void onResponse(Call<NetResult<String>> call, Response<NetResult<String>> response) {
                final NetResult<String> result = response.body();
                if (result == null) {
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getCode().equals(200)) {
                            Toast.makeText(mContext, "欢迎光临", Toast.LENGTH_SHORT).show();

                            Intent intent = getIntent();
                            intent.putExtra("position", "1");
                            intent.putExtra("idcard", tv_user_info_idcard_value.getText().toString());
                            setResult(RESULT_OK, intent);
                        }
                        tv_user_info_name_value.setText("");
                        tv_user_info_idcard_value.setText("");
                        tv_user_info_card_name_value.setText("");
                        BindIdCard.this.finish();
                    }
                });
            }

            @Override
            public void onFailure(Call<NetResult<String>> call, Throwable t) {
                Toast.makeText(mContext, "绑定失败, 请联系管理员", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("Info","enter onNewIntent");
        super.onNewIntent(intent);
        thisIntent = intent;
        mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCBUTTON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dynamicPermission.permissionRequestOperation(requestCode, permissions, grantResults);
    }
}
