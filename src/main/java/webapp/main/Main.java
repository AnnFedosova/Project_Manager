package webapp.main;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.*;
import server.dbService.DBException;
import server.dbService.DBService;
import server.dbService.DBType;
import server.dbService.entities.StateEntity;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import webapp.servlets.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Evgeny Levin
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());
    private static DBType dbType = DBType.POSTGRESQL_LOCALHOST;

    public static void main(String[] args) throws Exception {
        DBService dbService = DBService.getInstance();
        logger.info("DB info: " + '\n' + dbService.getConnectInfo());

        dbSetupData(dbService);
        logger.info("Data loaded");
        fillDB(dbService);
        logger.info("DB filled");


        int port;
        if (dbType == DBType.AZURE) {
            port = Integer.parseInt(System.getenv("HTTP_PLATFORM_PORT"));
        }
        else {
            port = 8080;
        }
        Server server = new Server(port);

        WebAppContext context = new WebAppContext();

        context.setResourceBase(Main.class.getClassLoader().getResource("css").toString().replace("css", ""));
        logger.info("Resource base setted for WebAppContext");

        context.setConfigurations(new Configuration[]
                {
                        new AnnotationConfiguration(),
                        new WebInfConfiguration(),
                        new WebXmlConfiguration(),
                        new MetaInfConfiguration(),
                        new FragmentConfiguration(),
                        new EnvConfiguration(),
                        new PlusConfiguration(),
                        new JettyWebXmlConfiguration()
                });

        URL classes = Main.class.getProtectionDomain().getCodeSource().getLocation();
        context.getMetaData().setWebInfClassesDirs(Collections.singletonList(Resource.newResource(classes)));


        context.setParentLoaderPriority(true);
        context.setContextPath("/");

        JDBCLoginService loginService;
        if (dbType == DBType.AZURE) {
            loginService = new JDBCLoginService("JCGRealm", Main.class.getClassLoader().getResource("jdbcrealm-Azure.properties").toString());
        }
        else {
            loginService = new JDBCLoginService("JCGRealm", Main.class.getClassLoader().getResource("jdbcrealm.properties").toString());
        }


        server.addBean(loginService);
        server.setHandler(context);
        server.start();
        logger.info("Server started on port " + port);
        server.join();
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
