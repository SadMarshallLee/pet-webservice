package com.pet.webservice.repository;

import com.pet.webservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* create repositories for any class and using Hibernate for this */
/* interface extends methods by JpaRepository for work with DB */
/* create some generics with object type which i wanted to get from DB and id type (Long) */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

/* Optional help avoid a NullPointException (absence of object)
if someone tries to find user which absence in DB, Optional return Optional object
with option to try availability object in DB */

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);
}
