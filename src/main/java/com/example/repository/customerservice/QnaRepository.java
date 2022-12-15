package com.example.repository.customerservice;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Member;
import com.example.entity.Qna;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
    
    List<Qna> findByMember(Member member);
    
    List<Qna> findByTypeContainingOrderByQnanoDesc(String type, PageRequest pageRequest);

    int countByTypeContaining(String type);
}
