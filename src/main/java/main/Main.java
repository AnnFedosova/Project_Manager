package main;

import dbService.DBException;
import dbService.DBService;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import servlets.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny Levin
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        dbService.printConnectInfo();

        dbSetupData(dbService);
        fillDB(dbService);

        Server server = new Server(8080);

        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/resources/");

        JDBCLoginService loginService = new JDBCLoginService("JCGRealm", "src/main/resources/cfg/jdbcrealm.properties");

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
            //Roles
            dbService.addRole("admin");
            dbService.addRole("user");

            //Users
            dbService.addAdmin("admin", "admin", "admin", "admin", "admin");

            //Positions
            dbService.addPosition("Receptionist");
            dbService.addPosition("Customer");
            dbService.addPosition("Quality assurance");
            dbService.addPosition("Developer");
            dbService.addPosition("Team leader");
            dbService.addPosition("System architect");
            dbService.addPosition("Analyst");
            dbService.addPosition("System administrator");


            //RequestStates
            dbService.addState("Postponed", true, false);
            dbService.addState("Complete", true, false);
            dbService.addState("Rejected", true, false);

            //RequestAndTaskStates
            dbService.addState("New", true, true);
            dbService.addState("On the go", true, true);
            dbService.addState("Closed", true, true);

            //TaskStates
            dbService.addState("Assigned", false, true);
            dbService.addState("Completed, waiting for testing", false, true);
            dbService.addState("In testing", false, true);


            //Priorities
            dbService.addPriority("High");
            dbService.addPriority("Medium");
            dbService.addPriority("Low");


        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    private static void fillDB(DBService dbService) {
        try {
            //Users
            dbService.addUser("leo", "leo", true, "Evgeny", "Levin", "Olegovich");
            dbService.addUser("belovivan", "belovivan", true, "Ivan", "Belov", "Aleksandrovich");
            dbService.addUser("musk", "musk", true, "Elon", "Mask", null);
            dbService.addUser("realtrump", "realtrump", false, "Donald", "Trump", null);

            //Projects
            long project1Id = dbService.addProject("Clean-Air Cabin", "Medical-grade air quality is delivered through a HEPA filtration system, specifically designed to prevent viruses and bacteria from entering the cabin. <BR>There are three modes: circulate with outside air, re-circulate inside air, and a bioweapon defense mode – which creates positive pressure inside the cabin to protect occupants.", "leo");
            long project2Id = dbService.addProject("Tesla autopilot", "All Tesla vehicles produced in our factory, including Model 3, have the hardware needed for full self-driving capability at a safety level substantially greater than that of a human driver.", "musk");
            long project3Id = dbService.addProject("World’s First Orbital-Class Rocket Reflight", "SpaceX believes a fully and rapidly reusable rocket is the pivotal breakthrough needed to substantially reduce the cost of space access.  The majority of the launch cost comes from building the rocket, which flies only once. Compare that to a commercial airliner – each new plane costs about the same as Falcon 9, but can fly multiple times per day, and conduct tens of thousands of flights over its lifetime. Following the commercial model, a rapidly reusable space launch vehicle could reduce the cost of traveling to space by a hundredfold. <BR>" +
                    "<BR>" +
                    "While most rockets are designed to burn up on reentry, SpaceX rockets can not only withstand reentry, but can also successfuly land back on Earth and refly again. ", "musk");


            //ProjectPositions
            dbService.addProjectPosition(3,  "Customer","realtrump");
            dbService.addProjectPosition(3, "Developer", "leo");
            dbService.addProjectPosition(3, "Team leader", "musk");
            dbService.addProjectPosition(1, "Team leader", "musk");
            dbService.addProjectPosition(1, "Developer", "musk");
            dbService.addProjectPosition(1, "Developer", "belovivan");

            //Requests
            long request1Id = dbService.addRequest("Create something amazing.", "Create something amazing, please.", "leo","realtrump", "high", 3);

            //Tasks
            dbService.addTask("Do it!", "Please", "leo", "belovivan", request1Id);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
