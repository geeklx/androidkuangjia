package com.geek.biz1.presenter;

import com.alibaba.fastjson.JSONObject;
import com.geek.biz1.api.Biz1Api;
import com.geek.biz1.bean.SPolyvList1Bean;
import com.geek.biz1.view.HPolyvList1View;
import com.fosung.lighthouse.dtsxbb.BuildConfigyewu;
import com.geek.libutils.libmvp.Presenter;
import com.geek.libutils.libretrofit.BanbenUtils;
import com.geek.libutils.libretrofit.ResponseSlbBean1;
import com.fosung.lighthouse.dtsxbb.RetrofitNetNew2;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HPolyvList1Presenter extends Presenter<HPolyvList1View> {

    public void get_polyv_list1(String status,String systemId,String pageNum,String pageSize) {
        JSONObject requestData = new JSONObject();
        requestData.put("status", status);//
        requestData.put("systemId", systemId);//
        requestData.put("pageNum", pageNum);//
        requestData.put("pageSize", pageSize);//
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), requestData.toString());
        RetrofitNetNew2.build(Biz1Api.class, getIdentifier()).get_polyv_list1(
                BuildConfigyewu.SERVER_ISERVICE_NEW1 + "/api/live/viewer/live/record/getLiveByStatus",
                BanbenUtils.getInstance().getVersion(),
                BanbenUtils.getInstance().getImei(),
                "", requestBody).enqueue(new Callback<ResponseSlbBean1<SPolyvList1Bean>>() {
            @Override
            public void onResponse(Call<ResponseSlbBean1<SPolyvList1Bean>> call, Response<ResponseSlbBean1<SPolyvList1Bean>> response) {
                if (!hasView()) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                if (!response.body().isSuccess()) {
                    getView().OnPolyvList1Nodata(response.body().getMsg());
                    return;
                }
                getView().OnPolyvList1Success(response.body().getResult());
                call.cancel();
            }

            @Override
            public void onFailure(Call<ResponseSlbBean1<SPolyvList1Bean>> call, Throwable t) {
                if (!hasView()) {
                    return;
                }
//                String string = t.toString();
                String string = BanbenUtils.getInstance().net_tips;
                getView().OnPolyvList1Fail(string);
                call.cancel();
            }
        });

    }

}
