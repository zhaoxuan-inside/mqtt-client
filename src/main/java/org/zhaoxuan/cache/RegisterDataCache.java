package org.zhaoxuan.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegisterDataCache {

    private final Map<String, byte[]> CACHE = new ConcurrentHashMap<>();



}
