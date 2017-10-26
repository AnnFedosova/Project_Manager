package main;

import dbService.DBService;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import servlets.LogoutServlet;
import servlets.SignUpServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        dbService.printConnectInfo();

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
    }
}
