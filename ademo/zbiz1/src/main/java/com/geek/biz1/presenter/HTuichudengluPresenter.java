package com.geek.biz1.presenter;


import com.geek.biz1.api.Biz1Api;
import com.geek.biz1.view.HTuichudengluView;
import com.fosung.lighthouse.dtsxbb.BuildConfigyewu;
import com.geek.libutils.libmvp.Presenter;
import com.geek.libutils.libretrofit.BanbenUtils;
import com.geek.libutils.libretrofit.ResponseSlbBean;
import com.fosung.lighthouse.dtsxbb.RetrofitNetNew2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HTuichudengluPresenter extends Presenter<HTuichudengluView> {

    public void get_tuichudenglu() {
        // NetConfig.SERVER_ISERVICE + "liveApp/logout"
        RetrofitNetNew2.build(Biz1Api.class, getIdentifier()).get_tuichudenglu(BuildConfigyewu.SERVER_ISERVICE_NEW1 + "liveApp/logout",
                BanbenUtils.getInstance().getVersion(),
                BanbenUtils.getInstance().getImei(),
                "").enqueue(new Callback<ResponseSlbBean<Object>>() {
            @Override
            public void onResponse(Call<ResponseSlbBean<Object>> call, Response<ResponseSlbBean<Object>> response) {
                if (!hasView()) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() != 0) {
                    getView().OnTuichudengluNodata(response.body().getMsg());
                    return;
                }
                getView().OnTuichudengluSuccess(response.body().getMsg());
                call.cancel();
            }

            @Override
            public void onFailure(Call<ResponseSlbBean<Object>> call, Throwable t) {
                if (!hasView()) {
                    return;
                }
//                String string = t.toString();
                String string = BanbenUtils.getInstance().net_tips;
                getView().OnTuichudengluFail(string);
                call.cancel();
            }
        });

    }

}
