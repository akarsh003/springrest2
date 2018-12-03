package com.example.demo.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;

@Service
public class DefPagingServiceEmployee implements EmployeeService{

	
	@Autowired
    private EmplRepo emplrepo;

//    @Override
    public Page<Employee> findAllEmployees(Pageable pageable) {
        return emplrepo.findAll(pageable);
    }

    public EmplRepo getProductRepository() {
        return emplrepo;
    }

	
}
