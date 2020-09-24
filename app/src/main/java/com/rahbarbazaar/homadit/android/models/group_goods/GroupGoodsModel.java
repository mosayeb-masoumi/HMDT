package com.rahbarbazaar.homadit.android.models.group_goods;

public class GroupGoodsModel {

    private String title;
    private String id;
    private boolean checked;

    public GroupGoodsModel(String title, String id, boolean checked) {
        this.title = title;
        this.id = id;
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
