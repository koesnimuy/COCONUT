package com.example.dto;

import java.util.List;

import com.example.entity.Lecture;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LectureList {
    
    public List<Lecture> lecturelist;

    public Long chapterno;
}
