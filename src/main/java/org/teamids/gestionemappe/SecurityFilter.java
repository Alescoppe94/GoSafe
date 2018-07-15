package org.teamids.gestionemappe;

import org.glassfish.jersey.internal.util.Base64;
import org.teamids.gestionemappe.model.ConnectorHelpers;
import org.teamids.gestionemappe.model.DAO.UtenteDAO;
import org.teamids.gestionemappe.model.DAO.UtenteDAOInterface;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Classe che si occupa di filtrare le richiste che richiedono alti privilegi (utente deve essere autenticato)
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "basic ";
    private static final String SECURED_URL_PREFIX = "secured";
    UtenteDAOInterface utenteDAOInterface = new UtenteDAO();

    /**
     * Permette di filtrare una richiesta privilegiata.
     * Si lascerà passare l’operazione se la richiesta ricevuta arriva da un utente autenticato,
     * in caso contrario la richiesta verrà rifiutata.
     * L’autenticità di una richiesta viene verificata controllando che
     * il token dell’utente da cui arriva la richiesta sia lo stesso di quello memorizzato sul database.
     * @param requestContext richiesta che proviene dal client
     * @throws IOException Eccezione dovuta ad errori di input/output
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException{
        //Se nell'URI della richiesta compare la stringa "secured" significa che è una richiesta che richiede alti privilegi
        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {
            ConnectorHelpers connector = new ConnectorHelpers();
            Connection db = connector.connect();
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
            //La richiesta del client deve contenere nell'Header l'idsessione dell'utente
            if(authHeader != null && authHeader.size()>0) {
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String idsessione = Base64.decodeAsString(authToken);
                //Se il token dell'utente da cui proviene la richiesta coincide con quello presente database la richiesta viene accettata
                if (utenteDAOInterface.isAutenticato(idsessione, db)) {
                    connector.disconnect();
                    return;
                }
            }
            //Se l'Header è nullo o è vuoto, la richiesta viene rifiutata (errore 401)
            Response unauthorizedStatus = Response
                                            .status(Response.Status.UNAUTHORIZED)
                                            .entity("User cannot access the resource.")
                                            .build();
            requestContext.abortWith(unauthorizedStatus);
            connector.disconnect();
            }
        }
}
