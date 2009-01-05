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
package org.apache.click.examples.control;

import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.click.control.Form;
import org.apache.click.control.Panel;
import org.apache.click.extras.control.DateField;
import org.apache.click.util.ClickUtils;

/**
 * Provides a custom date FilterPanel.
 *
 * @author Malcolm Edgar
 */
public class FilterPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private Form filterForm = new Form("filterForm");
    private DateField startDate = new DateField("startDate");
    private DateField endDate = new DateField("endDate");

    public FilterPanel() {
        startDate.setFormatPattern("dd MMM yyyy");
        startDate.setSize(11);

        endDate.setFormatPattern("dd MMM yyyy");
        endDate.setSize(11);

        filterForm.add(startDate);
        filterForm.add(endDate);

        add(filterForm);
    }

    public Date getEndDate() {
        return endDate.getDate();
    }

    public Date getStartDate() {
        return startDate.getDate();
    }

    public boolean isSelected() {
        return startDate.getDate() != null || endDate.getDate() != null;
    }

    /**
     * @see org.apache.click.Control#onDeploy(ServletContext)
     */
    public void onDeploy(ServletContext servletContext) {
        String[] files = new String[] {
            "/org/apache/click/examples/control/twistie-down.png",
            "/org/apache/click/examples/control/twistie-up.png"
        };

        ClickUtils.deployFiles(servletContext, files, "images");
    }

}
