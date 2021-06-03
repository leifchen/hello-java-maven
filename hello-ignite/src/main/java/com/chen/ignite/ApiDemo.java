package com.chen.ignite;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 缓存操作
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-06-03
 */
public class ApiDemo {

    public static void main(String[] args) throws Exception {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient client = Ignition.startClient(cfg)) {
            keyValueApi(client);
            sqlApi(client);
        }
    }

    /**
     * 键-值 API
     *
     * @throws Exception
     */
    private static void keyValueApi(IgniteClient client) {
        ClientCacheConfiguration cacheCfg = new ClientCacheConfiguration().setName("References")
                .setCacheMode(CacheMode.REPLICATED)
                .setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        ClientCache<Integer, String> cache = client.getOrCreateCache(cacheCfg);

        Map<Integer, String> data = IntStream.rangeClosed(1, 100).boxed()
                .collect(Collectors.toMap(i -> i, Object::toString));

        cache.putAll(data);

        assert !cache.replace(1, "2", "3");
        assert "1".equals(cache.get(1));
        assert cache.replace(1, "1", "3");
        assert "3".equals(cache.get(1));

        cache.put(101, "101");

        cache.removeAll(data.keySet());
        assert cache.size() == 1;
        assert "101".equals(cache.get(101));

        cache.removeAll();
        assert 0 == cache.size();
    }

    /**
     * SQL API
     *
     * @param client
     */
    private static void sqlApi(IgniteClient client) {
        client.query(new SqlFieldsQuery(String.format(
                "CREATE TABLE IF NOT EXISTS Person (id INT PRIMARY KEY, name VARCHAR) WITH \"VALUE_TYPE=%s\"",
                Person.class.getName())).setSchema("PUBLIC")).getAll();

        int key = 1;
        Person val = new Person(key, "LeifChen");

        client.query(new SqlFieldsQuery("INSERT INTO Person(id, name) VALUES(?, ?)").setArgs(val.getId(), val.getName())
                .setSchema("PUBLIC")).getAll();

        FieldsQueryCursor<List<?>> cursor = client
                .query(new SqlFieldsQuery("SELECT name from Person WHERE id=?").setArgs(key).setSchema("PUBLIC"));

        List<List<?>> results = cursor.getAll();
        results.stream().findFirst().ifPresent(columns -> {
            System.out.println("name = " + columns.get(0));
        });
    }
}
