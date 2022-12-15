package com.example.dto;

import java.util.List;

import com.example.entity.board.Board;

import lombok.Data;

@Data
public class BoardListLikeDTO {
    
    List<Board> board;

    int total;
}
