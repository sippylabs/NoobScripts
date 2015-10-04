package oldschool.scripts.NoobCrabs;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.Tasks.Find;
import oldschool.scripts.NoobCrabs.Tasks.Reset;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

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

    public static Npc nearestRock;
    public static final int[] Rocks = {101, 103};
    public static final int[] Crabs = {100, 102};
    public static final int THE_MINES_ID = 5008;
    public static final int THE_CRABS_ID = 5014;

    @Override
    public void start() {
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

    }
}