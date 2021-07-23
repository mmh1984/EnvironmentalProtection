package pep.com.environmentalprotection;

/**
 * Created by Maynard on 4/14/2017.
 */

public class NewsComments {


    String newsID;
    String newsComments;
    String newsDate;
    String newsPosted;

    public NewsComments(String newsID, String newsComments, String newsDate, String newsPosted) {
        this.newsID = newsID;
        this.newsComments = newsComments;
        this.newsDate = newsDate;
        this.newsPosted = newsPosted;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public String getNewsComments() {
        return newsComments;
    }

    public void setNewsComments(String newsComments) {
        this.newsComments = newsComments;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsPosted() {
        return newsPosted;
    }

    public void setNewsPosted(String newsPosted) {
        this.newsPosted = newsPosted;
    }
}
