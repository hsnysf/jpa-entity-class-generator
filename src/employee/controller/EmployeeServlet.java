package employee.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import employee.bean.EmployeeBean;
import employee.model.Address;
import employee.model.Certificate;
import employee.model.Manager;

@WebServlet("/")
public class EmployeeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	@EJB
	EmployeeBean employeeBean;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Manager manager = new Manager();
		manager.setDegree(12);
		manager.setAllowance(new BigDecimal(123));
		manager.setName("Hasan Yusuf");
		manager.setGender("M");
		manager.setAge((short) 35);
		manager.setCpr(40100162);
		manager.setAccountNo(12345678912345l);
		manager.setGpa(3.14f);
		manager.setSalary(555.45);
		manager.setAnnualIncome(new BigDecimal(9000.5));
		manager.setDateOfBirth(Date.valueOf("1988-01-27"));
		manager.setRegistrationTime(Timestamp.valueOf("2000-10-10 04:04:04"));
		manager.setSleepTime(Time.valueOf("05:05:05"));
		manager.setGraduated(true);
		
		Address address = new Address();
		address.setBuilding(12);
		address.setRoad(34);
		address.setBlock(56);
		address.setCity("Darkulaib");
		
		manager.setAddress(address);
		
		List<Certificate> certificates = new ArrayList<Certificate>();
		
		Certificate certificate1 = new Certificate();
		certificate1.setEmployee(manager);
		certificate1.setName("DB2");
		certificate1.setYear(2010);
		certificates.add(certificate1);
		
		Certificate certificate2 = new Certificate();
		certificate2.setEmployee(manager);
		certificate2.setName("JavaEE");
		certificate2.setYear(2020);
		certificates.add(certificate2);
		
		manager.setCertificates(certificates);
		
		employeeBean.saveEmployee(manager);
		
		response.getWriter().println(manager.getId());
	}
}
