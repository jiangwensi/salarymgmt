package com.example.salarymgt.repo;

import com.example.salarymgt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jiang Wensi on 14/12/2020
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(nativeQuery = true,
            value = "select * from user " +
                    "where salary>=:minSalary and salary<:maxSalary " +
                    "order by id asc " +
                    "limit :limit " +
                    "offset:offset")
    List<UserEntity> findBySalaryRangeOffsetLimit(@Param("minSalary") BigDecimal minSalary,
                                                  @Param("maxSalary") BigDecimal maxSalary,
                                                  @Param("offset") Integer offset,
                                                  @Param("limit") Integer limit);

    @Query(nativeQuery = true,
            value = "select * from user " +
                    "where salary>=:minSalary and salary<:maxSalary " +
                    "order by id asc " +
                    "limit 18446744073709551615  " +
                    "offset:offset")
    List<UserEntity> findBySalaryRangeOffset(@Param("minSalary") BigDecimal minSalary,
                                                  @Param("maxSalary") BigDecimal maxSalary,
                                                  @Param("offset") Integer offset);
    UserEntity findById(String id);
    void deleteById(String id);
    UserEntity findByLogin(String login);
}
