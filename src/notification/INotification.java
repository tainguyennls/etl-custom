package notification;

import java.sql.SQLException;

public interface INotification {
	void send() throws SQLException;
}
