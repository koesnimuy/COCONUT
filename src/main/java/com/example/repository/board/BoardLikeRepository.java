package com.example.repository.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Member;
import com.example.entity.board.Board;
import com.example.entity.board.BoardLike;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long>{
    
    // 아이디당 좋아요 눌린 게시글
    public List<BoardLike> findByUserid(Member userid);

    // 좋아요 눌린 게시글 확인
    public BoardLike findByBoardnoAndUserid(Board board, Member userid);
    
    // 게시글 좋아요 수
    public Long countByBoardno(Board boardno);

    @Transactional
    public void deleteByBoardno(Board board);

}
