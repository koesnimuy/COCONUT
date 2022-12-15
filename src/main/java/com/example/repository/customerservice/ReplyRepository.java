package com.example.repository.customerservice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Qna;
import com.example.entity.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    
    List<Reply> findByQna(Qna qna);
}
