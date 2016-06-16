package com.pantuo.dao;

import com.pantuo.dao.pojo.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by tliu on 8/12/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoBeanConfiguration.class})
public class DaoTest {

    @Autowired
    private UserDetailRepository userRepo;

    @Before
    public void before() {
        userRepo.deleteAll();
    }

    @Test
    public void testUser() {
        UserDetail u1 = new UserDetail("username1", "password1", "first1", "last1", "email1");
        UserDetail u2 = new UserDetail("username2", "password2", "first2", "last2", "email2");
        userRepo.save(u1);
        userRepo.save(u2);

        Iterable<UserDetail> users = userRepo.findAll();
        ArrayList<UserDetail> userList = new ArrayList<UserDetail> ();
        for (UserDetail user : users) {
            userList.add(user);
        }
        Assert.assertEquals(2, userList.size());

        UserDetail violation = new UserDetail("username1", "password1", "first1", "last1", "email1");
        try {
            userRepo.save(violation);
            Assert.fail("Should violate the constraint");
        } catch (DataIntegrityViolationException e) {

        }
    }


}
