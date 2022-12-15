package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Member;
import com.example.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
 
    public int countByUseridContaining(Member member);

    public List<Role> findByUserid(Member member);
}
