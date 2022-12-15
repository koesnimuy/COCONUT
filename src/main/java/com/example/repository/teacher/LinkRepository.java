package com.example.repository.teacher;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Link;
import com.example.entity.Teacher;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    public List<Link> findByTeacher(Teacher teacher);
    
}
