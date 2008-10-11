package net.sf.click.util;

import java.util.Locale;
import java.util.Map;
import junit.framework.TestCase;
import net.sf.click.MockContext;
import net.sf.click.Page;
import net.sf.click.control.Field;
import net.sf.click.control.Form;
import net.sf.click.control.TextField;

/**
 *
 * @author Bob Schellink
 */
public class ContainerMessageMapTest extends TestCase {

    /**
     * CLK-373. Assert that Control properties are resolved correctly when
     * the Control is part of a hierarchy of Controls e.g. Page -> Form -> Field.
     */
    public void testContainerMessageInheritance() {
        MockContext.initContext(Locale.ENGLISH);

        Page page = new Page();
        MyForm form = new MyForm("myform");
        page.addControl(form);
        Field snfld = form.getField("snfld");
        Map map = form.getMessages();
        assertFalse(map.isEmpty());
        assertTrue(map.size() >= 2);
        assertEquals("Special Name", snfld.getLabel());
        assertEquals("Enter the special name!", snfld.getTitle());
        assertEquals("Special Name", map.get("snfld.label"));
        assertEquals("Enter the special name!", map.get("snfld.title"));
    }

    public class MyForm extends Form {

        public MyForm(String name) {
            super(name);
            buildForm();
        }

        private void buildForm() {
            TextField snfld = new TextField("snfld");
            this.add(snfld);
        }
    }
}