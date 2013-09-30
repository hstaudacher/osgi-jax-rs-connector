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

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Node;

import com.github.restdriver.XmlUtil;
import com.github.restdriver.matchers.util.HamcrestConverter;
import com.google.common.base.Function;

public final class HasXPath {

    private HasXPath() {
    }

    /**
     * For wrapping the Node Matcher
     * @see HamcrestConverter
     */
    private static final HamcrestConverter<Node, String> NODE_TO_STRING_MATCHER = new HamcrestConverter<Node, String>(new Function<String, Node>() {
        @Override
        public Node apply(String s) {
            return XmlUtil.asXml(s);
        }
    });

    /**
     * Returns a Matcher&lt;String&gt; (presumably for matching a string containing XML) which checks that the given xPath is matched by the matcher.
     *
     * @param xPath   The XPath to check
     * @param matcher The matcher to verify
     * @return The matcher
     */
    public static TypeSafeMatcher<String> hasXPath(String xPath, Matcher<String> matcher) {
        return NODE_TO_STRING_MATCHER.convert(Matchers.hasXPath(xPath, matcher));
    }

    /**
     * Returns a Matcher&lt;String&gt; (presumably for matching a string containing XML) which checks that the given xPath exists (ie doesn't return null).
     *
     * @param xPath The XPath to check
     * @return The matcher
     */
    public static TypeSafeMatcher<String> hasXPath(String xPath) {
        return NODE_TO_STRING_MATCHER.convert(Matchers.hasXPath(xPath));
    }
}