package employee.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import employee.model.Employee;

@Stateless
public class EmployeeBean {

	@PersistenceContext
	EntityManager entityManager;
	
	public Employee saveEmployee(Employee employee) {
		
		entityManager.persist(employee);
		
		return employee;
	}
}
