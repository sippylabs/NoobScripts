package oldschool.scripts.noobcrabs;

import oldschool.scripts.common.Task;
import oldschool.scripts.noobcrabs.enums.Location;
import oldschool.scripts.noobcrabs.gui.Paint;
import oldschool.scripts.noobcrabs.gui.StartupInterface;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Script.Manifest(
        name = "[OS] NoobCrabs v1.2",
        description = "Kills rock crabs. Start with food in inventory. If not eating, just click Start.",
        properties = "client = 4;topic=1287402"
)
public class NoobCrabs extends PollingScript<ClientContext> implements PaintListener {
    public ArrayList<Task> tasks = new ArrayList<Task>();
    public Startup start = new Startup();

    public static boolean initialising = true;
    public static boolean resetting = false;
    public static boolean hopping = false;
    public static String status = "Initialising...";
    public static Location location;

    @Override
    public void start() {
        StartupInterface dialog = new StartupInterface(ctx, start, tasks);
        dialog.pack();
        dialog.setMinimumSize(dialog.getSize());
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