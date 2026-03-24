package com.nguyenthanhtai.kt.repository;

import com.nguyenthanhtai.kt.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
