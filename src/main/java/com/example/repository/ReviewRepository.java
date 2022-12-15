package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    
    public List<Review> findByCourscode_coursno(Long coursno);

    public List<Review> findByCourscode_coursno(Long coursno, Pageable pageRequest);

    public int countByCourscode_coursno(Long coursno);

    public List<Review> findByUserid_id(String id);

    public Review findByUserid_idAndCourscode_coursno(String id, Long coursno);





    
}
