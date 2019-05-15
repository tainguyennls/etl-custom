package settings;

import java.sql.Connection;
import java.sql.SQLException;

import connections.MySQLConnection;

public class MySQLSetting {

	public static void switchModeLocalInfile(String mode, String urlConn) throws SQLException {
		String sqlCommand;
		MySQLConnection mySQLConn = new MySQLConnection();
		Connection conn = mySQLConn.getConn(urlConn);
		if ("On".equalsIgnoreCase(mode)) {
			sqlCommand = "SET GLOBAL local_infile = 'On'";
		} else {
			sqlCommand = "SET GLOBAL local_infile = 'Off'";
		}
		conn.createStatement().executeQuery(sqlCommand);
		mySQLConn.close(conn);
	}
}
