package com.example.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.board.BoardImage;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    
}
