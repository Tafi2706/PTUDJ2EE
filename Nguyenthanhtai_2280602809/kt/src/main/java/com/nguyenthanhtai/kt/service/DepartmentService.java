package com.nguyenthanhtai.kt.service;

import com.nguyenthanhtai.kt.entity.Department;
import com.nguyenthanhtai.kt.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
