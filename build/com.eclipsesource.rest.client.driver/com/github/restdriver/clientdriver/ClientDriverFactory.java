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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.restdriver.clientdriver.jetty.DefaultClientDriverJettyHandler;

/**
 * Main entry point to the Rest Client Driver.
 */
public final class ClientDriverFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDriverFactory.class);
    
    /**
     * Factory method to create and start a {@link ClientDriver}. A port will be chosen automatically.
     * 
     * @return A new {@link ClientDriver}, which has found a free port, bound to it and started up.
     */
    public ClientDriver createClientDriver() {
        ClientDriver clientDriver = new ClientDriver(new DefaultClientDriverJettyHandler(new DefaultRequestMatcher()));
        LOGGER.debug("ClientDriver created at '" + clientDriver.getBaseUrl() + "'.");
        return clientDriver;
    }
    
    /**
     * Factory method to create and start a {@link ClientDriver} on a specific port. This is <em>absolutely</em> not the recommended
     * way to use the client driver. The no-arg method will choose a free port, use of this method will fail if the port is not free.
     * 
     * @param port The port to listen on. If this port is not available a runtime exception will be thrown.
     * 
     * @return A new {@link ClientDriver}, which has found a free port, bound to it and started up.
     */
    public ClientDriver createClientDriver(int port) {
        ClientDriver clientDriver = new ClientDriver(new DefaultClientDriverJettyHandler(new DefaultRequestMatcher()), port);
        LOGGER.debug("ClientDriver created at '" + clientDriver.getBaseUrl() + "'.");
        return clientDriver;
    }
    
}
