package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    
    public List<Chapter> findByCours_coursno(Long coursno);
}
