package com.training.springbootbuyitem.repository.jpa;

import com.training.springbootbuyitem.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE lower(u.username) = ?1")
    User findUserByUsername(String username);

    User findByUsername(String userName);

    long countByUsername(String userName);

}
