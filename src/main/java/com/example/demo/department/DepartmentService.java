package com.example.demo.department;

//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Department;

public interface DepartmentService {

	
	public Page<Department> findAllDepartments(Pageable pageable);
}
