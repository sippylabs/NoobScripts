package oldschool.scripts.NoobCrabs.Enums;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

public enum Location {
    LEFT("Left", new Area(new Tile(2662, 3734, 0), new Tile(2688, 3712, 0))),
    RIGHT("Right", new Area(new Tile(2692, 3730, 0), new Tile(2720, 3713, 0)));

    private final String side;
    private final Area area;

    Location(final String side, final Area area) {
        this.side = side;
        this.area = area;
    }

    public String side() {
        return this.side;
    }

    public Area area() {
        return this.area;
    }
}
