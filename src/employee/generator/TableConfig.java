package employee.generator;

import java.util.LinkedHashMap;
import java.util.Map;

public class TableConfig {

	public String name;
	public String className;
	public String propertyName;
	public boolean hasSqlDate;
	public boolean hasSqlTimestamp;
	public boolean hasSqlTime;
	public boolean hasBigDecimal;
	public boolean hasSqlArray;
	public boolean hasImportedKeys;
	public boolean hasChildTables;
	public ColumnConfig primaryColumn;
	public TableConfig parentTable;
	
	public Map<String, ColumnConfig> columns = new LinkedHashMap<String, ColumnConfig>();
	public Map<TableConfig, ColumnConfig> exportedKeys = new LinkedHashMap<TableConfig, ColumnConfig>();
	
	@Override
	public String toString() {

		return name + " " + columns;
	}
}
