/**
 * Copyright © 2010-2011 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.restdriver.clientdriver.jetty;

import org.eclipse.jetty.server.Handler;

import com.github.restdriver.clientdriver.ClientDriverExpectation;
import com.github.restdriver.clientdriver.ClientDriverRequest;
import com.github.restdriver.clientdriver.ClientDriverResponse;

/**
 * Interface for classes which handle incoming HTTP requests in the Client Driver.
 */
public interface ClientDriverJettyHandler extends Handler {
    
    /**
     * Add in a {@link ClientDriverRequest}/{@link ClientDriverResponse} pair.
     * 
     * @param request
     *            The expected request
     * @param response
     *            The response to serve to that request
     * @return The added expectation
     */
    ClientDriverExpectation addExpectation(ClientDriverRequest request, ClientDriverResponse response);
    
    /**
     * This method will throw a ClientDriverFailedExpectationException if there have been any unexpected requests.
     */
    void checkForUnexpectedRequests();
    
    /**
     * This method will throw a ClientDriverFailedExpectationException if any expectations have not been met.
     */
    void checkForUnmatchedExpectations();
    
}
