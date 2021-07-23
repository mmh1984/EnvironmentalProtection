package pep.com.environmentalprotection;

/**
 * Created by Maynard on 4/14/2017.
 */

public class News {

    String ID;
    String title;
    String contents;
    String dateposted;
    String addedby;
    String organization;


    public News(String ID, String title, String contents, String dateposted, String addedby,String organization) {
        this.ID = ID;
        this.title = title;
        this.contents = contents;
        this.dateposted = dateposted;
        this.addedby = addedby;
        this.organization=organization;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDateposted() {
        return dateposted;
    }

    public void setDateposted(String dateposted) {
        this.dateposted = dateposted;
    }

    public String getAddedby() {
        return addedby;
    }

    public void setAddedby(String addedby) {
        this.addedby = addedby;
    }
}
