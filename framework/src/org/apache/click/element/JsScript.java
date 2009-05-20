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
package org.apache.click.element;

import java.util.HashMap;
import java.util.Map;
import org.apache.click.Context;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Provides a HEAD element for including <tt>inline</tt> JavaScript using the
 * &lt;script&gt; tag.
 * <p/>
 * Example usage:
 *
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *
 *     public List getHeadElements() {
 *         // We use lazy loading to ensure the JS is only added the
 *         // first time this method is called.
 *         if (headElements == null) {
 *             // Get the head elements from the super implementation
 *             headElements = super.getHeadElements();
 *
 *             JsScript jsScript = new JsScript("alert('Hello World!);");
 *             headElements.add(jsScript);
 *         }
 *         return headElements;
 *     }
 * } </pre>
 *
 * The <tt>jsScript</tt> instance will be rendered as follows:
 *
 * <pre class="prettyprint">
 * &lt;script type="text/javascript"&gt;
 * alert('Hello World');
 * &lt;/script&gt; </pre>
 *
 * Below is an example showing how to render inline Javascript from a
 * Velocity template.
 * <p/>
 * First we create a Velocity template <tt>(/js/mycorp-template.js)</tt> which
 * contains the variable <tt>$divId</tt> that must be replaced at runtime with
 * the real Div ID attribute:
 *
 * <pre class="prettyprint">
 * hide = function() {
 *     var div = document.getElementById('$divId');
 *     div.style.display = "none";
 * } </pre>
 *
 * Next is the Page implementation:
 *
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *
 *     public List getHeadElements() {
 *         // We use lazy loading to ensure the JS is only added the
 *         // first time this method is called.
 *         if (headElements == null) {
 *             // Get the head elements from the super implementation
 *             headElements = super.getHeadElements();
 *
 *             // Create a default template model to pass to the template
 *             Map model = ClickUtils.createTemplateModel(this, getContext());
 *
 *             // Add the id of the div to hide
 *             model.put("divId", "myDiv");
 *
 *             // Specify the path to the JavaScript template
 *             String templatePath = "/js/mycorp-template.js";
 *
 *             // Create the inline JavaScript for the given template path and model
 *             JsScript jsScript = new JsScript(templatePath, model);
 *             headElements.add(jsScript);
 *         }
 *         return headElements;
 *     }
 * } </pre>
 *
 * The <tt>jsScript</tt> instance will render as follows (assuming the context
 * path is <tt>myApp</tt>):
 *
 * <pre class="prettyprint">
 * &lt;script type="text/javascript"&gt;
 *     hide = function() {
 *         var div = document.getElementById('myDiv');
 *         div.style.display = "none";
 *     }
 * &lt;/style&gt; </pre>
 *
 * <h3>Character data (CDATA) support</h3>
 *
 * Sometimes it is necessary to wrap <tt>inline</tt> {@link JsScript JavaScript}
 * in CDATA tags. Two use cases are common for doing this:
 * <ul>
 * <li>For XML parsing: When using Ajax one often send back partial
 * XML snippets to the browser, which is parsed as valid XML. However the XML
 * parser will throw an error if the content contains reserved XML characters
 * such as '&amp;', '&lt;' and '&gt;'. For these situations it is recommended
 * to wrap the script content inside CDATA tags.
 * </li>
 * <li>XHTML validation: if you want to validate your site using an XHTML
 * validator e.g: <a target="_blank" href="http://validator.w3.org/">http://validator.w3.org/</a>.</li>
 * </ul>
 *
 * To wrap the JavaScript content in CDATA tags, set
 * {@link #setCharacterData(boolean)} to true. Below is shown how the JavaScript
 * content would be rendered:
 *
 * <pre class="codeHtml">
 * &lt;script type="text/javascript"&gt;
 *  <span style="color:#3F7F5F">/&lowast;&lt;![CDATA[&lowast;/</span>
 *
 *  if(x &lt; y) alert('Hello');
 *
 *  <span style="color:#3F7F5F">/&lowast;]]&gt;&lowast;/</span>
 * &lt;/script&gt; </pre>
 *
 * Notice the CDATA tags are commented out which ensures older browsers that
 * don't understand the CDATA tag, will ignore it and only process the actual
 * content.
 * <p/>
 * For an overview of XHTML validation and CDATA tags please see
 * <a target="_blank" href="http://javascript.about.com/library/blxhtml.htm">http://javascript.about.com/library/blxhtml.htm</a>.
 *
 * @author Bob Schellink
 */
public class JsScript extends ResourceElement {

    // -------------------------------------------------------------- Variables

    /** A buffer holding the inline JavaScript content. */
    private HtmlStringBuffer content = new HtmlStringBuffer();

    /**
     * Indicates if the JsScript's content should be wrapped in a CDATA tag.
     */
    private boolean characterData = false;

    /**
     * Indicates the JsScript content must be executed as soon as the browser
     * DOM is available, default value is false.
     */
    private boolean executeOnDomReady = false;

    /** The path of the template to render. */
    private String template;

    /** The model of the template to render. */
    private Map model;

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new inline JavaScript element.
     */
    public JsScript() {
        this(null);
    }

    /**
     * Construct a new inline JavaScript element with the given content.
     *
     * @param content the JavaScript content
     */
    public JsScript(String content) {
        if (content != null) {
            this.content.append(content);
        }
        setAttribute("type", "text/javascript");
    }

    /**
     * Construct a new inline JavaScript element for the given template path
     * and template model.
     * <p/>
     * When the JsScript is rendered the template and model will be merged and
     * the result will be rendered together with any JsScript
     * {@link #setContent(org.apache.click.util.HtmlStringBuffer) content}.
     * <p/>
     *
     * For example:
     * <pre class="prettyprint">
     * public class MyPage extends Page {
     *     public void onInit() {
     *         Context context = getContext();
     *
     *         // Create a default template model
     *         Map model = ClickUtils.createTemplateModel(this, context);
     *
     *         // Create JsScript for the given template path and model
     *         JsScript script = new JsScript("/mypage-template.js", model);
     *
     *         // Add script to the Page Head elements
     *         getHeadElements().add(script);
     *     }
     * } </pre>
     *
     * @param template the path of the template to render
     * @param model the template model
     */
    public JsScript(String template, Map model) {
        this(null);
        setTemplate(template);
        setModel(model);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Returns the JavaScript HTML tag: &lt;script&gt;.
     *
     * @return the JavaScript HTML tag: &lt;script&gt;
     */
    public String getTag() {
        return "script";
    }

    /**
     * Return the JavaScript content buffer.
     *
     * @return the JavaScript content buffer
     */
    public HtmlStringBuffer getContent() {
        return content;
    }

    /**
     * Set the JavaScript content buffer.
     *
     * @param content the new content buffer
     */
    public void setContent(HtmlStringBuffer content) {
        this.content = content;
    }

    /**
     * Return true if the JsScript's content should be wrapped in CDATA tags,
     * false otherwise.
     *
     * @return true if the JsScript's content should be wrapped in CDATA tags,
     * false otherwise
     */
    public boolean isCharacterData() {
        return characterData;
    }

    /**
     * Sets whether the JsScript's content should be wrapped in CDATA tags or not.
     *
     * @param characterData true indicates that the JsScript's content should be
     * wrapped in CDATA tags, false otherwise
     */
    public void setCharacterData(boolean characterData) {
        this.characterData = characterData;
    }

    /**
     * Return true if the JsScript content must be executed as soon as the
     * browser DOM is ready, false otherwise. Default value is false.
     *
     * @see #setExecuteOnDomReady(boolean)
     *
     * @return true if the JsScript content must be executed as soon as the
     * browser DOM is ready.
     */
    public boolean isExecuteOnDomReady() {
        return executeOnDomReady;
    }

    /**
     * Sets whether the JsScript content must be executed as soon as the browser
     * DOM is ready.
     * <p/>
     * If this flag is true, the JsScript content will be registered with
     * the "<tt>addLoadEvent</tt>" function from the JavaScript file
     * "<tt>control.js</tt>".
     * <p/>
     * <b>Please note:</b> when setting this flag to true, the file JavaScript
     * file "<tt>control.js</tt>" must already be included in the Page or
     * Control, it won't be included automatically.
     *
     * @param executeOnDomReady indicates whether the JsScript content must be
     * executed as soon as the browser DOM is ready.
     */
    public void setExecuteOnDomReady(boolean executeOnDomReady) {
        this.executeOnDomReady = executeOnDomReady;
    }

    /**
     * Return the path of the template to render.
     *
     * @see #setTemplate(java.lang.String)
     *
     * @return the path of the template to render
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the path of the template to render.
     * <p/>
     * If the {@link #template} property is set, the template and {@link #model}
     * will be merged and the result will be rendered together with any JsScript
     * {@link #setContent(org.apache.click.util.HtmlStringBuffer) content}.
     *
     * @param template the path of the template to render
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Return the model of the {@link #setTemplate(java.lang.String) template}
     * to render.
     *
     * @see #setModel(java.util.Map)
     *
     * @return the model of the template to render
     */
    public Map getModel() {
        return model;
    }

    /**
     * Set the model of the template to render.
     * <p/>
     * If the {@link #template} property is set, the template and {@link #model}
     * will be merged and the result will be rendered together with any JsScript
     * {@link #setContent(org.apache.click.util.HtmlStringBuffer) content}.
     *
     * @param model the model of the template to render
     */
    public void setModel(Map model) {
        this.model = model;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Append the given JavaScript string to the content buffer.
     *
     * @param content the JavaScript string to append to the content
     * buffer
     * @return the JavaScript content buffer
     */
    public HtmlStringBuffer append(String content) {
        return this.content.append(content);
    }

    /**
     * Render the HTML representation of the JsScript element to the specified
     * buffer.
     *
     * @param buffer the buffer to render output to
     */
    public void render(HtmlStringBuffer buffer) {

        // Render IE conditional comment if conditional comment was set
        renderConditionalCommentPrefix(buffer);

        buffer.elementStart(getTag());

        buffer.appendAttribute("id", getId());
        appendAttributes(buffer);

        buffer.closeTag();

        buffer.append("\n");

        // Render CDATA tag if necessary
        renderCharacterDataPrefix(buffer);

        // Render the DOM ready function prefix
        renderDomReadyPrefix(buffer);

        renderContent(buffer);

        renderDomReadySuffix(buffer);

        renderCharacterDataSuffix(buffer);

        buffer.append("\n");

        buffer.elementEnd(getTag());

        renderConditionalCommentSuffix(buffer);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @param o the object with which to compare this instance with
     * @return true if the specified object is the same as this object
     */
    public boolean equals(Object o) {
        if (!isUnique()) {
            return super.equals(o);
        }

        //1. Use the == operator to check if the argument is a reference to this object.
        if (o == this) {
            return true;
        }

        //2. Use the instanceof operator to check if the argument is of the correct type.
        if (!(o instanceof JsScript)) {
            return false;
        }

        //3. Cast the argument to the correct type.
        JsScript that = (JsScript) o;

        String id = getId();
        String thatId = that.getId();
        return id == null ? thatId == null : id.equals(thatId);
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     * @return a hash code value for this object
     */
    public int hashCode() {
        if (!isUnique()) {
            return super.hashCode();
        }
        return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Render the JsScript {@link #setContent(org.apache.click.util.HtmlStringBuffer) content}
     * to the specified buffer.
     * <p/>
     * <b>Please note:</b> if the {@link #setTemplate(java.lang.String) template}
     * property is set, this method will merge the {@link #setTemplate(java.lang.String) template}
     * and {@link #setModel(java.util.Map) model} and the result will be
     * rendered, together with the JsScript
     * {@link #setContent(org.apache.click.util.HtmlStringBuffer) content},
     * to the specified buffer.
     *
     * @param buffer the buffer to append the output to
     */
    protected void renderContent(HtmlStringBuffer buffer) {
        if (getTemplate() != null) {
            Context context = getContext();

            Map templateModel = getModel();
            if (templateModel == null) {
                templateModel = new HashMap();
            }
            buffer.append(context.renderTemplate(getTemplate(), templateModel));

        }
        buffer.append(getContent());
    }

    /**
     * Render the "<tt>addLoadEvent</tt>" function prefix to ensure the script
     * is executed as soon as the browser DOM is available. The prefix is
     * "<tt>addLoadEvent(function(){</tt>".
     *
     * @see #renderDomReadySuffix(org.apache.click.util.HtmlStringBuffer)
     *
     * @param buffer buffer to append the addLoadEvent function to
     */
    protected void renderDomReadyPrefix(HtmlStringBuffer buffer) {
        // Wrap content in addLoadEvent function
        if (isExecuteOnDomReady()) {
            buffer.append("addLoadEvent(function(){\n");
        }
    }

    /**
     * Render the "<tt>addLoadEvent</tt>" function suffix. The suffix is
     * "<tt>});</tt>".
     *
     * @see #renderDomReadyPrefix(org.apache.click.util.HtmlStringBuffer)
     *
     * @param buffer buffer to append the conditional comment prefix
     */
    protected void renderDomReadySuffix(HtmlStringBuffer buffer) {
        // Close addLoadEvent function
        if (isExecuteOnDomReady()) {
            buffer.append("});");
        }
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * Render the CDATA tag prefix to the specified buffer if
     * {@link #isCharacterData()} returns true. The prefix is
     * <tt>/&lowast;&lt;![CDATA[&lowast;/</tt>.
     *
     * @see #renderCharacterDataSuffix(org.apache.click.util.HtmlStringBuffer)
     *
     * @param buffer buffer to append the conditional comment prefix
     */
    void renderCharacterDataPrefix(HtmlStringBuffer buffer) {
        // Wrap character data in CDATA block
        if (isCharacterData()) {
            buffer.append("/*<![CDATA[*/ ");
        }
    }

    /**
     * Render the CDATA tag suffix to the specified buffer if
     * {@link #isCharacterData()} returns true. The prefix is
     * <tt>/&lowast;]]&gt;&lowast;/</tt>.
     *
     * @see #renderCharacterDataPrefix(org.apache.click.util.HtmlStringBuffer)
     *
     * @param buffer buffer to append the conditional comment prefix
     */
    void renderCharacterDataSuffix(HtmlStringBuffer buffer) {
        if (isCharacterData()) {
            buffer.append(" /*]]>*/");
        }
    }
}