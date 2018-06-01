package org.teamids.gestionemappe;

import org.glassfish.jersey.internal.util.Base64;
import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DAO.UtenteDAO;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "basic ";
    private static final String SECURED_URL_PREFIX = "secured";
    UtenteDAO utenteDAO = new UtenteDAO();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException{
        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {
            ConnectorHelpers connector = new ConnectorHelpers();
            Connection db = connector.connect();
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
            if(authHeader != null && authHeader.size()>0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String idsessione = Base64.decodeAsString(authToken);
                if (utenteDAO.isAutenticato(idsessione, db)) {
                    connector.disconnect();
                    return;
                }
            }
            Response unauthorizedStatus = Response
                                            .status(Response.Status.UNAUTHORIZED)
                                            .entity("User cannot access the resource.")
                                            .build();
            requestContext.abortWith(unauthorizedStatus);
            connector.disconnect();
            }
        }
}
