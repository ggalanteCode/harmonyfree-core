package com.generation153.harmonyfree.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation153.harmonyfree.core.entity.User;

public interface UserRepository  extends JpaRepository<User , Long> {

}
