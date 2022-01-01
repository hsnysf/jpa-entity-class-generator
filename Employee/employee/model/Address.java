package employee.model;

import org.apache.commons.beanutils.BeanUtils;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
@Table(name="address")
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="addr_id")
	private Integer id;
	@Column(name="addr_building")
	private Integer building;
	@Column(name="addr_road")
	private Integer road;
	@Column(name="addr_block")
	private Integer block;
	@Column(name="addr_city")
	private String city;
	@OneToMany(mappedBy="address", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Employee> employees;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setBuilding(Integer building) {
		this.building = building;
	}

	public Integer getBuilding() {
		return building;
	}

	public void setRoad(Integer road) {
		this.road = road;
	}

	public Integer getRoad() {
		return road;
	}

	public void setBlock(Integer block) {
		this.block = block;
	}

	public Integer getBlock() {
		return block;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public boolean equals(Object object) {

 		if(object != null && object instanceof Address){

			Address address = (Address) object;

			if(address.getId() != null && this.getId() != null){
				return address.getId() == this.getId();
			}else if(address.getId() == null && this.getId() == null){
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