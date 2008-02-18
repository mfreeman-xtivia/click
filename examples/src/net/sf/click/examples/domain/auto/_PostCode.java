package net.sf.click.examples.domain.auto;

/** Class _PostCode was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually,
  * since it may be overwritten next time code is regenerated.
  * If you need to make any customizations, please use subclass.
  */
public abstract class _PostCode extends net.sf.click.examples.domain.BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String LOCALITY_PROPERTY = "locality";
    public static final String POST_CODE_PROPERTY = "postCode";
    public static final String STATE_PROPERTY = "state";

    public static final String ID_PK_COLUMN = "id";

    public void setLocality(String locality) {
        writeProperty("locality", locality);
    }
    public String getLocality() {
        return (String)readProperty("locality");
    }


    public void setPostCode(String postCode) {
        writeProperty("postCode", postCode);
    }
    public String getPostCode() {
        return (String)readProperty("postCode");
    }


    public void setState(String state) {
        writeProperty("state", state);
    }
    public String getState() {
        return (String)readProperty("state");
    }


}
