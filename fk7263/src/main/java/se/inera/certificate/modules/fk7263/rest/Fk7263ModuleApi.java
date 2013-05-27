package se.inera.certificate.modules.fk7263.rest;

import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.model.Valideringsresultat;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author andreaskaltenbach
 */
public class Fk7263ModuleApi {

    @POST
    @Path( "/extension" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces(MediaType.APPLICATION_JSON)
    public Object extract(Lakarutlatande lakarutlatande) {
        return new Object();
    }

    @POST
    @Path( "/valid" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces(MediaType.APPLICATION_JSON)
    public Valideringsresultat validate(Lakarutlatande lakarutlatande) {
        return new Valideringsresultat();
    }
}
