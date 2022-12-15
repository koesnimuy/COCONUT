package com.example.repository.teacher;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Teacher;
import com.example.entity.TeacherImage;

@Repository
public interface TeacherImageRepository extends JpaRepository<TeacherImage, Long> {

    public List<TeacherImage> findByTeacher(Teacher teacher);
    
}
