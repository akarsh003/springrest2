package com.example.demo.department;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.example.demo.model.Department;
import com.example.demo.model.Employee;

@Component
public class DepartmentResourceAssembler implements  ResourceAssembler<Department, Resource<Department>> {
	
	//TOResource old method
	public Resource<Department> toResource(Department department) {

		Employee e=department.getId();
		int id=e.getId();
		return new Resource<>(department,
			linkTo(methodOn(DepartmentController.class).findOne(department.getDeptid())).withSelfRel());
//			linkTo(methodOn(DepartmentController.class).all()).withRel("department"),
//			linkTo(methodOn(EmployeeController.class).findOne(id)).withRel("employee"));
	}



}
