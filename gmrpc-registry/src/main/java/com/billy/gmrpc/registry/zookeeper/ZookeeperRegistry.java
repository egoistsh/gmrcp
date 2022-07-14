package com.billy.gmrpc.registry.zookeeper;

import com.billy.gmrpc.registry.AbstractRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ZookeeperRegistry extends AbstractRegistry {
    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final int SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRIES = 2;

    private Map<String, List<String>> serviceProviderMap = new HashMap<>();
    private CuratorFramework curatorFramework;

    @Override
    protected void init(String registryAddress) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(registryAddress)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES))
                .build();
        curatorFramework.start();
    }

    @Override
    public void register(String providerAddress, String service) {
        try {
            String servicePath = FOLDER + SEPARATOR + service;
            Stat stat = curatorFramework.checkExists().forPath(servicePath);
            if (stat == null) {
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            String provider = servicePath + SEPARATOR + providerAddress;
            curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(provider);
            log.info("provider:{} is registered to {}", providerAddress, servicePath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected List<String> lookup(String service) {
        try {
            String path = FOLDER + SEPARATOR + service;
            if (serviceProviderMap.containsKey(service)) {
                return serviceProviderMap.get(service);
            }
            List<String> providers = curatorFramework.getChildren().forPath(path);
            serviceProviderMap.put(service, providers);
            watchProvider(service);
            return providers;
        } catch (Exception e) {
            log.error("call ZookeeperRegistry.discover occur exception, service:{}, error message:{}", service, e.getMessage());
            return null;
        }
    }

    private void watchProvider(String service) {
        String path = FOLDER + SEPARATOR + service;
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                //如果provider有更新，则更新到内存中
                serviceProviderMap.put(service, curatorFramework.getChildren().forPath(path));
            }
        };
        childrenCache.getListenable().addListener(listener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
