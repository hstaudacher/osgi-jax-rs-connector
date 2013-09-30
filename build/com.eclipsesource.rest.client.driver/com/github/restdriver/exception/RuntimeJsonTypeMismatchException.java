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
package com.github.restdriver.exception;

/**
 * Runtime Exception class for errors when JSONpath returns a result of an unexpected type.
 */
public class RuntimeJsonTypeMismatchException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5947827084788598073L;
    
    /**
     * Constructor.
     * 
     * @param message The message.
     * @param ex The ClassCastException which we're wrapping.
     */
    public RuntimeJsonTypeMismatchException(String message, ClassCastException ex) {
        super(message, ex);
    }
    
    /**
     * Constructor.
     * 
     * @param message The message.
     */
    public RuntimeJsonTypeMismatchException(String message) {
        super(message);
    }
    
}
