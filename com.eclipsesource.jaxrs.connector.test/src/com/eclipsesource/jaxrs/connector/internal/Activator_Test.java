package com.eclipsesource.jaxrs.connector.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;


@RunWith( MockitoJUnitRunner.class )
@SuppressWarnings( { "unchecked", "rawtypes" } )
public class Activator_Test {
  
  private Activator activator;
  @Mock
  private BundleContext context;
  @Mock
  private ServiceRegistration registration;

  @Before
  public void setUp() throws InvalidSyntaxException {
    activator = new Activator();
    Filter filter = mock( Filter.class );
    when( context.createFilter( anyString() ) ).thenReturn( filter );
    when( context.registerService( anyString(), 
                                   anyObject(), 
                                   any( Dictionary.class ) ) ).thenReturn( registration );
  }
  
  @Test
  public void testRegisterService() throws Exception {
    
    activator.start( context );
    
    verify( context ).registerService( eq( JAXRSConnector.class.getName() ), 
                                       any( JAXRSConnector.class ), 
                                       any( Dictionary.class ) );
  }
  
  @Test
  public void testDeregisterService() throws Exception {
    activator.start( context );
    
    activator.stop( context );
    
    verify( registration ).unregister();
  }
}
