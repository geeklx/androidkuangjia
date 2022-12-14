package com.geek.biz1.presenter;

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

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinitPresenter extends Presenter<FinitView> {

    public void getinit(String url) {
//        JSONObject requestData = new JSONObject();
//        requestData.put("configkey", configkey);//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestData.toString());
        RetrofitNetNew2.build(Biz2Api.class, getIdentifier()).getinit(BuildConfigyewu.SERVER_ISERVICE_NEW1 + url + "").enqueue(new Callback<ResponseSlbBean2<FinitBean>>() {
            @Override
            public void onResponse(Call<ResponseSlbBean2<FinitBean>> call, Response<ResponseSlbBean2<FinitBean>> response) {
                if (!hasView()) {
                    return;
                }
                if (response.body() == null) {
//                    // huancun
//                    Request request = call.request();
//                    String url = request.url().toString();
//                    RequestBody requestBody = request.body();
//                    Charset charset = Charset.forName("UTF-8");
//                    StringBuilder sb = new StringBuilder();
//                    sb.append(url);
//                    if (request.method().equals("POST")) {
//                        MediaType contentType = requestBody.contentType();
//                        if (contentType != null) {
//                            charset = contentType.charset(Charset.forName("UTF-8"));
//                        }
//                        Buffer buffer = new Buffer();
//                        try {
//                            requestBody.writeTo(buffer);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        sb.append(buffer.readString(charset));
//                        buffer.close();
//                    }
//                    String cache = "";
//                    //
//                    if (!TextUtils.isEmpty(cache)) {
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(cache);
//                            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
//                            FinitBean finitBean = new Gson().fromJson(jsonObject1.toString(), FinitBean.class);
//                            if (finitBean != null) {
//                                getView().OnFinitSuccess(finitBean);
//                                call.cancel();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
                    // 缓存bufen
                    FinitBean finitBean = new Gson().fromJson(CacheManager.getInstance().get_huancun_data(CacheManager.getInstance().huancun_getdata(call.request())), FinitBean.class);
                    if (finitBean != null) {
                        getView().OnFinitSuccess(finitBean);
                        call.cancel();
                    }
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
                if (!hasView()) {
                    return;
                }
//                String string = t.toString();
                String string = BanbenUtils.getInstance().net_tips;
                getView().OnFinitFail(string);
                call.cancel();
//                if (NetworkUtils.isConnected()) {
////                String string = t.toString();
//                    String string = BanbenUtils.getInstance().net_tips;
//                    getView().OnFinitFail(string);
//                    call.cancel();
//                    return;
//                }

//                String string = BanbenUtils.getInstance().net_tips;
//                getView().OnFinitFail(string);
//                call.cancel();
            }
        });
    }
}
