package chat.utils;

import chatEngine.chat.ChatManager;
import chatEngine.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static chat.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    public static UserManager getUserManager(ServletContext servletContext) {
	if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
	    servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
	}
	return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static ChatManager getChatManager(ServletContext servletContext) {
	if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
	    servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
	}
	return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
	String value = request.getParameter(name);
	if (value != null) {
	    try {
		return Integer.parseInt(value);
	    } catch (NumberFormatException numberFormatException) {
	    }
	}
	return INT_PARAMETER_ERROR;
    }
}
