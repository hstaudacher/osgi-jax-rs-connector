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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import com.github.restdriver.clientdriver.capture.BodyCapture;
import com.github.restdriver.matchers.MatchesRegex;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Class for encapsulating an HTTP request.
 */
public final class ClientDriverRequest {

    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * HTTP method enum for specifying which method you expect to be called with.
     */
    public enum Method {
        GET, POST, PUT, DELETE, OPTIONS, HEAD, TRACE
    }

    private final Object path;
    private final Multimap<String, Matcher<? extends String>> params;
    private final Map<String, Object> headers;

    private Method method;
    private Matcher<? extends String> bodyContentMatcher;
    private Object bodyContentType;
    private boolean anyParams;
    private BodyCapture<?> bodyCapture;

    /**
     * Constructor taking String.
     *
     * @param path The mandatory argument is the path which will be listened on
     */
    public ClientDriverRequest(String path) {
        this.path = path;
        method = Method.GET;
        params = HashMultimap.create();
        headers = new HashMap<String, Object>();
        anyParams = false;
    }

    /**
     * Constructor taking Pattern.
     *
     * @param path The mandatory argument is the path which will be listened on
     */
    public ClientDriverRequest(Pattern path) {
        this.path = path;
        method = Method.GET;
        params = HashMultimap.create();
        headers = new HashMap<String, Object>();
        anyParams = false;
    }

    /**
     * Get the path.
     *
     * @return the path which requests are expected on.
     */
    public Object getPath() {
        return path;
    }

    /**
     * @param withMethod the method to set
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withMethod(Method withMethod) {
        this.method = withMethod;
        return this;
    }

    /**
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Setter for expecting any number of querystring parameters with any values.
     * With this set, any expected parameters are ignored.
     *
     * @return
     */
    public ClientDriverRequest withAnyParams() {
        anyParams = true;
        return this;
    }

    /**
     * Setter for expecting query-string parameters on the end of the url.
     *
     * @param key   The key from ?key=value
     * @param value The value from ?key=value in the form of a String
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withParam(String key, String value) {
        params.put(key, new IsEqual<String>(value));
        return this;
    }

    /**
     * Setter for expecting query-string parameters on the end of the url.
     *
     * @param key   The key from ?key=value
     * @param value The value from ?key=value in the form of a String
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withParam(String key, int value) {
        return withParam(key, String.valueOf(value));
    }

    /**
     * Setter for expecting query-string parameters on the end of the url.
     *
     * @param key   The key from ?key=value
     * @param value The value from ?key=value in the form of a String
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withParam(String key, long value) {
        return withParam(key, String.valueOf(value));
    }

    /**
     * Setter for expecting query-string parameters on the end of the url.
     *
     * @param key   The key from ?key=value
     * @param value The value from ?key=value in the form of a String
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withParam(String key, boolean value) {
        return withParam(key, String.valueOf(value));
    }

    /**
     * Setter for expecting query-string parameters on the end of the url.
     *
     * @param key   The key from ?key=value
     * @param value The value from ?key=value in the form of a String
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withParam(String key, Object value) {
        return withParam(key, value.toString());
    }

    /**
     * Setter for expecting query-string parameters on the end of the url.
     *
     * @param key   The key from ?key=value
     * @param value The value from ?key=value in the form of a Pattern
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withParam(String key, Pattern value) {
        params.put(key, new MatchesRegex(value));
        return this;
    }

    /**
     * Setter for expecting query-string parameters on the end of the url.
     *
     * @param key   The key from ?key=value
     * @param value The value from ?key=value in the form of a Matcher
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withParam(String key, Matcher<? extends String> value) {
        params.put(key, value);
        return this;
    }

    /**
     * Setter for expecting multiple query-string parameters on the end of the url.
     *
     * @param newParams The map of key value pairs from ?key=value
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withParams(Map<String, Object> newParams) {
        for (Entry<String, Object> entry : newParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Pattern) {
                this.params.put(key, new MatchesRegex((Pattern) value));
            } else {
                this.params.put(key, new IsEqual<String>(value.toString()));
            }
        }
        return this;
    }

    /**
     * @return the params
     */
    Map<String, Collection<Matcher<? extends String>>> getParams() {
        return params.asMap();
    }

    /**
     * @return the anyParams
     */
    boolean getAnyParams() {
        return anyParams;
    }

    /**
     * toString.
     *
     * @return a String representation of the request
     */
    @Override
    public String toString() {

        List<String> queryStringValues = new ArrayList<String>();

        for (Entry<String, Matcher<? extends String>> entry : params.entries()) {
            String stringified = removeQuotes(entry.getValue());
            queryStringValues.add(entry.getKey() + "=" + stringified);
        }

        String queryString = StringUtils.join(queryStringValues, "&");

        if (queryStringValues.size() > 0) {
            queryString = "?" + queryString;
        }

        return "ClientDriverRequest: " + method + " " + path.toString() + queryString + "; ";
    }

    private static String removeQuotes(Matcher<? extends String> matcher) {
        return StringUtils.removeEnd(StringUtils.removeStart(matcher.toString(), "\""), "\"");
    }

    /**
     * @return The body content matcher
     */
    public Matcher<? extends String> getBodyContentMatcher() {
        return bodyContentMatcher;
    }

    /**
     * @return the bodyContentType
     */
    public Object getBodyContentType() {
        return bodyContentType;
    }

    /**
     * Setter for expecting body content and type, where content is in the form of a String and type is in the form of a
     * String.
     *
     * @param withBodyContent the bodyContent to set
     * @param withContentType eg "text/plain"
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withBody(String withBodyContent, String withContentType) {
        bodyContentMatcher = new IsEqual<String>(withBodyContent);
        bodyContentType = withContentType;
        return this;
    }

    /**
     * Setter for expecting body content and type, where content is in the form of a String and type is in the form of a
     * Pattern.
     *
     * @param withBodyContent the bodyContent to set
     * @param contentType     eg "text/plain"
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withBody(String withBodyContent, Pattern contentType) {
        bodyContentMatcher = new IsEqual<String>(withBodyContent);
        bodyContentType = contentType;
        return this;
    }

    /**
     * Setter for expecting body content and type, where content is in the form of a Pattern and type is in the form of
     * a String.
     *
     * @param withBodyContent the bodyContent to set
     * @param contentType     eg "text/plain"
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withBody(Pattern withBodyContent, String contentType) {
        bodyContentMatcher = new MatchesRegex(withBodyContent);
        bodyContentType = contentType;
        return this;
    }

    /**
     * Setter for expecting body content and type, where content is in the form of a Pattern and type is in the form of
     * a Pattern.
     *
     * @param withBodyContent the bodyContent to set
     * @param contentType     eg "text/plain"
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withBody(Pattern withBodyContent, Pattern contentType) {
        bodyContentMatcher = new MatchesRegex(withBodyContent);
        bodyContentType = contentType;
        return this;
    }

    /**
     * Setter for expecting body content and type, where content is in the form of a Matcher and type is in the form of
     * a Pattern.
     *
     * @param bodyContentMatcher the Matcher&lt;String&gt; to use to set
     * @param contentType        eg "text/plain"
     * @return the object you called the method on, so you can chain these calls.
     */
    public ClientDriverRequest withBody(Matcher<? extends String> bodyContentMatcher, String contentType) {
        this.bodyContentMatcher = bodyContentMatcher;
        this.bodyContentType = contentType;
        return this;
    }

    /**
     * Setter for adding a {@link BodyCapture} to the expectation for later assertions/debugging.
     *
     * @param bodyCapture the capturing object.
     * @return this, for chaining.
     */
    public ClientDriverRequest capturingBodyIn(BodyCapture<?> bodyCapture) {
        this.bodyCapture = bodyCapture;
        return this;
    }

    public BodyCapture<?> getBodyCapture() {
        return bodyCapture;
    }

    /**
     * Setter for expecting a specific header name and value pair.
     *
     * @param withHeaderName  the headerName to match on
     * @param withHeaderValue the headerValue to match on
     * @return the object you called the method on, so you can chain these calls
     */
    public ClientDriverRequest withHeader(String withHeaderName, String withHeaderValue) {
        if (CONTENT_TYPE.equalsIgnoreCase(withHeaderName)) {
            bodyContentType = withHeaderValue;
        } else {
            headers.put(withHeaderName, withHeaderValue);
        }
        return this;
    }

    /**
     * Setter for expecting a specific header name and value pair, where value is in the form of a Pattern.
     *
     * @param withHeaderName  the headerName to match on
     * @param withHeaderValue the headerValue to match on
     * @return the object you called the method on, so you can chain these calls
     */
    public ClientDriverRequest withHeader(String withHeaderName, Pattern withHeaderValue) {
        if (CONTENT_TYPE.equalsIgnoreCase(withHeaderName)) {
            bodyContentType = withHeaderValue;
        } else {
            headers.put(withHeaderName, withHeaderValue);
        }
        return this;
    }

    public ClientDriverRequest withBasicAuth(String username, String password) {
        headers.put("Authorization", "Basic " + base64(username + ":" + password));
        return this;
    }

    /**
     * @return the headers
     */
    public Map<String, Object> getHeaders() {
        return headers;
    }

    private static String base64(String content) {
        return new String(Base64.encodeBase64(content.getBytes()));
    }

}
