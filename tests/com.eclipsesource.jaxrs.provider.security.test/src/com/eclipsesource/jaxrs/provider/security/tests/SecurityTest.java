package com.eclipsesource.jaxrs.provider.security.tests;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;
import org.junit.Test;

public class SecurityTest {
	private static WebTarget target;

	@BeforeClass
	public static void classSetUp() {
	    Client client = ClientBuilder.newClient();
		target = client.target("http://localhost:9090/services");
	}
	
	@Test
	public void testPublic() {
		String result = target.path("/test/public").request(MediaType.TEXT_PLAIN).cookie("user", "none").get(String.class);
		assertEquals(result, "public");
	}
	
	@Test
	public void testSecureWithGoodCredentials() {
		String result = target.path("/test/secure").request(MediaType.TEXT_PLAIN).cookie("user", "junit").get(String.class);
		assertEquals(result, "secure");
	}
	
	@Test(expected=ForbiddenException.class)
	public void testSecureWithBadCredentials() {
		target.path("/test/secure").request(MediaType.TEXT_PLAIN).cookie("user", "none").get(String.class);
	}
}
