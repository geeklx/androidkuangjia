package com.example.bizyewu2.view;

import com.example.bizyewu2.bean.HUserInfoBean;
import com.geek.libutils.libmvp.IView;

public interface HUserInfoView extends IView {

    void OnUserInfoSuccess(HUserInfoBean bean);

    void OnUserInfoNodata(String bean);

    void OnUserInfoFail(String msg);

}
