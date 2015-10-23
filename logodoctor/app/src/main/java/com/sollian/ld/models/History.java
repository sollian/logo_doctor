package com.sollian.ld.models;

import android.text.TextUtils;

import com.sollian.ld.business.net.NetManager;

/**
 * Created by sollian on 2015/9/21.
 */
public class History implements IWrapImg {
    private String id;
    private String userName;
    private String img;
    private String logoId;
    /**
     * 0未读；1已读
     */
    private byte read;
    /**
     * 0处理中；1已处理
     */
    private byte processing;
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLogoId() {
        return logoId;
    }

    public void setLogoId(String logoId) {
        this.logoId = logoId;
    }

    public byte getRead() {
        return read;
    }

    public void setRead(byte read) {
        this.read = read;
    }

    public byte getProcessing() {
        return processing;
    }

    public void setProcessing(byte processing) {
        this.processing = processing;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 重要性由大到小
     * isProcessing,isSuccess,isRead
     */
    public boolean isProcessing() {
        return getProcessing() == 1;
    }

    public boolean isSuccess() {
        if (TextUtils.isEmpty(getLogoId())) {
            return false;
        }
        try {
            return Long.parseLong(getLogoId()) > 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isRead() {
        return getRead() == 1;
    }

    @Override
    public String getWrappedImg() {
        return NetManager.BASE_URL + getImg();
    }
}
