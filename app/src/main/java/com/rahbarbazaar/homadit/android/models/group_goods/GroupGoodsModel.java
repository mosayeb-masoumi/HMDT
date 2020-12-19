package com.rahbarbazaar.homadit.android.models.group_goods;

public class GroupGoodsModel {

    private String title;
    private String id;
    private String icon;
    private boolean checked;

    public GroupGoodsModel(String title, String id, String icon, boolean checked) {
        this.title = title;
        this.id = id;
        this.icon = icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
