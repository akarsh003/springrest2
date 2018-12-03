package com.example.demo.employee;

//import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.example.demo.model.Department;
import com.example.demo.model.Employee;
import com.example.demo.department.*;

import org.springframework.stereotype.Component;

/*@Component
public class EmployeeResourceAssembler implements  ResourceAssembler<Employee, Resource<Employee>> {

	
	public Resource<Employee> toResource(Employee employee) {

		Department d=employee.getDeptid();
		System.out.println(d.getDeptid());
		int x=d.getDeptid();
		return new Resource<>(employee,
//			linkTo(methodOn(EmployeeController.class).findOne(employee.getId())).withSelfRel());
			linkTo(methodOn(EmployeeController.class).all(Pageable pagable)).withRel("employee"),
//			linkTo(methodOn(DepartmentController.class).findOne(x)).withRel("department"));
		
	}

	
	
}*/
