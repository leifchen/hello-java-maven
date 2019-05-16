package com.chen.webservice.service.impl;

import com.chen.webservice.entity.User;
import com.chen.webservice.repository.UserRepository;
import com.chen.webservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

/**
 * UserService的实现类
 * <p>
 * @Author LeifChen
 * @Date 2019-05-16
 */
@Service("userService")
@WebService(serviceName = "UserService",
        targetNamespace = "http://service.webservice.chen.com",
        endpointInterface = "com.chen.webservice.service.UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(Integer id) {
        return userRepository.findUser(id);
    }
}
