package oldschool.scripts.NoobCrabs.GUI;

import org.powerbot.script.PaintListener;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;

public class Paint extends ClientAccessor implements PaintListener {
    private final Color color1 = new Color(141, 0, 0, 73);
    private final Color color2 = new Color(0, 0, 0);
    private final Color color3 = new Color(255, 255, 255);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Arial", 3, 20);
    private final Font font2 = new Font("Arial", 0, 9);

    private long start = 0;
    private String status;

    public Paint(ClientContext ctx, long start, String status) {
        super(ctx);
        this.start = start;
        this.status = status;
    }

    @Override
    public void repaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        g.setColor(color1);
        g.fillRoundRect(5, 5, 140, 137, 16, 16);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRoundRect(5, 5, 140, 137, 16, 16);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("NoobCrabs", 21, 26);
        g.setFont(font2);
        g.drawString("Status: " + getStatus(), 23, 48);
        g.drawString("Timer: " + getTimeRunning(), 23, 73);
        g.drawString("XP Gained: ", 23, 98);
        g.drawString("XP/h: ", 23, 123);
    }

    private String getStatus() {
        return this.status;
    }

    private String getTimeRunning() {
        int hoursRunning = (int) (System.currentTimeMillis() - start) / 3600000;
        int minutesRunning = (int) (System.currentTimeMillis() - start) / 60000 - hoursRunning * 60;
        int secondsRunning = (int) (System.currentTimeMillis() - start) / 1000 - hoursRunning * 3600 - minutesRunning * 60;

        return String.format("%02d", hoursRunning) + ":"
                + String.format("%02d", minutesRunning) + ":"
                + String.format("%02d", secondsRunning);
    }
}
