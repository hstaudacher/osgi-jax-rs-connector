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
package com.github.restdriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import com.github.restdriver.exception.RuntimeConfigurationException;

public final class RestDriverProperties {
    
    static {
        InputStream io = RestDriverProperties.class.getClassLoader().getResourceAsStream("rest-driver.properties");
        Properties properties = new Properties();
        try {
            properties.load(io);
            for (Entry<Object, Object> property : properties.entrySet()) {
                System.setProperty(property.getKey().toString(), property.getValue().toString());
            }
        } catch (IOException e) {
            throw new RuntimeConfigurationException(e);
        }
        
    }
    
    private RestDriverProperties() {
    }
    
    public static String getVersion() {
        return System.getProperty("rest.driver.version", "");
    }
    
}
