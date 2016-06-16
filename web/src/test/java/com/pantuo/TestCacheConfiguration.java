package com.pantuo;

import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tliu on 9/3/14.
 */
@Configuration
@EnableCaching(mode= AdviceMode.ASPECTJ)
//@EnableCaching
public class TestCacheConfiguration {

    @Bean
    public SimpleCacheManager cacheManager(){
        SimpleCacheManager cacheManager = new SimpleCacheManager() {
            @Override
            public Cache getCache(String name) {
                Cache c = super.getCache(name);
                if (c == null) {
                    addCache(cacheBean(name).getObject());
                    c = super.getCache(name);
                }
                return c;
            }
        };
        List<Cache> caches = new ArrayList<Cache>();
        caches.add(cacheBean("default").getObject());
        cacheManager.setCaches(caches );
        return cacheManager;
    }

    public ConcurrentMapCacheFactoryBean cacheBean(String name){
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName(name);
        cacheFactoryBean.setStore(new ConcurrentHashMap<Object, Object>() {
            @Override
            public Object get(Object key) {
                System.out.println("TestCache: get (" + key + ")");
                return super.get(key);
            }

            @Override
            public Object put(Object key, Object value) {
                System.out.println("TestCache: put (" + key + ", " + value + ")");
                return super.put(key, value);
            }

            @Override
            public boolean remove(Object key, Object value) {
                System.out.println("TestCache: remove (" + key + ", " + value + ")");
                return super.remove(key, value);
            }

            @Override
            public Object remove(Object key) {
                System.out.println("TestCache: remove (" + key + ")");
                return super.remove(key);
            }

            @Override
            public boolean replace(Object key, Object oldValue, Object newValue) {
                System.out.println("TestCache: replace (" + key + ", " + oldValue + ", " + newValue + ")");
                return super.replace(key, oldValue, newValue);
            }

            @Override
            public Object replace(Object key, Object value) {
                System.out.println("TestCache: replace (" + key + ", " + value + ")");
                return super.replace(key, value);
            }

            @Override
            public Object putIfAbsent(Object key, Object value) {
                System.out.println("TestCache: putIfAbsent (" + key + ", " + value + ")");
                return super.putIfAbsent(key, value);
            }

            @Override
            public void putAll(Map<?, ?> m) {
                System.out.println("TestCache: putAll (" + m + ")");
                super.putAll(m);
            }

            @Override
            public void clear() {
                System.out.println("TestCache: clear ()");
                super.clear();
            }
        });
        cacheFactoryBean.afterPropertiesSet();
        return cacheFactoryBean;
    }
}
