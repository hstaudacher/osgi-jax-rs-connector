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
 * Matcher to check the size of a Json array.
 */
public final class WithSize extends TypeSafeMatcher<JsonNode> {
    
    private final Matcher<Integer> matcher;
    
    /**
     * Creates a new instance of this matcher.
     * 
     * @param matcher The matcher against which the size of the JSON node will be evaluated
     */
    public WithSize(Matcher<Integer> matcher) {
        this.matcher = matcher;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("A JSON array with size: ");
        matcher.describeTo(description);
    }
    
    @Override
    public boolean matchesSafely(JsonNode node) {
        
        if (!node.isArray()) {
            return false;
        }
        
        return matcher.matches(node.size());
        
    }
    
}
