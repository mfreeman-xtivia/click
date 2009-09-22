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
package org.apache.click.examples.page.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.click.Context;
import org.apache.click.control.Checkbox;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.examples.page.BorderPage;
import org.apache.click.examples.util.ExampleUtils;
import org.apache.click.extras.tree.Tree;
import org.apache.click.extras.tree.TreeListener;
import org.apache.click.extras.tree.TreeNode;

/**
 * Example usage of some extra functionality of the {@link Tree} control.
 *
 * @author Bob Schellink
 */
public class AdvancedTreePage extends BorderPage implements TreeListener {

    public static final String TREE_NODES_SESSION_KEY = "advancedTreeNodes";

    private Tree tree;

    // --------------------------------------------------------- Event Handlers

    /**
     * @see org.apache.click.Page#onInit()
     */
    @Override
    public void onInit() {
        super.onInit();

        //Create the tree and tree model and add it to the page
        tree = buildTree();
        tree.addListener(this);
        addControl(tree);

        //Build the options user interface for users to interactively
        //change the tree values.
        buildOptionsUI();
    }

    /**
     * Called when user submits the options form.
     */
    public boolean onApplyOptionsClick() {
        //Reset the tree and nodes to default values
        resetTree();

        //Store the users options in the session
        TreeOptions options = new TreeOptions();
        options.javascriptEnabled = jsEnabled.isChecked();
        options.rootNodeDisplayed = rootNodeDisplayed.isChecked();
        ExampleUtils.setSessionObject(options);

        //Apply users new options
        applyOptions();

        return true;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Creates and return a new tree instance.
     */
    public Tree createTree() {
        return new Tree("advancedTree" );
    }

    /**
     * Build the tree model and stores it in the session.
     */
    public Tree buildTree() {
        tree = createTree();

        //Try and load the already stored nodes from the session.
        //If this is the first time we access the page, this method
        //will return null, indicating no nodes is stored.
        TreeNode existingRootNode = loadNodesFromSession();

        if(existingRootNode != null) {
            //OK we had already nodes stored in the session, so no need
            //to rebuild them. We attach the root node and return.
            tree.setRootNode(existingRootNode);

            return tree;
        }

        //Create a node representing the root directory with the specified
        //parameter as the value. Because an id is not specified, a random
        //one will be generated by the node. By default the root node is
        //not rendered by the tree. This can be changed by calling
        //tree.setRootNodeDisplayed(true).
        TreeNode root = new TreeNode("c:");

        //Create a new directory, setting the root directory as its parent. Here
        //we do specify a id as the 2nd argument, so no id is generated.
        TreeNode dev = new TreeNode("dev","1", root);

        //The follwing 2 nodes represent files in the directory, setting the
        //dev node as their parent. Note the false argument to the constructor.
        //This means that the specific node does not support child nodes, and
        //it will be rendered as a leaf icon. If children are supported (the
        //default value) then even  if the node is a leaf, it will still be rendered
        //as a collapsed icon. In the example a default leaf node will be
        //rendered as a directory, and a node that does not support children is
        //rendered as a file.
        new TreeNode("java.pdf", "2", dev, false);
        new TreeNode("ruby.pdf", "3", dev, false);

        //We continue constructing the rest of the tree
        TreeNode programFiles = new TreeNode("program files", "4", root);
        new TreeNode("Adobe", "5", programFiles);

        TreeNode download = new TreeNode("downloads","6", root);

        TreeNode web = new TreeNode("web", "7", download);
        new TreeNode("html.pdf", "8", web, false);
        new TreeNode("css.html", "9", web, false);

        TreeNode databases = new TreeNode("databases", "10", download);
        new TreeNode("mysql.html","11",databases, false);
        new TreeNode("oracle.pdf","12",databases, false);
        new TreeNode("postgres","13",databases, false);

        //Attach the root node containing all the other nodes to the tree
        tree.setRootNode(root);

        //By default root node is not displayed in browser. Here we expand it,
        //so that the root's children are visible.
        expandRoot();

        //Store the tree nodes in the session so on the next request we do not
        //have to rebuild it. Also because we keep the tree nodes in the session
        //the nodes will keep their state between requests.
        storeNodesInSession(root);

        return tree;
    }

    // --------------------------------------------------- TreeListener Support

    /**
     * This method, which implements TreeListener, is called when a node is selected
     */
    public void nodeSelected(Tree tree, TreeNode node, Context context, boolean oldValue) {
        List list = getModelValue("selected");
        list.add(node);
    }

    /**
     * This method, which implements TreeListener, is called when a node is deselected
     */
    public void nodeDeselected(Tree tree, TreeNode node, Context context, boolean oldValue) {
        List list = getModelValue("deselected");
        list.add(node);
    }

    /**
     * This method, which implements TreeListener, is called when a node is expanded
     */
    public void nodeExpanded(Tree tree, TreeNode node, Context context, boolean oldValue) {
        List list = getModelValue("expanded");
        list.add(node);
    }

    /**
     * This method, which implements TreeListener, is called when a node is collapsed
     */
    public void nodeCollapsed(Tree tree, TreeNode node, Context context, boolean oldValue) {
        List list = getModelValue("collapsed");
        list.add(node);
    }

    /**
     * Returns the value for the specified name from the page's model.
     * If a value is not found, a new ArrayList is created and added to the model.
     */
    private List getModelValue(String name) {
        List list = (List) getModel().get(name);
        if(list == null) {
            list = new ArrayList();
            addModel(name, list);
        }
        return list;
    }

    // ------------------------------------------------------------------- NOTE
    //The code below is not specific to the tree control. It was moved here
    //so as not to distract the user from the use of the tree control.

    private Form optionsForm;
    private Checkbox jsEnabled = new Checkbox("jsEnabled", "JavaScript Enabled");
    private Checkbox rootNodeDisplayed = new Checkbox("rootNodeDisplayed", "Display root node");
    private Submit applyOptions = new Submit("apply", "Apply Options", this, "onApplyOptionsClick");

    /**
     * Builds the user interface for users to change the tree options interactively.
     */
    public void buildOptionsUI() {
        //Form to handle user selected options
        optionsForm = new Form("optionForm");
        FieldSet fieldSet = new FieldSet("options", "Form Options");
        fieldSet.add(jsEnabled);
        fieldSet.add(rootNodeDisplayed);
        optionsForm.add(fieldSet);

        addControl(optionsForm);
        optionsForm.add(applyOptions);

        //Apply users existing options
        applyOptions();
    }

    /** Form options holder. */
    public static class TreeOptions implements Serializable {
        private static final long serialVersionUID = 1L;
        boolean javascriptEnabled= false;
        boolean rootNodeDisplayed = false;
    }

    /**
     * Reset the tree to initial state
     */
    private void resetTree() {
        // Remove any Session entries made by Tree
        tree.cleanupSession();

        //Temporarily disable notification to any tree listeners while we reset the tree
        tree.setNotifyListeners(false);

        //Clear all current selections so the tree can start afresh with new options enabled
        tree.deselectAll();
        tree.collapseAll();

        //Enable notification to any tree listeners again
        tree.setNotifyListeners(true);

        if(!rootNodeDisplayed.isChecked()) {
            //If rootNodeDisplayed is false, the root node is not displayed in browser. Here we
            //expand it,  so that the root's children are visible.
            expandRoot();
        }
    }

    /**
     * Store the tree nodes in the session
     */
    private void storeNodesInSession(TreeNode rootNode) {
        if (tree.getRootNode() == null) {
            return;
        }

        getContext().getSession().setAttribute(TREE_NODES_SESSION_KEY, rootNode);
    }

    /**
     * Retrieve the tree nodes from the session if available. Otherwise we return
     * null.
     */
    private TreeNode loadNodesFromSession() {
        return (TreeNode) getContext().getSession().getAttribute(TREE_NODES_SESSION_KEY);
    }

    /**
     * Enable javascript so that there are no round trips to server
     * when expand and collapsing nodes.
     */
    private void enableJavascript(boolean enable) {
        //This call enables the client side javascript functionality
        tree.setJavascriptEnabled(enable);
    }

    /**
     * Applies the user selected options to the tree like enabling javascript,
     * displaying the root node etc.
     */
    private void applyOptions() {

        //We retrieve our stored options from the session and set the controls state
        TreeOptions options = (TreeOptions)
            ExampleUtils.getSessionObject(TreeOptions.class);

        jsEnabled.setChecked(options.javascriptEnabled);
        rootNodeDisplayed.setChecked(options.rootNodeDisplayed);

        //Enable or disable javascript functionality based on users current option
        enableJavascript(options.javascriptEnabled);

        //Indicates if we want to display the root node or not.
        tree.setRootNodeDisplayed(options.rootNodeDisplayed);
    }

    /**
     * Expand the root but do not notify any tree listeners of the change.
     */
    private void expandRoot() {
        //Temporarily disable notification to any tree listeners while we expand the root node
        tree.setNotifyListeners(false);

        tree.expand(tree.getRootNode());

        //Enable notification to any tree listeners again
        tree.setNotifyListeners(true);
    }

}
