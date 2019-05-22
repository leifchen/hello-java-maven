package com.chen.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * HelloService接口
 * <p>
 * @Author LeifChen
 * @Date 2019-05-22
 */
@WebService(targetNamespace = "http://service.webservice.chen.com")
public interface HelloService {

    /**
     * hello
     * @param msg
     * @return
     */
    @WebMethod
    String hello(@WebParam(name = "msg") String msg);
}
