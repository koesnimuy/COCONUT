package com.example.service.board;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.dto.BoardListLikeDTO;
import com.example.entity.Member;
import com.example.entity.board.Board;

@Service
public interface BoardService {
    
    // 게시판 글쓰기
    public int writeBoard(Board board);

    // 게시판 글쓰기
    public int writeBoard50(Board board);

    // 게시판 글 상세정보
    public Board selectOneBoard(Long no);

    // 게시판 글수정(내용만)
    public int updateBoard(Board board);

    // 조회수 증가
    public int updateHit(Long no);

    // 게시판 글목록(제목, 페이지네이션)
    public List<Board> selectList(String title, PageRequest pageRequest);

    // 게시판 글 개수(제목)
    public Long selecListTotal(String title);

    // 게시판 글목록(제목, 카테고리, 페이지네이션)
    public List<Board> selectListCate(String title, String category, PageRequest pageRequest);

    // 게시판 글 개수(제목, 카테고리)
    public Long selecListTotalCate(String title, String category);

    // 게시판 글목록(제목, 카테고리, 페이지네이션, 아이디)
    public List<Board> selectListCateId(String title, String category, Member member, PageRequest pageRequest);

    // 게시판 글 개수(제목, 카테고리)
    public Long selecListTotalCateId(String title, String category, Member member);

    // 게시판 글목록(조회수 순)
    public List<Board> selectListCateHit(String title, String category, PageRequest pageRequest, String hit);

    // 게시판 글목록(일정 추천수이상 리스트)
    public BoardListLikeDTO selectListLike(String title, String category, PageRequest pageRequest, String hit, int like);

    // 게시판 글 삭제
    public int deleteBoard(Board board);
}
