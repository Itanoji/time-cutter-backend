package com.itanoji.time.cutter.repository;

import com.itanoji.time.cutter.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByLogin(String login);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);
}
