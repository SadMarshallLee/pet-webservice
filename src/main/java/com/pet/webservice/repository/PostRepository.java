package com.pet.webservice.repository;

import com.pet.webservice.entity.Post;
import com.pet.webservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /* return all posts for concrete user */
    /* Hibernate make: SELECT * FROM POST as p WHERE User='user SORT DESC'*/
    List<Post> findAllByUserOrderByCreatedDateDesc(User user);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndUser(Long id, User user);
}
