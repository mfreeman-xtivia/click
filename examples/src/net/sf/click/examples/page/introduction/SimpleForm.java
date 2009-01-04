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
package net.sf.click.examples.page.introduction;

import net.sf.click.control.Form;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;
import net.sf.click.examples.page.BorderPage;

/**
 * Provides a simple Form example Page.
 * <p/>
 * Note the public scope Form control field is automatically added to the Page's
 * list of controls and the String msg field is automatically added to the
 * Page's model.
 * <p/>
 * The form <tt>onSubmit</tt> control listener is invoked when the submit button
 * is clicked.
 *
 * @author Malcolm Edgar
 */
public class SimpleForm extends BorderPage {

    public Form form = new Form();
    public String msg;

    // ------------------------------------------------------------ Constructor

    public SimpleForm() {
        form.add(new TextField("name", true));
        form.add(new Submit("OK"));

        form.setListener(this, "onSubmit");
    }

    // --------------------------------------------------------- Event Handlers

    /**
     * Handle the form submit event.
     */
    public boolean onSubmit() {
        if (form.isValid()) {
            msg = "Your name is " + form.getFieldValue("name");
        }
        return true;
    }

}