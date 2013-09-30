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

import java.util.Iterator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Matcher which checks that a JSON array contains the specified value.
 */
public final class ContainingValue extends TypeSafeMatcher<JsonNode> {
    
    private final Matcher<?> matcher;
    
    /**
     * Create a new instance which uses the given matcher against all values in a JSON array.
     * 
     * @param matcher The matcher to be used for evaluation
     */
    public ContainingValue(Matcher<?> matcher) {
        this.matcher = matcher;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("A JSON array containing: ");
        matcher.describeTo(description);
    }
    
    @Override
    public boolean matchesSafely(JsonNode node) {
        
        if (!node.isArray()) {
            return false;
        }
        
        Iterator<JsonNode> nodeIterator = node.elements();
        
        while (nodeIterator.hasNext()) {
            
            String value = nodeIterator.next().textValue();
            
            if (matcher.matches(value)) {
                return true;
            }
        }
        
        return false;
    }
    
}
