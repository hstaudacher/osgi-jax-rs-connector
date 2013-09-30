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
package com.github.restdriver.matchers;

import java.io.IOException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.restdriver.exception.RuntimeAssertionFailure;

/**
 * A matcher for string which treats the string as a JsonNode.
 */
public class HasJsonWhich extends TypeSafeMatcher<String> {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private final Matcher<JsonNode> matcher;
    
    public HasJsonWhich(Matcher<JsonNode> matcher) {
        this.matcher = matcher;
    }
    
    @Override
    public void describeTo(Description description) {
        matcher.describeTo(description);
    }
    
    @Override
    protected void describeMismatchSafely(String item, Description mismatchDescription) {
        JsonNode node = createNode(item);
        matcher.describeMismatch(node, mismatchDescription);
    }
    
    @Override
    protected boolean matchesSafely(String item) {
        return matcher.matches(createNode(item));
    }
    
    private static JsonNode createNode(String item) {
        try {
            return MAPPER.readTree(item);
        } catch (IOException e) {
            throw new RuntimeAssertionFailure("Failed to create JsonNode", e);
        }
    }
    
}
