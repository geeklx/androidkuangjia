package com.geek.biz1.presenter;

import com.alibaba.fastjson.JSONObject;
import com.fosung.lighthouse.dtsxbb.cache.CacheManager;
import com.geek.biz1.api.Biz2Api;
import com.geek.biz1.bean.FconfigBean;
import com.geek.biz1.bean.FshengjiBean;
import com.geek.biz1.view.Fconfig1View;
import com.geek.libutils.libmvp.Presenter;
import com.fosung.lighthouse.dtsxbb.BuildConfigyewu;
import com.geek.libutils.libretrofit.BanbenUtils;
import com.geek.libutils.libretrofit.ResponseSlbBean2;
import com.fosung.lighthouse.dtsxbb.RetrofitNetNew2;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fconfig1Presenter extends Presenter<Fconfig1View> {

    // 获取第三方服务拼接地址标识 认证 authorized，资源 resource，统一存储 oss
    public void getconfig1(String url, String serverType) {
        JSONObject requestData = new JSONObject();
        requestData.put("serverType", serverType);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestData.toString());
        RetrofitNetNew2.build(Biz2Api.class, getIdentifier()).getconfig1(
                BuildConfigyewu.SERVER_ISERVICE_NEW1 + url + "/getconfig", requestBody).enqueue(new Callback<ResponseSlbBean2<FconfigBean>>() {
            @Override
            public void onResponse(Call<ResponseSlbBean2<FconfigBean>> call, Response<ResponseSlbBean2<FconfigBean>> response) {
                if (!hasView()) {
                    return;
                }
                if (response.body() == null) {
                    // 缓存bufen
                    FconfigBean fbean = new Gson()
                            .fromJson(CacheManager.getInstance()
                                    .get_huancun_data(CacheManager.getInstance()
                                            .huancun_getdata(call.request())), FconfigBean.class);
                    if (fbean != null) {
                        getView().Onconfig2Success(serverType,fbean);
                        call.cancel();
                    }
                    return;
                }
                if (!response.body().isSuccess()) {
                    getView().Onconfig2Nodata(response.body().getMsg());
                    return;
                }
                getView().Onconfig2Success(serverType, response.body().getData());
                call.cancel();
            }

            @Override
            public void onFailure(Call<ResponseSlbBean2<FconfigBean>> call, Throwable t) {
                if (!hasView()) {
                    return;
                }
//                String string = t.toString();
                String string = BanbenUtils.getInstance().net_tips;
                getView().Onconfig2Fail(string);
                call.cancel();
            }
        });
    }

}
