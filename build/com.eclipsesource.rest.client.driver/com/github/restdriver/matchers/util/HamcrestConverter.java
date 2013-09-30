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
package com.github.restdriver.matchers.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Function;

/**
 * Used to convert a Matcher&lt;T&gt; to a Matcher &lt;U&gt; by using a provided Function&lt;U, T&gt;.
 */
public class HamcrestConverter<T, U> {

    private final Function<U, T> converter;

    public HamcrestConverter(Function<U, T> converter) {
        this.converter = converter;
    }

    public TypeSafeMatcher<U> convert(Matcher<T> matcher) {
        return new ConverterMatcher<T, U>(matcher, this.converter);
    }

    private static class ConverterMatcher<V, W> extends TypeSafeMatcher<W> {

        private final Matcher<V> matcher;
        private final Function<W, V> converter;

        public ConverterMatcher(Matcher<V> matcher, Function<W, V> converter) {
            this.matcher = matcher;
            this.converter = converter;
        }

        @Override
        protected boolean matchesSafely(W item) {
            return this.matcher.matches(this.converter.apply(item));
        }

        @Override
        public void describeTo(Description description) {
            this.matcher.describeTo(description);
        }
    }
}