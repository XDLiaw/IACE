package core.util;

public class StringUtil {

	public static String filterHtml(String htmlString) {
		if (htmlString == null) {
			return null;
		}
		return htmlString.replaceAll("\\<[^>]*>","").replaceAll("&.*?;","");
	}

}
