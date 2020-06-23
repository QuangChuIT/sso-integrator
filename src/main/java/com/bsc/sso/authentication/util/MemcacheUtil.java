
package com.bsc.sso.authentication.util;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

public class MemcacheUtil {

    private static MemcacheUtil INSTANCE;

    private MemCachedClient memcacheClient = new MemCachedClient();

    public static final String SERVER_NAME = "memcachedServerUrl";

    static public MemcacheUtil getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new MemcacheUtil();
        }
        return INSTANCE;
    }

    private MemcacheUtil() {
        String serverName = ConfigUtil.getInstance().getProperty(SERVER_NAME);
        SockIOPool pool = SockIOPool.getInstance();
        String[] servers = {serverName};
        pool.setServers(servers);
        pool.setFailover(true);
        pool.setInitConn(10);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        pool.setMaintSleep(30);//
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setAliveCheck(true);

        pool.initialize();
    }

    public static void main(String[] args) {
//		AssetEntryCache entry = new AssetEntryCache();
//		entry.setTitle("abcd");
//		MemcacheUtil.getInstance().set("entry-1", entry);
//		AssetEntryCache entry = (AssetEntryCache)MemcacheUtil.getInstance().get("entry-1");
//		System.out.println(entry.getTitle());
    }

    public Object get(String key) {

        return memcacheClient.get(key);
    }

    public boolean set(String key, Object value) {

        return memcacheClient.set(key, value);
    }

    public boolean delete(String key) {

        return memcacheClient.delete(key);
    }

    public boolean clearAllCache() {

        try {

            if (memcacheClient.flushAll())
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }
}
