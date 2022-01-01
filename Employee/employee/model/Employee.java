package employee.model;

import org.apache.commons.beanutils.BeanUtils;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Array;
import java.math.BigDecimal;

@Entity
@Table(name="employee")
@Inheritance(strategy=InheritanceType.JOINED)
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="emp_id")
	private Integer id;
	@Column(name="emp_name")
	private String name;
	@Column(name="emp_gender")
	private String gender;
	@Column(name="emp_age")
	private Short age;
	@Column(name="emp_cpr")
	private Integer cpr;
	@Column(name="emp_account_no")
	private Long accountNo;
	@Column(name="emp_gpa")
	private Float gpa;
	@Column(name="emp_salary")
	private Double salary;
	@Column(name="emp_annual_income")
	private BigDecimal annualIncome;
	@Column(name="emp_date_of_birth")
	private Date dateOfBirth;
	@Column(name="emp_registration_time")
	private Timestamp registrationTime;
	@Column(name="emp_sleep_time")
	private Time sleepTime;
	@Column(name="emp_graduated")
	private Boolean graduated;
	@Column(name="emp_hobbies")
	private Array hobbies;
	@JoinColumn(name="emp_address_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Address address;
	@OneToMany(mappedBy="employee", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Certificate> certificates;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	public Short getAge() {
		return age;
	}

	public void setCpr(Integer cpr) {
		this.cpr = cpr;
	}

	public Integer getCpr() {
		return cpr;
	}

	public void setAccountNo(Long accountNo) {
		this.accountNo = accountNo;
	}

	public Long getAccountNo() {
		return accountNo;
	}

	public void setGpa(Float gpa) {
		this.gpa = gpa;
	}

	public Float getGpa() {
		return gpa;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Double getSalary() {
		return salary;
	}

	public void setAnnualIncome(BigDecimal annualIncome) {
		this.annualIncome = annualIncome;
	}

	public BigDecimal getAnnualIncome() {
		return annualIncome;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setRegistrationTime(Timestamp registrationTime) {
		this.registrationTime = registrationTime;
	}

	public Timestamp getRegistrationTime() {
		return registrationTime;
	}

	public void setSleepTime(Time sleepTime) {
		this.sleepTime = sleepTime;
	}

	public Time getSleepTime() {
		return sleepTime;
	}

	public void setGraduated(Boolean graduated) {
		this.graduated = graduated;
	}

	public Boolean getGraduated() {
		return graduated;
	}

	public void setHobbies(Array hobbies) {
		this.hobbies = hobbies;
	}

	public Array getHobbies() {
		return hobbies;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public boolean equals(Object object) {

 		if(object != null && object instanceof Employee){

			Employee employee = (Employee) object;

			if(employee.getId() != null && this.getId() != null){
				return employee.getId() == this.getId();
			}else if(employee.getId() == null && this.getId() == null){
				return true;
			}else{
				return false;
			}

		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.getId() != null ? this.getId() : 0;
	}

	public String toString() {

		try{
			return BeanUtils.describe(this).toString();
		}catch(Exception e) {
			return null;
		}
	}
}