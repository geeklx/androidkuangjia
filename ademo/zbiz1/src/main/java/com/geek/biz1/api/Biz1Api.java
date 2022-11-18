package com.geek.biz1.api;

import com.geek.biz1.bean.HLoginBean;
import com.geek.biz1.bean.HMobid2Bean;
import com.geek.biz1.bean.HTyxs1Bean;
import com.geek.biz1.bean.HUserInfoBean;
import com.geek.biz1.bean.SNew1SearchBean;
import com.geek.biz1.bean.SNewListBean1;
import com.geek.biz1.bean.SPolyvList1Bean;
import com.geek.biz1.bean.SPolyvList1Detail1Bean1;
import com.geek.biz1.bean.SbbdBean;
import com.geek.libutils.libretrofit.ResponseSlbBean;
import com.geek.libutils.libretrofit.ResponseSlbBean1;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Biz1Api {

    // 用户登录
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<HLoginBean>> get_yonghudenglu(@Url String path, @Header("version") String version,
                                                       @Header("imei") String imei, @Header("token") String token, @Body RequestBody body);

    // 退出登录
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<Object>> get_tuichudenglu(@Url String path, @Header("version") String version,
                                                   @Header("imei") String imei, @Header("token") String token);

    // 获取个人资料信息
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<HUserInfoBean>> get_userinfo(@Url String path, @Header("version") String version,
                                                      @Header("imei") String imei, @Header("token") String token);

    // 获取二维码
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST(NetConfig.SERVER_ISERVICE + "liveApp/sendloginVCode2")
    @POST()
    Call<ResponseSlbBean<Object>> get_erweima(@Url String path, @Header("version") String version,
                                              @Header("imei") String imei, @Header("token") String token, @Body RequestBody body);

    // ceshi mob tuisong id
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<Object> get_mob_id(@Url String path, @Body RequestBody body);


    // ceshi mob tuisong id
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<HMobid2Bean> get_mob_id2(@Url String path, @Body RequestBody body);

    // 搜索new1
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<SNew1SearchBean>> get_my_search_new1(@Url String path, @Header("version") String version,
                                                              @Header("imei") String imei, @Header("token") String token, @Body RequestBody body);

    // 搜索new2
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<SNewListBean1>> get_my_search_new2(@Url String path, @Body RequestBody body);

    // 保利威直播分类列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean1<SPolyvList1Bean>> get_polyv_list1(@Url String path,
                                                            @Header("version") String version,
                                                            @Header("imei") String imei,
                                                            @Header("token") String token,
                                                            @Body RequestBody body);

    // 保利威直播分类列表详情
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<SPolyvList1Detail1Bean1>> get_polyvList1Detail1(@Url String path,
                                                                         @Header("version") String version,
                                                                         @Header("imei") String imei,
                                                                         @Header("token") String token,
                                                                         @Body RequestBody body);

    // 保利威直播用户进入统计start
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<Object>> get_PolyvUserIn(@Url String path,
                                                  @Header("version") String version,
                                                  @Header("imei") String imei,
                                                  @Header("token") String token,
                                                  @Header("latitude") String latitude,
                                                  @Header("longitude") String longitude,
                                                  @Body RequestBody body);

    // 保利威直播用户出来统计end
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<Object>> get_PolyvUserOut(@Url String path,
                                                   @Header("version") String version,
                                                   @Header("imei") String imei,
                                                   @Header("token") String token,
                                                   @Header("latitude") String latitude,
                                                   @Header("longitude") String longitude,
                                                   @Body RequestBody body);

    // 保利威直播用户心跳统计
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<Object>> get_PolyvUserHeart(@Url String path,
                                                     @Header("version") String version,
                                                     @Header("imei") String imei,
                                                     @Header("token") String token,
                                                     @Header("latitude") String latitude,
                                                     @Header("longitude") String longitude,
                                                     @Body RequestBody body);

    // 更新版本1
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<SbbdBean>> get_version6(@Url String path);

    // 课程开始学习
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<HTyxs1Bean>> get7(@Url String path, @Body RequestBody body);

    // 课程学习进度记录
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<Object>> get8(@Url String path, @Body RequestBody body);

    // 课程学习完记录
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Call<ResponseSlbBean<Object>> get9(@Url String path, @Body RequestBody body);

}
