package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    
    public List<Comment> findByCourscode_coursnoOrderByCommentnoDesc(Long coursno, Pageable pageable);

    public int countByCourscode_coursno(Long coursno);

    public List<Comment> findByUserid_idOrderByCommentnoDesc(String userid , Pageable pageable);

    public int countByUserid_id(String userid);
}
