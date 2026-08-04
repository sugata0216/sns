package com.example.sns.repository;

import com.example.sns.entity.Role;
import com.example.sns.mapper.RoleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepository {
    private final RoleMapper roleMapper;

    public RoleRepository(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
    public Role findById(long roleId) {
        return roleMapper.findById(roleId);
    }
    public Role findByName(String roleName) {
        return roleMapper.findByName(roleName);
    }
    public List<Role> findAll() {
        return roleMapper.findAll();
    }
}
