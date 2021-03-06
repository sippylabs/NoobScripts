package oldschool.scripts.noobcrabs.enums;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

public enum Location {
    LEFT(
            new Area(new Tile(2662, 3734, 0), new Tile(2688, 3712, 0)),
            new Tile[]{new Tile(2678, 3720), new Tile(2674, 3712), new Tile(2674, 3701), new Tile(2674, 3692), new Tile(2673, 3684)}
    ),
    LEFTPURE(
            new Area(new Tile(2662, 3721, 0), new Tile(2688, 3712, 0)),
            new Tile[]{new Tile(2674, 3712), new Tile(2674, 3701), new Tile(2674, 3692), new Tile(2673, 3684)}
    ),
    RIGHT(
            new Area(new Tile(2692, 3730, 0), new Tile(2720, 3713, 0)),
            new Tile[]{new Tile(2704, 3720), new Tile(2710, 3718), new Tile(2716, 3717), new Tile(2722, 3714), new Tile(2727, 3714), new Tile(2730, 3713)}
    );

    private final Area area;
    private final Tile[] resetPath;

    Location(final Area area, final Tile[] resetPath) {
        this.area = area;
        this.resetPath = resetPath;
    }

    public Area area() {
        return this.area;
    }

    public Tile[] resetPath() {
        return this.resetPath;
    }
}
