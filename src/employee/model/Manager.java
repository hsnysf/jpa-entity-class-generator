package employee.model;

import org.apache.commons.beanutils.BeanUtils;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;

@Entity
@Table(name="manager")
@PrimaryKeyJoinColumn(name = "mngr_id")
@Inheritance(strategy=InheritanceType.JOINED)
public class Manager extends Employee {

	private static final long serialVersionUID = 1L;

	@Column(name="mngr_degree")
	private Integer degree;
	@Column(name="mngr_allowance")
	private BigDecimal allowance;

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setAllowance(BigDecimal allowance) {
		this.allowance = allowance;
	}

	public BigDecimal getAllowance() {
		return allowance;
	}

	public boolean equals(Object object) {

 		if(object != null && object instanceof Manager){

			Manager manager = (Manager) object;

			if(manager.getId() != null && this.getId() != null){
				return manager.getId() == this.getId();
			}else if(manager.getId() == null && this.getId() == null){
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