package com.example.snsapi.repository;

import com.example.snsapi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findByUserId(UUID id, Pageable pageable);

    @Query("SELECT t FROM Task t JOIN FETCH t.user")
    Page<Post> findAllWithUser(Pageable pageable);

    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") UUID id);
}
