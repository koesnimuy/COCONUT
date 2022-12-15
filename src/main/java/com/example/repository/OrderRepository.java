package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    
    public Order findByUserid_idAndCourscode_coursno(String id, Long coursno);

    public List<Order> findByUserid_id(String id);
}
