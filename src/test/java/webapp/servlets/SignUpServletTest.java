package webapp.servlets;

import server.dbService.DBService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Evgeny Levin
 */
public class SignUpServletTest {

    private HttpServletRequest getMockedRequest(String login, String password, String firstName, String lastName, String middleName, String internal) {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("first_name")).thenReturn(firstName);
        when(request.getParameter("last_name")).thenReturn(lastName);
        when(request.getParameter("middle_name")).thenReturn(middleName);
        when(request.getParameter("internal")).thenReturn(internal);

        return request;
    }

    @Test
    public void doPost() throws Exception {
        DBService dbService = mock(DBService.class);

        String login = "testLogin";
        String password = "testPassword";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        String middleName = "testMiddleName";
        String internal = "testIntenal";

        HttpServletRequest request = getMockedRequest(login, password, firstName, lastName, middleName, internal);
        MockHttpServletResponse response = new MockHttpServletResponse();

        new SignUpServlet().doPost(request, response);

        verify(request, times(1)).getParameter("login");
        verify(request, times(1)).getParameter("password");
        verify(request, times(1)).getParameter("first_name");
        verify(request, times(1)).getParameter("last_name");
        verify(request, times(1)).getParameter("middle_name");
        verify(request, times(1)).getParameter("internal");
        verify(dbService, times(1)).addUser(login, password, Boolean.parseBoolean(internal), firstName, lastName, middleName);

        assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
    }

    @Test
    public void doPostEmptyLogin() throws Exception {
        DBService dbService = mock(DBService.class);

        String login = null;
        String password = "testPassword";
        String firstName = "testFirstName";
        String lastName = "testLastName";
        String middleName = "testMiddleName";
        String internal = "testIntenal";

        HttpServletRequest request = getMockedRequest(login, password, firstName, lastName, middleName, internal);
        MockHttpServletResponse response = new MockHttpServletResponse();

        new SignUpServlet().doPost(request, response);

        verify(request, times(1)).getParameter("login");
        verify(dbService, times(0)).addUser(login, password, Boolean.parseBoolean(internal), firstName, lastName, middleName);

        assertEquals(response.getStatus(), HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void doPostEmptyPassword() throws Exception {
        DBService dbService = mock(DBService.class);

        String login = "login";
        String password = null;
        String firstName = "testFirstName";
        String lastName = "testLastName";
        String middleName = "testMiddleName";
        String internal = "testIntenal";

        HttpServletRequest request = getMockedRequest(login, password, firstName, lastName, middleName, internal);
        MockHttpServletResponse response = new MockHttpServletResponse();

        new SignUpServlet().doPost(request, response);

        verify(request, times(1)).getParameter("password");
        verify(dbService, times(0)).addUser(login, password, Boolean.parseBoolean(internal), firstName, lastName, middleName);

        assertEquals(response.getStatus(), HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void doPostEmptyFirstName() throws Exception {
        DBService dbService = mock(DBService.class);

        String login = "login";
        String password = "password";
        String firstName = null;
        String lastName = "testLastName";
        String middleName = "testMiddleName";
        String internal = "testIntenal";

        HttpServletRequest request = getMockedRequest(login, password, firstName, lastName, middleName, internal);
        MockHttpServletResponse response = new MockHttpServletResponse();

        new SignUpServlet().doPost(request, response);

        verify(request, times(1)).getParameter("first_name");
        verify(dbService, times(0)).addUser(login, password, Boolean.parseBoolean(internal), firstName, lastName, middleName);

        assertEquals(response.getStatus(), HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void doPostEmptyLastName() throws Exception {
        DBService dbService = mock(DBService.class);

        String login = "login";
        String password = "password";
        String firstName = "firstName";
        String lastName = null;
        String middleName = "testMiddleName";
        String internal = "testIntenal";

        HttpServletRequest request = getMockedRequest(login, password, firstName, lastName, middleName, internal);
        MockHttpServletResponse response = new MockHttpServletResponse();

        new SignUpServlet().doPost(request, response);

        verify(request, times(1)).getParameter("last_name");
        verify(dbService, times(0)).addUser(login, password, Boolean.parseBoolean(internal), firstName, lastName, middleName);

        assertEquals(response.getStatus(), HttpServletResponse.SC_BAD_REQUEST);
    }

}