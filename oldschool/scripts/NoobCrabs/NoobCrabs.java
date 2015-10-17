package oldschool.scripts.NoobCrabs;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.Enums.Location;
import oldschool.scripts.NoobCrabs.GUI.Paint;
import oldschool.scripts.NoobCrabs.Tasks.Find;
import oldschool.scripts.NoobCrabs.Tasks.Reset;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@Script.Manifest(
        name = "OS NoobCrabs",
        description = "new quote",
        properties = "client = 4"
)
public class NoobCrabs extends PollingScript<ClientContext> implements PaintListener {
    public ArrayList<Task> tasks = new ArrayList<Task>();

    public long start = 0;

    public static boolean resetting = false;
    public static final int[] Rocks = {101, 103};
    public static final int[] Crabs = {100, 102};
    public static final int resetCaveId = 5008;
    public static final int resetCaveExitId = 5014;
    public static Game session;

    public static String status = "Initialising...";
    public static int atkxp = 0;
    public static int strxp = 0;
    public static int defxp = 0;
    public static int hpxp = 0;
    public static Location location;

    @Override
    public void start() {
        session = new Game(ctx);
        atkxp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        strxp = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        defxp = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        hpxp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);

        if (Location.LEFT.area().contains(ctx.players.local()))
            location = Location.LEFT;
        else if (Location.RIGHT.area().contains(ctx.players.local()))
            location = Location.RIGHT;
        else {
            Tile player = ctx.players.local().tile();
            location = (player.distanceTo(Location.LEFT.area().getCentralTile())
                    > player.distanceTo(Location.RIGHT.area().getCentralTile()))
                    ? Location.RIGHT : Location.LEFT;
        }

        start = System.currentTimeMillis();
        tasks.addAll(Arrays.asList(new Find(ctx), new Reset(ctx)));
    }

    @Override
    public void poll() {
        for (Task task : tasks)
            if (task.activate())
                task.execute();
    }

    @Override
    public void stop() {

    }

    @Override
    public void repaint(Graphics g) {
        Paint paint = new Paint(ctx, start, status);
        paint.repaint(g);
    }
}