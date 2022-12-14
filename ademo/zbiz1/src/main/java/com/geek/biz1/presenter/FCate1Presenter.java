package com.geek.biz1.presenter;

import com.alibaba.fastjson.JSONObject;
import com.fosung.lighthouse.dtsxbb.cache.CacheManager;
import com.geek.biz1.api.Biz2Api;
import com.geek.biz1.bean.BjyyBeanYewu4;
import com.geek.biz1.bean.FgrxxBean;
import com.geek.biz1.view.FCate1View;
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

public class FCate1Presenter extends Presenter<FCate1View> {

    public void getcate1list(String url, String id, String limit, String page, String categoryType, String cityName, String categoryCode) {
        JSONObject requestData = new JSONObject();
        requestData.put("id", id);
        requestData.put("pageSize", limit);
        requestData.put("pageNum", page);
        requestData.put("categoryType", categoryType);
        requestData.put("cityName", cityName);
        requestData.put("categoryCode", categoryCode);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                , requestData.toString());
        RetrofitNetNew2.build(Biz2Api.class, getIdentifier()).getcate1(
                BuildConfigyewu.SERVER_ISERVICE_NEW1 + url,
                requestBody)
                .enqueue(new Callback<ResponseSlbBean2<BjyyBeanYewu4>>() {
                    @Override
                    public void onResponse(Call<ResponseSlbBean2<BjyyBeanYewu4>> call, Response<ResponseSlbBean2<BjyyBeanYewu4>> response) {
                        if (!hasView()) {
                            return;
                        }
                        if (response.body() == null) {
                            // 缓存bufen
                            BjyyBeanYewu4 fbean = new Gson()
                                    .fromJson(CacheManager.getInstance()
                                            .get_huancun_data(CacheManager.getInstance()
                                                    .huancun_getdata(call.request())), BjyyBeanYewu4.class);
                            if (fbean != null) {
                                getView().OnCate1Success(categoryType,fbean);
                                call.cancel();
                            }
                            return;
                        }
                        if (!response.body().isSuccess()) {
                            getView().OnCate1Nodata(response.body().getMsg());
                            return;
                        }
                        getView().OnCate1Success(categoryType, response.body().getData());
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<ResponseSlbBean2<BjyyBeanYewu4>> call, Throwable t) {
                        if (!hasView()) {
                            return;
                        }
//                String string = t.toString();
                        String string = BanbenUtils.getInstance().net_tips;
                        getView().OnCate1Fail(string);
                        call.cancel();
                    }
                });

    }


}
