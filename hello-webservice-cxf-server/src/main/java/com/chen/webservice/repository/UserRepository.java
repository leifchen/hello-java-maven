package com.chen.webservice.repository;

import com.chen.webservice.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * UserRepository
 * <p>
 * @Author LeifChen
 * @Date 2019-05-16
 */
@Repository
public class UserRepository {

    private static final Map<Integer, User> USERS = new HashMap<>();

    @PostConstruct
    public void initData() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("LeifChen");
        user1.setAge(29);
        USERS.put(user1.getId(), user1);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Test");
        user2.setAge(18);
        USERS.put(user2.getId(), user2);
    }

    public User findUser(Integer id) {
        Assert.notNull(id, "The user's id must not be null");
        return USERS.get(id);
    }
}
