package com.eclipsesource.jaxrs.publisher;

import java.util.Dictionary;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;

/**
 * Service that allows contribution of initial parameters and HttpContext for the Jersey servlet.
 * Only the first tracked service implementing this interface will be used.
 * 
 * @author Ivan Iliev
 */
public interface ServletConfigurationService {

  /**
   * Returns an HttpContext or null for the given httpService and rootPath.
   * 
   * @param httpService
   * @param rootPath
   * @return
   */
  public HttpContext getHttpContext( HttpService httpService, String rootPath );

  /**
   * Returns initial parameters or null for the given httpService and rootPath.
   * @param httpService
   * @param rootPath
   * @return
   */
  @SuppressWarnings( "rawtypes" )
  public Dictionary getInitParams( HttpService httpService, String rootPath );
}
