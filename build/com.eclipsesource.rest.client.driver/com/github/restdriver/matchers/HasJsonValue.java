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
 * Matcher to check that a given key has a particular value in a JsonNode.
 */
public final class HasJsonValue extends TypeSafeMatcher<JsonNode> {
    
    private final String fieldName;
    private final Matcher<?> valueMatcher;
    
    /**
     * Creates an instance of this matcher.
     * 
     * @param fieldName The field name against which the matcher will be evaluated
     * @param valueMatcher The matcher to be used to evaluate the value found at the given field name
     */
    public HasJsonValue(String fieldName, Matcher<?> valueMatcher) {
        this.fieldName = fieldName;
        this.valueMatcher = valueMatcher;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("JsonNode with '" + fieldName + "' matching: ");
        valueMatcher.describeTo(description);
    }
    
    @Override
    public boolean matchesSafely(JsonNode jsonNode) {
        
        JsonNode node = jsonNode.get(fieldName);
        
        if (node == null) {
            return false;
        }
        
        if (node.isInt()) {
            return valueMatcher.matches(node.intValue());
            
        } else if (node.isLong()) {
            return valueMatcher.matches(node.longValue());
            
        } else if (node.isTextual()) {
            return valueMatcher.matches(node.textValue());
            
        } else if (node.isBoolean()) {
            return valueMatcher.matches(node.booleanValue());
            
        } else if (node.isDouble()) {
            return valueMatcher.matches(node.doubleValue());
            
        } else if (node.isObject()) {
            return valueMatcher.matches(node);
            
        } else if (node.isNull()) {
            return valueMatcher.matches(null);
            
        } else {
            return false;
            
        }
        
    }
    
}
