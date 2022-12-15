package com.example.repository.customerservice;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
    
    List<Faq> findByOrderByFaqnoDesc(PageRequest pageRequest);
}
