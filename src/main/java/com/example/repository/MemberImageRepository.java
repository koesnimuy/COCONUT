package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.MemberImage;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
 
    public Long countByUseridContaining(String userid);

    public MemberImage findByUserid(String userid);

}
