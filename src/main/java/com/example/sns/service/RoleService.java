package com.example.sns.service;

import com.example.sns.entity.Role;
import com.example.sns.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role findById(long roleId) {
        return roleRepository.findById(roleId);
    }
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
