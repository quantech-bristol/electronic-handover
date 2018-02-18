package com.quantech.model.user;

public class ChangePassword {

    private String initial;

    private String confirm;

    private String current;

    public ChangePassword() {
        this.initial = "";
        this.confirm = "";
        this.current = "";
    }

    public ChangePassword(String initial, String confirm, String current) {
        this.initial = initial;
        this.confirm = confirm;
        this.current = current;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}

