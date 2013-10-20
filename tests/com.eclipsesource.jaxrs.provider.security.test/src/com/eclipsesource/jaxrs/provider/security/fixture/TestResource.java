package com.eclipsesource.jaxrs.provider.security.fixture;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestResource {
	@GET
	@Path("/public")
	public String getPublic() {
		return "public";
	}
	
	@GET
	@Path("/secure")
	@RolesAllowed("secure")
	public String getSecure() {
		return "secure";
	}
}
