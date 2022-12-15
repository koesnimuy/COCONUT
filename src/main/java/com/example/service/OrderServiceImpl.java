package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Order;
import com.example.repository.CoursRepository;
import com.example.repository.MemberRepository;
import com.example.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    final OrderRepository orderRepository;
    final MemberRepository memberRepository;
    final CoursRepository coursRepository;

    @Override
    public int insertorder(Order order) {
        orderRepository.save(order);
        return 1;
    }

    @Override
    public int updateorder(Order order) {
        
        return 0;
    }
    
}
