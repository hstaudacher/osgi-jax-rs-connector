package com.eclipsesource.jaxrs.connector.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;


@RunWith( MockitoJUnitRunner.class )
public class HttpTracker_Test {
  
  private HttpTracker httpTracker;
  @Mock
  private JAXRSConnector connector;
  @Mock
  private ServiceReference<HttpService> reference;

  @Before
  public void setUp() {
    BundleContext context = mock( BundleContext.class );
    httpTracker = new HttpTracker( context, connector );
  }
  
  @Test
  public void delegatesAddHttpService() {
    httpTracker.addingService( reference );
    
    verify( connector ).addHttpService( reference );
  }
  
  @Test
  public void delegatesRemoveHttpService() {
    HttpService service = mock( HttpService.class );
    
    httpTracker.removedService( reference, service );
    
    verify( connector ).removeHttpService( service );
  }
}
