package employee.generator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

public class TableModelGenerator {

	public static void main(String[] args) throws Exception {

		Map<String, TableConfig> tableMap = GeneratorUtils.getTableMap();

		for (TableConfig table : tableMap.values()) {

			StringBuilder builder = new StringBuilder();

			builder.append("package employee.model;");
			builder.append("\r\n\r\n");
			builder.append("import org.apache.commons.beanutils.BeanUtils;");
			builder.append("\r\n");
			builder.append("import javax.persistence.Entity;");
			builder.append("\r\n");
			builder.append("import javax.persistence.Table;");
			builder.append("\r\n");
			builder.append("import javax.persistence.Column;");
			
			if(table.parentTable == null) {
				builder.append("\r\n");
				builder.append("import javax.persistence.Id;");
				builder.append("\r\n");
				builder.append("import javax.persistence.GeneratedValue;");
				builder.append("\r\n");
				builder.append("import javax.persistence.GenerationType;");
				builder.append("\r\n");
				builder.append("import java.io.Serializable;");
			}
			
			if(table.hasImportedKeys) {
				builder.append("\r\n");
				builder.append("import javax.persistence.JoinColumn;");
				builder.append("\r\n");
				builder.append("import javax.persistence.ManyToOne;");
			}
			
			if(table.hasImportedKeys || !table.exportedKeys.isEmpty()) {
				builder.append("\r\n");
				builder.append("import javax.persistence.FetchType;");
				builder.append("\r\n");
				builder.append("import javax.persistence.CascadeType;");
			}
			
			if (!table.exportedKeys.isEmpty()) {
				builder.append("\r\n");
				builder.append("import java.util.List;");
				builder.append("\r\n");
				builder.append("import javax.persistence.OneToMany;");
			}
			
			if(table.parentTable != null || table.hasChildTables){
				builder.append("\r\n");
				builder.append("import javax.persistence.Inheritance;");
				builder.append("\r\n");
				builder.append("import javax.persistence.InheritanceType;");
			}
			
			if(table.parentTable != null) {
				builder.append("\r\n");
				builder.append("import javax.persistence.PrimaryKeyJoinColumn;");
			}
			
			if(table.hasSqlDate) {
				builder.append("\r\n");
				builder.append("import java.sql.Date;");
			}
			
			if(table.hasSqlTimestamp) {
				builder.append("\r\n");
				builder.append("import java.sql.Timestamp;");
			}
			
			if(table.hasSqlTime) {
				builder.append("\r\n");
				builder.append("import java.sql.Time;");
			}
			
			if(table.hasSqlArray) {
				builder.append("\r\n");
				builder.append("import java.sql.Array;");
			}
			
			if(table.hasBigDecimal) {
				builder.append("\r\n");
				builder.append("import java.math.BigDecimal;");
			}
			
			builder.append("\r\n\r\n");
			builder.append("@Entity");
			builder.append("\r\n");
			builder.append("@Table(name=\"" + table.name + "\")");
			
			if(table.parentTable != null) {
				builder.append("\r\n");
				builder.append("@PrimaryKeyJoinColumn(name = \"" + table.primaryColumn.name + "\")");
			}
			
			if(table.parentTable != null || table.hasChildTables) {
				builder.append("\r\n");
				builder.append("@Inheritance(strategy=InheritanceType.JOINED)");
			}
			
			builder.append("\r\n");
			
			if(table.parentTable != null) {
				builder.append("public class " + table.className + " extends " + table.parentTable.className + " {");
			}else {
				builder.append("public class " + table.className + " implements Serializable {");
			}
			
			builder.append("\r\n\r\n");
			builder.append("	private static final long serialVersionUID = 1L;");
			builder.append("\r\n");
			
			for (ColumnConfig column : table.columns.values()) {
				
				if(column.relationTable != null && !column.primaryKey) {
					
					builder.append("\r\n");
					builder.append("	@JoinColumn(name=\"" + column.name + "\")");
					builder.append("\r\n");
					builder.append("	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})");
					builder.append("\r\n");
					builder.append("	private " + column.relationTable.className + " " + column.relationPropertyName + ";");
					
				}else if(column.relationTable == null) {
					
					if(column.primaryKey) {
						
						builder.append("\r\n");
						builder.append("	@Id");
						builder.append("\r\n");
						builder.append("	@GeneratedValue(strategy = GenerationType.IDENTITY)");
					}
					
					builder.append("\r\n");
					builder.append("	@Column(name=\"" + column.name + "\")");
					builder.append("\r\n");
					builder.append("	private " + column.propertyType + " " + column.propertyName + ";");
				}
			}
			
			for (Entry<TableConfig, ColumnConfig> entry : table.exportedKeys.entrySet()) {

				TableConfig relationTableConfig = entry.getKey();
				ColumnConfig relationColumnConfig = entry.getValue();
				
				builder.append("\r\n");
				builder.append("	@OneToMany(mappedBy=\"" + relationColumnConfig.relationPropertyName + "\", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})");
				builder.append("\r\n");
				builder.append("	private List<" + relationTableConfig.className + "> " + relationTableConfig.propertyName + "s;");
			}
			
			for (ColumnConfig column : table.columns.values()) {
				
				if(column.relationTable != null && !column.primaryKey) {
					
					builder.append("\r\n\r\n");
					builder.append("	public void set" + column.relationMethodName + "(" + column.relationTable.className + " " + column.relationPropertyName + ") {");
					builder.append("\r\n");
					builder.append("		this." + column.relationPropertyName + " = " + column.relationPropertyName + ";");
					builder.append("\r\n");
					builder.append("	}");

					builder.append("\r\n\r\n");
					builder.append("	public " + column.relationTable.className + " get" + column.relationMethodName + "() {");
					builder.append("\r\n");
					builder.append("		return " + column.relationPropertyName + ";");
					builder.append("\r\n");
					builder.append("	}");
					
				}else if(column.relationTable == null) {
					
					builder.append("\r\n\r\n");
					builder.append("	public void set" + column.methodName + "(" + column.propertyType + " " + column.propertyName + ") {");
					builder.append("\r\n");
					builder.append("		this." + column.propertyName + " = " + column.propertyName + ";");
					builder.append("\r\n");
					builder.append("	}");
					builder.append("\r\n\r\n");
					builder.append("	public " + column.propertyType + " get" + column.methodName + "() {");
					builder.append("\r\n");
					builder.append("		return " + column.propertyName + ";");
					builder.append("\r\n");
					builder.append("	}");
				}
			}
			
			for (TableConfig relationTableConfig : table.exportedKeys.keySet()) {

				builder.append("\r\n\r\n");
				builder.append("	public void set" + relationTableConfig.className + "s(List<" + relationTableConfig.className + "> " + relationTableConfig.propertyName + "s) {");
				builder.append("\r\n");
				builder.append("		this." + relationTableConfig.propertyName + "s = " + relationTableConfig.propertyName + "s;");
				builder.append("\r\n");
				builder.append("	}");

				builder.append("\r\n\r\n");
				builder.append("	public List<" + relationTableConfig.className + "> get" + relationTableConfig.className + "s() {");
				builder.append("\r\n");
				builder.append("		return " + relationTableConfig.propertyName + "s;");
				builder.append("\r\n");
				builder.append("	}");
			}
			
			builder.append("\r\n\r\n");
		    builder.append("	public boolean equals(Object object) {");
		    builder.append("\r\n\r\n");
		    builder.append(" 		if(object != null && object instanceof " + table.className + "){");
		    builder.append("\r\n\r\n");
		    builder.append("			" + table.className + " " + table.propertyName + " = " + "(" + table.className + ") object;");
		    builder.append("\r\n\r\n");
		    builder.append("			if(" + table.propertyName + ".getId() != null && this.getId() != null){");
		    builder.append("\r\n");
		    builder.append("				return " + table.propertyName + ".getId() == this.getId();");
		    builder.append("\r\n");
		    builder.append("			}else if(" + table.propertyName + ".getId() == null && this.getId() == null){");
		    builder.append("\r\n");
		    builder.append("				return true;");
		    builder.append("\r\n");
		    builder.append("			}else{");
		    builder.append("\r\n");
		    builder.append("				return false;");
		    builder.append("\r\n");
		    builder.append("			}");
		    builder.append("\r\n\r\n");
		    builder.append("		} else {");
		    builder.append("\r\n");
		    builder.append("			return false;");
		    builder.append("\r\n");
		    builder.append("		}");
		    builder.append("\r\n");
		    builder.append("	}");
		    
		    builder.append("\r\n\r\n");
		    builder.append("	public int hashCode() {");
		    builder.append("\r\n");
		    builder.append("		return this.getId() != null ? this.getId() : 0;");
		    builder.append("\r\n");
		    builder.append("	}");
		    
		    builder.append("\r\n\r\n");
		    builder.append("	public String toString() {");
		    builder.append("\r\n\r\n");
		    builder.append("		try{");
		    builder.append("\r\n");
		    builder.append("			return BeanUtils.describe(this).toString();");
		    builder.append("\r\n");
		    builder.append("		}catch(Exception e) {");
		    builder.append("\r\n");
		    builder.append("			return null;");
		    builder.append("\r\n");
		    builder.append("		}");
		    builder.append("\r\n");
		    builder.append("	}");
			
			builder.append("\r\n");
			builder.append("}");
			
			Files.createDirectories(Paths.get("./src/main/java/employee/model"));
			Files.write(Paths.get("./src/main/java/employee/model/" + table.className + ".java"), builder.toString().getBytes());
		}
	}
}
