package com.ecronin.multithreading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecronin.multithreading.entity.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {


}
