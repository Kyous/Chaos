package com.chaos.utils;

/**
 * Created by mac on 8/10/15.
 */
public class ReportType {

    public String id;

    public String name;

    public ReportType(String _Value,String _ID) {
        id = _ID;
        name = _Value;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}