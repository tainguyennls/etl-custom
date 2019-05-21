/**
 * 
 */
package sql;

import control.Configuration;

public class GenerateQuery{

	public static String sqlCreateTable(Configuration conf) {
		String[] lstProps = conf.getPropsForStaging().split(",");
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(conf.getTableStaging());
		sb.append("(");
		for (int i = 0; i < lstProps.length; i++) {
			sb.append(lstProps[i].trim() + " text, ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("SOURCE_ID  text, SOURCE_NAME text, ");
		sb.append(" ID_ST int AUTO_INCREMENT primary key");
		sb.append(")");
		sb.append(" CHARACTER SET utf8mb4");
		return sb.toString();
	}
}
