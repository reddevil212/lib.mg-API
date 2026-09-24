package com.lib.mg.repository;

import com.lib.mg.entity.UserInfo;
import com.lib.mg.entity.UserRole;
import com.lib.mg.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);
    List<UserInfo> findByRole(UserRole role);
    List<UserInfo> findByRoleRoleId(Long roleId);
    List<UserInfo> findByRoleRoleName(Roles roleName);
}
