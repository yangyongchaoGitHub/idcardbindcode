package com.dataexpo.cbi.retrofitInf;


import com.dataexpo.cbi.pojo.ExpoInfo;
import com.dataexpo.cbi.pojo.NetResult;
import com.dataexpo.cbi.pojo.PdaUserInfo;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.dataexpo.cbi.retrofitInf.URLs.*;

public interface ApiService {
    //获取项目数据
    @GET(VerifyExpo)
    Call<NetResult<ExpoInfo>> verifyExpo(@Query("expoId") int expoId);

    @GET(queryUserInfoUrl)
    Call<NetResult<PdaUserInfo>> queryUserInfo(@Query("expoId") int expoId, @Query("code") String code);

    @GET(bindIdCardToUserUrl)
    Call<NetResult<String>> bindIdCardToUser(@Query("uid") int uid, @Query("name") String name,
                                                  @Query("idCard") String idCard);


    @POST(uploadOneSignInUrl)
    Call<NetResult<String>> uploadOneSignIn(@Query("eucode") String eucode, @Query("time") Date time,
                                            @Query("deviceKey") String deviceKey, @Query("expoId") Integer expoId,
                                            @Query("address") String address);

//    @GET(bomDeviceUrl)
//    Call<NetResult<List<Device>>> getBomDevice(@Query("bomId") int bomId);
//
//    @GET(bomFindDeviceInfoUrl)
//    Call<NetResult<Device>> queryDeviceInfo(@Query("code") String code);
//
//    @POST(addDeviceInBomUrl)
//    Call<NetResult<String>> addDeviceInBom(@Body BomDeviceVo bomDeviceVo);
//
//    @Deprecated
//    @GET(bomFindDeviceInfoByRfidUrl)
//    Call<NetResult<Device>> queryDeviceInfoByRfid(@Query("rfid") String rfid);
//
//    @POST(deleteBomDeviceUrl)
//    Call<NetResult<String>> deleteBomDevice(@Body BomDeviceVo bomDeviceVo);
//
//    @GET(deviceInfoUrl)
//    Call<NetResult<List<DeviceUsingInfo>>> getDeviceInfo(@Query("code") String code, @Query("type") Integer type);
//
//    @POST(addBomSeriesUrl)
//    Call<NetResult<String>> addBomSeries(@Body BomSeriesVo bomSeriesVo);
//
//    @GET(deleteBomSeriesUrl)
//    Call<NetResult<String>> deleteBomSeries(@Query("bsId") Integer dsId);
//
//    @POST(addInHomeUrl)
//    Call<NetResult<String>> addInHome(@Body BomDeviceVo bomDeviceVo);
}
