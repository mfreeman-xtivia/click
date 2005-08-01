/*
 * Copyright 2004-2005 Malcolm A. Edgar
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
package net.sf.click.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Provides the default Velocity template format object. A format object
 * is added to the Velocity Context using the name "<span class="blue">format</span>",
 * and is then available in the Page for formatting objects.
 * <p/>
 * For example the following Page code adds a date to the model:
 *
 * <pre class="codeJava">
 * <span class="kw">public void</span> onGet() {
 *    Date date = order.deliveryDate();
 *    addModel("<span class="st">deliveryDate</span>", date);
 * } </pre>
 *
 * In the page template we use the format object:
 *
 * <pre class="codeHtml">
 * Delivery date: <span class="red">$format</span>.date(<span class="st">$deliveryDate</span>, "dd MMM yyyy") </pre>
 *
 * Which renders the output as:
 *
 * <table class="htmlExample" cellspacing="12">
 * <tr><td>
 * Delivery date: 21 Jan 2004
 * </td></tr>
 * </table>
 *
 * The format object class can defined in the "click.xml" configuration file
 * using the syntax:
 *
 * <pre class="codeConfig">
 * &lt;format classname="<span class="st">com.mycorp.utils.Format</span>"/&gt; </pre>
 *
 * The format class must provide a no-args public constructor . After a Page is
 * created its <a href="../Page.html#format">format</a> property is set.
 * The ClickServlet will then add this property to the Velocity Context.
 * <p/>
 * When designing a format object ensure it is light weight, as a new
 * format object will be created for every new Page.
 *
 * @author Malcolm Edgar
 * @version $Id$
 */
public class Format {

    /**
     * Return a currency formatted String value for the given number, using
     * the default Locale. If the number is null this method will return
     * "&amp;nbsp;"
     *
     * @param number the number to format
     * @return a currency formatted number string
     */
    public String currency(Number number) {
        if (number != null) {
            NumberFormat format = NumberFormat.getCurrencyInstance();

            return format.format(number.doubleValue());

        } else {
            return "&nbsp;";
        }
    }

    /**
     * Return a formatted date string using the given date and formatting
     * pattern. See SimpleDateFormat for information on the format
     * pattern string.
     * <p/>
     * If the date is null this method will return "&amp;nbsp;"
     *
     * @param date the date value to format
     * @param pattern the SimpleDateFormat formatting pattern
     * @return a formatted date string
     * @throws IllegalArgumentException if the pattern string is null
     */
    public String date(Date date, String pattern) {
        if (date != null) {
            if (pattern == null) {
                throw new IllegalArgumentException("Null pattern parameter");
            }

            SimpleDateFormat format = new SimpleDateFormat(pattern);

            return format.format(date);

        } else {
            return "&nbsp;";
        }
    }

    /**
     * Return a formatted date string using the given date and the default
     * DateFormat. If the date is null this method will return "&amp;nbsp;"
     *
     * @param date the date value to format
     * @return a formatted date string
     */
    public String date(Date date) {
        if (date != null) {
            DateFormat format = DateFormat.getDateInstance();

            return format.format(date);

        } else {
            return "&nbsp;";
        }
    }

    /**
     * Return a decimal formatted string using the given number and pattern.
     * See DecimalFormat for information on the format pattern string.
     * <p/>
     * If the number is null this method will return "&amp;nbsp;"
     *
     * @param number the number to format
     * @param pattern the decimal format pattern
     * @return the fornmatted decimal number
     * @throws IllegalArgumentException if the pattern string is null
     */
    public String decimal(Number number, String pattern) {
        if (number != null) {
            if (pattern == null) {
                throw new IllegalArgumentException("Null pattern parameter");
            }

            DecimalFormat format = new DecimalFormat(pattern);

            return format.format(number.doubleValue());

        } else {
            return "&nbsp;";
        }
    }

    /**
     * Return a decimal formatted string using the given number and pattern.
     * If the number is null this method will return "&amp;nbsp;"
     *
     * @param number the number to format
     * @return the fornmatted decimal number
     */
    public String decimal(Number number) {
        if (number != null) {
            DecimalFormat format = new DecimalFormat();

            return format.format(number.doubleValue());

        } else {
            return "&nbsp;";
        }
    }

    /**
     * Escape the given object value as a HTML string, or "&amp;nbsp;"
     * if the object is null.
     * <p>
     * Implementation is provided by Jakarta Commons Lang utility:
     * <tt>StringEscapeUtils.escapeHtml(String)</tt>
     *
     * @param value unescaped HTML
     * @return the HTML escaped string
     */
    public String html(Object value) {
        if (value != null) {
            return StringEscapeUtils.escapeHtml(value.toString());
        } else {
            return "&nbsp;";
        }
    }

    /**
     * Escape the given object value as a JavaScript string, or "" if the object
     * is null.
     * <p>
     * Implementation is provided by Jakarta Commons Lang utility:
     * <tt>StringEscapeUtils.escapeJavaScript(String)</tt>
     *
     * @param value unescaped JavaScript
     * @return the JavaScript escaped string
     */
    public String javascript(String value) {
        if (value != null) {
            return StringEscapeUtils.escapeJavaScript(value);
        } else {
            return "";
        }
    }

    /**
     * Return the value string limited to maxlength characters. If the string
     * gets curtailed, "..." is appended to it.
     * <p/>
     * Adapted from Velocity Tools Formatter.
     *
     * @param value the string value to limit the length of
     * @param maxlength An int with the maximum length
     * @return a length limited string
     */
    public String limitLength(String value, int maxlength) {
        return limitLength(value, maxlength, "...");
    }

    /**
     * Return the value string limited to maxlength characters. If the string
     * gets curtailed and the suffix parameter is appended to it.
     * <p/>
     * Adapted from Velocity Tools Formatter.
     *
     * @param value the string value to limit the length of
     * @param maxlength An int with the maximum length
     * @param suffix the suffix to append to the length limited string
     * @return a length limited string
     */
    public String limitLength(String value, int maxlength, String suffix) {
        String ret = value;
        if (value.length() > maxlength) {
            ret = value.substring(0, maxlength - suffix.length()) + suffix;
        }
        return ret;
    }

    /**
     * Return a percentage formatted number string using number. If the number
     * is null this method will return "&amp;nbsp;"
     *
     * @param number the number value to format
     * @return a percentage formatted number string
     */
    public String percentage(Number number) {
        if (number != null) {
            NumberFormat format = NumberFormat.getPercentInstance();

            return format.format(number.doubleValue());

        } else {
            return "&nbsp;";
        }
    }

    /**
     * Return a formatted time string using the given date and the default
     * DateFormat. If the date is null this method will return "&amp;nbsp;"
     *
     * @param date the date value to format
     * @return a formatted time string
     */
    public String time(Date date) {
        if (date != null) {
            DateFormat format = DateFormat.getTimeInstance();

            return format.format(date);

        } else {
            return "&nbsp;";
        }
    }

    /**
     * Return the string representation of the given object, or "&amp;nbsp;"
     * if the object is null.
     *
     * @param object the object to format
     * @return the string representation of the object
     */
    public String string(Object object) {
        if (object != null) {
            return object.toString();
        } else {
            return "&nbsp;";
        }
    }
}
