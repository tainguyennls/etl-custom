package notification;

public class Client {

	private String user;
	private String pass;
	private String source;
	private String urlConn;
	private String table;
	private String columns;
	private String server;
	private String script;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSource() {
		return source;
	}
	public String getUrlConn() {
		return urlConn;
	}
	public void setUrlConn(String urlConn) {
		this.urlConn = urlConn;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
}
