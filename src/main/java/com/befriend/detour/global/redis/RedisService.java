package com.befriend.detour.global.redis;

import org.redisson.api.RedissonClient;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private final RedissonClient redissonClient;

    @Value("${redis.lock.timeout:10}")
    private long lockTimeout;

    @Value("${redis.lock.waitTime:30}")
    private long waitTime;

    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public boolean acquireLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 시도 후 waitTime 초 동안 lock을 획득 대기
            return lock.tryLock(waitTime, lockTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void releaseLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
