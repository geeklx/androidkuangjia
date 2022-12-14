package com.geek.biz1.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fosung.lighthouse.dtsxbb.BuildConfigyewu;
import com.fosung.lighthouse.dtsxbb.RetrofitNetNew2;
import com.fosung.lighthouse.dtsxbb.cache.CacheManager;
import com.geek.biz1.api.Biz2Api;
import com.geek.biz1.bean.FinitBean;
import com.geek.biz1.view.FinitView;
import com.geek.libutils.libmvp.Presenter;
import com.geek.libutils.libretrofit.BanbenUtils;
import com.geek.libutils.libretrofit.ResponseSlbBean2;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinitPresenter extends Presenter<FinitView> {

    public void getinit(String url) {
//        JSONObject requestData = new JSONObject();
//        requestData.put("configkey", configkey);//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestData.toString());
        RetrofitNetNew2.build(Biz2Api.class, getIdentifier()).getinit(
                BuildConfigyewu.SERVER_ISERVICE_NEW1 + url + "").enqueue(new Callback<ResponseSlbBean2<FinitBean>>() {
            @Override
            public void onResponse(Call<ResponseSlbBean2<FinitBean>> call, Response<ResponseSlbBean2<FinitBean>> response) {
                if (!hasView()) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (!response.body().isSuccess()) {
                    getView().OnFinitNodata(response.body().getMsg());
                    return;
                }
                getView().OnFinitSuccess(response.body().getData());
                call.cancel();
            }

            @Override
            public void onFailure(Call<ResponseSlbBean2<FinitBean>> call, Throwable t) {
                ToastUtils.showLong("111111111");
                if (!hasView()) {
                    return;
                }
                ToastUtils.showLong("222222");
                if (NetworkUtils.isConnected()) {
//                String string = t.toString();
                    String string = BanbenUtils.getInstance().net_tips;
                    getView().OnFinitFail(string);
                    call.cancel();
                    return;
                }
                ToastUtils.showLong("3333333");
                Request request = call.request();
                String url = request.url().toString();
                RequestBody requestBody = request.body();
                Charset charset = Charset.forName("UTF-8");
                StringBuilder sb = new StringBuilder();
                sb.append(url);
                if (request.method().equals("POST")) {
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(Charset.forName("UTF-8"));
                    }
                    Buffer buffer = new Buffer();
                    try {
                        requestBody.writeTo(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sb.append(buffer.readString(charset));
                    buffer.close();
                }
                //
                String cache = CacheManager.getInstance().getCache(sb.toString());
                Log.d(CacheManager.TAG, "get cache->" + cache);
                if (!TextUtils.isEmpty(cache)) {
                    ResponseSlbBean2 obj = new Gson().fromJson(cache, ResponseSlbBean2.class);
                    if (obj != null) {
                        getView().OnFinitSuccess((FinitBean) obj.getData());
                        call.cancel();
                        return;
                    }
                }
//                String string = BanbenUtils.getInstance().net_tips;
//                getView().OnFinitFail(string);
//                call.cancel();
            }
        });
    }
}
