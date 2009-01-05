/*
* Copyright 2004 The Apache Software Foundation
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

package org.apache.click.extras.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Implementation of <tt>HttpServletResponseWrapper</tt> that works with
 * the CompressionServletResponseStream implementation.
 * <p/>
 * Note this Jakarta Tomcat examples Filter is packaged in Click Extras for
 * convenience.
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @version Revision: 1.3, Date: 2004/03/18 16:40:28
 */
class CompressionServletResponseWrapper extends HttpServletResponseWrapper {

    // ----------------------------------------------------- Constructor

    /**
     * Calls the parent constructor which creates a ServletResponse adaptor
     * wrapping the given response object.
     */

    public CompressionServletResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * Original response.
     */
    protected HttpServletResponse origResponse = null;

    /**
     * Descriptive information about this Response implementation.
     */
    protected static final String INFO = "CompressionServletResponseWrapper";

    /**
     * The ServletOutputStream that has been returned by
     * <code>getOutputStream()</code>, if any.
     */
    protected ServletOutputStream stream = null;

    /**
     * The PrintWriter that has been returned by
     * <code>getWriter()</code>, if any.
     */
    protected PrintWriter writer = null;

    /**
     * The threshold number to compress.
     */
    protected int threshold = 0;

    /**
     * Debug level.
     */
    private int debug = 0;

    /**
     * Content type.
     */
    protected String contentType = null;

    // --------------------------------------------------------- Public Methods

    /**
     * Set content type.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
        origResponse.setContentType(contentType);
    }


    /**
     * Set threshold number.
     */
    public void setCompressionThreshold(int threshold) {
        this.threshold = threshold;
    }


    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {

        CompressionResponseStream stream =
            new CompressionResponseStream(origResponse);
        stream.setBuffer(threshold);

        return stream;
    }

    /**
     * Finish a response.
     */
    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
        }
    }

    // ------------------------------------------------ ServletResponse Methods

    /**
     * Flush the buffer and commit this response.
     *
     * @exception IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {
        // CLK-323 fix NPE
        if (writer != null) {
            writer.flush();
        } else if (stream != null) {
            stream.flush();
        }
    }

    /**
     * Return the servlet output stream associated with this Response.
     *
     * @exception IllegalStateException if <code>getWriter</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream getOutputStream() throws IOException {

        if (writer != null) {
            String msg =
                "getWriter() has already been called for this response";
            throw new IllegalStateException(msg);
        }

        if (stream == null) {
            stream = createOutputStream();
        }

        return (stream);

    }

    /**
     * Return the writer associated with this Response.
     *
     * @exception IllegalStateException if <code>getOutputStream</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public PrintWriter getWriter() throws IOException {

        if (writer != null) {
            return (writer);
        }

        if (stream != null) {
            String msg =
                "getOutputStream() has already been called for this response";
            throw new IllegalStateException(msg);
        }

        stream = createOutputStream();
        if (debug > 1) {
            System.out.println("stream is set to " + stream + " in getWriter");
        }
        //String charset = getCharsetFromContentType(contentType);
        String charEnc = origResponse.getCharacterEncoding();

        // HttpServletResponse.getCharacterEncoding() shouldn't return null
        // according the spec, so feel free to remove that "if"
        if (charEnc != null) {
            writer = new PrintWriter(new OutputStreamWriter(stream, charEnc));
        } else {
            writer = new PrintWriter(stream);
        }

        return (writer);

    }

    public void setContentLength(int length) {
    }

    public void setIntHeader(String header, int value) {
        if (!"Content-Length".equals(header)) {
            super.setIntHeader(header, value);
        }
    }

    public void setHeader(String header, String value) {
        if (!"Content-Length".equals(header)) {
            super.setHeader(header, value);
        }
    }
}
