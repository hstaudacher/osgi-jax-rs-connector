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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Matcher to verify that a JsonNode has an array with the specified field name.
 */
public final class HasJsonArray extends TypeSafeMatcher<JsonNode> {
    
    private final String fieldName;
    private final Matcher<?> arrayMatcher;
    
    /**
     * Create an instance of this matcher.
     * 
     * @param fieldName The field name to check for
     * @param arrayMatcher The matcher to be used to check the array found at the given field name
     */
    public HasJsonArray(String fieldName, Matcher<?> arrayMatcher) {
        this.fieldName = fieldName;
        this.arrayMatcher = arrayMatcher;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("JsonNode with '" + fieldName + "' matching: ");
        arrayMatcher.describeTo(description);
    }
    
    @Override
    public boolean matchesSafely(JsonNode jsonNode) {
        
        JsonNode node = jsonNode.get(fieldName);
        
        if (node == null) {
            return false;
        }
        
        if (node.isArray()) {
            
            return arrayMatcher.matches(node);
            
        } else {
            return false;
            
        }
        
    }
    
}
