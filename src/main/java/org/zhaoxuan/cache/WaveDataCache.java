package org.zhaoxuan.cache;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.util.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WaveDataCache {

    private final Map<String, byte[]> CACHE = new ConcurrentHashMap<>();

    public void add(String key, byte[] data) {
        byte[] bytes = CACHE.get(key);
        if (ObjectUtils.isEmpty(bytes)) {
            bytes = data;
            CACHE.put(key, bytes);
            return;
        }
        bytes = ArrayUtil.addAll(bytes, data);
        CACHE.put(key, bytes);
    }

    @Nullable
    public byte[] get(String key) {
        return CACHE.get(key);
    }

    public void remove(String key) {
        CACHE.remove(key);
    }

}
