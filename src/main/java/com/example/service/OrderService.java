package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Order;

@Service
public interface OrderService {
    
    public int insertorder(Order order);

    public int updateorder(Order order);
}
