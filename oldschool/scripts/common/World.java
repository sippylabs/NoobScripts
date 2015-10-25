package oldschool.scripts.common;

import org.powerbot.script.rt4.Component;

public class World {
    public Component component;
    public boolean membersOnly;
    public int worldNumber;
    public int countryId;
    public int population;
    public String comment;
    public boolean enabled;

    public World() {

    }

    public World(Component world, boolean membersOnly, int worldNumber,
                 int countryId, int population, String comment, boolean enabled) {
        this.component = world;
        this.membersOnly = membersOnly;
        this.worldNumber = worldNumber;
        this.countryId = countryId;
        this.population = population;
        this.comment = comment;
        this.enabled = enabled;
    }

    public String print() {
        return "World " + this.worldNumber + " / "
                + "Members: " + this.membersOnly + " / "
                + "Country ID: " + this.countryId + " / "
                + "Population: " + this.population + " / "
                + "Comment: " + this.comment;
    }
}