package com.pantuo.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by tliu on 8/12/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/beans-test.xml"})
@ImportResource("classpath:/beans-test.xml")
public class JasyptTest {

    @Autowired
    private StandardPBEStringEncryptor encryptor;

    @Value("${password}")
    private String password;

    @Before
    public void before() {
    }

    @Test
    public void testEncrypt() {
        System.out.println(encryptor.encrypt("abcdef"));
    }

    @Test
    public void testDecrypt() {
        System.out.println(password);
    }
}
