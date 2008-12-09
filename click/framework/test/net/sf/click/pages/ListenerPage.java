package net.sf.click.pages;

import junit.framework.Assert;
import net.sf.click.ActionListener;
import net.sf.click.Control;
import net.sf.click.Page;
import net.sf.click.control.Form;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;

/**
 * Page which tests action listener functionality.
 */
public class ListenerPage extends Page {

    /** Form holder. */
    public Form form = new Form("form");
    
    /** Indicates if the submit Assertion succeeded or not. */
    public boolean success = false;

    /**
     * Initialize page.
     */
    public void onInit() {

        // Create and add submit button *before* adding the textField
        Submit submit = new Submit("submit");
        form.add(submit);

        // Add listener on submit button
        submit.setActionListener(new ActionListener() {
            public boolean onAction(Control source) {
                // Assert that this listener can access the textfield value
                Assert.assertEquals("one", form.getFieldValue("field"));
                success = true;
                return true;
            }
        });

        // Add textfield after the button.
        form.add(new TextField("field"));
    }
}