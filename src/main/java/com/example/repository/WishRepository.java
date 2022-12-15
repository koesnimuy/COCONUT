package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Wish;

@Repository
public interface WishRepository extends JpaRepository<Wish,Long>{
    
    public Wish findByCourscode_coursno(Long coursno);

    public List<Wish> findByUserid_id(String id);
}
