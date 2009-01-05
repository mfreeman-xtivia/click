/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click.examples.control.html;

import org.apache.click.control.AbstractControl;
import org.apache.click.util.HtmlStringBuffer;

/**
 * This control renders a string of text.
 *
 * @author Bob Schellink
 */
public class Text extends AbstractControl {

    private Object text;

    public Text() {

    }

    public Text(Object text) {
        this.text = text;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(20);
        render(buffer);
        return buffer.toString();
    }

    public void render(HtmlStringBuffer buffer) {
        if(getText() != null) {
            buffer.append(getText());
        }
    }
}
