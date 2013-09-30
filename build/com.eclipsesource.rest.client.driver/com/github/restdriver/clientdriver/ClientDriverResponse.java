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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import com.github.restdriver.clientdriver.exception.ClientDriverResponseCreationException;

/**
 * Class for encapsulating an HTTP response.
 */
public final class ClientDriverResponse {
    
    private static final int DEFAULT_STATUS_CODE = 200;
    private static final int EMPTY_RESPONSE_CODE = 204;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String DEFAULT_TEXT_CONTENT_TYPE = "text/plain";
    
    private int status;
    private final byte[] content;
    private String contentType;
    private final Map<String, String> headers;
    
    private long delayTime;
    private TimeUnit delayTimeUnit = TimeUnit.SECONDS;
    
    private long waitUntil;
    
    /**
     * Creates a new response with an empty body, a status code of 204 and
     * no Content-Type.
     */
    public ClientDriverResponse() {
        this((String) null, null);
    }
    
    /**
     * Creates a new response with the given body, a suitable default status
     * code and a Content-Type of 'text/plain'.
     * <p/>
     * If the content given is null a 204 status code is given, otherwise 200.
     * 
     * @param content
     *            The content of the response
     * @deprecated Use {@link #ClientDriverResponse(String, String)} instead.
     */
    @Deprecated
    public ClientDriverResponse(String content) {
        this(convertStringToByteArray(content), DEFAULT_TEXT_CONTENT_TYPE);
    }
    
    /**
     * Creates a new response with the given body, a suitable default status
     * code and a given content-type.
     * <p/>
     * If the content given is null a 204 status code is given, otherwise 200.
     * 
     * @param content
     *            The content of the response
     * @param contentType
     *            The content type
     */
    public ClientDriverResponse(String content, String contentType) {
        this(convertStringToByteArray(content), contentType);
    }
    
    /**
     * Creates a new response with the given body, a suitable default status
     * code and a given content-type.
     * <p/>
     * If the content given is null a 204 status code is given, otherwise 200.
     * 
     * @param content
     *            The content of the response
     * @param contentType
     *            The content type
     */
    public ClientDriverResponse(InputStream content, String contentType) {
        this(convertInputStreamToByteArray(content), contentType);
    }
    
    private ClientDriverResponse(byte[] content, String contentType) {
        this.status = statusCodeForContent(content);
        this.content = content;
        
        if (content != null && content.length != 0) {
            this.contentType = contentType;
        } else {
            this.contentType = null;
        }
        
        this.headers = new HashMap<String, String>();
    }
    
    private static byte[] convertStringToByteArray(String content) {
        return content != null ? content.getBytes() : null;
    }
    
    private static byte[] convertInputStreamToByteArray(InputStream content) {
        try {
            return content != null ? IOUtils.toByteArray(content) : null;
        } catch (IOException e) {
            throw new ClientDriverResponseCreationException("unable to create client driver response", e);
        }
    }
    
    private static int statusCodeForContent(byte[] content) {
        return content != null ? DEFAULT_STATUS_CODE : EMPTY_RESPONSE_CODE;
    }
    
    /**
     * @return The content as a byte array
     */
    public byte[] getContentAsBytes() {
        if (content == null || content.length == 0) {
            return null;
        } else {
            return content;
        }
    }
    
    /**
     * @return The content as a string, or an empty string if the content byte array is null or empty.
     */
    public String getContent() {
        if (getContentAsBytes() == null) {
            return "";
        } else {
            return new String(content);
        }
    }
    
    /**
     * @param withStatus
     *            the status to set
     * @return the object you called the method on, so you can chain these
     *         calls.
     */
    public ClientDriverResponse withStatus(int withStatus) {
        status = withStatus;
        return this;
    }
    
    /**
     * Modifies a ClientDriverRequest to specify some time to wait before
     * responding. This enables you to simulate slow services or networks, eg
     * for testing timeout behaviour of your clients.
     * 
     * @param delay
     *            How long to delay for.
     * @param timeUnit
     *            The time unit to use when counting the delay.
     * 
     * @return The modified ClientDriverRequest.
     */
    public ClientDriverResponse after(long delay, TimeUnit timeUnit) {
        this.delayTime = delay;
        this.delayTimeUnit = timeUnit;
        return this;
    }
    
    /**
     * @return the amount of time to delay the response
     */
    public long getDelayTime() {
        return delayTime;
    }
    
    /**
     * @return the unit of time for which we will delay the response
     */
    public TimeUnit getDelayTimeUnit() {
        return delayTimeUnit;
    }
    
    public boolean canExpire() {
        return waitUntil != 0;
    }
    
    public boolean hasNotExpired() {
        return waitUntil > System.currentTimeMillis();
    }
    
    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }
    
    /**
     * @param withContentType
     *            the contentType to set
     * @return the object you called the method on, so you can chain these
     *         calls.
     * @deprecated You shouldn't need to use this method any more. Use one of
     * the creator methods which specifies a content-type.
     */
    @Deprecated
    public ClientDriverResponse withContentType(String withContentType) {
        this.contentType = withContentType;
        return this;
    }
    
    /**
     * Set headers on the response.
     * 
     * @param name
     *            The header name
     * @param value
     *            The header value
     * @return the object you called the method on, so you can chain these
     *         calls.
     */
    public ClientDriverResponse withHeader(String name, String value) {
        if (CONTENT_TYPE.equalsIgnoreCase(name)) {
            contentType = value;
        } else {
            headers.put(name, value);
        }
        return this;
    }
    
    /**
     * Sets the amount of time to allow this response to match within.
     * 
     * @param interval
     *            The number of given unit to wait
     * @param unit
     *            The unit to wait for
     * @return This object, so you can chain these calls.
     */
    public ClientDriverResponse within(long interval, TimeUnit unit) {
        this.waitUntil = System.currentTimeMillis() + unit.toMillis(interval);
        return this;
    }
    
    /**
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    /**
     * @return whether the response has a body
     */
    public boolean hasBody() {
        return content != null && content.length != 0;
    }
    
}
