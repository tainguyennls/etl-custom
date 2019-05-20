package transform;

import java.text.SimpleDateFormat;
import java.util.Date;

@Deprecated
public class DateTransform {

	public String convertDateStr(String dateFormat, String dateString) {
		SimpleDateFormat results = new SimpleDateFormat("yyyy-MM-dd");
		String output = null;
		try {
			Date sdf = new SimpleDateFormat(dateFormat).parse(dateString);
			output = results.format(sdf);
		} catch (Exception e) {
			System.out.println("Date Transform: " + e.getMessage());
		}
		return output;
	}

	
	public String getCurrentDateStr() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

}
