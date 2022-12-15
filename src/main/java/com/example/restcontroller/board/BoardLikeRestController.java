package com.example.restcontroller.board;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.BoardLikeDTO;
import com.example.entity.Member;
import com.example.entity.board.Board;
import com.example.entity.board.BoardLike;
import com.example.service.board.BoardLikeService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/api/boardlike")
@RequiredArgsConstructor
public class BoardLikeRestController {
    
    final BoardLikeService boardLikeService;


    // 좋아요
    // 127.0.0.1:8081/COCO/api/boardlike/insert.json
    @PostMapping(value="insert.json")
    public Map<String, Object> insertPOST(
        HttpServletRequest request,
        @RequestBody BoardLikeDTO boardLikeDTO
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            BoardLike boardLike = new BoardLike();
            String username = (String) request.getAttribute("username");

            Member member = new Member();
            member.setId(username);
            Board board = new Board();
            board.setNo(boardLikeDTO.getBoardno());

            boardLike.setBoardno(board);
            boardLike.setUserid(member);
            int ret = boardLikeService.insertBoardLike(boardLike);
            if(ret == 1){
                map.put("status", 200);
            }
            else map.put("status", 0);            
            map.put("result", ret);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
        }
        
        return map;
    }
    

}
