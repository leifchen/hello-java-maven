package com.chen.webservice.service;

import com.chen.webservice.entity.User;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * UserService接口
 * <p>
 * @Author LeifChen
 * @Date 2019-05-16
 */
@WebService(targetNamespace = "http://service.webservice.chen.com")
public interface UserService {

    /**
     * 获取用户
     * @param id
     * @return
     */
    @WebMethod
    User getUser(@WebParam(name = "id") Integer id);
}
