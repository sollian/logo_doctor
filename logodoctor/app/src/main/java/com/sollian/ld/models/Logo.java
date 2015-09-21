package com.sollian.ld.models;

import com.sollian.ld.business.net.NetManager;
import com.sollian.ld.utils.pinyinutils.IPinYin;

/**
 * Created by sollian on 2015/9/18.
 */
public class Logo implements IPinYin, IWrapImg {
    private String id;
    private String name;
    private String img;
    private String category;
    private String extra;
    private String desc;
    private String sortLetters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    @Override
    public String getWrappedImg() {
        return NetManager.BASE_URL + getImg();
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public String getSortedLetters() {
        return sortLetters;
    }
}
