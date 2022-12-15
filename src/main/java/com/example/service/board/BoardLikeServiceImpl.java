package com.example.service.board;

import org.springframework.stereotype.Service;

import com.example.entity.board.Board;
import com.example.entity.board.BoardLike;
import com.example.repository.board.BoardLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {
    
    final BoardLikeRepository boardLikeRepository;
    
    @Override
    public int insertBoardLike(BoardLike boardLike) {
        try {
            BoardLike ret = boardLikeRepository.findByBoardnoAndUserid(boardLike.getBoardno(), boardLike.getUserid());
            if(ret == null){
                BoardLike ret2 = boardLikeRepository.save(boardLike);
                if(ret2 != null){
                    return 1;
                }
            }
            return 0;       
        } catch (Exception e) {
            e.printStackTrace();
            return -1;    
        }
    }

    @Override
    public int deleteBoardLikeAll(Board board) {
        try {
            boardLikeRepository.deleteByBoardno(board);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
