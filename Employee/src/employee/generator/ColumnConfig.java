package employee.generator;

public class ColumnConfig {

	public String name;
	public int type;
	public String methodName;
	public String propertyName;
	public String propertyType;
	public String relationPropertyName;
	public String relationMethodName;
	public ColumnConfig relationColumn;
	public TableConfig relationTable;
	public boolean primaryKey;
	
	@Override
	public String toString() {
		return name;
	}
}
