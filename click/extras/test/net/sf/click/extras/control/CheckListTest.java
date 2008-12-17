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
package net.sf.click.extras.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.click.control.Option;

import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.control.Form;

public class CheckListTest extends TestCase {

    public CheckListTest() {
        super();
    }
    
    public void testSortOptions() {
        CheckList cL = new CheckList();
        int[] in = {1,2,3,4,5,6};
        List oL = createOptionsList(in);

        cL.setOptionList(oL);
        int[] sort = {6,4,2,1,3,5};
        cL.sortOptions(createValues(sort));
        compareOptions(sort,cL.getOptionList());
        
        cL.setOptionList(oL);
        cL.sortOptions(createValues(in));
        compareOptions(in,cL.getOptionList());
        
        cL.setOptionList(oL);
        sort = new int[]{5,4,6,10};
        cL.sortOptions(createValues(sort));
        compareOptions(new int[]{5,4,6,1,2,3},cL.getOptionList());
        
        cL.setOptionList(oL);
        sort = new int[]{7,10,5,9,4,6,1,2,3,8};
        cL.sortOptions(createValues(sort));
        compareOptions(new int[]{5,4,6,1,2,3},cL.getOptionList());
        
    }
    
    private String[] createValues(int[] is) {
        String[] ret = new String[is.length];
        for (int i = 0; i < is.length; i++) {
            ret[i] = Integer.toString(is[i]);
        }
        return ret;
    }
    
    private List createOptionsList(int[] values) {
        List ret = new ArrayList();
        for(int i=0; i<values.length; i++) {
            String value = Integer.toString(values[i]);
            String label = "Label: "+i;
            ret.add(new Option(value,label));
        }
        return ret;
    }
    
    private void compareOptions(int[] values,List options) {
        assertNotNull(options);
        String demanded ="";
        String given = "";
        for(int i=0;i<values.length;i++) {
            demanded += values[i]+",";
        }
        for (Iterator it = options.iterator(); it.hasNext(); ) {
            Option opt = (Option) it.next();
            assertNotNull(opt);
            given += opt.getValue()+",";
        }
        
        assertEquals(demanded,given);
    }

    /**
     * Check that FileField value is escaped. This protects against
     * cross-site scripting attacks (XSS).
     */
    public void testEscapeValue() {
        MockContext.initContext();

        Form form = new Form("form");
        CheckList checkList = new CheckList("check");
        form.add(checkList);

        String value = "<script>";
        String expected = "&lt;script&gt;";

        checkList.add(value);

        assertTrue(checkList.toString().indexOf(expected) > 1);
        
        // Check that the value <script> is not rendered
        assertTrue(checkList.toString().indexOf(value) < 0);
    }
}
