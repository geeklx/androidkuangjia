package com.example.bizyewu2.view;

import com.geek.libutils.libmvp.IView;

public interface HPolyvUserHeartView extends IView {

    void OnPolyvUserHeartSuccess(String bean);

    void OnPolyvUserHeartNodata(String bean);

    void OnPolyvUserHeartFail(String msg);

}
