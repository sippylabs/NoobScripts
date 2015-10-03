package oldschool.scripts.NoobCrabs;

import oldschool.scripts.Common.Utilities.*;
import oldschool.scripts.NoobCrabs.Tasks.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@Script.Manifest(
        name = "OS Noob Crabs",
        description = "quote",
        properties = "client = 4"
)
public class NoobCrabs extends PollingScript<ClientContext> implements PaintListener {
    public ArrayList<Task> tasks = new ArrayList<Task>();
    public static Npc nearestRock;

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