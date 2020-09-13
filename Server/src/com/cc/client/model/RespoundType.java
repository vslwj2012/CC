package com.cc.client.model;

import java.io.Serializable;

public enum RespoundType implements Serializable {
    REPEAT,//用户已存在
    REGISTERSUCCESS,//注册成功
    RELOGIN,//重复登陆
    DISLOGIN,//账号密码错误
    LOGINSUCCESS,//登陆成功
}
