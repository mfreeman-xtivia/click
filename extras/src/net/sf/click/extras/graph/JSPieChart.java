package net.sf.click.extras.graph;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.ArrayList;

import net.sf.click.util.ClickUtils;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.click.control.AbstractControl;

/**
 * Control that displays a Pie Chart based on JavaScript only.
 * <p/>
 * <table class='htmlHeader' cellspacing='10'>
 * <tr>
 * <td>
 * <img align='middle' hspace='2' src='pie-chart.png' title='Line Chart'/>
 * </td>
 * </tr>
 * </table>
 *
 * @author Ahmed Mohombe
 */
public class JSPieChart extends AbstractControl {

    /** The HTML imports statements. */
    protected static final String CHART_IMPORTS =
            "<script type=\"text/javascript\" src=\"$/click/graph/jsgraph/wz_jsgraphics.js\"></script>\n"
            + "<script type=\"text/javascript\" src=\"$/click/graph/jsgraph/pie.js\"></script>\n";

    /** Chart resource file names. */
    protected static final String[] CHART_RESOURCES = {
            "/net/sf/click/extras/graph/jsgraph/wz_jsgraphics.js",
            "/net/sf/click/extras/graph/jsgraph/pie.js"
    };

    /** Width of the DIV element that encloses this chart. */
    private int chartWidth = 380; // default value for width

    /** Height of the DIV element that encloses this chart. */
    private int chartHeight = 350; // default value for height

    private List xLabels = new ArrayList();
    private List yValues = new ArrayList();

    /** The chart display label. */
    private String label = "Pie Chart"; // default label

    /**
     * Create a PieChart Control with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public JSPieChart() {
    }

    /**
     * Create a pie chart with the given name.
     *
     * @param name the button name
     */    
    public JSPieChart(String name) {
        super.setName(name);
    }

    /**
     * Create a pie chart with the given name and label.
     *
     * @param name the name of the chart control
     * @param label the label of the chart that will be displayed
     */
    public JSPieChart(String name, String label) {
        super.setName(name);
        setLabel(label);
    }

    /**
     * Adds a "point" to the grapic/chart at the end of the list.
     *
     * @param pointLabel the displayed label of the "point"
     * @param pointValue the value of the "point".
     */    
    public void addPoint(String pointLabel, Integer pointValue) {
        xLabels.add(pointLabel);
        yValues.add(pointValue);
    }

    /**
     * Adds a "point" to the grapic/chart at a specified index in the list.
     *
     * @param index index at which the specified point is to be inserted
     * @param pointLabel the displayed label of the "point"
     * @param pointValue the value of the "point".
     */        
    public void addPoint(int index, String pointLabel, Integer pointValue) {
        xLabels.add(index, pointLabel);
        yValues.add(index, pointValue);
    }

    /**
     * Return the width of the chart (the enclosing DIV element).
     *
     * @return the width of the chart
     */
    public int getChartWidth() {
        return chartWidth;
    }

    /**
     * Set the width of the chart (of the enclosing DIV element), as a
     * pixel value.
     *
     * @param chartWidth the chart width in pixels.
     */
    public void setChartWidth(int chartWidth) {
        this.chartWidth = chartWidth;
    }

    /**
     * Return the height of the chart (the enclosing DIV element).
     *
     * @return the height of the chart
     */
    public int getChartHeight() {
        return chartHeight;
    }

    /**
     * Set the height of the chart (of the enclosing DIV element), as a
     * pixel value.
     *
     * @param chartHeight the chart height in pixels.
     */    
    public void setChartHeight(int chartHeight) {
        this.chartHeight = chartHeight;
    }

    /**
     * Return the label of the chart.
     *
     * @return the label of the chart
     */    
    public String getLabel() {
        return label;
    }

    /**
     * Set the chart display caption.
     *
     * @param label the display label of the chart
     */        
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Return the HTML head import statements for the javascript files
     * used by this control.
     *
     * @see net.sf.click.Control#getHtmlImports()
     *
     * @return the HTML head import statements for the javascript files
     * used by this control.
     */    
    public String getHtmlImports() {
        String path = getContext().getRequest().getContextPath();
        StringBuffer buffer = new StringBuffer(100);
        buffer.append(StringUtils.replace(CHART_IMPORTS, "$", path));
        return buffer.toString();
    }

    /**
     * This method does nothing.
     *
     * @see net.sf.click.Control#setListener(Object, String)
     *
     * @param listener the listener object with the named method to invoke
     * @param method the name of the method to invoke
     */
    public void setListener(Object listener, String method) {
        // do nothing
    }

    /**
     * Deploys the javascript files of this control to the <code>[click/graph/jsgraph]</code> directory.
     *
     * @see net.sf.click.Control#onDeploy(ServletContext)
     *
     * @param servletContext the webapplication's servlet context
     */    
    public void onDeploy(ServletContext servletContext) {
        ClickUtils.deployFiles(servletContext, CHART_RESOURCES, "click/graph/jsgraph");
    }

    /**
     * Returns true, as javascript charts perform no server side logic.
     *
     * @return true
     */
    public boolean onProcess() {
        return true;
    }

    /**
     * Return the HTML rendered pie chart.
     *
     * @return the HTML rendered pie chart string
     */
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.elementStart("div");
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("style", "overflow: auto; position:relative;height:" + getChartHeight() + "px;width:" + getChartWidth() + "px;");
        buffer.closeTag();
        buffer.elementEnd("div");

        String var = "g_" + getId();
        buffer.append("\n<script type=\"text/javascript\">\n");
        buffer.append("var " + var + " = new pie();\n");
        for (int i = 0; i < xLabels.size(); i++) {
            String pointLabel = (String) xLabels.get(i);
            Integer pointValue = (Integer) yValues.get(i);
            buffer.append(var + ".add('" + pointLabel + "'," + pointValue + ");\n");
        }
        buffer.append(var + ".render('" + getId() + "','" + getLabel() + "');\n");
        buffer.append("</script> \n");
        return buffer.toString();
    }

}
