package oldschool.scripts.NoobCrabs;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.Enums.Location;
import oldschool.scripts.NoobCrabs.GUI.Paint;
import oldschool.scripts.NoobCrabs.GUI.StartupInterface;
import oldschool.scripts.NoobCrabs.Tasks.Attack;
import oldschool.scripts.NoobCrabs.Tasks.Eat;
import oldschool.scripts.NoobCrabs.Tasks.Find;
import oldschool.scripts.NoobCrabs.Tasks.Reset;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Script.Manifest(
        name = "OS NoobCrabs",
        description = "new quote",
        properties = "client = 4"
)
public class NoobCrabs extends PollingScript<ClientContext> implements PaintListener {
    public ArrayList<Task> tasks = new ArrayList<Task>();

    public static long start = 0;

    public static boolean initialising = true;
    public static boolean resetting = false;
    public static final int[] Rocks = {101, 103};
    public static final int[] Crabs = {100, 102};
    public static final int resetCaveId = 5008;
    public static final int resetCaveExitId = 5014;
    public static Game session;

    public static String status = "Initialising...";
    public static double eatAtPercentage = 0.5;
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

        tasks.addAll(Arrays.asList(new Find(ctx), new Reset(ctx), new Attack(ctx), new Eat(ctx)));

        StartupInterface dialog = new StartupInterface(ctx);
        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    public void poll() {
        if (!initialising)
            for (Task task : tasks)
                if (task.activate())
                    task.execute();
    }

    @Override
    public void stop() {
        screenShot();
    }

    @Override
    public void repaint(Graphics g) {
        Paint paint = new Paint(ctx, start, status);
        paint.repaint(g);
    }

    private void screenShot() {
        final int width = ctx.game.dimensions().width, height = ctx.game.dimensions().height;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // your paint's repaint(Graphics) method
        repaint(img.createGraphics());
        img = img.getSubimage(5, 5, 140, 180);
        final File screenshot = new File(getStorageDirectory(), ctx.controller.script().getName() + "_"
                + String.valueOf(new SimpleDateFormat("ddMMyy-HHmmssSSS").format(new Date())).concat(".png"));
        try {
            ImageIO.write(img, "png", screenshot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}