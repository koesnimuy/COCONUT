package com.example.service.board;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.dto.BoardListLikeDTO;
import com.example.entity.Member;
import com.example.entity.board.Board;
import com.example.repository.board.BoardLikeRepository;
import com.example.repository.board.BoardReplyRepository;
import com.example.repository.board.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    final BoardRepository boardRepository;
    final BoardLikeRepository boardLikeRepository;
    final BoardReplyRepository boardReplyRepository;

    @Override
    public int writeBoard(Board board) {
        try {
            boardRepository.save(board);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;   
        }
    }

    @Override
    public int writeBoard50(Board board) {
        try {
            List<Board> list = new ArrayList<>();
            for(int i=0; i<30; i++){
                Board board2 = new Board();
                board2.setTitle(board.getTitle()+i);
                board2.setContent(board.getContent());
                board2.setUserid(board.getUserid());
                board2.setCategory(board.getCategory());
                
                list.add(board2);
            }
            boardRepository.saveAll(list);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;   
        }
    }

    @Override
    public Board selectOneBoard(Long no) {
        try {
            Board board = boardRepository.findById(no).orElse(null);
            if(boardRepository.findTopByNoLessThanOrderByNoDesc(no) != null){
                board.setPrevno(boardRepository.findTopByNoLessThanOrderByNoDesc(no).getNo());
            }
            else{
                board.setPrevno(0L);
            }
            
            if(boardRepository.findTopByNoGreaterThan(no) != null){
                board.setNextno(boardRepository.findTopByNoGreaterThan(no).getNo());
            }
            else{
                board.setNextno(0L);
            }
            board.setNickname(board.getUserid().getId());
            board.setBoardlike(boardLikeRepository.countByBoardno(board));
            return board;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateBoard(Board board) {
        
        try {
            Board upBoard = boardRepository.findById(board.getNo()).orElse(null);
            upBoard.setTitle(board.getTitle());
            upBoard.setContent(board.getContent());
            boardRepository.save(upBoard);
            return 1;   
        } catch (Exception e) {
            e.printStackTrace();
            return -1;   
        }
    }

    @Override
    public int updateHit(Long no) {
        
        try {
            Board board = boardRepository.findById(no).orElse(null);
            board.setHit(board.getHit()+1);
            boardRepository.save(board);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;   
        }
    }

    @Override
    public List<Board> selectList(String title, PageRequest pageRequest) {
        try {
            List<Board> ret = boardRepository.findByTitleContainingOrderByNoDesc(title, pageRequest);
            for(Board board : ret){
                board.setNickname(board.getUserid().getId());
                board.setBoardlike(boardLikeRepository.countByBoardno(board));
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Override
    public List<Board> selectListCate(String title, String category, PageRequest pageRequest) {
        try {
            List<Board> ret = boardRepository.findByTitleContainingAndCategoryContainingOrderByNoDesc(title, category, pageRequest);
            for(Board board : ret){
                board.setNickname(board.getUserid().getId());
                board.setBoardlike(boardLikeRepository.countByBoardno(board));
                board.setTotalreply(boardReplyRepository.countByBoardno(board));
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long selecListTotal(String title) {
        try {
            Long ret = boardRepository.countByTitleContaining(title);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Long selecListTotalCate(String title, String category) {
        try {
            Long ret = boardRepository.countByTitleContainingAndCategoryContaining(title, category);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public List<Board> selectListCateHit(String title, String category, PageRequest pageRequest, String hit) {
        try {
            List<Board> ret = new ArrayList<>();
            if(hit.equals("desc")){
                ret = boardRepository.findByTitleContainingAndCategoryContainingOrderByHitDesc(title, category, pageRequest);
            }
            else {
                ret = boardRepository.findByTitleContainingAndCategoryContainingOrderByHitAsc(title, category, pageRequest);
            }
            for(Board board : ret){
                board.setNickname(board.getUserid().getId());
                board.setBoardlike(boardLikeRepository.countByBoardno(board));
                board.setTotalreply(boardReplyRepository.countByBoardno(board));
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BoardListLikeDTO selectListLike(String title, String category, PageRequest pageRequest, String hit, int like) {
        try {
            BoardListLikeDTO ret = new BoardListLikeDTO();
            List<Board> retlist = new ArrayList<>();
            int total = 0;
            if(like == 0){
                
            }
            else{
                if(hit.equals("")){
                    List<Board> list = boardRepository.findByTitleContainingAndCategoryContainingOrderByNoDesc(title, category);
                    for(Board board : list){
                        if(boardLikeRepository.countByBoardno(board) >= like){
                            total++;
                            if(pageRequest.getPageSize()*pageRequest.getPageNumber()+1 <= total && total <= pageRequest.getPageSize()*(pageRequest.getPageNumber()+1)){
                                board.setNickname(board.getUserid().getId());
                                board.setBoardlike(boardLikeRepository.countByBoardno(board));
                                board.setTotalreply(boardReplyRepository.countByBoardno(board));
                                retlist.add(board);
                            }
                        }
                    }
                }
                else if(hit.equals("desc")) {
                    List<Board> list = boardRepository.findByTitleContainingAndCategoryContainingOrderByHitDesc(title, category);
                    for(Board board : list){
                        if(boardLikeRepository.countByBoardno(board) >= like){
                            total++;
                            if(pageRequest.getPageSize()*pageRequest.getPageNumber()+1 <= total && total <= pageRequest.getPageSize()*(pageRequest.getPageNumber()+1)){
                                board.setNickname(board.getUserid().getId());
                                board.setBoardlike(boardLikeRepository.countByBoardno(board));
                                board.setTotalreply(boardReplyRepository.countByBoardno(board));
                                retlist.add(board);
                            }
                        }
                    }
                }
                else {
                    List<Board> list = boardRepository.findByTitleContainingAndCategoryContainingOrderByHitAsc(title, category);
                    for(Board board : list){
                        if(boardLikeRepository.countByBoardno(board) >= like){
                            total++;
                            if(pageRequest.getPageSize()*pageRequest.getPageNumber()+1 <= total && total <= pageRequest.getPageSize()*(pageRequest.getPageNumber()+1)){
                                board.setNickname(board.getUserid().getId());
                                board.setBoardlike(boardLikeRepository.countByBoardno(board));
                                board.setTotalreply(boardReplyRepository.countByBoardno(board));
                                retlist.add(board);
                            }
                        }
                    }
                }
            }

            ret.setBoard(retlist);
            ret.setTotal(total);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int deleteBoard(Board board) {
        try {
            boardRepository.deleteById(board.getNo());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;   
        }
    }

    @Override
    public List<Board> selectListCateId(String title, String category, Member member, PageRequest pageRequest) {
        try {
            List<Board> ret = boardRepository.findByTitleContainingAndCategoryContainingAndUseridContainingOrderByNoDesc(title, category, member, pageRequest);
            for(Board board : ret){
                board.setNickname(board.getUserid().getId());
                board.setBoardlike(boardLikeRepository.countByBoardno(board));
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long selecListTotalCateId(String title, String category, Member member) {
        try {
            Long ret = boardRepository.countByTitleContainingAndCategoryContainingAndUseridContaining(title, category, member);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

   
    
}
