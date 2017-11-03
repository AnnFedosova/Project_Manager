package servlets;

import org.eclipse.jetty.server.session.Session;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.mockito.Mockito.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.*;

/**
 * @author Evgeny Levin
 */
public class LogoutServletTest {
    @Test
    public void doGet() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        Session session = mock(Session.class);

        when(request.getSession()).thenReturn(session);

        new LogoutServlet().doGet(request, response);

        doNothing().when(session).invalidate();

        verify(session, times(1)).invalidate();

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

}