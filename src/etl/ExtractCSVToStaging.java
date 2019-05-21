package etl;

import java.io.File;
import java.sql.Connection;
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
import sql.GenerateQuery;

public class ExtractCSVToStaging {
	
	private DBConnection mySQLConn;
	private Connection stConn;
	
	public ExtractCSVToStaging() {
		mySQLConn = new MySQLConnection("jdbc:mysql://localhost/STAGING");
		stConn = mySQLConn.getConn();
	}

	public void extract(Configuration conf, Log log) throws SQLException {

		boolean isError = false;
		int numRecordsExtracted = 0;
		ILog mySQLLog = new MySQLLog();
		mySQLLog.logStartExtractToStaging(conf, log);

		String sqlCret = GenerateQuery.sqlCreateTable(conf);
		String path = conf.getLocalDir() + File.separator + File.separator + log.getFeedName();
		String sqlLoad = "LOAD DATA LOCAL INFILE '" + path 
					   + "' INTO TABLE " + conf.getTableStaging()
					   + " FIELDS TERMINATED BY '" + conf.getFeedDelimiter() 
					   + "' LINES TERMINATED BY '\\n' IGNORE 1 ROWS"
					   + " SET SOURCE_ID='"+ conf.getIdConf()+"', SOURCE_NAME='" + conf.getSrcFeed()+"'";
		try {
			int index = stConn.createStatement().executeUpdate(sqlCret);
			if (index > 0) {
				String sqlIndex = "CREATE INDEX au_st_id ON STAGING." + conf.getTableStaging() + " (ID_ST)";
				stConn.createStatement().executeUpdate(sqlIndex);
			}
			numRecordsExtracted = stConn.createStatement().executeUpdate(sqlLoad);
		} catch (SQLException sql) {
			System.out.println(log.getFeedName() + " happend error when extracting to staging with " + sql.getMessage());
			isError = true;
		} finally {
			if (!isError) {
				System.out.println("Finish extract " + log.getFeedName() + " to staging ");
				mySQLLog.logAfterExtractToStaging(conf, log, numRecordsExtracted);
			} else {
				System.out.println("Failed extract " + log.getFeedName() + " to staging ");
			}
		}
	}
	
	public void finish() {
		if(null != stConn) {
			mySQLConn.close(stConn);
		}
	}

	public static void main(String[] args) throws SQLException {

		DBConnection mySQLConn = new MySQLConnection();
		List<Configuration> lstConf = mySQLConn.loadAllConfs();
		ExtractCSVToStaging e = new ExtractCSVToStaging();

		String ftpDownload = Action.FTP_DOWNLOAD.name().toLowerCase();
		String ftpDownloadSucc = Result.SUCCESS.name().toLowerCase();

		for (Configuration conf : lstConf) {
			int id = conf.getIdConf();
			String sqlGetLogLoadST = "SELECT * FROM data_file_log WHERE ID_CONF=" + id + " AND ACTION_TYPE='"
								   + ftpDownload + "' AND LOG_STATUS='" + ftpDownloadSucc + "'";
			List<Log> lstLog = mySQLConn.getLogs(sqlGetLogLoadST);
			if (lstLog.size() > 0) {
				for (Log log : lstLog) {
					e.extract(conf, log);
				}
			}
		}
		e.finish();
		
	}
}
