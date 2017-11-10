package com.ajq.aijieqian102.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ToolItem implements Serializable {
    private int ImageId;
    private String Text;

    public ToolItem() {
    }

    public ToolItem(int imageId, String text) {
        ImageId = imageId;
        Text = text;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }


    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

}
