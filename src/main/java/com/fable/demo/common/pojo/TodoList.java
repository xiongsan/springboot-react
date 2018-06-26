package com.fable.demo.common.pojo;

import java.io.Serializable;

/**
 * Created by Wanghairui on 2017/6/9.
 */
public class TodoList implements Serializable {
    private String id;
    private String title;
    private String checked;
    private String sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
