package com.example.restcontroller.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BoardListLikeDTO;
import com.example.entity.Member;
import com.example.entity.board.Board;
import com.example.service.board.BoardLikeService;
import com.example.service.board.BoardReplyService;
import com.example.service.board.BoardService;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping(value = "/api/board")
@RequiredArgsConstructor
public class BoardRestController {

    final BoardService boardService;
    final BoardReplyService boardReplyService;
    final BoardLikeService boardLikeService;

    // 게시판 글쓰기
    // 127.0.0.1:8081/COCO/api/board/insert.json
    @PostMapping(value="/insert.json")
    public Map<String, Object> insertPOST(
        HttpServletRequest request,
        @RequestBody Board board
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");
            System.out.println(username);
            Member member = new Member();
            member.setId(username);
            board.setUserid(member);
            int ret = boardService.writeBoard(board);
            map.put("status", 200);
            map.put("result", ret);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }
    @PostMapping(value="/insert50.json")
    public Map<String, Object> insert50POST(
        HttpServletRequest request,
        @RequestBody Board board
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");
            System.out.println(username);
            Member member = new Member();
            member.setId(username);
            board.setUserid(member);

            boardService.writeBoard50(board);
            
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 게시판 글 상세보기
    // 127.0.0.1:8081/COCO/api/board/selectone?no=1
    @GetMapping(value="/selectone")
    public Map<String, Object> selectOneGET(
        @RequestParam(name = "no") Long no
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            Board board = boardService.selectOneBoard(no);
            map.put("status", 200);
            map.put("result", board);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }
    
    // 게시판 글 수정(내용)
    // 127.0.0.1:8081/COCO/api/board/update.json
    @PutMapping(value="/update.json")
    public Map<String, Object> updatePUT(
        HttpServletRequest request,
        @RequestBody Board board
        ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");
            Board chkboard = boardService.selectOneBoard(board.getNo());

            map.put("status", 0);
            if(username.equals(chkboard.getUserid().getId())){
                int ret = boardService.updateBoard(board);
                map.put("status", 200);
                map.put("result", ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 조회수 증가
    // 127.0.0.1:8081/COCO/api/board/updatehit?no=6
    @PutMapping(value="/updatehit")
    public Map<String, Object> updateHitPOST(
        @RequestParam(name = "no")Long no
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            int ret = boardService.updateHit(no);
            map.put("status", 200);
            map.put("result", ret);
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 200);
        }
        return map;
    }

    // 글목록 페이지네이션
    // 127.0.0.1:8081/COCO/api/board/selectlist?page=&keyword=t&category=
    @GetMapping(value="/selectlist")
    public Map<String, Object> selectlistGET(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "category", defaultValue = "") String category,
        @RequestParam(name = "keyword", defaultValue = "") String keyword,
        @RequestParam(name = "hit", defaultValue = "") String hit,
        @RequestParam(name = "like", defaultValue = "0") int like
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            PageRequest pageRequest = PageRequest.of(page-1, 10);
            System.out.println(category);
            Map<String, Object> minmap = new HashMap<>();
            if(like == 0){
                if(hit.equals("")){
                    List<Board> list = boardService.selectListCate(keyword, category, pageRequest);
                    Long total = boardService.selecListTotalCate(keyword, category);
                    minmap.put("list", list);
                    minmap.put("total", total);
                    map.put("result", minmap);
                }
                else {
                    List<Board> list = boardService.selectListCateHit(keyword, category, pageRequest, hit);
                    Long total = boardService.selecListTotalCate(keyword, category);
                    minmap.put("list", list);
                    minmap.put("total", total);
                    map.put("result", minmap);
                }
            }
            else {
                BoardListLikeDTO ret = boardService.selectListLike(keyword, category, pageRequest, hit, like);
                minmap.put("list", ret.getBoard());
                minmap.put("total", ret.getTotal());
                map.put("result", minmap);
            }
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        
        return map;
    }

    // 게시판 글 수정(내용)
    // 127.0.0.1:8081/COCO/api/board/delete.json
    @PostMapping(value="/delete.json")
    public Map<String, Object> deletePOST(
        HttpServletRequest request,
        @RequestBody Board board
        ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");
            Board chkboard = boardService.selectOneBoard(board.getNo());

            map.put("status", 0);
            if(username.equals(chkboard.getUserid().getId())){
                int ret2 = boardLikeService.deleteBoardLikeAll(board);
                int ret1 = boardReplyService.deleteBoardReplyAll(board);
                if(ret1 == 1 && ret2 == 1){
                    int ret = boardService.deleteBoard(board);
                    map.put("status", 200);
                    map.put("result", ret);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
    

    
}
