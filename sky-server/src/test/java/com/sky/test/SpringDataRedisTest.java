package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
        //string数据操作
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //hash类型的数据操作
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        //list类型的数据操作
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        //set类型数据操作
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        //zset类型数据操作
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     * 操作字符串类型的数据
     */
    @Test
    public void testString() {
        // set get setex setnx
        redisTemplate.opsForValue().set("name", "小明");
        String city = (String) redisTemplate.opsForValue().get("name");
        System.out.println(city);
        redisTemplate.opsForValue().set("code", "1234", 3, TimeUnit.MINUTES);
        redisTemplate.opsForValue().setIfAbsent("lock", "1");
        redisTemplate.opsForValue().setIfAbsent("lock", "2");
    }

    /**
     * 操作哈希类型的数据
     */
    @Test
    public void testHash() {
        //hset hget hdel hkeys hvals
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        hashOperations.put("100", "name", "tom");
        hashOperations.put("100", "age", "20");

        String name = (String) hashOperations.get("100", "name");
        System.out.println(name);

        Set<Object> keys = hashOperations.keys("100");
        System.out.println(keys);

        List<Object> values = hashOperations.values("100");
        System.out.println(values);

        hashOperations.delete("100", "age");
    }

    /**
     * 操作列表类型的数据
     */
    @Test
    public void testList(){
        //lpush lrange rpop llen
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();

        listOperations.leftPushAll("mylist", new String[]{"a"}, new String[]{"b"}, new String[]{"c"});
        listOperations.leftPush("mylist","d");

        List<Object> mylist = listOperations.range("mylist", 0, -1);
        System.out.println(mylist);

        listOperations.rightPop("mylist");

        Long size = listOperations.size("mylist");
        System.out.println(size);
    }

    /**
     * 操作集合类型的数据
     */
    @Test
    public void testSet(){
        //sadd smembers scard sinter sunion srem
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();

        setOperations.add("set1", new String[]{"a"}, new String[]{"b"}, new String[]{"c"}, new String[]{"d"});
        setOperations.add("set2", new String[]{"a"}, new String[]{"b"}, new String[]{"x"}, new String[]{"y"});

        Set<Object> members = setOperations.members("set1");
        System.out.println(members);

        Long size = setOperations.size("set1");
        System.out.println(size);

        Set<Object> intersect = setOperations.intersect("set1", "set2");
        System.out.println(intersect);

        Set<Object> union = setOperations.union("set1", "set2");
        System.out.println(union);

        setOperations.remove("set1","a","b");
    }

    /**
     * 操作有序集合类型的数据
     */
    @Test
    public void testZSet(){
        //zadd zrange zincrby zrem
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        zSetOperations.add("zSet1","a",10);
        zSetOperations.add("zSet1","b",12);
        zSetOperations.add("zSet1","c",9);

        Set<Object> zSet1 = zSetOperations.range("zSet1", 0, -1);
        System.out.println(zSet1);

        zSetOperations.incrementScore("zSet1","c",10);

        zSetOperations.remove("zSet1","a","b");
    }

    /**
     * 通用命令操作
     */
    @Test
    public void testCommon(){
        //keys exists type del
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);

        Boolean name = redisTemplate.hasKey("name");
        Boolean set1 = redisTemplate.hasKey("set1");

        for (String key : keys) {
            DataType type = redisTemplate.type(key);
            System.out.println(type.name());
        }

        redisTemplate.delete("mylist");
    }
}
