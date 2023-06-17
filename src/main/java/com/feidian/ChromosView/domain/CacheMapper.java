package com.feidian.ChromosView.domain;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class CacheMapper<K,V> extends ConcurrentHashMap<K,V> {
    public V getObject(K key){
        return this.get(key);
    }
    public V addObject(K key,V value){
        return super.put(key, value);
    }
    public V deleteObject(K key){
        return super.remove(key);
    }

    public V updateObject(K key, V value) {
        return super.replace(key,value);
    }
}
