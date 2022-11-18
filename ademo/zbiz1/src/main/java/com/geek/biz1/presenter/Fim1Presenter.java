package com.geek.biz1.presenter;

import com.alibaba.fastjson.JSONObject;
import com.fosung.lighthouse.dtsxbb.BuildConfigyewu;
import com.geek.biz1.api.Biz2Api;
import com.geek.biz1.bean.FImBean;
import com.geek.biz1.view.Fim1View;
import com.geek.libutils.libmvp.Presenter;
import com.geek.libutils.libretrofit.BanbenUtils;
import com.geek.libutils.libretrofit.ResponseSlbBean2;
import com.fosung.lighthouse.dtsxbb.RetrofitNetNew2;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fim1Presenter extends Presenter<Fim1View> {

    public void getim1(String url,String userId,String userName) {
        JSONObject requestData = new JSONObject();
        requestData.put("userId", userId);//
        requestData.put("userName", userName);//
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestData.toString());
        RetrofitNetNew2.build(Biz2Api.class, getIdentifier()).get_im1(
                BuildConfigyewu.SERVER_ISERVICE_NEW1 + url + "",
                requestBody).enqueue(new Callback<ResponseSlbBean2<FImBean>>() {
            @Override
            public void onResponse(Call<ResponseSlbBean2<FImBean>> call, Response<ResponseSlbBean2<FImBean>> response) {
                if (!hasView()) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (!response.body().isSuccess()) {
                    getView().OnFim1Nodata(response.body().getMsg());
                    return;
                }
                getView().OnFim1Success(response.body().getData());
                call.cancel();
            }

            @Override
            public void onFailure(Call<ResponseSlbBean2<FImBean>> call, Throwable t) {
                if (!hasView()) {
                    return;
                }
//                String string = t.toString();
                String string = BanbenUtils.getInstance().net_tips;
                getView().OnFim1Fail(string);
                call.cancel();
            }
        });
    }
}
