package com.example.salarymgt.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void findBySalaryRangeOffsetLimit() {
        //todo
    }

    @Test
    void findBySalaryRangeOffset() {
        //todo
    }

    @Test
    void findById() {
        //todo
    }

    @Test
    void deleteById() {
        //todo
    }

    @Test
    void findByLogin() {
        //todo
    }
}