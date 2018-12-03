package com.example.demo.employee;

//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Employee;

public interface EmployeeService {

	
	public Page<Employee> findAllEmployees(Pageable pageable);
}
