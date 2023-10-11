package com.example.searchpharmacyproject.pharmacy.cache

import com.example.searchpharmacyproject.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate

class RedisTemplateTest extends AbstractIntegrationContainerBaseTest{

    @Autowired
    private RedisTemplate redisTemplate

    def "RedisTemplate String operations"() {
        given:
        def valueOperations = redisTemplate.opsForValue() //Redis DB에서 단일 값을 저장하고 검색
        def key = "stringkey"
        def value = "hello"

        when:
        valueOperations.set(key, value)

        then:
        def result = valueOperations.get(key)
        result == value
    }

    def "RedisTemplate set operations"() {
        given:
        def setOperations = redisTemplate.opsForSet() //Redis Set은 중복 요소가 없는 값의 집합, 주로 집합 연산을 수행할 때 사용
        def key = "setKey"

        when:
        setOperations.add(key, "h", "e", "l", "l", "o") //추가 , remove : 삭제

        then:
        def size = setOperations.size(key)
        size == 4 //set은 중복이 허용되지 않기 때문에 h, e, l, o 이렇게 총 4개
    }

    def "RedisTemplate hash operations"() {
        given:
        def hashOperations = redisTemplate.opsForHash() //필드와 값을 사용하여 연관성 있는 데이터를 저장, 필드를 통해 데이터를 빠르게 조회
        def key = "hashkey"

        when:
        hashOperations.put(key, "subkey", "value") //추가, (myhash, field, value) 형식으로 추가하는 , myhash에 계속 추가, field는 유일해야 함.

        then:
        def result = hashOperations.get(key, "subkey") //조회
        result == "value"

        def entries = hashOperations.entries(key) //hashkey를 맵 형태로 저장
        //맵에 존재하는지
        entries.keySet().contains("subkey")
        entries.values().contains("value")

        def size = hashOperations.size(key)
        size == entries.size()
    }
}