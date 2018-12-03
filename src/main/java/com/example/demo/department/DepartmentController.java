package com.example.demo.department;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.example.demo.model.Department;
import com.example.demo.model.Employee;
import com.example.demo.department.DepartmentController;

@RestController
public class DepartmentController {

	@Autowired
	private final DepartmentService depser;
	@Autowired
	private final DeptRepo repository;
	@Autowired
	private final DepartmentResourceAssembler assembler;
	@Autowired
	private ProjectionFactory factory;
	@Autowired
	private PagedResourcesAssembler<InlineRecordsDepartment> assembleremployee;
//	@Autowired
//	private final Employee employee;
//	@Autowired 
//	private final Department department;
	
	
	DepartmentController(/*Department department,Employee employee,*/DepartmentService depser,DeptRepo repository,DepartmentResourceAssembler assembler) {
		this.repository = repository;
		this.depser=depser;
//		this.department=department;
		this.assembler=assembler;
//		this.employee=employee;
	}

	
	 
	
	@GetMapping("/department")
//	 public ResponseEntity<PagedResources<Department>> all(Pageable pageable, PagedResourcesAssembler assembler){

	public ResponseEntity<?> all(Pageable pageable){

//	Resources<Resource<Department>> all() {
//		ResponseEntity<PagedResources<Department>> all(Pageable pageable,PagedResourcesAssembler assembler){
//
//		List<Resource<Department>> Departments = repository.findAll().stream().map(assembler::toResource)
//				.collect(Collectors.toList());
//
//		return new Resources<>(Departments,
//			linkTo(methodOn(DepartmentController.class).all()).withSelfRel());
		
//		Page<Department> departments = repository.findAll(pageable);
//        PagedResources<Department> pr= assembler.toResource(departments,linkTo(DepartmentController.class).slash("/department").withSelfRel());
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.add("Link",createLinkHeader(pr));
//        return new ResponseEntity<>(assembler.toResource(departments,linkTo(DepartmentController.class).slash("/department").withSelfRel()),responseHeaders,HttpStatus.OK);


		Page<Department> emplo= repository.findAll(pageable);
        Page<InlineRecordsDepartment> projected = emplo.map(l -> factory.createProjection(InlineRecordsDepartment.class, l));
        PagedResources<Resource<InlineRecordsDepartment>> resources = assembleremployee.toResource(projected);
        return ResponseEntity.ok(resources);
		

	}

	
	@GetMapping(value = "/department/{id}", produces = MediaTypes.HAL_JSON_VALUE)
	ResponseEntity<Resource<Department>> findOne(@PathVariable int id){

			return repository.findById(id)
				.map(assembler::toResource)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
		}	
	
	
	//Paging and sorting
	@GetMapping(value = "/department/",produces =MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<PagedResources<Department>> AllProducts(Pageable pageable, PagedResourcesAssembler assembler){
	
		 
		 	Page<Department> products = depser.findAllDepartments(pageable);
	        PagedResources<Department> pr= assembler.toResource(products,linkTo(DepartmentController.class).slash("/department").withSelfRel());
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.add("Link",createLinkHeader(pr));
	        return new ResponseEntity<>(assembler.toResource(products,linkTo(DepartmentController.class).slash("/department").withSelfRel()),responseHeaders,HttpStatus.OK);
	
	 }
	    
	 private String createLinkHeader(PagedResources<Department> pr){
		 
	        final StringBuilder linkHeader = new StringBuilder();
	        linkHeader.append(buildLinkHeader(  pr.getLinks("first").get(0).getHref(),"first"));
	        linkHeader.append(", ");
	        linkHeader.append(buildLinkHeader( pr.getLinks("next").get(0).getHref(),"next"));
	        return linkHeader.toString();
	        
	 }

	 public static String buildLinkHeader(final String uri, final String rel) {
		 
	        return "<" + uri + ">; rel=\"" + rel + "\"";
	        
	 }
	
//	 @PostMapping("/department")
//		Department newEmployee(@RequestBody Department newdepartment) {
//			return repository.save(newdepartment);
//		}
	
	
	@PostMapping("/department")
	ResponseEntity<Resource<Department>> newDepartment(@RequestBody Department newDepartment) throws URISyntaxException {

		Resource<Department> resource = assembler.toResource(repository.save(newDepartment));

		return ResponseEntity
			.created(new URI(resource.getId().expand().getHref()))
			.body(resource);
	}
	
	
	

	@PutMapping("/department/{id}")
	Department replaceDepartment(@RequestBody Department newDepartment, @PathVariable int id) {

		return repository.findById(id)
			.map(Department -> {
				Department.setDeptid(newDepartment.getDeptid());
				Department.setDeptname(newDepartment.getDeptname());
				Department.setId(newDepartment.getId());
				
				return repository.save(Department);
			})
			.orElseGet(() -> {
				newDepartment.setDeptid(id);
				return repository.save(newDepartment);
			});
	}

	
	
	@DeleteMapping("/department/{id}")
	void deleteDepartment(@PathVariable int id) {
		repository.deleteById(id);
	}
}

