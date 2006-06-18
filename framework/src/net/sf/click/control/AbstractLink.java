/*
 * Copyright 2004-2006 Malcolm A. Edgar
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
package net.sf.click.control;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import net.sf.click.Context;
import net.sf.click.Control;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.click.util.MessagesMap;

public abstract class AbstractLink implements Control {

    private static final long serialVersionUID = 1L;

    // ----------------------------------------------------- Instance Variables

    /** The link attributes map. */
    protected Map attributes;

    /** The context. */
    protected transient Context context;

    /** The Field disabled value. */
    protected boolean disabled;

    /** The link display label. */
    protected String label;

    /** The link localized messages Map. */
    protected Map messages;

    /** The link name. */
    protected String name;

    /** The control's parent. */
    protected Object parent;

    /** The link parameters map. */
    protected Map parameters;

    /** The link title attribute, which acts as a tooltip help message. */
    protected String title;

    // ----------------------------------------------------------- Constructors

    /**
     * Create an AbstractLink for the given name.
     *
     * @param name the page link name
     * @throws IllegalArgumentException if the name is null
     */
    public AbstractLink(String name) {
        setName(name);
    }

    /**
     * Create an AbstractLink with no name defined, <b>please note</b> the
     * control's name must be defined before it is valid.
     * <p/>
     * <div style="border: 1px solid red;padding:0.5em;">
     * No-args constructors are provided for Java Bean tools support and are not
     * intended for general use. If you create a control instance using a
     * no-args constructor you must define its name before adding it to its
     * parent. </div>
     */
    public AbstractLink() {
    }

    // ------------------------------------------------------ Public Attributes

    /**
     * Return the link HTML attribute with the given name, or null if the
     * attribute does not exist.
     *
     * @param name the name of link HTML attribute
     * @return the link HTML attribute
     */
    public String getAttribute(String name) {
        if (hasAttributes()) {
            return (String) getAttributes().get(name);
        } else {
            return null;
        }
    }

    /**
     * Set the link attribute with the given attribute name and value. You would
     * generally use attributes if you were creating the entire AbstractLink
     * programatically and rendering it with the {@link #toString()} method.
     * <p/>
     * For example given the AbstractLink:
     *
     * <pre class="codeJava">
     * AbstractLink addLink = <span class="kw">new</span> AbstractLink(<span class="red">"addLink"</span>, <span class="st">"Add"</span>);
     * addLink.setAttribute(<span class="st">"class"</span>, <span class="st">"table"</span>); </pre>
     *
     * And the page template:
     * <pre class="codeHtml">
     * $<span class="red">addLink</span> </pre>
     *
     * Will render the HTML as:
     * <pre class="codeHtml">
     * &lt;a href="..?AbstractLink=<span class="red">addLink</span>" <span class="st">class</span>=<span class="st">"table"</span>&gt;<span class="st">Add</span>&lt;/a&gt; </pre>
     *
     * @param name the attribute name
     * @param value the attribute value
     * @throws IllegalArgumentException if name parameter is null
     */
    public void setAttribute(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        if (value != null) {
            getAttributes().put(name, value);
        } else {
            getAttributes().remove(name);
        }
    }

    /**
     * Return the AbstractLink attributes Map.
     *
     * @return the AbstractLink attributes Map
     */
    public Map getAttributes() {
        if (attributes == null) {
            attributes = new HashMap(5);
        }
        return attributes;
    }

    /**
     * Return true if the AbstractLink has attributes or false otherwise.
     *
     * @return true if the AbstractLink has attributes on false otherwise
     */
    public boolean hasAttributes() {
        if (attributes != null && !attributes.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see Control#getContext()
     *
     * @return the Page request Context
     */
    public Context getContext() {
        return context;
    }

    /**
     * @see Control#setContext(Context)
     *
     * @param context the Page request Context
     * @throws IllegalArgumentException if the Context is null
     */
    public void setContext(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Null context parameter");
        }
        this.context = context;
    }

    /**
     * Return true if the AbstractLink is a disabled.
     *
     * @return true if the AbstractLink is a disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Set the disabled flag.
     *
     * @param disabled the disabled flag
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Return the AbstractLink anchor &lt;a&gt; tag href attribute.
     * This method will encode the URL with the session ID
     * if required using <tt>HttpServletResponse.encodeURL()</tt>.
     *
     * @return the AbstractLink HTML href attribute
     */
    public abstract String getHref();

    /**
     * This method returns null.
     *
     * @see net.sf.click.Control#getHtmlImports()
     *
     * @return null
     */
    public String getHtmlImports() {
        return null;
    }

    /**
     * Return the "id" attribute value if defined, or the AbstractLink name
     * otherwise.
     *
     * @see net.sf.click.Control#getId()
     *
     * @return HTML element identifier attribute "id" value
     */
    public String getId() {
        if (hasAttributes() && getAttributes().containsKey("id")) {
            return getAttribute("id");
        } else {
            return getName();
        }
    }

    /**
     * Return the label for the AbstractLink.
     * <p/>
     * If the label value is null, this method will attempt to find a
     * localized label message in the parent messages using the key:
     * <blockquote>
     * <tt>getName() + ".label"</tt>
     * </blockquote>
     * If not found then the message will be looked up in the
     * <tt>/click-control.properties</tt> file using the same key.
     * If a value still cannot be found then the ActinLink name will be converted
     * into a label using the method: {@link ClickUtils#toLabel(String)}
     * <p/>
     * For examle given a <tt>OrderPage</tt> with the properties file
     * <tt>OrderPage.properties</tt>:
     *
     * <pre class="codeConfig">
     * <span class="st">checkout</span>.label=<span class="red">Checkout</span>
     * <span class="st">checkout</span>.title=<span class="red">Proceed to Checkout</span> </pre>
     *
     * The page AbstractLink code:
     * <pre class="codeJava">
     * <span class="kw">public class</span> OrderPage <span class="kw">extends</span> Page {
     *     AbstractLink checkoutLink = <span class="kw">new</span> AbstractLink(<span class="st">"checkout"</span>);
     *     ..
     * } </pre>
     *
     * Will render the AbstractLink label and title properties as:
     * <pre class="codeHtml">
     * &lt;a href="order-page.htm?actionLink=<span class="st">checkout</span>" title="<span class="red">Proceed to Checkout</span>"&gt;<span class="red">Checkout</span>&lt;/a&gt; </pre>
     *
     * When a label value is not set, or defined in any properties files, then
     * its value will be created from the Fields name.
     * <p/>
     * For example given the ActionLink code:
     *
     * <pre class="codeJava">
     * ActionLink nameField = <span class="kw">new</span> ActionLink(<span class="st">"deleteItem"</span>);  </pre>
     *
     * Will render the ActionLink label as:
     * <pre class="codeHtml">
     * &lt;a href="order-page.htm?actionLink=<span class="st">deleteItem</span>"&gt;<span class="red">Delete Item</span>&lt;/a&gt; </pre>
     *
     * Note the ActionLink label can include raw HTML to render other elements.
     * <p/>
     * For example the configured label:
     *
     * <pre class="codeConfig">
     * <span class="st">edit</span>.label=<span class="red">&lt;img src="images/edit.png" title="Edit Item"/&gt;</span> </pre>
     *
     * Will render the ActionLink label as:
     * <pre class="codeHtml">
     * &lt;a href="..?actionLink=<span class="st">edit</span>"&gt;<span class="red">&lt;img src="images/edit.png" title="Edit Item"/&gt;</span>&lt;/a&gt; </pre>
     *
     * @return the label for the ActionLink
     */
    public String getLabel() {
        if (label == null) {
            label = getMessage(getName() + ".label");
        }
        if (label == null) {
            label = ClickUtils.toLabel(getName());
        }
        return label;
    }

    /**
     * Set the label for the ActionLink.
     *
     * @see #getLabel()
     *
     * @param label the label for the ActionLink
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Return the localized message for the given key, or null if not found.
     * <p/>
     * This method will attempt to lookup the localized message in the
     * parent's messages, which by resolves to the Page's resource bundle.
     * <p/>
     * If the message was not found, the this method will attempt to look up the
     * value in the <tt>/click-control.properties</tt> message properties file.
     * <p/>
     * If still not found, this method will return null.
     *
     * @param name the name of the message resource
     * @return the named localized message, or null if not found
     */
    public String getMessage(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        String message = null;

        Map parentMessages = ClickUtils.getParentMessages(this);
        if (parentMessages.containsKey(name)) {

            message = (String) parentMessages.get(name);
        }

        if (message == null && getMessages().containsKey(name)) {
            message = (String) getMessages().get(name);
        }

        return message;
    }

    /**
     * Return a Map of localized messages for the ActionLink.
     *
     * @return a Map of localized messages for the ActionLink
     * @throws IllegalStateException if the context for the link has not be set
     */
    public Map getMessages() {
        if (messages == null) {
            if (getContext() != null) {
                messages =
                    new MessagesMap(this, CONTROL_MESSAGES, getContext());

            } else {
                String msg = "Cannot initialize messages as context not set";
                throw new IllegalStateException(msg);
            }
        }
        return messages;
    }

    /**
     * @see net.sf.click.Control#getName()
     *
     * @return the name of the control
     */
    public String getName() {
        return name;
    }

    /**
     * @see net.sf.click.Control#setName(String)
     *
     * @param name of the control
     * @throws IllegalArgumentException if the name is null
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }
        this.name = name;
    }

    /**
     * Set the link parameter with the given parameter name and value. You would
     * generally use parameter if you were creating the entire AbstractLink
     * programatically and rendering it with the {@link #toString()} method.
     * <p/>
     * For example given the AbstractLink:
     *
     * <pre class="codeJava">
     * AbstractLink addLink = <span class="kw">new</span> AbstractLink(<span class="red">"addLink"</span>, <span class="st">"Add"</span>);
     * addLink.setAttribute(<span class="st">"class"</span>, <span class="st">"table"</span>); </pre>
     *
     * And the page template:
     * <pre class="codeHtml">
     * $<span class="red">addLink</span> </pre>
     *
     * Will render the HTML as:
     * <pre class="codeHtml">
     * &lt;a href="..?AbstractLink=<span class="red">addLink</span>" <span class="st">class</span>=<span class="st">"table"</span>&gt;<span class="st">Add</span>&lt;/a&gt; </pre>
     *
     * @param name the attribute name
     * @param value the attribute value
     * @throws IllegalArgumentException if name parameter is null
     */
    public void setParameter(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        if (value != null) {
            getParameters().put(name, value);
        } else {
            getParameters().remove(name);
        }
    }

    /**
     * Return the AbstractLink parameters Map.
     *
     * @return the AbstractLink parameters Map
     */
    public Map getParameters() {
        if (parameters == null) {
            parameters = new TreeMap();
        }
        return parameters;
    }

    /**
     * Return true if the AbstractLink has parameters or false otherwise.
     *
     * @return true if the AbstractLink has parameters on false otherwise
     */
    public boolean hasParameters() {
        if (parameters != null && !parameters.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see Control#getParent()
     *
     * @return the Control's parent
     */
    public Object getParent() {
        return parent;
    }

    /**
     * @see Control#setParent(Object)
     *
     * @param parent the parent of the Control
     */
    public void setParent(Object parent) {
        this.parent = parent;
    }

    /**
     * Return the 'title' attribute, or null if not defined. The title
     * attribute acts like tooltip message over the link.
     * <p/>
     * If the title value is null, this method will attempt to find a
     * localized label message in the parent messages using the key:
     * <blockquote>
     * <tt>getName() + ".title"</tt>
     * </blockquote>
     * If not found then the message will be looked up in the
     * <tt>/click-control.properties</tt> file using the same key.
     * <p/>
     * For examle given a <tt>ItemsPage</tt> with the properties file
     * <tt>ItemPage.properties</tt>:
     *
     * <pre class="codeConfig">
     * <span class="st">edit</span>.label=<span class="red">Edit</span>
     * <span class="st">edit</span>.title=<span class="red">Edit Item</span> </pre>
     *
     * The page ActionLink code:
     * <pre class="codeJava">
     * <span class="kw">public class</span> ItemsPage <span class="kw">extends</span> Page {
     *     ActionLink editLink = <span class="kw">new</span> ActionLink(<span class="st">"edit"</span>);
     *     ..
     * } </pre>
     *
     * Will render the ActionLink label and title properties as:
     * <pre class="codeHtml">
     * &lt;a href="items-page.htm?actionLink=<span class="st">edit</span>" title="<span class="red">Edit Item</span>"&gt;<span class="red">Edit</span>&lt;/a&gt; </pre>
     *
     * @return the 'title' attribute tooltip message
     */
    public String getTitle() {
        if (title == null) {
            title = getMessage(getName() + ".title");
        }
        return title;
    }

    /**
     * Set the 'title' attribute tooltip message.
     *
     * @see #getTitle()
     *
     * @param value the 'title' attribute tooltip message
     */
    public void setTitle(String value) {
        title = value;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * This method does nothing.
     *
     * @see net.sf.click.Control#onDeploy(ServletContext)
     *
     * @param servletContext the servlet context
     */
    public void onDeploy(ServletContext servletContext) {
    }

    /**
     * Return the HTML rendered anchor link string. This method
     * will render the entire anchor link including the tags, the label and
     * any attributes, see {@link #setAttribute(String, String)} for an
     * example.
     *
     * @see Object#toString()
     *
     * @return the HTML rendered anchor link string
     */
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();

        buffer.elementStart("a");

        buffer.appendAttribute("href", getHref());
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("title", getTitle());
        if (hasAttributes()) {
            buffer.appendAttributes(getAttributes());
        }
        if (isDisabled()) {
            buffer.appendAttributeDisabled();
        }
        buffer.closeTag();

        buffer.append(getLabel());

        buffer.elementEnd("a");

        return buffer.toString();
    }
}
