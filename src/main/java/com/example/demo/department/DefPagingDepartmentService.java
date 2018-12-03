package com.example.demo.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Department;

@Service
public class DefPagingDepartmentService implements DepartmentService{

	
	@Autowired
    private DeptRepo emplrepo;

//    @Override
    public Page<Department> findAllDepartments(Pageable pageable) {
        return emplrepo.findAll(pageable);
    }

    public DeptRepo getProductRepository() {
        return emplrepo;
    }

	
}
