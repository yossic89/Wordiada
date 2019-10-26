package chat.servlets;

import Engine.EngineController;
import Engine.GameEngine;
import chat.constants.Constants;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import chatEngine.users.User;
import chatEngine.users.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yossi_c89 on 13/07/2017.
 */

@WebServlet(name="MenuServlet", urlPatterns={"/menu"})
//@WebServlet("/menu")
@MultipartConfig
public class MenuServlet extends HttpServlet {
    private EngineController engineController = EngineController.getInstance();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "tableData":
                this.getTableData(req, resp);
                break;
            case "tableUsers":
                this.getUsersTable(req,resp);
                break;
            case "enterToGame":
                this.enterToGame(req,resp);
                break;
            case "logout":
                this.logout(req, resp);
                break;
            case "username":
                this.getUserName(req, resp);
                break;


        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.uploadNewGame(req, resp);
    }

    private void getUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String owner = (String )request.getSession(false).getAttribute(Constants.USERNAME);
        boolean isComp = ServletUtils.getUserManager(getServletContext()).getPlayerFromUserName(owner).isComputer();
        String[] res = new String[2];
        res[0] = owner;
        res[1] = isComp ? "Computer" : "Human";
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(res));
    }

    private void uploadNewGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Part config = request.getPart("config");
        Part dictName = request.getPart("dictName");
        Part dict = request.getPart("dict");
        String owner = (String )request.getSession(false).getAttribute(Constants.USERNAME);
        String configStr = this.convertStreamToString(config.getInputStream());
        String dictNameStr = this.convertStreamToString(dictName.getInputStream());
        String dictStr = this.convertStreamToString(dict.getInputStream());
        List<String> res = this.loadConfig(configStr, dictNameStr, owner);

        //if res size is 1 the config is ok, so upload dict
        if (res.size() == 1)
        {
            String gameName = res.get(0);
            if (!(this.uploadDict(dictStr, gameName)))
                res.add("Dict upload error");
        }
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.println(gson.toJson(res));
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        UserManager manager = ServletUtils.getUserManager(getServletContext());
        String userName = (String )request.getSession(false).getAttribute(Constants.USERNAME);
        manager.removeUser(userName);
        String usernameFromSession = SessionUtils.getUsername(request);

        if (usernameFromSession != null) {
            for (User user : manager.getUsers()) {
                if (user.getName().equals(usernameFromSession)) manager.getUsers().remove(user);
            }
            SessionUtils.clearSession(request);
        }
    }

    private void enterToGame(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        String game = request.getParameter("game");
        String user = (String )request.getSession(false).getAttribute(Constants.USERNAME);
        UserManager manager = ServletUtils.getUserManager(getServletContext());
        out.println(gson.toJson(this.engineController.registerPlayerToGame(user, game, manager)));
    }

    private List<String> loadConfig(String gameContent, String dictName, String owner)
    {
        GameEngine engine = new GameEngine();
        List<String> res = engine.loadConfFileOnline(gameContent);
        engine.config.setDictionaryName(dictName);
        if (res.size() == 0)
        {
            if (!(engineController.addLoadedGame(engine, owner))) {
                res.add("Game " + engine.config.getName() + " already exists");
            }
        }
        res.add(engine.config.getName());
        return res;
    }

    private boolean uploadDict(String dict, String game)
    {
        GameEngine eng = engineController.getLoadingGame(game);
        boolean res = false;
        if (eng != null)
        {
            eng.setDictionary(dict);
            res = engineController.addEngine(game);
        }
        return res;
    }

    private void getTableData(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.println(gson.toJson(this.engineController.getGameTable()));
    }

    private void getUsersTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        ArrayList<User> res = new ArrayList<>();
        res.addAll(ServletUtils.getUserManager(getServletContext()).getUsers());
        out.println(gson.toJson(res));
    }

}


