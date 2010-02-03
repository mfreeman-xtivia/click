package net.sf.click.examples.page.ajax;

import java.util.List;

import net.sf.click.control.Form;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;
import net.sf.click.examples.domain.Customer;
import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.control.AutoCompleteTextField;

/**
 * Provides AJAX AutoCompleteTextField example page.
 *
 * @author Malcolm Edgar
 */
public class AutoCompletePage extends BorderPage {

    public Form form = new Form();
    public Customer customer;

    private TextField nameField;

    // ------------------------------------------------------------ Constructor

    public AutoCompletePage() {
        nameField = new AutoCompleteTextField("name", true) {
            public List getAutoCompleteList(String criteria) {
                return getCustomerService().getCustomerNamesLike(criteria);
            }
        };

        form.add(nameField);

        form.add(new Submit(" OK ", this, "onOkClick"));
    }

    // --------------------------------------------------------- Event Handlers

    public boolean onOkClick() {
        if (form.isValid()) {
            String name = nameField.getValue();
            customer = getCustomerService().findCustomerByName(name);
        }
        return true;
    }

}