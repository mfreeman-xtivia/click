/*
 * Copyright 2005 Malcolm A. Edgar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.click.extras.control;

import java.util.Date;

import net.sf.click.MockContext;
import net.sf.click.MockRequest;
import net.sf.click.extras.control.DateField;
import junit.framework.TestCase;

/**
 * Provides DateField JUnit TestCase.
 *
 * @author Malcolm Edgar
 */
public class DateFieldTest extends TestCase {

    public void testFormatPattern() throws Exception {
        DateField dateField = new DateField("Delivery date");
        assertEquals("dd MMM yyyy", dateField.getFormatPattern());
        assertEquals("%d %b %Y", dateField.getCalendarPattern());
        
        dateField = new DateField("Delivery date");
        dateField.setFormatPattern(" dd MMM yyyy ");
        assertEquals(" dd MMM yyyy ", dateField.getFormatPattern());
        assertEquals(" %d %b %Y ", dateField.getCalendarPattern());        

        dateField = new DateField("Delivery date");
        dateField.setFormatPattern("dd/MMM/yyyy");
        assertEquals("dd/MMM/yyyy", dateField.getFormatPattern());
        assertEquals("%d/%b/%Y", dateField.getCalendarPattern()); 
        
        dateField = new DateField("Delivery date");
        dateField.setFormatPattern("dd.MMM.yyyy");
        assertEquals("dd.MMM.yyyy", dateField.getFormatPattern());
        assertEquals("%d.%b.%Y", dateField.getCalendarPattern());
        
        dateField = new DateField("Delivery date");
        dateField.setFormatPattern("dd.MM.yy");
        assertEquals("dd.MM.yy", dateField.getFormatPattern());
        assertEquals("%d.%m.%y", dateField.getCalendarPattern());
        
        dateField = new DateField("Delivery date");
        dateField.setFormatPattern("d/M/yy");
        assertEquals("d/M/yy", dateField.getFormatPattern());
        assertEquals("%e/%m/%y", dateField.getCalendarPattern());
    }
    
    public void testNullParameter() {
        MockRequest request = new MockRequest();
        MockContext.initContext(request);
        
        DateField dateField = new DateField("dateField");
        assertEquals("dateField", dateField.getName());
        
        request.getParameterMap().put("dateField", "");        
        dateField.onProcess();        
        Date date = dateField.getDate();
        assertNull(date);
        
        request.getParameterMap().put("dateField", " ");        
        dateField.onProcess();        
        date = dateField.getDate();
        assertNull(date);

        request.getParameterMap().put("dateField", null);        
        dateField.onProcess();        
        date = dateField.getDate();
        assertNull(date);
        
        dateField.setValue(null);
        date = dateField.getDate();
        assertNull(date);
    }

}