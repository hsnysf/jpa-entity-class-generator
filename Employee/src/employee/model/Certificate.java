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

@Entity
@Table(name="certificate")
public class Certificate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cert_id")
	private Integer id;
	@JoinColumn(name="cert_employee_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Employee employee;
	@Column(name="cert_name")
	private String name;
	@Column(name="cert_year")
	private Integer year;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getYear() {
		return year;
	}

	public boolean equals(Object object) {

 		if(object != null && object instanceof Certificate){

			Certificate certificate = (Certificate) object;

			if(certificate.getId() != null && this.getId() != null){
				return certificate.getId() == this.getId();
			}else if(certificate.getId() == null && this.getId() == null){
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