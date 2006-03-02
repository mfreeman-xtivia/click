package net.sf.click.extras.cayenne;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.click.control.Decorator;
import net.sf.click.control.Option;
import net.sf.click.control.Select;
import net.sf.click.util.ClickUtils;
import ognl.Ognl;

import org.apache.commons.lang.StringUtils;
import org.objectstyle.cayenne.DataObject;
import org.objectstyle.cayenne.DataObjectUtils;
import org.objectstyle.cayenne.access.DataContext;
import org.objectstyle.cayenne.query.SelectQuery;

/**
 * TODO:
 *
 * @author Malcolm Edgar
 */
public class PropertySelect extends Select {

    private static final long serialVersionUID = 1L;

    // ----------------------------------------------------- Instance Variables

    /** The option label rendering decorator. */
    protected Decorator decorator;

    /** The name of the configured select query. */
    protected String queryName;

    /** The data object property to render as the option label. */
    protected String optionLabel;

    /** The option list Cayenne <tt>SelectQuery</tt>. */
    protected SelectQuery selectQuery;

    /** The property value object. */
    protected DataObject valueObject;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a PropertySelect field with the given name.
     *
     * @param name the name of the field
     */
    public PropertySelect(String name) {
        super(name);
    }

    /**
     * Create a PropertySelect field with the given name and label.
     *
     * @param name the name of the field
     * @param label the label of the field
     */
    public PropertySelect(String name, String label) {
        super(name, label);
    }

    /**
     * Create a PropertySelect field with the given name and required status.
     *
     * @param name the name of the field
     * @param required the field required status
     */
    public PropertySelect(String name, boolean required) {
        super(name, required);
    }

    /**
     * Create a PropertySelect field with the given name, label and required
     * status.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param required the field required status
     */
    public PropertySelect(String name, String label, boolean required) {
        super(name, label, required);
    }

    /**
     * Create a PropertySelect field with no name defined, <b>please note</b>
     * the control's name must be defined before it is valid.
     * <p/>
     * <div style="border: 1px solid red;padding:0.5em;">
     * No-args constructors are provided for Java Bean tools support and are not
     * intended for general use. If you create a control instance using a
     * no-args constructor you must define its name before adding it to its
     * parent. </div>
     */
    public PropertySelect() {
        super();
    }

    // ------------------------------------------------------------- Properties

    /**
     * Return the option label rendering decorator.
     *
     * @return the option label rendering decorator
     */
    public Decorator getDecorator() {
        return decorator;
    }

    /**
     * Set the decorator to render the select values.
     *
     * @param decorator the decorator to render the select values with
     */
    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    /**
     * Return true if multiple options can be selected.
     *
     * @see Select#isMultiple()
     *
     * @return false
     */
    public boolean isMultiple() {
        return false;
    }

    /**
     * Set the multiple options can be selected flag.
     *
     * @see Select#setMultiple(boolean)
     *
     * @param value the multiple options can be selected flag
     */
    public void setMultiple(boolean value) {
        String msg = "PropertySelect does not support multiple property values";
        throw new UnsupportedOperationException(msg);
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public SelectQuery getSelectQuery() {
        return selectQuery;
    }

    public void setSelectQuery(SelectQuery selectQuery) {
        this.selectQuery = selectQuery;
    }

    public Class getValueClass() {
        return valueObject.getClass();
    }

    public Object getValueObject() {
        return valueObject;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * @see Select#bindRequestValue()
     */
    public void bindRequestValue() {

        loadOptionList();

        super.bindRequestValue();

        if (StringUtils.isNotBlank(getValue())) {

            CayenneForm form = (CayenneForm) getForm();
            Class doClass = form.getDataObjectClass();
            String getterName = ClickUtils.toGetterName(getName());

            try {
                Method method = doClass.getMethod(getterName, null);

                DataContext dataContext = form.getDataContext();

                Class propertyClass = method.getReturnType();

                Integer propertyPk = new Integer(getValue());

                valueObject =
                    DataObjectUtils.objectForPK
                        (dataContext, propertyClass, propertyPk);

                setValue(propertyPk.toString());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @see Select#toString()
     */
    public String toString() {

        // Load property options if not already set
        if (getOptionList().isEmpty()) {
            loadOptionList();
        }

        // Select option value if value defined and not form submission
        if (getValueObject() == null && !getForm().isFormSubmission()) {

            CayenneForm form = (CayenneForm) getForm();
            DataContext dataContext = form.getDataContext();
            Class doClass = form.getDataObjectClass();
            Integer doPk = form.getDataObjectPk();

            if (doPk != null) {
                DataObject dataObject =
                    DataObjectUtils.objectForPK(dataContext, doClass, doPk);

                String getterName = ClickUtils.toGetterName(getName());

                try {
                    Method method = doClass.getMethod(getterName, null);

                    DataObject property =
                        (DataObject) method.invoke(dataObject, null);

                    if (property != null) {
                        Object propPk = DataObjectUtils.pkForObject(property);
                        setValue(propPk.toString());
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return super.toString();
    }

    // ------------------------------------------------------ Protected Methods

    protected void loadOptionList() {

        CayenneForm form = (CayenneForm) getForm();
        DataContext dataContext = form.getDataContext();

        try {
            List list = null;

            if (getSelectQuery() != null) {
                list = dataContext.performQuery(getSelectQuery());

            } else if (getQueryName() != null) {
                 list = dataContext.performQuery(getQueryName(), false);

            } else {
                Class doClass = form.getDataObjectClass();
                String getterName = ClickUtils.toGetterName(getName());
                Method method = doClass.getMethod(getterName, null);
                Class propertyClass = method.getReturnType();

                list = dataContext.performQuery(new SelectQuery(propertyClass));
            }

            if (isRequired()) {
                getOptionList().add(Option.EMPTY_OPTION);
            }

            Map ognlContext = new HashMap();

            for (int i = 0; i < list.size(); i++) {
                DataObject dataObject = (DataObject) list.get(i);
                int pk = DataObjectUtils.intPKForObject(dataObject);

                String value = String.valueOf(pk);
                Object label = null;

                if (getDecorator() != null) {
                    label = getDecorator().render(dataObject, getContext());
                } else {
                    label = Ognl.getValue(optionLabel, ognlContext, dataObject);
                }

                Option option = new Option(value, label.toString());

                add(option);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
