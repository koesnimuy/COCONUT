package com.example.repository.board;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.board.Board;
import com.example.entity.board.BoardReply;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {
    
    public List<BoardReply> findByBoardno(Board board);

    //페이지네이션
    public List<BoardReply> findByBoardnoOrderByNoDesc(Board board, PageRequest pageRequest);

    public Long countByBoardno(Board board);

    @Transactional
    public void deleteByBoardno(Board board);
}
