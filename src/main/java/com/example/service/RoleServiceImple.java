package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Role;
import com.example.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImple implements RoleService {
    
    final RoleRepository roleRepository;
    
    @Override
    public int upsertRole(Role role) {
        try {
            roleRepository.save(role);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int selectbyuserid(Role role) {
        try {
            return roleRepository.countByUseridContaining(role.getUserid());
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int insertAdminRole(List<Role> role) {
        try {
            roleRepository.saveAll(role);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
}
