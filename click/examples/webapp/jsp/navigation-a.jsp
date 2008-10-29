You are currently on page <span>NavigationA</span>.
<p>

<h4>Forward</h4>

To forward to <b>NavigationB</b> click <a href="${forwardLink.href}">here</a>.

<pre class="javaCode">
    setForward(<span class='st'>"/navigation-b.jsp"</span>);
</pre>

To forward to <b>NavigationB</b> passing the parameter ${forwardParamLink.value} click
<a href="${forwardParamLink.href}">here</a>.

<pre class="javaCode">
    getContext().getRequest().setAttribute(<span class='st'>"param"</span>, param);
    setForward(<span class='st'>"/navigation-b.jsp"</span>);
</pre>

<h4 style="margin-top:2em;">Redirect</h4>

To redirect to <b>NavigationB</b> click <a href="${redirectLink.href}">here</a>.

<pre class="javaCode">
    setRedirect(<span class='st'>"/navigation-b.jsp"</span>);
</pre>

To redirect to <b>NavigationB</b> passing the parameter ${redirectParamLink.value} click
<a href="${redirectParamLink.href}">here</a>.

<pre class="javaCode">
    setRedirect(<span class='st'>"/navigation-b.jsp?param="</span> + param);
</pre>


<p style="margin-top:2em;">
Take notice of the different URLs in your browser when you use forward and redirect.