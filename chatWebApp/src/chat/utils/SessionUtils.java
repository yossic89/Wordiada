package chat.utils;

import chat.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static boolean getIsComputer (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        System.out.println(session.getAttributeNames());
        Object sessionAttribute = session != null ? session.getAttribute("comp") : null;
        String str=sessionAttribute != null ? sessionAttribute.toString() : null;
        if (str==null) return false;
        if (str.equals("true")) return true;
        else return false;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}