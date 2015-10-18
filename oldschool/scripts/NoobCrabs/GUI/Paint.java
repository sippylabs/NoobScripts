package oldschool.scripts.NoobCrabs.GUI;

import oldschool.scripts.common.utilities.Startup;
import org.powerbot.script.PaintListener;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.awt.*;

public class Paint extends ClientAccessor implements PaintListener {
    private final Color color1 = new Color(141, 0, 0, 73);
    private final Color color2 = new Color(0, 0, 0);
    private final Color color3 = new Color(255, 255, 255);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Arial", 3, 20);
    private final Font font2 = new Font("Arial", 0, 9);

    private Startup start;
    private String status;
    private int atkxp = 0;
    private int strxp = 0;
    private int defxp = 0;
    private int hpxp = 0;

    public Paint(ClientContext ctx, Startup start, String status) {
        super(ctx);
        this.start = start;
        this.status = status;

        this.atkxp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        this.strxp = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        this.defxp = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        this.hpxp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
    }

    @Override
    public void repaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        g.setColor(color1);
        g.fillRoundRect(5, 5, 140, 180, 16, 16);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRoundRect(5, 5, 140, 180, 16, 16);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("NoobCrabs", 21, 26);
        g.setFont(font2);
        g.drawString("Status: " + getStatus(), 15, 48);
        g.drawString("Timer: " + getTimeRunning(), 15, 73);
        g.drawString("CB XP gained: " + getCombatXPGained(), 15, 98);
        g.drawString("CB XP/h: " + getCombatXPPerHour(), 15, 123);
        g.drawString("HP XP gained: " + getHpXPGained(), 15, 148);
        g.drawString("HP XP/h: " + getHpXPPerHour(), 15, 173);
    }

    private String getStatus() {
        return this.status;
    }

    private String getTimeRunning() {
        long timeRunning = System.currentTimeMillis() - start.startTime;

        int hoursRunning = (int) timeRunning / 3600000;
        int minutesRunning = (int) timeRunning / 60000 - hoursRunning * 60;
        int secondsRunning = (int) timeRunning / 1000 - hoursRunning * 3600 - minutesRunning * 60;

        return String.format("%02d", hoursRunning) + ":"
                + String.format("%02d", minutesRunning) + ":"
                + String.format("%02d", secondsRunning);
    }

    private int getCombatXPGained() {
        int atkxp = this.atkxp - start.atkxp;
        int strxp = this.strxp - start.strxp;
        int defxp = this.defxp - start.defxp;

        return atkxp + strxp + defxp;
    }

    private int getCombatXPPerHour() {
        int xp = getCombatXPGained();

        if (xp < 1)
            return 0;
        return (int) (xp * 3600000D / (System.currentTimeMillis() - start.startTime));
    }

    private int getHpXPGained() {
        return this.hpxp - start.hpxp;
    }

    private int getHpXPPerHour() {
        int xp = getHpXPGained();

        if (xp < 1)
            return 0;
        return (int) (xp * 3600000D / (System.currentTimeMillis() - start.startTime));
    }
}
