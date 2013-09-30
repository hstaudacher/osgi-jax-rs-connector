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
package com.github.restdriver.clientdriver;

/**
 * Pairs an expected request with its response.
 */
public final class ClientDriverRequestResponsePair {
    
    private final ClientDriverRequest request;
    private final ClientDriverResponse response;
    
    /**
     * Constructor.
     * 
     * @param request
     *            The {@link ClientDriverRequest}
     * @param response
     *            The {@link ClientDriverResponse}
     */
    public ClientDriverRequestResponsePair(ClientDriverRequest request, ClientDriverResponse response) {
        this.request = request;
        this.response = response;
    }
    
    /**
     * @return the response
     */
    public ClientDriverResponse getResponse() {
        return response;
    }
    
    /**
     * @return the request
     */
    public ClientDriverRequest getRequest() {
        return request;
    }
    
}
