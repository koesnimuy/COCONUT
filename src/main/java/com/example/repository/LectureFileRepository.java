package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.LectureFile;

@Repository
public interface LectureFileRepository extends JpaRepository<LectureFile, Long> {
    
    public LectureFile findByLecture_no(Long no);
}
