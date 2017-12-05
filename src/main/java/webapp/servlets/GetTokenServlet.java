package webapp.servlets;

import server.dbService.DBService;
import webapp.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Evgeny Levin
 */
@WebServlet(name = "GetToken", urlPatterns = "/token")
public class GetTokenServlet extends HttpServlet {
    public GetTokenServlet() {}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.instance().getPage("token/gettoken.html", null));
    }
}
