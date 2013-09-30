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

import com.github.restdriver.clientdriver.exception.ClientDriverInvalidExpectationException;

/**
 * An expectation made within the client driver.
 */
public class ClientDriverExpectation {
    
    private final ClientDriverRequestResponsePair pair;
    private int numberOfTimes = 1;
    private int numberOfMatches;
    private boolean matchAnyTimes;
    private MatchedRequestHandler matchedRequestHandler = new NullRequestHandler();
    
    /**
     * Creates a new expectation instance.
     * 
     * @param pair The request/response pair the expectation covers.
     */
    public ClientDriverExpectation(ClientDriverRequestResponsePair pair) {
        this.pair = pair;
    }
    
    /**
     * Gets the request/response pair this expectation covers.
     * 
     * @return The request/response pair
     */
    public final ClientDriverRequestResponsePair getPair() {
        return pair;
    }
    
    /**
     * Indicate that this expectation should be matched a given number of times.
     * 
     * @param times The number of times this expectation should be matched
     */
    public final ClientDriverExpectation times(int times) {
        if (times < 1) {
            throw new ClientDriverInvalidExpectationException("Expectation cannot be matched less than once");
        }
        numberOfTimes = times;
        
        return this;
    }
    
    /**
     * Indicate that this expectation should be matched any number of times.
     */
    public final ClientDriverExpectation anyTimes() {
        matchAnyTimes = true;
        return this;
    }
    
    /**
     * Indicate that this expectation has been matched.
     * 
     * @param realRequest
     */
    public final void match(HttpRealRequest realRequest) {
        numberOfMatches += 1;
        
        matchedRequestHandler.onMatch(realRequest);
    }
    
    /**
     * Determine whether this expectation has been satisfied.
     * 
     * @return True if the expectation has been matched as many times as it should have
     */
    public final boolean isSatisfied() {
        return !matchAnyTimes && numberOfTimes == numberOfMatches;
    }
    
    /**
     * Whether this expectation should match any number of times.
     * 
     * @return True if the expectation should match any number of times
     */
    public final boolean shouldMatchAnyTimes() {
        return matchAnyTimes;
    }
    
    /**
     * Gets a string giving the status of the expectation.
     * 
     * @return The status string
     */
    public final String getStatusString() {
        
        String expectedString;
        
        if (matchAnyTimes) {
            expectedString = "any";
        } else {
            expectedString = String.valueOf(numberOfTimes);
        }
        
        return "expected: " + expectedString + ", actual: " + numberOfMatches;
        
    }
    
    /**
     * When a call is matched call a handler
     * 
     * @param matchedRequestHandler called when the request is matched
     * @return
     */
    public ClientDriverExpectation whenMatched(MatchedRequestHandler matchedRequestHandler) {
        this.matchedRequestHandler = matchedRequestHandler;
        return this;
    }
}
