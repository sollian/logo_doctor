package com.sollian.ld.business;

import android.text.TextUtils;

/**
 * Created by sollian on 2015/9/16.
 */
public class LDResponse<T> {
    private String errorMsg;
    private T obj;

    public LDResponse() {
    }

    public LDResponse(T obj, String errorMsg) {
        this.obj = obj;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public boolean success() {
        return TextUtils.isEmpty(errorMsg);
    }
}
