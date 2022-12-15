package com.example.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.TestBoard;

@Repository
public interface TestBoardRepository extends JpaRepository<TestBoard, Long> {
    
}
