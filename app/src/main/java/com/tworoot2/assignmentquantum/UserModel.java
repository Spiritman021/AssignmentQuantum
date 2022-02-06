package com.tworoot2.assignmentquantum;

public class UserModel {

    private String uid, name,  email,phone, password;

    public UserModel() {

    }

    public UserModel(String uid, String name, String email, String phone, String password) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
