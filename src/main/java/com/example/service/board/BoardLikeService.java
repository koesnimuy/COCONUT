package com.example.service.board;

import org.springframework.stereotype.Service;

import com.example.entity.board.Board;
import com.example.entity.board.BoardLike;

@Service
public interface BoardLikeService {
    
    public int insertBoardLike(BoardLike boardLike);

    public int deleteBoardLikeAll(Board board);
}
