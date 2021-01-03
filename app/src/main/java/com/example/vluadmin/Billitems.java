package com.example.vluadmin;

public class Billitems {

    private String _cvUser;
    private String _cvDate;

    public Billitems(String cvUser, String cvDate){
        _cvUser = cvUser;
        _cvDate = cvDate;
    }

    public String get_cvUser() {
        return _cvUser;
    }

    public String get_cvDate() {
        return _cvDate;
    }
}
