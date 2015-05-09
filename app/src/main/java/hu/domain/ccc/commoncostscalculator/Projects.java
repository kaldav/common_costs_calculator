package hu.domain.ccc.commoncostscalculator;

import java.util.Date;

/**
 * Created by Rolu on 2015.03.25..
 */
public class Projects {
    private String name;
    private Date startTime;
    private String description;
    private int id;
    private int creatorId;
    private boolean closed;

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {

        this.closed = closed;
    }

    public Projects(String name, Date startTime, String description, int id, boolean closed,int creatorId) {
        this.name = name;
        this.creatorId=creatorId;
        this.startTime = startTime;
        this.description = description;
        this.id = id;
        this.closed=closed;
    }
    //van ahol ennyi elég, sporoltam kicsit
    public Projects(String name, String description, int id) {
        this.name = name;
        //this.startTime = startTime;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
