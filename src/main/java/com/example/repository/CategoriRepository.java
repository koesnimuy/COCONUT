package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.InterestCategori;

@Repository
public interface CategoriRepository extends JpaRepository<InterestCategori, Long>{
    
    public List<InterestCategori> findByName(String name);

    public List<InterestCategori> findByCourscode_coursno(Long courscode);
}
