package com.example.service.board;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.entity.board.Board;
import com.example.entity.board.BoardReply;
import com.example.repository.board.BoardReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements BoardReplyService {

    final BoardReplyRepository boardReplyRepository;

    @Override
    public int insertBoardReply(BoardReply boardReply) {
        try {
            BoardReply ret = boardReplyRepository.save(boardReply);
            if(ret != null){
                return 1;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        
    }

    @Override
    public List<BoardReply> selectBoardReply(Board board) {
        try {
            List<BoardReply> list = boardReplyRepository.findByBoardno(board);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateBoardReply(BoardReply boardReply) {
        try {
            BoardReply uBoardReply = boardReplyRepository.findById(boardReply.getNo()).orElse(null);
            System.out.println(uBoardReply.getUserid().getId());
            System.out.println(boardReply.getUserid().getId());
            if(uBoardReply.getUserid().getId().equals(boardReply.getUserid().getId())){
                uBoardReply.setContent(boardReply.getContent());    
                boardReplyRepository.save(uBoardReply);
                return 1;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<BoardReply> selectlistBoardReply(Board board, PageRequest pageRequest) {
        try {
            List<BoardReply> list = boardReplyRepository.findByBoardnoOrderByNoDesc(board, pageRequest);
            for(BoardReply boardReply : list){
                boardReply.setNickname(boardReply.getUserid().getId());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long selectlistBoardReplyTotal(Board board) {
        try {
            Long ret = boardReplyRepository.countByBoardno(board);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public int deleteBoardReply(Long no) {
        try {
            boardReplyRepository.deleteById(no);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        
    }

    @Override
    public int deleteBoardReplyAll(Board board) {
        try {
            boardReplyRepository.deleteByBoardno(board);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
}
