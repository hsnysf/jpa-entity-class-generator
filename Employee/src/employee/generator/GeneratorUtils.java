package employee.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeneratorUtils {

	//Get Connection to Postgres Database
	public static Connection getConnection() throws Exception{
		
		return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
	}
	
	//Initialize Postgres Database Metadata
	static DatabaseMetaData databaseMetaData;

	static {

		try {

			Connection connection = getConnection();

			databaseMetaData = connection.getMetaData();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	//Get Table Name Configuration Mapping
	public static Map<String, TableConfig> getTableMap() throws Exception {

		Map<String, TableConfig> tableMap = new LinkedHashMap<String, TableConfig>();
		
		//Get All Tables in Database
		ResultSet result = databaseMetaData.getTables(null, null, null, new String[] {"TABLE"});
		
		while (result.next()) {
			
			//Create Table Configuration Object
			TableConfig tableConfig = new TableConfig();
			
			//Get Table Name
			tableConfig.name = result.getString("table_name");
			//Get Table Class Name Equivalent example: person_details to PersonDetails
			tableConfig.className = getTableClassName(tableConfig.name);
			//Get Table Class Object Name Equivalent example: person_details to personDetails
			tableConfig.propertyName = getTablePropertyName(tableConfig.name);
			
			//Get All Columns for Table Name
			ResultSet columnSet = databaseMetaData.getColumns(null, null, tableConfig.name, null);
			
			while (columnSet.next()) {
				
				ColumnConfig column = new ColumnConfig();
				//Get Column Name
				column.name = columnSet.getString("column_name");
				//Get Column Types per java example: VARCHAR to String
				column.type = columnSet.getInt("data_type");
				//Get Column Method Name Equivalent example: prsn_last_name to LastName
				column.methodName = getColumnMethodName(column.name);
				//Get Column Property Name Equivalent example: prsn_last_name to lastName
				column.propertyName = getColumnPropertyName(column.name);
				//Get Property Data Type for Column Types per java
				column.propertyType = getColumnPropertyType(column.type);
				//Get Column Relation Property Name Equivalent example: prsn_home_address_id to homeAddress
				column.relationPropertyName = getRelationColumnPropertyName(column.name);
				//Get Column Relation Method Name Equivalent example: prsn_home_address_id to HomeAddress
				column.relationMethodName = getRelationColumnMethodName(column.name);
				
				//Check if column type is date so mark table as has column with date type
				if(column.type == Types.DATE) {
					tableConfig.hasSqlDate = true;
				//Check if column type is timestamp so mark table as has column with timestamp type
				}else if(column.type == Types.TIMESTAMP) {
					tableConfig.hasSqlTimestamp = true;
					//Check if column type is time so mark table as has column with time type
				}else if(column.type == Types.TIME) {
					tableConfig.hasSqlTime = true;
					//Check if column type is array so mark table as has column with array type
				}else if(column.type == Types.ARRAY) {
					tableConfig.hasSqlArray = true;
					//Check if column type is decimal so mark table as has column with decimal type
				}else if(column.type == Types.DECIMAL) {
					tableConfig.hasBigDecimal = true;
				//Check if column type is decimal so mark table as has column with decimal type
				}else if(column.type == Types.NUMERIC) {
					tableConfig.hasBigDecimal = true;
				}
				
				//Add Column Configuration To Table Column List
				tableConfig.columns.put(column.name, column);
			}
			
			//Add Table Configuration To List
			tableMap.put(tableConfig.name, tableConfig);
		}
		
		//Get Primary Key Column for each table
		for(TableConfig tableConfig : tableMap.values()) {
			
			//Get Table Primary Key Column
			ResultSet primarySet = databaseMetaData.getPrimaryKeys(null, null, tableConfig.name);

			if (primarySet.next()) {

				String primaryColumn = primarySet.getString("column_name");
				
				//Get Column Configuration for Primary Key
				ColumnConfig columnConfig = tableConfig.columns.get(primaryColumn);
				
				//Mark Column as Primary Key
				columnConfig.primaryKey = true;
				
				//Mark Column as Primary Key for Table
				tableConfig.primaryColumn = columnConfig;
			}
			
			//Get Foreign Keys Columns for Table
			ResultSet importedKeysSet = databaseMetaData.getImportedKeys(null, null, tableConfig.name);
			
			while(importedKeysSet.next()) {
				
				//Get Current Table Foreign Column
				String column = importedKeysSet.getString("fkcolumn_name");
				//Get Relation Primary Table
				String relationTable = importedKeysSet.getString("pktable_name");
				//Get Relation Primary Table Column
				String relationColumn = importedKeysSet.getString("pkcolumn_name");
				
				//Get Current Table Foreign Column
				ColumnConfig columnConfig = tableConfig.columns.get(column);
				
				//Point Relation Table of Foreign Column to Primary Key Table
				columnConfig.relationTable = tableMap.get(relationTable);
				//Point Relation Primary Column of Foreign Column to Primary Key Column
				columnConfig.relationColumn = columnConfig.relationTable.columns.get(relationColumn);
				
				//Mark Table as has Foreign keys with other tables only if Foreign columns are not primary
				if(!columnConfig.primaryKey) {
					tableConfig.hasImportedKeys = true;
				}
			}
		}
		
		for(TableConfig tableConfig : tableMap.values()) {
			
			//Get Foreign Keys Columns from other Tables
			ResultSet exportedKeysSet = databaseMetaData.getExportedKeys(null, null, tableConfig.name);

			while (exportedKeysSet.next()) {

				//Get Relation Primary Table
				String table = exportedKeysSet.getString("fktable_name");
				//Get Relation Table Foreign Column
				String column = exportedKeysSet.getString("fkcolumn_name");
				
				//Get Relation Table Configuration
				TableConfig relationTableConfig = tableMap.get(table);
				//Get Relation Table Foreign Column Configuration
				ColumnConfig relationColumnConfig = relationTableConfig.columns.get(column);
				
				//Mark Current Table with has Child Tables if Primary Column of Exported Table refer to Current Table
				if(relationTableConfig.primaryColumn.relationTable == tableConfig) {
					tableConfig.hasChildTables = true;
				}else {
					//Get Relation Table & Column To Current Table Exported Relations if Column is not primary
					tableConfig.exportedKeys.put(relationTableConfig, relationColumnConfig);
				}
			}
			
			//Mark Other Table as Parent of Current Table if Primary Key refer to that Table
			tableConfig.parentTable = tableConfig.primaryColumn.relationTable;
		}
		
		return tableMap;
	}
	
	//Get Table Class Name Equivalent example: person_details to PersonDetails
	public static String getTableClassName(String name) {

		char[] chars = name.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (i == 0 || chars[i - 1] == '_') {
				chars[i] = Character.toUpperCase(chars[i]);
			} else {
				chars[i] = Character.toLowerCase(chars[i]);
			}
		}

		name = new String(chars);
		name = name.replaceAll("_", "");

		return name;
	}
	
	//Get Table Property Name Equivalent example: person_details to personDetails
	public static String getTablePropertyName(String name) {

		char[] chars = name.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (i == 0 || chars[i - 1] != '_') {
				chars[i] = Character.toLowerCase(chars[i]);
			} else {
				chars[i] = Character.toUpperCase(chars[i]);
			}
		}

		name = new String(chars);
		name = name.replaceAll("_", "");

		return name;
	}
	
	//Get Column Property Type Equivalent Data Type example: VARCHAR to String
	public static String getColumnPropertyType(int columnType) {

		if (columnType == Types.VARCHAR) {
			return "String";
		}else if (columnType == Types.CHAR) {
			return "String";
		}else if (columnType == Types.SMALLINT) {
			return "Short";
		}else if (columnType == Types.INTEGER) {
			return "Integer";
		} else if (columnType == Types.BIGINT) {
			return "Long";
		}else if (columnType == Types.FLOAT) {
			return "Float";
		}else if (columnType == Types.REAL) {
			return "Float";
		}else if (columnType == Types.DOUBLE) {
			return "Double";
		}else if (columnType == Types.DECIMAL) {
			return "BigDecimal";
		}else if (columnType == Types.NUMERIC) {
			return "BigDecimal";
		}else if (columnType == Types.DATE) {
			return "Date";
		} else if (columnType == Types.TIMESTAMP) {
			return "Timestamp";
		}else if (columnType == Types.TIME) {
			return "Time";
		} else if (columnType == Types.BOOLEAN) {
			return "Boolean";
		} else if (columnType == Types.BIT) {
			return "Boolean";
		} else if (columnType == Types.ARRAY) {
			return "Array";
		} else {
			return "String";
		}
	}
	
	//Get Column Property Name Equivalent example: prsn_last_name to lastName
	public static String getColumnPropertyName(String name) {

		int index = name.indexOf("_") + 1;

		if (index != 0) {
			name = name.substring(index);
		}

		char[] chars = name.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (i != 0 && chars[i - 1] == '_') {
				chars[i] = Character.toUpperCase(chars[i]);
			} else {
				chars[i] = Character.toLowerCase(chars[i]);
			}
		}

		name = new String(chars);
		name = name.replaceAll("_", "");

		return name;
	}
	
	//Get Column Method Name Equivalent example: prsn_last_name to LastName
	public static String getColumnMethodName(String name) {

		int index = name.indexOf("_");

		if (index != -1) {
			name = name.substring(index + 1);
		}

		char[] chars = name.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (i == 0 || chars[i - 1] == '_') {
				chars[i] = Character.toUpperCase(chars[i]);
			} else {

				chars[i] = Character.toLowerCase(chars[i]);
			}
		}

		name = new String(chars);
		name = name.replaceAll("_", "");

		return name;
	}
	
	//Get Relation Column Property Name Equivalent example: prsn_home_address_id to homeAddress
	public static String getRelationColumnPropertyName(String name) {

		int index = name.indexOf("_");

		if (index != -1) {
			name = name.substring(index + 1);
		}

		int lastIndex = name.lastIndexOf("_");

		if (lastIndex != -1) {
			name = name.substring(0, lastIndex);
		}

		char[] chars = name.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (i != 0 && chars[i - 1] == '_') {
				chars[i] = Character.toUpperCase(chars[i]);
			} else {
				chars[i] = Character.toLowerCase(chars[i]);
			}
		}

		name = new String(chars);
		name = name.replaceAll("_", "");

		return name;
	}
	
	//Get Relation Column Method Name Equivalent example: prsn_home_address_id to HomeAddress
	public static String getRelationColumnMethodName(String name) {

		int index = name.indexOf("_");

		if (index != -1) {
			name = name.substring(index + 1);
		}

		int lastIndex = name.lastIndexOf("_");

		if (lastIndex != -1) {
			name = name.substring(0, lastIndex);
		}

		char[] chars = name.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (i == 0 || chars[i - 1] == '_') {
				chars[i] = Character.toUpperCase(chars[i]);
			} else {
				chars[i] = Character.toLowerCase(chars[i]);
			}
		}

		name = new String(chars);
		name = name.replaceAll("_", "");

		return name;
	}
}
