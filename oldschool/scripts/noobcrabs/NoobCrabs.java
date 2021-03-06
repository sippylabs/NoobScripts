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
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Script.Manifest(
        name = "[OS] NoobCrabs v1.24",
        description = "Kills rock crabs. Start with food in inventory. If not eating, just click Start. ",
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
        try {
            if (System.currentTimeMillis() - start.startTime > 3600000)
                screenShot();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void repaint(Graphics g) {
        if (!initialising) {
            Paint paint = new Paint(ctx, start, status);
            paint.repaint(g);
        }
    }

    private void screenShot() throws IOException {
        final int width = ctx.game.dimensions().width, height = ctx.game.dimensions().height;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final String albumId = "75nXaJFy979q9z1";
        final String imgurKey = "89fa33eb76e11fe";
        final String uploadUrl = "https://api.imgur.com/3/image";
        final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        OutputStream out = null;

        repaint(img.createGraphics());
        img = img.getSubimage(5, 5, 140, 180);
        final File screenshot = new File(getStorageDirectory(), ctx.controller.script().getName() + "_"
                + String.valueOf(new SimpleDateFormat("ddMMyy-HHmmssSSS").format(new Date())).concat(".png"));
        try {
            ImageIO.write(img, "png", byteArray);

            out = new FileOutputStream(screenshot);
            byteArray.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
        }

        try {
            System.setProperty("https.protocols", "TLSv1");

            final String imageData = DatatypeConverter.printBase64Binary(byteArray.toByteArray());
            final String image = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imageData, "UTF-8");
            final String album = URLEncoder.encode("album", "UTF-8") + "=" + URLEncoder.encode(albumId, "UTF-8");
            final String type = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("base64", "UTF-8");
            final String data = image + "&" + album + "&" + type;

            HttpsURLConnection conn = (HttpsURLConnection) new URL(uploadUrl).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + imgurKey);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            StringBuilder stb = new StringBuilder();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                stb.append(line).append("\n");
            }
            wr.close();
            rd.close();

            System.out.println(stb.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}