package etl;

import java.sql.*;

import connection.DBConnection;
import connection.MySQLConnection;

public class LoadWarehouseToDataMart {

	public static void createTmpTableDataMart() {
		DBConnection dbConnection = new MySQLConnection();
		Connection dmConn = dbConnection.getConn();
		try {
			String sql = "CREATE TABLE IF NOT EXISTS DATAMART.tmp SELECT *  FROM DATAMART.export";
			dmConn.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		dbConnection.close(dmConn);
	}

	public static void load(String propsForDataMart) {
		createTmpTableDataMart();
		DBConnection dbConnection = new MySQLConnection();
		Connection dmConn = dbConnection.getConn();
		
		try {
			dmConn.setAutoCommit(false);
			String sqlInsert = "INSERT INTO DATAMART.tmp(" + propsForDataMart + ") SELECT " + propsForDataMart + " FROM WAREHOUSE.repository WHERE IS_ACTIVE=1";
			dmConn.createStatement().executeUpdate(sqlInsert);
			dmConn.commit();
			coppyDataTmpToDatamart();
		} catch (SQLException e) {
			//dmConn.rollback();
			e.printStackTrace();
		}
		dbConnection.close(dmConn);
	}

	public static void truncateDatamart(Connection dmConn) {
		try {
			String queryTruncate = "TRUNCATE TABLE DATAMART.export";
			PreparedStatement ps = dmConn.prepareStatement(queryTruncate);
			ps.execute();
		} catch (SQLException sql) {
			System.out.println("TRUNCATE Datamart: " + sql.getMessage());
		}
	}

	public static void coppyDataTmpToDatamart() {
		MySQLConnection mySQLConn = new MySQLConnection();
		Connection dmConn = mySQLConn.getConn();
		try {
			dmConn.setAutoCommit(false);
			String sqlCoppyData = "INSERT INTO DATAMART.export SELECT * FROM DATAMART.tmp";
			dmConn.createStatement().executeUpdate(sqlCoppyData);
			dmConn.commit();
			if(dmConn != null) {
				dmConn.rollback();
			}
		} catch (SQLException sql) {
			System.out.println("Copy DataMart: " + sql.getMessage());
		}
		mySQLConn.close(dmConn);
		System.out.println("Finish load data from WAREHOUSE to DATAMART");
	}
}
