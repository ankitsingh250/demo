package com.demo.util;

public class StringUtil {

	public static Object getFieldValue(String str) {
		if(str==null) {
			return null;
		}
		if (str.startsWith("\'") && str.endsWith("\'")) {
			return new String(str.substring(1, str.length() - 1));
		}
		else if (!str.startsWith("\"") && !str.endsWith("\"") && (str.equals("true") || str.equals("false"))) {
			return Boolean.valueOf(str);

		}
		else if (!str.startsWith("\"") && !str.endsWith("\"")) {
			return Integer.valueOf(str);

		}
		else {
			try {
				System.out.println("quotes missing");
				throw new Exception("quotes missing");

			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

}
