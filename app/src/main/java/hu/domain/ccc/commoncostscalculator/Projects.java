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

    public Projects(String name, Date startTime, String description, int id) {
        this.name = name;
        this.startTime = startTime;
        this.description = description;
        this.id = id;
    }

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
