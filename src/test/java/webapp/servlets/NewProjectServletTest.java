package webapp.servlets;

import server.dbService.DBService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Evgeny Levin
 */
public class NewProjectServletTest {

    private HttpServletRequest getMockedRequest(String title, String description) {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("description")).thenReturn(description);

        return request;
    }


    @Test
    public void doGet() throws Exception {
    }

    @Test
    public void doPost() throws Exception {
        String projectTitle = "Test project title";
        String projectDescription = "Test project description";
        String userName = "testUserName";
        HttpServletRequest request = getMockedRequest(projectTitle, projectDescription);
        MockHttpServletResponse response = new MockHttpServletResponse();
        DBService dbService = mock(DBService.class);
        Principal user = mock(Principal.class);

        when(request.getUserPrincipal()).thenReturn(user);
        when(user.getName()).thenReturn(userName);
        when(dbService.addProject(projectTitle, projectDescription, userName)).thenReturn(0L);

        new NewProjectServlet(dbService).doPost(request, response);

        verify(request, times(1)).getParameter("title");
        verify(request, times(1)).getParameter("description");
        verify(request, times(1)).getUserPrincipal();
        verify(dbService, times(1)).addProject(projectTitle, projectDescription, userName);

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void doPostTitleEmpty() throws Exception {
        String projectTitle = null;
        String projectDescription = "Test project description";
        String userName = "testUserName";
        HttpServletRequest request = getMockedRequest(projectTitle, projectDescription);
        MockHttpServletResponse response = new MockHttpServletResponse();
        DBService dbService = mock(DBService.class);
        Principal user = mock(Principal.class);

        when(request.getUserPrincipal()).thenReturn(user);
        when(user.getName()).thenReturn(userName);

        new NewProjectServlet(dbService).doPost(request, response);

        verify(request, times(1)).getParameter("title");
        verify(dbService, times(0)).addProject(projectTitle, projectDescription, userName);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    public void doPostDescriptionEmpty() throws Exception {
        String projectTitle = "title";
        String projectDescription = null;
        String userName = "testUserName";
        HttpServletRequest request = getMockedRequest(projectTitle, projectDescription);
        MockHttpServletResponse response = new MockHttpServletResponse();
        DBService dbService = mock(DBService.class);
        Principal user = mock(Principal.class);

        when(request.getUserPrincipal()).thenReturn(user);
        when(user.getName()).thenReturn(userName);

        new NewProjectServlet(dbService).doPost(request, response);

        verify(request, times(1)).getParameter("description");
        verify(dbService, times(0)).addProject(projectTitle, projectDescription, userName);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }



}