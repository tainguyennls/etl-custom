package etl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import connection.DBConnection;
import connection.MySQLConnection;
import constant.Action;
import constant.Result;
import control.Configuration;
import log.ILog;
import log.Log;
import log.MySQLLog;

public class LoadStagingToWareHouse {

	public static void load(Configuration conf, Log log) throws SQLException {

		System.out.println("Start load data from " + conf.getSrcFeed());

		ILog iLog = new MySQLLog();
		iLog.logStartLoadToDataWarehouse(conf, log);

		String propsSTForWH = conf.getPropsStagingForWareHouse();
		String propsOfWH = conf.getPropsWarehouse();
		String tableST = conf.getTableStaging();
		int srcId = conf.getIdConf();
		String srcFeed = conf.getSrcFeed();
		int numOfRecordsInserted = 0;
		int numRecordsUpdated = 0;

		String sqlGetAllDataFromStaging = "SELECT ID_ST," + propsSTForWH + " FROM " + tableST + " AS TMP";

		DBConnection mySQLConnST = new MySQLConnection("jdbc:mysql://localhost/STAGING");
		DBConnection mySQLConnWH = new MySQLConnection("jdbc:mysql://localhost/WAREHOUSE");
		Connection stConn = mySQLConnST.getConn();
		Connection whConn = mySQLConnWH.getConn();

		ResultSet rs = stConn.createStatement().executeQuery(sqlGetAllDataFromStaging);
		while (rs.next()) {

			String stNK = rs.getString("ST_NK");
			String sqlCheckExistStudent = "SELECT * FROM repository WHERE SOURCE_ID=" + srcId + " AND STUDENT_ID='" + stNK + "' AND IS_ACTIVE=1";
			ResultSet rsCheckStudent = whConn.createStatement().executeQuery(sqlCheckExistStudent);
			if (rsCheckStudent.next()) {
				String sqlUpdate = "UPDATE repository SET IS_ACTIVE=0 " + "WHERE SOURCE_ID=" + srcId + " AND STUDENT_ID='" + stNK + "' AND IS_ACTIVE=1";
				numRecordsUpdated += whConn.createStatement().executeUpdate(sqlUpdate);
			}
			String sqlInsert = "INSERT INTO WAREHOUSE.repository(" + propsOfWH + ")  SELECT " + propsSTForWH + " FROM STAGING." + tableST 
					+ " AS TMP WHERE ID_ST=" + rs.getInt("ID_ST");
			numOfRecordsInserted += whConn.createStatement().executeUpdate(sqlInsert);
		}
		stConn.createStatement().executeUpdate("TRUNCATE TABLE STAGING." + tableST);
		iLog.logAfterLoadToDataWarehouse(conf, log, numOfRecordsInserted, numRecordsUpdated);
		
		mySQLConnST.close(stConn);
		mySQLConnWH.close(whConn);

		System.out.println("Finished load data from " + srcFeed + " with " + numOfRecordsInserted + " records inserted into warehouse");

	}

	public static void main(String[] args) throws SQLException {
		DBConnection mySQLConn = new MySQLConnection();
		List<Configuration> lstConf = mySQLConn.loadAllConfs();

		String fileToStaging = Action.FILE_TO_STAGING.name().toLowerCase();
		String success = Result.SUCCESS.name().toLowerCase();

		System.out.println("LOAD DATA FROM STAGING TO WAREHOUSE\n");
		for (Configuration conf : lstConf) {
			String sqlLoadSTToWH = "SELECT * FROM data_file_log WHERE ID_CONF=" + conf.getIdConf()
			+ " AND ACTION_TYPE='" + fileToStaging + "' AND LOG_STATUS='" + success + "'";
			List<Log> lstLog = mySQLConn.getLogs(sqlLoadSTToWH);
			if (lstLog.size() > 0) {
				for (Log log : lstLog) {
					LoadStagingToWareHouse.load(conf, log);
				}
			}
		}
	}
}
