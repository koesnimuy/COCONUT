package com.example.service.board;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.entity.board.Board;
import com.example.entity.board.BoardReply;

@Service
public interface BoardReplyService {
    
    // 댓글 추가
    public int insertBoardReply(BoardReply boardReply);

    // 댓글 목록
    public List<BoardReply> selectBoardReply(Board board);

    // 댓글 수정
    public int updateBoardReply(BoardReply boardReply);

    // 댓글 목록 (페이지네이션)
    public List<BoardReply> selectlistBoardReply(Board board, PageRequest pageRequest);

    // 게시글 댓글 개수
    public Long selectlistBoardReplyTotal(Board board);

    // 댓글 삭제
    public int deleteBoardReply(Long no);

    // 게시판 삭제시 댓글 삭제
    public int deleteBoardReplyAll(Board board);
}
