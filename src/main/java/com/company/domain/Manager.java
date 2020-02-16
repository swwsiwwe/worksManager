package com.company.domain;

import java.io.Serializable;

public class Manager implements Serializable {
    private String id="root";
    private String password="root";
    public String getPassword() {
        return password;
    }
}
