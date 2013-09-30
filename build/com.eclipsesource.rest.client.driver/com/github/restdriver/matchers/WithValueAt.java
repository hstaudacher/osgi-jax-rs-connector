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
 * Matcher to check that a JSON array has a particular value at the specified index.
 */
public final class WithValueAt extends TypeSafeMatcher<JsonNode> {
    
    private final int position;
    private final Matcher<?> matcher;
    
    /**
     * Create a new instance of this matcher.
     * 
     * @param position The position in the array at which the value is to be evaluated
     * @param matcher The matcher to use to evaluate the value
     */
    public WithValueAt(int position, Matcher<?> matcher) {
        this.position = position;
        this.matcher = matcher;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("A JSON array with value at " + position + " which matches: ");
        matcher.describeTo(description);
    }
    
    @Override
    public boolean matchesSafely(JsonNode node) {
        
        if (!node.isArray()) {
            return false;
        }
        
        Iterator<JsonNode> nodeIterator = node.elements();
        int nodeCount = 0;
        
        while (nodeIterator.hasNext()) {
            
            JsonNode currentNode = nodeIterator.next();
            
            if (nodeCount == position) {
                return matcher.matches(currentNode.textValue());
            }
            
            nodeCount++;
            
        }
        
        return false;
    }
}
