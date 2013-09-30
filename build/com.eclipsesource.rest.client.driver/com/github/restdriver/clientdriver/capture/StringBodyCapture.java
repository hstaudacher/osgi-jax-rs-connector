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
package com.github.restdriver.clientdriver.capture;

import com.github.restdriver.clientdriver.capture.BodyCapture;

/**
 * Implementation of BodyCapture which just keeps the body as a String.
 */
public class StringBodyCapture implements BodyCapture<String> {

    private String content;

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setBody(String content) {
        this.content = content;
    }
}
