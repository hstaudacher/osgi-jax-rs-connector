package com.eclipsesource.jaxrs.publisher.internal;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;

import com.eclipsesource.jaxrs.publisher.ServiceProperties;


public class AllResourceFilterTest {
  
  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullContext() {
    new AllResourceFilter( null );
  }
  
  @Test
  public void testHasAllFilter() throws InvalidSyntaxException {
    BundleContext context = mock( BundleContext.class );
    Filter filter = mock( Filter.class );
    when( context.createFilter( anyString() ) ).thenReturn( filter );
    AllResourceFilter resourceFilter = new AllResourceFilter( context );
    
    Filter actualFilter = resourceFilter.getFilter();
    
    assertSame( filter, actualFilter );
  }
  
  @Test
  public void testUsesAllFilterExpression() throws InvalidSyntaxException {
    BundleContext context = mock( BundleContext.class );
    Filter filter = mock( Filter.class );
    when( context.createFilter( anyString() ) ).thenReturn( filter );
    AllResourceFilter resourceFilter = new AllResourceFilter( context );
    
    resourceFilter.getFilter();
    
    verify( context ).createFilter( AllResourceFilter.ANY_SERVICE_FILTER );
  }
  
  @Test
  public void testRespectsNoPublishProperty() throws InvalidSyntaxException {
    String noPublish = "!(" + ServiceProperties.PUBLISH +"=false)";
    
    assertTrue( AllResourceFilter.ANY_SERVICE_FILTER.contains( noPublish ) );
  }
}
