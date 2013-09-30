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
package com.github.restdriver.clientdriver.exception;

/**
 * Runtime exception which is thrown for a variety of causes from the Client Driver. This exception is thrown within
 * the HTTP server itself, so will not cause tests to fail, but will appear in the server logs (usually to STDERR)
 */
public class ClientDriverSetupException extends RuntimeException {
    
    private static final long serialVersionUID = -2270688849375416363L;
    
    /**
     * Constructor.
     * 
     * @param message
     *            The message
     * @param cause
     *            The throwable which we caught before throwing this one. Could be null.
     */
    public ClientDriverSetupException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
