package main;

import dbService.DBException;
import dbService.DBService;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import servlets.*;

/**
 * @author Evgeny Levin
 */
public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        dbService.printConnectInfo();

        dbSetupData(dbService);

        Server server = new Server(8080);

        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/resources/");

        JDBCLoginService loginService = new JDBCLoginService("JCGRealm", "src/main/resources/config/jdbcrealm_Postgres.properties");
        //JDBCLoginService loginService = new JDBCLoginService("JCGRealm", "src/main/resources/config/jdbcrealm_Oracle.properties");

        addServlets(dbService, context);

        server.addBean(loginService);
        server.setHandler(context);
        server.start();
        server.join();
    }

    private static void addServlets(DBService dbService, ServletContextHandler context) {
        context.addServlet(new ServletHolder(new LogoutServlet()), LogoutServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new SignUpServlet(dbService)), SignUpServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new ProjectsServlet(dbService)), ProjectsServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new ProjectServlet(dbService)), ProjectServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new NewProjectServlet(dbService)), NewProjectServlet.PAGE_URL);
    }

    private static void dbSetupData(DBService dbService) {
        try {
            dbService.addNewRole("admin");
            dbService.addNewRole("user");
            dbService.addNewAdmin("admin", "admin", "admin", "admin", "admin");
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
