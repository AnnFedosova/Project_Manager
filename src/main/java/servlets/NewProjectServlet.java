package servlets;

import dbService.DBException;
import dbService.DBService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.security.Principal;

/**
 * @author Evgeny Levin
 */
public class NewProjectServlet  extends HttpServlet {
    public static final String PAGE_URL = "/projects/new";
    private DBService dbService;

    public NewProjectServlet(DBService dbService) {
        this.dbService = dbService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("html/projects/new/new_project.html", null));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        //TextArea description = request.getParameter("description");
        StringBuffer description = new StringBuffer(request.getParameter("description"));

        int loc = (new String(description)).indexOf('\n');
        while(loc > 0){
            description.replace(loc, loc+1, "<BR>");
            loc = (new String(description)).indexOf('\n');
        }



        try {
            Principal user = request.getUserPrincipal();
            dbService.addNewProject(title, description.toString(), user.getName());
        } catch (DBException e) {
            e.printStackTrace();
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("Successfully!");
    }
}