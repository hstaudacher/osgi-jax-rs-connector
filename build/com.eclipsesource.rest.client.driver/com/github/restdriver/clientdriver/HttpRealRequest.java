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

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class HttpRealRequest implements RealRequest {
    
    private final Method method;
    private final String path;
    private final Multimap<String, String> params;
    private final Map<String, Object> headers;
    private final String bodyContent;
    private final String bodyContentType;
    
    @SuppressWarnings("unchecked")
    public HttpRealRequest(HttpServletRequest request) {
        this.path = request.getPathInfo();
        this.method = Enum.valueOf(Method.class, request.getMethod());
        this.params = HashMultimap.create();
        
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Entry<String, String[]> paramEntry : parameterMap.entrySet()) {
            String[] values = paramEntry.getValue();
            for (String value : values) {
                this.params.put(paramEntry.getKey(), value);
            }
        }
        
        headers = new HashMap<String, Object>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.put(headerName, request.getHeader(headerName));
            }
        }
        
        try {
            this.bodyContent = IOUtils.toString(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read body of request", e);
        }
        
        this.bodyContentType = request.getContentType();
    }
    
    @Override
    public final Method getMethod() {
        return method;
    }
    
    @Override
    public final String getPath() {
        return path;
    }
    
    @Override
    public final Map<String, Collection<String>> getParams() {
        if (params == null) {
            return null;
        }
        return Collections.unmodifiableMap(params.asMap());
    }
    
    @Override
    public final Map<String, Object> getHeaders() {
        if (headers == null) {
            return null;
        }
        return Collections.unmodifiableMap(headers);
    }
    
    @Override
    public final String getBodyContent() {
        return bodyContent;
    }
    
    @Override
    public final String getBodyContentType() {
        return bodyContentType;
    }
    
}
