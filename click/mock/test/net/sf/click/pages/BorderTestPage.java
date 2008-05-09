package net.sf.click.pages;

public class BorderTestPage extends BorderPage {
    
    public boolean onSecurityCheck() {
        return true;
    }

    public void onInit() {
        getContext().setRequestAttribute("id", "200");
        System.out.println("myparam value: " + getContext().getRequestParameter("myparam"));
        System.out.println("myfile value " + getContext().getFileItem("myfile"));
        addModel("myparam", getContext().getRequestParameter("myparam"));
    }

    public void onGet() {
    }

    public void onPost() {
    }

    public void onRender() {
    }
    
    public void onDestroy() {
    }

}
