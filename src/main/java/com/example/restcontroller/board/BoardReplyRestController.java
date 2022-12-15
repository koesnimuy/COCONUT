package com.example.restcontroller.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BoardReplyDTO;
import com.example.dto.BoardReplyDelDTO;
import com.example.dto.CustomBoardReply;
import com.example.entity.Member;
import com.example.entity.board.Board;
import com.example.entity.board.BoardReply;
import com.example.service.board.BoardReplyService;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping(value = "/api/boardreply")
@RequiredArgsConstructor
public class BoardReplyRestController {
    
    final BoardReplyService boardReplyService;

    // 댓글 쓰기
    // 127.0.0.1:8081/COCO/api/boardreply/insert.json
    @PostMapping(value="/insert.json")
    public Map<String, Object> insertPOST(
        HttpServletRequest request,
        @RequestBody CustomBoardReply customBoardReply
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");

            Member member = new Member();
            member.setId(username);

            Board board = new Board();
            board.setNo(customBoardReply.getBoardno());
            
            BoardReply boardReply = new BoardReply();
            boardReply.setContent(customBoardReply.getContent());
            boardReply.setBoardno(board);
            boardReply.setUserid(member);
            boardReplyService.insertBoardReply(boardReply);
            
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 0);
        }
        return map;
    }

    // 댓글 목록
    // 127.0.0.1:8081/COCO/api/boardreply/selectlist.json
    @PostMapping(value="/selectlist.json")
    public Map<String, Object> selectGET(
        @RequestBody BoardReplyDTO boardReplyDTO
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> minmap = new HashMap<>();
            PageRequest pageRequest = PageRequest.of(boardReplyDTO.getPage()-1, 5);
            Board board = new Board();
            board.setNo(boardReplyDTO.getBoardno());
            List<BoardReply> list = boardReplyService.selectlistBoardReply(board, pageRequest);

            minmap.put("total", boardReplyService.selectlistBoardReplyTotal(board));
            minmap.put("boardreply", list);
            map.put("status", 200);
            map.put("result", minmap);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }
    
    // 댓글 수정
    // 127.0.0.1:8081/COCO/api/boardreply/update.json
    @PutMapping(value="update.json")
    public Map<String, Object> updatePUT(
        HttpServletRequest request,
        @RequestBody BoardReply boardReply
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");

            Member member = new Member();
            member.setId(username);

            boardReply.setUserid(member);
            int ret = boardReplyService.updateBoardReply(boardReply);
            map.put("status", 200);
            map.put("result", ret);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    // 127.0.0.1:8081/COCO/api/boardreply/delete.json
    @PostMapping(value = "delete.json")
    public Map<String, Object> deletePOST(
        HttpServletRequest request,
        @RequestBody BoardReplyDelDTO boardReplyDelDTO
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            String username = (String) request.getAttribute("username");

            if(username.equals(boardReplyDelDTO.getId())){
                int ret = boardReplyService.deleteBoardReply(boardReplyDelDTO.getNo());
                if(ret == 1){
                    map.put("status", 200);
                }
                else{
                    map.put("status", 0);
                }
            } 
            else {
                map.put("status", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        return map;
    }

    

    
}
