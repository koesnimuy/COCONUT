package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.CoursImage;

@Repository
public interface CoursImageREpository extends JpaRepository<CoursImage, Long>{
    
    public CoursImage findBycours_coursno(Long coursno);
}
