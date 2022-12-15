package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.ReComment;

@Repository
public interface ReCommentRepository extends JpaRepository<ReComment,Long>{
 
    public List<ReComment> findByCommentno_commentnoOrderByRecommentnoAsc(Long commentno, Pageable pageable);

    public int countByCommentno_commentno(Long commentno);
}
