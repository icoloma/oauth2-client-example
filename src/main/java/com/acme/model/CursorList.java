package com.acme.model;

import java.util.List;

/**
 * A paginated list of data
 */
public class CursorList<T> {

    private List<T> data;

    private String cursor;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
