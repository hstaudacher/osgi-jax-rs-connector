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
package com.github.restdriver;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.restdriver.matchers.HasJsonPath;
import com.github.restdriver.matchers.HasJsonWhich;
import com.github.restdriver.matchers.IsEquivalentXml;
import com.github.restdriver.matchers.MatchesRegex;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

public final class Matchers {

    private Matchers() {
    }

    public static MatchesRegex matchingRegex(String pattern) {
        return new MatchesRegex(Pattern.compile(pattern));
    }

    public static MatchesRegex matchingRegex(Pattern pattern) {
        return new MatchesRegex(pattern);
    }

    public static IsEquivalentXml equivalentXmlTo(String xml) {
        return new IsEquivalentXml(xml);
    }

    public static HasJsonWhich hasJsonWhich(Matcher<JsonNode> matcher) {
        return new HasJsonWhich(matcher);
    }

    /**
     * Checks whether the given JSON object matches the JSONpath.
     *
     * No matcher is used on the matched value. It is based only on the existence of something at the given JSONpath.
     *
     * @param jsonPath The JSONpath to match.
     * @param <T> The type of the matcher.
     * @return The new matcher.
     */
    public static <T> TypeSafeMatcher<JsonNode> hasJsonPath(String jsonPath) {
        return new HasJsonPath<T>(jsonPath);
    }

    /**
     * Checks whether the given JSON object matches the JSONpath. NB when asserting on numeric values you will *have* to use Longs and
     * Doubles, or face the wrath of the ClassCastException!
     *
     * @param jsonPath The JSONpath to match.
     * @param matcher The matcher to apply to the result of the JSONpath.
     * @param <T> The type of the matcher.
     * @return The new matcher.
     */
    public static <T> TypeSafeMatcher<JsonNode> hasJsonPath(String jsonPath, Matcher<T> matcher) {
        return new HasJsonPath<T>(jsonPath, matcher);
    }


}
