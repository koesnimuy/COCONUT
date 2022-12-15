package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.CustomCours;

@Mapper
public interface CoursMapper {
    
    public List<CustomCours> selectall(Map<String,Object> map);

    public int countselect(Map<String,Object> map);
}
