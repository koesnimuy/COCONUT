package com.example.repository.board;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Member;
import com.example.entity.board.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
 
    // 검색, 페이지네이션
    public List<Board> findByTitleContainingOrderByNoDesc(String title, PageRequest pageRequest);

    // 검색 총개수
    public Long countByTitleContaining(String title);
    
    // 카테고리, 검색, 페이지네이션
    public List<Board> findByTitleContainingAndCategoryOrderByNoDesc(String title, String category, PageRequest pageRequest);

    // 카테고리, 검색, 페이지네이션
    public List<Board> findByTitleContainingAndCategoryContainingOrderByNoDesc(String title, String category, PageRequest pageRequest);

    // 카테고리, 검색, 페이지네이션 (최종)
    public List<Board> findByTitleContainingAndCategoryContainingAndUseridContainingOrderByNoDesc(String title, String category, Member member, PageRequest pageRequest);

    // 카테고리, 검색
    public List<Board> findByTitleContainingAndCategoryContainingOrderByNoDesc(String title, String category);

    // 카테고리 검색 총개수
    public Long countByTitleContainingAndCategoryContaining(String title, String category);

    // 카테고리 검색 총개수 (최종)
    public Long countByTitleContainingAndCategoryContainingAndUseridContaining(String title, String category, Member member);

    // 카테고리, 검색, 페이지네이션 조회수 순
    public List<Board> findByTitleContainingAndCategoryContainingOrderByHitDesc(String title, String category);

    // 카테고리, 검색, 페이지네이션 조회수 순
    public List<Board> findByTitleContainingAndCategoryContainingOrderByHitDesc(String title, String category, PageRequest pageRequest);

    // 카테고리, 검색, 페이지네이션 조회수 순 (최종)
    public List<Board> findByTitleContainingAndCategoryContainingAndUseridContainingOrderByHitDesc(String title, String category, Member member, PageRequest pageRequest);

    // 조회수 역순
    public List<Board> findByTitleContainingAndCategoryContainingOrderByHitAsc(String title, String category);

    // 조회수 역순
    public List<Board> findByTitleContainingAndCategoryContainingOrderByHitAsc(String title, String category, PageRequest pageRequest);

    // 조회수 역순 (최종)
    public List<Board> findByTitleContainingAndCategoryContainingAndUseridContainingOrderByHitAsc(String title, String category, Member member, PageRequest pageRequest);

    // 다음글
    public Board findTopByNoGreaterThan(Long no);

    // 이전글
    public Board findTopByNoLessThanOrderByNoDesc(Long no);
}