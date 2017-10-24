package main;

import dbService.DBService;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import servlets.LogoutServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        dbService.printConnectInfo();

        Server server = new Server(8080);

        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/html");

        JDBCLoginService loginService = new JDBCLoginService("JCGRealm", "properties/jdbcrealm_Postgres.properties");
        //JDBCLoginService loginService = new JDBCLoginService("JCGRealm", "properties/jdbcrealm_Oracle.properties");

        context.addServlet(new ServletHolder(new LogoutServlet()), LogoutServlet.PAGE_URL);



        server.addBean(loginService);
        server.setHandler(context);
        server.start();
        server.join();
    }
}
