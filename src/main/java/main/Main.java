package main;

import dbService.DBService;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();


        Server server = new Server(8080);

        WebAppContext ctx = new WebAppContext();
        ctx.setResourceBase("src/main/html");

        JDBCLoginService loginService = new JDBCLoginService("JCGRealm", "properties/jdbcrealm_Postgres.properties");

//        HashLoginService loginService = new HashLoginService("JCGRealm");
//        loginService.setConfig("jcgrealm.txt");

        server.addBean(loginService);

        server.setHandler(ctx);

        server.start();
        server.join();
    }
}
