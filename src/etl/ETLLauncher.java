package etl;

import java.sql.SQLException;
import java.util.List;

import connection.DBConnection;
import connection.MySQLConnection;
import constant.Action;
import constant.Result;
import control.Configuration;
import log.Log;
import setting.MySQLSetting;

public class ETLLauncher {

	public ETLLauncher() throws SQLException {

		// 01. Download file from FTPServer
		System.out.println("\nLOAD CONFIGURATION\n");
		System.out.println("START ETL PROCESS");
		DBConnection mySQLConn = new MySQLConnection();
		List<Configuration> lstConf = mySQLConn.loadAllConfs();
		
		String ftpDownload = Action.FTP_DOWNLOAD.name().toLowerCase();
		String ftpUpload = Action.FTP_UPLOAD.name().toLowerCase();
		String ftpUploaded = Result.UPLOADED.name().toLowerCase();
		String failed = Result.FAILED.name().toLowerCase();
		String success = Result.SUCCESS.name().toLowerCase();
		String fileToStaging = Action.FILE_TO_STAGING.name().toLowerCase(); 
		
		System.out.println("\nDOWNLOAD FILE FROM FTPSERVER\n");
		for (Configuration conf : lstConf) {
			int id = conf.getIdConf();
			String sqlGetLogsDownloadFTP = "SELECT * FROM data_file_log WHERE (ID_CONF=" + id + " AND ACTION_TYPE='"
					+ ftpDownload + "' AND LOG_STATUS='" + failed + "')" + " OR (ID_CONF=" + id
					+ " AND ACTION_TYPE='" + ftpUpload + "' AND LOG_STATUS='" + ftpUploaded + "')";
			List<Log> lstLog = mySQLConn.getLogs(sqlGetLogsDownloadFTP);
			if (lstLog.size() > 0) {
				for (Log log : lstLog) {
					FTPFileDownloader.download(conf, log);
				}
			}
		}

		// 02. Extract CSV to STAGING
		System.out.println("\nEXTRACT DATA FROM CSV TO STAGING\n");
		DBConnection dbConnection = new MySQLConnection();
		MySQLSetting.switchModeLocalInfile("On", dbConnection.getConn()); // remove to ETL after, present only for test
		ExtractCSVToStaging ecs = new ExtractCSVToStaging();
		for (Configuration conf : lstConf) {
			String sqlLoadST = "SELECT * FROM data_file_log WHERE ID_CONF=" + conf.getIdConf() + " AND ACTION_TYPE='" + ftpDownload
								   + "' AND LOG_STATUS='" + success + "'";
			List<Log> lstLog = dbConnection.getLogs(sqlLoadST);
			if(lstLog.size() > 0) {
				for (Log log : lstLog) {
					ecs.extract(conf, log);
				}
			}
		}
		ecs.finish(); // close Connection

		// 03. Loading data from STAGING to WAREHOUSE
		System.out.println("\nLOAD DATA FROM STAGING TO WAREHOUSE\n");
		DBConnection dbCtConn = new MySQLConnection();
		for (Configuration conf : lstConf) {
			String sqlLoadWH = "SELECT * FROM data_file_log WHERE ID_CONF="+ conf.getIdConf()+ " AND ACTION_TYPE='" + fileToStaging
								 + "' AND LOG_STATUS='" + success +"'";
			List<Log> lstLogs = dbCtConn.getLogs(sqlLoadWH);
			if(lstLogs.size() > 0) {
				for (Log log : lstLogs) {
					LoadStagingToWareHouse.load(conf, log);
				}
			}
		}

		
		// 04. Loading data from WAREHOUSE to DATAMART
		//System.out.println("\nLOAD DATA FROM WAREHOUSE TO DATAMART\n");
		//LoadWarehouseToDataMart.load("ID_SK, STUDENT_ID, FULL_NAME, DATE_OF_BIRTH, ADDRESS, GENDER, TIME_IMPORT");

		// End Script
		System.out.println("\nEND ETL PROCESS");
	}

	public static void main(String[] args) throws SQLException {
		new ETLLauncher();
	}
}
