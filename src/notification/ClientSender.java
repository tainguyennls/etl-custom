package notification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.DBConnection;
import connection.MySQLConnection;
import connection.SQLServerConnection;

public class ClientSender implements INotification {

	public ArrayList<Client> getClient() throws SQLException{
		ArrayList<Client> lstClient = new ArrayList<>();
		String sql = "SELECT * FROM client";
		DBConnection dbConnection = new MySQLConnection();
		Connection conn = dbConnection.getConn();

		ResultSet rs = conn.createStatement().executeQuery(sql);
		while(rs.next()){
			Client client = new Client();

			client.setUser(rs.getString("USER"));
			client.setPass(rs.getString("PASS"));
			client.setSource(rs.getString("SOURCE"));
			client.setUrlConn(rs.getString("URL_CONN"));
			client.setTable(rs.getString("TABLE"));
			client.setColumns(rs.getString("COLUMNS"));
			client.setServer("SERVER_TYPE");
			client.setScript("SCRIPT_NOTI");

			lstClient.add(client);
		}
		dbConnection.close(conn);
		return lstClient;
	}

	@Override
	public void send() throws SQLException{
		DBConnection dbConnection = null;
		Connection conn;

		ArrayList<Client> lstCient = getClient();

		for (Client client : lstCient) {

			String user = client.getUser();
			String pass = client.getPass();
			String url = client.getUrlConn();

			if ("mysql".equals(client.getServer())) {
				dbConnection = new MySQLConnection(url, user, pass);
			} else if ("sqlserver".equals(client.getServer())) {
				dbConnection = new SQLServerConnection(url, user, pass);
			}
			
			String sql = "INSERT INTO " + client.getTable() + "(" + client.getColumns() + ") VALUES("+ client.getScript()+")"; 
			conn = dbConnection.getConn();
			int update = conn.createStatement().executeUpdate(sql);

			if(update > 0){
				System.out.println("Success when notifiction to " + client.getSource());
			}
			else{
				System.out.println("Failed when notifiction to " + client.getSource());
			}

			dbConnection.close(conn);
		}
	}
}
