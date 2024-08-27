package com.example.tm.repository;

import com.example.tm.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment getCommentById(Long id);
    Comment findFirstByOrderByIdDesc();
    List<Comment> findCommentsByTaskId(Long id, Pageable pageable);
}
