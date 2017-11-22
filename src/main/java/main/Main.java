package main;

import dbService.DBException;
import dbService.DBService;
import dbService.entities.StateEntity;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import servlets.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * @author Evgeny Levin
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());
    private static String DBType = "AZURE";

    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        logger.info("DB info: " + '\n' + dbService.getConnectInfo());

        dbSetupData(dbService);
        logger.info("Data loaded");
        fillDB(dbService);
        logger.info("DB filled");


        int port;
        if (DBType.equals("AZURE")) {
            port = Integer.parseInt(System.getenv("HTTP_PLATFORM_PORT"));
        }
        else {
            port = 8080;
        }
        Server server = new Server(port);

        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/resources/");

        JDBCLoginService loginService = new JDBCLoginService("JCGRealm", "src/main/resources/cfg/jdbcrealm-Azure.properties");

        addServlets(dbService, context);
        logger.info("Servlets added");

        server.addBean(loginService);
        server.setHandler(context);
        server.start();
        logger.info("Server started at port " + port);
        server.join();
    }


    private static void addServlets(DBService dbService, ServletContextHandler context) {
        context.addServlet(new ServletHolder(new LoginServlet()), LoginServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new LogoutServlet()), LogoutServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new SignUpServlet(dbService)), SignUpServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new ProjectsServlet(dbService)), ProjectsServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new ProjectServlet(dbService)), ProjectServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new NewProjectServlet(dbService)), NewProjectServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new NewRequestServlet(dbService)), NewRequestServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new RequestServlet(dbService)), RequestServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new TaskServlet(dbService)), TaskServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new NewTaskServlet(dbService)), NewTaskServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new UserServlet(dbService)), UserServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new AdminServlet(dbService)), AdminServlet.PAGE_URL);
        context.addServlet(new ServletHolder(new EditTaskServlet(dbService)), EditTaskServlet.PAGE_URL);
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
            StateEntity statePostponed = new StateEntity("Postponed", true, false);
            StateEntity stateComplete = new StateEntity("Complete", true, false);
            StateEntity stateRejected = new StateEntity("Rejected", true, false);

            dbService.addState(statePostponed);
            dbService.addState(stateComplete);
            dbService.addState(stateRejected);

            //RequestAndTaskStates
            StateEntity stateNew = new StateEntity("New", true, true);
            StateEntity stateOnTheGo = new StateEntity("On the go", true, true);
            StateEntity stateClosed = new StateEntity("Closed", true, true);

            dbService.addState(stateNew);
            dbService.addState(stateOnTheGo);
            dbService.addState(stateClosed);

            //TaskStates
            StateEntity stateAssigned = new StateEntity("Assigned", false, true);
            StateEntity stateWaitingForTesting = new StateEntity("Complete, waiting for testing", false, true);
            StateEntity stateInTesting = new StateEntity("In testing", false, true);

            dbService.addState(stateAssigned);
            dbService.addState(stateWaitingForTesting);
            dbService.addState(stateInTesting);

            //StateTransitions
            dbService.addStateTransition(stateNew, stateOnTheGo);
            dbService.addStateTransition(stateNew, stateRejected);
            dbService.addStateTransition(stateNew, statePostponed);
            dbService.addStateTransition(statePostponed, stateNew);
            dbService.addStateTransition(stateOnTheGo, statePostponed);
            dbService.addStateTransition(stateOnTheGo, stateComplete);
            dbService.addStateTransition(stateComplete, stateOnTheGo);
            dbService.addStateTransition(statePostponed, stateOnTheGo);
            dbService.addStateTransition(stateComplete, stateClosed);
            dbService.addStateTransition(stateComplete, statePostponed);
            dbService.addStateTransition(statePostponed, stateComplete);

            dbService.addStateTransition(stateNew, stateAssigned);
            dbService.addStateTransition(stateAssigned, stateOnTheGo);
            dbService.addStateTransition(stateOnTheGo, stateAssigned);
            dbService.addStateTransition(stateOnTheGo, stateWaitingForTesting);
            dbService.addStateTransition(stateWaitingForTesting, stateInTesting);
            dbService.addStateTransition(stateInTesting, stateAssigned);
            dbService.addStateTransition(stateInTesting, stateClosed);

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
            dbService.addUser("musk", "musk", true, "Elon", "Musk", null);
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
            long request1Id = dbService.addRequest("Create something amazing.", "Create something amazing, please.", "leo","realtrump", "High", 3);

            //Tasks
            dbService.addTask("Do it!", "Please", "leo", 2, request1Id);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
