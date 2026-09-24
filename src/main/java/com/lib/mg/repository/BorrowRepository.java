package com.lib.mg.repository;

import com.lib.mg.entity.Borrow;
import com.lib.mg.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    List<Borrow> findByUserIdAndStatus(Long userId, Status status);

}
