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
package com.github.restdriver.clientdriver.capture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.restdriver.exception.RuntimeMappingException;

import java.io.IOException;

/**
 * Implementation of BodyCapture which marshalls the body into a {@link JsonNode}.
 */
public class JsonBodyCapture implements BodyCapture<JsonNode> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int PARSE_ERROR_EXCERPT_LENGTH = 16;

    private JsonNode content;

    @Override
    public JsonNode getContent() {
        return content;
    }

    @Override
    public void setBody(String json) {
        try {
            this.content = MAPPER.readTree(json);

        } catch (IOException e) {
            throw new RuntimeMappingException("Can't parse JSON.  Bad content >> " + json.substring(0, PARSE_ERROR_EXCERPT_LENGTH) + "...", e);

        }
    }
}
