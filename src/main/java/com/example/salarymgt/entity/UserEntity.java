package com.example.salarymgt.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Jiang Wensi on 14/12/2020
 */
@Data
@Entity(name = "user")
@Builder
public class UserEntity {

    @Id
    private String id;
    @Column(nullable = false,unique = true)
    private String login;
    @Column(nullable = false)
    private String name;
    @Column(precision = 10,scale = 2, nullable = false)
    private BigDecimal salary;
    @Column(nullable = false)
    private Date startDate;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp lastModifiedAt;

    public UserEntity(String id, String login, String name, BigDecimal salary, Date startDate, Timestamp createdAt, Timestamp lastModifiedAt) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public UserEntity() {
    }
}
