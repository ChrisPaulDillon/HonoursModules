package labstuff.gcu.me.org.assessment;

import java.io.Serializable;

/*
 * Created by Christopher Dillon
 *  Student ID: S1514278
 */

public class Traffic implements Serializable
{
    private String title;
    private String description;
    private String link;
    private String georss;
    private String author;
    private String comments;
    private String pubDate;

    public Traffic()
    {
        title = "";
        description = "";
        link = "";
        georss = "";
        author = "";
        comments = "";
        pubDate = "";
    }

    public Traffic(String atitle , String adescription, String alink, String ageorss, String aauthor, String acomments, String apubDate)
    {
        title = atitle;
        description = adescription;
        link = alink;
        georss = ageorss;
        author = aauthor;
        comments = acomments;
        pubDate = apubDate;
    }

    public Traffic(String atitle , String adescription, String alink)
    {
        title = atitle;
        description = adescription;
        link = alink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }


} // End of class