package com.bsc.sso.authentication.endpoints.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/error")
public class ErrorEndpoint {

    @GET
    public Response error(@Context HttpServletRequest request) {
        String error = request.getParameter("error");
        String errorDescription = request.getParameter("error_description");
        StringBuilder builder = new StringBuilder();
        builder.append("Error: " + error + ". ");
        builder.append("Error Description: " + errorDescription);
        Response response = Response.status(Response.Status.BAD_REQUEST).entity(builder.toString()).build();
        return response;
    }
}
