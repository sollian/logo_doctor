package com.sollian.ld.models;

import com.sollian.ld.business.net.NetManager;

/**
 * Created by sollian on 2015/9/21.
 */
public class History implements IWrapImg {
    private String id;
    private String img;
    private String logoId;
    private boolean isRead;
    private boolean isProcessing;
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(boolean isProcessing) {
        this.isProcessing = isProcessing;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getWrappedImg() {
        return NetManager.BASE_URL + getImg();
    }
}
