package com.example.salarymgt.repo;

import com.example.salarymgt.entity.UserEntity;
import com.example.salarymgt.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        loadData();
    }

    @Test
    void countTestData() {
        assertEquals(3, userRepository.findAll().size());
    }

    @Test
    void findBySalaryRangeOffsetLimit() {
        List<UserEntity> userEntities = userRepository.findBySalaryRangeOffsetLimit(new BigDecimal("1000.00"),
                new BigDecimal("1700.00"), 1, 1);
        assertEquals(1, userEntities.size());
        assertEquals(userRepository.findById("e004"),
                userEntities.stream().filter(e -> e.getId().equals("e004")).collect(Collectors.toList()).get(0));
    }

    @Test
    void findBySalaryRangeOffset() {
        List<UserEntity> userEntities = userRepository.findBySalaryRangeOffset(new BigDecimal("1000.00"),
                new BigDecimal("1700.00"), 1);
        assertEquals(2, userEntities.size());
        assertEquals(userRepository.findById("e004"),
                userEntities.stream().filter(e -> e.getId().equals("e004")).collect(Collectors.toList()).get(0));
        assertEquals(userRepository.findById("e005"), userEntities.stream().filter(e -> e.getId().equals("e005")).collect(Collectors.toList()).get(0));
    }

    @Test
    void findById() throws ParseException {
        assertEquals("ssnape4",userRepository.findById("e004").getLogin());

    }

    @Test
    void findByLogin() {
        assertEquals("e004",userRepository.findByLogin("ssnape4").getId());
    }

    @Test
    @Transactional
    void deleteById() {
        userRepository.deleteById("e004");
        UserEntity userEntity = userRepository.findById("e004");
        assertNull(userEntity);
    }




    void loadData() {
        List<UserEntity> userEntities = new ArrayList<>();
        try {
            userEntities.add(new UserEntity().builder().id("e001").login("hpotter").name("Harry Potter").salary(new BigDecimal("1234.00")).startDate(DateUtil.parse("16-Nov-01")).build());
            userEntities.add(new UserEntity().builder().id("e002").login("rwesley").name("Ron Weasley").salary(new BigDecimal("19234.50")).startDate(DateUtil.parse("2001-11-16")).build());
            userEntities.add(new UserEntity().builder().id("e003").login("ssnape").name("Severus Snape").salary(new BigDecimal("4000.0")).startDate(DateUtil.parse("17-Nov-01")).build());
            userEntities.add(new UserEntity().builder().id("e004").login("ssnape4").name("Severus Snape 4").salary(new BigDecimal("1500.0")).startDate(DateUtil.parse("17-Nov-01")).build());
            userEntities.add(new UserEntity().builder().id("e005").login("ssnape5").name("Severus Snape 5").salary(new BigDecimal("1600.0")).startDate(DateUtil.parse("17-Nov-01")).build());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userRepository.saveAll(userEntities);
    }
}