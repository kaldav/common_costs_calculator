package hu.domain.ccc.commoncostscalculator;

import java.util.Date;

/**
 * Created by Rolu on 2015.03.25..
 */
public class Projects {
    private String name;

    public Projects(String name, Date startDate, String description) {
        this.name = name;
        this.startDate = startDate;
        this.description = description;
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

    private Date startDate;
    private String description;
}
