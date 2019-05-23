package etl;

import java.sql.*;

import connection.DBConnection;
import connection.MySQLConnection;

public class LoadWarehouseToDataMart {

	public static void createTmpTableDataMart() {

		DBConnection dbConnection = new MySQLConnection("jdbc:mysql://localhost/DATAMART");
		Connection dmConn = dbConnection.getConn();
		try {
			String sql = "CREATE TABLE IF NOT EXISTS DATAMART.tmp SELECT *  FROM DATAMART.datamart";
			dmConn.createStatement().execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void renamedatamartTotmp() {

		DBConnection dbConnection = new MySQLConnection("jdbc:mysql://localhost/DATAMART");
		Connection dmConn = dbConnection.getConn();
		try {

			String sqlRenamedtd = "RENAME TABLE datamart TO datamartnew";
			String sqlRenamettd = "RENAME TABLE tmp  TO datamart";
			String sqlRenamedtt = "RENAME TABLE datamartnew TO tmp";
			PreparedStatement pst1 = dmConn.prepareStatement(sqlRenamedtd);
			PreparedStatement pst2 = dmConn.prepareStatement(sqlRenamettd);
			PreparedStatement pst3 = dmConn.prepareStatement(sqlRenamedtt);
			pst1.execute();
			pst2.execute();
			pst3.execute();
		} catch (SQLException sql) {
			System.out.println("Copy DataMart: " + sql.getMessage());
		}
	}

	public static void load(String propsForDataMart) {

		createTmpTableDataMart();
		renamedatamartTotmp();
		DBConnection dbConnection = new MySQLConnection("jdbc:mysql://localhost/DATAMART");
		Connection dmConn = dbConnection.getConn();
		try {

			dmConn.setAutoCommit(false);
			String sqlInsert = "INSERT INTO DATAMART.tmp(" + propsForDataMart + ") SELECT " + propsForDataMart
					+ " FROM WAREHOUSE.repository WHERE IS_ACTIVE = 1";
			dmConn.createStatement().executeUpdate(sqlInsert);
			dmConn.commit();

		} catch (SQLException e) {

			try {
				if (dmConn != null)
					dmConn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		renameTmpToDatamart();
	}

	public static void renameTmpToDatamart() {

		DBConnection dbConnection = new MySQLConnection("jdbc:mysql://localhost/DATAMART");
		Connection dmConn = dbConnection.getConn();
		try {

			String sqlRenamettd = "RENAME TABLE tmp TO datamartnew";
			String sqlRenamedtt = "RENAME TABLE datamart  TO tmp";
			String sqlRenamedtd = "RENAME TABLE datamartnew TO datamart";
			PreparedStatement pst1 = dmConn.prepareStatement(sqlRenamettd);
			PreparedStatement pst2 = dmConn.prepareStatement(sqlRenamedtt);
			PreparedStatement pst3 = dmConn.prepareStatement(sqlRenamedtd);
			pst1.execute();
			pst2.execute();
			pst3.execute();

		} catch (SQLException sql) {
			System.out.println("Copy DataMart: " + sql.getMessage());
		} finally {
			dbConnection.close(dmConn);
			System.out.println("Finish load data from WAREHOUSE to DATAMART");
		}
	}
}
