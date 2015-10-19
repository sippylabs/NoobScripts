package oldschool.scripts.noobcrabs;

import oldschool.scripts.common.utilities.Startup;
import oldschool.scripts.common.utilities.Task;
import oldschool.scripts.noobcrabs.enums.Location;
import oldschool.scripts.noobcrabs.gui.Paint;
import oldschool.scripts.noobcrabs.gui.StartupInterface;
import oldschool.scripts.noobcrabs.tasks.Attack;
import oldschool.scripts.noobcrabs.tasks.Eat;
import oldschool.scripts.noobcrabs.tasks.Find;
import oldschool.scripts.noobcrabs.tasks.Reset;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Script.Manifest(
        name = "[OS] NoobCrabs v1.0",
        description = "Start with food in inventory. If not eating, just click Start.",
        properties = "client = 4;topic=1287402"
)
public class NoobCrabs extends PollingScript<ClientContext> implements PaintListener {
    public ArrayList<Task> tasks = new ArrayList<Task>();
    public Startup start;

    public static boolean initialising = true;
    public static boolean resetting = false;
    public static String status = "Initialising...";
    public static Location location;

    @Override
    public void start() {
        start = new Startup();
        start.atkxp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        start.strxp = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        start.defxp = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        start.hpxp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);

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

        tasks.addAll(Arrays.asList(new Find(ctx), new Reset(ctx), new Attack(ctx), new Eat(ctx, start.eatAtPercentage)));

        StartupInterface dialog = new StartupInterface(ctx, start);
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