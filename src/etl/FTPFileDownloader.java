package etl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import connection.DBConnection;
import connection.MySQLConnection;
import constant.Action;
import constant.Result;
import control.Configuration;
import log.ILog;
import log.Log;
import log.MySQLLog;

public class FTPFileDownloader {

	public static void download(Configuration conf, Log log) throws SQLException {

		boolean isDownloaded = false;
		FTPClient ftpClient = new FTPClient();
		ILog mySQLLog = new MySQLLog();

		mySQLLog.logStartDownloadFTPFeed(conf, log);

		try {
			ftpClient.connect(conf.getHostName(), conf.getPort());
			boolean isLogin = ftpClient.login(conf.getUserHost(), conf.getUserPass());
			if (isLogin) {
				ftpClient.enterLocalPassiveMode();
				ftpClient.setConnectTimeout(10000);
				ftpClient.setFileType(2);
				ftpClient.changeWorkingDirectory(conf.getRemoteDir());
				File f = new File(conf.getLocalDir());
				if (!f.exists()) {
					f.mkdir();
				}
				File fDes = new File(conf.getLocalDir() + File.separator + log.getFeedName());
				OutputStream os = new BufferedOutputStream(new FileOutputStream(fDes));
				isDownloaded = ftpClient.retrieveFile(log.getFeedName(), os);
				ftpClient.disconnect();
				os.close();
				if (!isDownloaded) {
					fDes.delete();
				}
			}
		} catch (IOException io) {
			System.out.println("FTPReceiver Error: " + io.getMessage());
		} finally {
			if (isDownloaded) {
				System.out.println(conf.getSrcFeed() + ": Finish download file: " + log.getFeedName());
				mySQLLog.logAfterDownloadFTPFeed(conf, log);
			} else {
				System.out.println(conf.getSrcFeed() + ": Failed download file: " + log.getFeedName());
			}
			mySQLLog.close();
		}
	}

	public static void main(String[] args) throws SQLException {

		DBConnection mySQLConn = new MySQLConnection();
		List<Configuration> lstConf = mySQLConn.loadAllConfs();

		String ftpDownload = Action.FTP_DOWNLOAD.name().toLowerCase();
		String ftpUpload = Action.FTP_UPLOAD.name().toLowerCase();
		String ftpUploaded = Result.UPLOADED.name().toLowerCase();
		String ftpDownloadFailed = Result.FAILED.name().toLowerCase();

		System.out.println("DOWNLOAD FILE FROM FTPSERVER\n");
		for (Configuration conf : lstConf) {
			int id = conf.getIdConf();
			String sqlGetLogsDownloadFTP = "SELECT * FROM data_file_log WHERE (ID_CONF=" + id + " AND ACTION_TYPE='"
					+ ftpDownload + "' AND LOG_STATUS='" + ftpDownloadFailed + "')" + " OR (ID_CONF=" + id
					+ " AND ACTION_TYPE='" + ftpUpload + "' AND LOG_STATUS='" + ftpUploaded + "')";
			List<Log> lstLog = mySQLConn.getLogs(sqlGetLogsDownloadFTP);
			if (lstLog.size() > 0) {
				for (Log log : lstLog) {
					download(conf, log);
				}
			}
		}
	}
}
