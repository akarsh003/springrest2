package com.example.demo.employee;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//import com.example.demo.model.Employee;
//import com.example.demo.model.SearchOperation;
//import com.google.common.base.Joiner;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
//import com.querydsl.core.types.Predicate;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.*;
import com.example.demo.department.DepartmentResourceAssembler;
import com.example.demo.model.Employee;


//@EnableSpringDataWebSupport
@RestController
class EmployeeController {

//	@Autowired
//	private final DeptRepo deptrepo; 
	@Autowired
	private final EmplRepo repository;
//	@Autowired
//	private final EmployeeResourceAssembler assembler;
	@Autowired
	private final DepartmentResourceAssembler deptass;
//	@Autowired
//	private final EmployeeWithDepartmentResourceAssembler employeewithdepartmentresourceassembler;
	@Autowired
	private final EmployeeService empser;
	@Autowired
	private ProjectionFactory factory;
	@Autowired
	private PagedResourcesAssembler<InlineRecordsEmployee> assembleremployee;
	
	
	EmployeeController(EmployeeService empser,DepartmentResourceAssembler deptass/*,DeptRepo deptrepoEmployeeService empser*/,EmplRepo repository/*,EmployeeResourceAssembler assembler*/) {
		this.repository = repository;
		this.deptass=deptass;
//		this.deptrepo=deptrepo;
//		this.assembler=assembler;
		this.empser=empser;
//		this.employeewithdepartmentresourceassembler=employeewithdepartmentresourceassembler;
	}

	//Git code
	
//	@GetMapping(value = "/employee", produces = MediaTypes.HAL_JSON_VALUE)
//	public ResponseEntity<Resources<Resource<Employee>>> findAll() {
//		return ResponseEntity.ok(
//			assembler.toResource(repository.findAll()));
//
//	}
	
//	@GetMapping("/employee")
//	@RequestMapping( value = "/employee" )
	@RequestMapping(value = "/employee", method = RequestMethod.GET)
//	List<Employee> all(){
//    public ResponseEntity<PagedResources<Employee>> all(Pageable pageable, PagedResourcesAssembler assembler){

	public ResponseEntity<?> all(Pageable pageable){
		
//	Resources<Resource<Employee>> all() {

		
//		List<Resource<Employee>> employees = repository.findAll().stream().map(assembler::toResource)
//				.collect(Collectors.toList());
//		
//		return new Resources<>(employees,
//			linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
		

		 Page<Employee> emplo= repository.findAll(pageable);
	        Page<InlineRecordsEmployee> projected = emplo.map(l -> factory.createProjection(InlineRecordsEmployee.class, l));
	        PagedResources<Resource<InlineRecordsEmployee>> resources = assembleremployee.toResource(projected);
	        return ResponseEntity.ok(resources);
		
	        
//	        Page<Employee> employees = repository.findAll(pageable);
//	        PagedResources<Employee> pr= assembler.toResource(employees,linkTo(EmployeeController.class).slash("/employee").withSelfRel());
//	        
//	        
//	        HttpHeaders responseHeaders = new HttpHeaders();
//	        responseHeaders.add("Link",createLinkHeader(pr));
//	        return new ResponseEntity<>(assembler.toResource(employees,linkTo(EmployeeController.class).slash("/employee").withSelfRel()),responseHeaders,HttpStatus.OK);
	  
//		return repository.findAll();
		
	}
	
	
	@GetMapping(value = "/employee/{id}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> all(@PathVariable int id,Pageable pageable){

//	public ResponseEntity<Resource<Employee>> findOne(@PathVariable int id) {

//		return repository.findById(id)
//			.map(assembler::toResource)
//			.map(ResponseEntity::ok)
//			.orElse(ResponseEntity.notFound().build());
		
		Page<Employee> emplo= repository.findById(id,pageable);
        Page<InlineRecordsEmployee> projected = emplo.map(l -> factory.createProjection(InlineRecordsEmployee.class, l));
        PagedResources<Resource<InlineRecordsEmployee>> resources = assembleremployee.toResource(projected);
        return ResponseEntity.ok(resources);
	
		
	}
	
	
	//Paging and sorting
	@GetMapping(value = "/employee/",produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResources<Employee>> AllEmployees(Pageable pageable, PagedResourcesAssembler assembler){
		
        Page<Employee> products = empser.findAllEmployees(pageable);
        PagedResources<Employee> pr= assembler.toResource(products,linkTo(EmployeeController.class).slash("/employee").withSelfRel());
        
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link",createLinkHeader(pr));
        return new ResponseEntity<>(assembler.toResource(products,linkTo(EmployeeController.class).slash("/employee").withSelfRel()),responseHeaders,HttpStatus.OK);
    }
	
	private String createLinkHeader(PagedResources<Employee> pr){
		
	        final StringBuilder linkHeader = new StringBuilder();
	        linkHeader.append(buildLinkHeader(  pr.getLinks("first").get(0).getHref(),"first"));
	        linkHeader.append(", ");
	        linkHeader.append(buildLinkHeader( pr.getLinks("next").get(0).getHref(),"next"));
	        return linkHeader.toString();
    }

	public static String buildLinkHeader(final String uri, final String rel) {
		
	        return "<" + uri + ">; rel=\"" + rel + "\"";
	}
	
	
	//Complex/Advanced Search
	
	@GetMapping(value = "/employee/search/byadvsearch")
    @ResponseBody
    public ResponseEntity<?> findAllByAdvPredicate(@RequestParam(value = "advsearch") String search,Pageable pageable){
		
//        Specification<Employee> spec = resolveSpecificationFromInfixExpr(search);
//        Page<InlineRecordsEmployee> projected = emplo.map(l -> factory.createProjection(InlineRecords2.class, l));
//        PagedResources<Resource<InlineRecordsEmployee>> resources = assembler.toResource(projected);
//        return ResponseEntity.ok(resources);
        
        Specification<Employee> spec = resolveSpecificationFromInfixExpr(search);
        Page<Employee> emplo= repository.findAll(spec, pageable);
        Page<InlineRecordsEmployee> projected = emplo.map(l -> factory.createProjection(InlineRecordsEmployee.class, l));
        PagedResources<Resource<InlineRecordsEmployee>> resources = assembleremployee.toResource(projected);
        return ResponseEntity.ok(resources);
//        return repository.findAll(spec, pageable);

       
	}
	
	
    protected Specification<Employee> resolveSpecificationFromInfixExpr(String searchParameters) {
        CrieteriaParser parser = new CrieteriaParser();
        GenericSpecificationBuilder<Employee> specBuilder = new GenericSpecificationBuilder<>();
        return specBuilder.build(parser.parse(searchParameters), EmployeeSpecification::new);
    }
	
	
	

//	@GetMapping(value = "/department/{id}/employee", produces = MediaTypes.HAL_JSON_VALUE)
//	Resources<Resource<Employee>>  allbydept(@PathVariable int id) {
//
//	
//		
//	List<Resource<Employee>> employees = repository.findAll().stream().map(assembler::toResource)
//			.collect(Collectors.toList());
//
//	return new Resources<>(employees,
//		linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
//	
//	}
	
	
	



	

//	@PostMapping("/employee")
//	ResponseEntity<Resource<Employee>> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
//
//		Resource<Employee> resource = assembler.toResource(repository.save(newEmployee));
//
//		return ResponseEntity
//			.created(new URI(resource.getId().expand().getHref()))
//			.body(resource);
//	}
	
	@PostMapping("/employee")
	Employee newEmployee(@RequestBody Employee newEmployee) {
		return repository.save(newEmployee);
	}

	
	@PutMapping("/employee/{id}")
	Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable int id) {

		return repository.findById(id)
			.map(employee -> {
				employee.setName(newEmployee.getName());
				employee.setGrade(newEmployee.getGrade());
				employee.setSalary(newEmployee.getSalary());
				employee.setDeptid(newEmployee.getDeptid());
				employee.setSkill(newEmployee.getSkill());
				employee.setDoj(newEmployee.getDoj());
				employee.setDesg(newEmployee.getDesg());
				employee.setCity(newEmployee.getCity());
				employee.setCountry(newEmployee.getCountry());
				
				return repository.save(employee);
			})
			.orElseGet(() -> {
				newEmployee.setId(id);
				return repository.save(newEmployee);
			});
	}

	
	
	@DeleteMapping("/employee/{id}")
	void deleteEmployee(@PathVariable int id) {
		repository.deleteById(id);
	}
}
