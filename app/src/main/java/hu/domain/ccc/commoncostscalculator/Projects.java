package hu.domain.ccc.commoncostscalculator;

import java.util.Date;

/**
 * Created by Rolu on 2015.03.25..
 */
public class Projects {
    private String name;
    private Date startDate;
    private String description;



    private int id;

    public Projects(String name, Date startDate, String description, int id) {
        this.name = name;
        this.startDate = startDate;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
