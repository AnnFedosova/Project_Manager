package server.api.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.jboss.crypto.CryptoUtil;
import server.dbService.DBService;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.security.Key;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/api")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
public class GetTokenService {
    private DBService dbService = DBService.getInstance();

    @POST
    @Path("/getToken")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String login,
                                     @FormParam("password") String password) {
        try {

            // Authenticate the user using the credentials provided
            authenticate(login, password);

            // Issue a token for the user
            String token = issueToken(login);

            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            return Response.status(FORBIDDEN).build();
        }
    }

    private String issueToken(String username) {
        Key key = MacProvider.generateKey();

        return Jwts.builder().setSubject(username).signWith(SignatureAlgorithm.HS512, key).compact();
    }

    private void authenticate(String username, String password) throws Exception {
        password = CryptoUtil.createPasswordHash("MD5", CryptoUtil.BASE64_ENCODING, null, null, password);
        if (!dbService.getUser(username).getPassword().equals(password)) {
            throw new Exception("The credentials are invalid");
        }
    }
}
