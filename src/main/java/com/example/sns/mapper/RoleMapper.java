package com.example.sns.mapper;

import com.example.sns.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Select("""
        SELECT *
        FROM roles
        WHERE role_id = #{roleId}
    """)
    Role findById(long roleId);
    @Select("""
        SELECT *
        FROM roles
        WHERE role_name = #{roleName}
    """)
    Role findByName(String roleName);
    @Select("""
        SELECT
            role_id,
            role_name
        FROM roles
        ORDER BY role_id
    """)
    List<Role> findAll();
}
