package com.whzl.mengbi.model.entity.js;

/**
 * @author nobody
 * @date 2018/9/29
 */
public class LoginStateBean {
    public String loginState;

    public LoginStateBean(String loginState) {
        this.loginState = loginState;
    }

    @Override
    public String toString() {
        return "LoginStateBean{" +
                "loginState=\"" + loginState + '\"' +
                '}';
    }
}
