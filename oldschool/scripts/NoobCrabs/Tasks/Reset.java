package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.Enums.Location;
import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.TilePath;

public class Reset extends Task<ClientContext> {
    public Reset(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        final Npc nearbyRock = ctx.npcs.select().id(NoobCrabs.Rocks).within(NoobCrabs.location.area()).nearest().poll();

        return nearbyRock != null
                && !ctx.players.local().inCombat()
                && ctx.players.local().tile().distanceTo(nearbyRock) <= 1
                && !ctx.players.local().inMotion()
                && !nearbyRock.inCombat();
    }

    @Override
    public void execute() {
        NoobCrabs.resetting = true;
        NoobCrabs.status = "Resetting.";

        TilePath reset = ctx.movement.newTilePath(NoobCrabs.location.resetPath());

        while (NoobCrabs.session.loggedIn() && !ctx.players.local().tile().equals(reset.end())) {
            reset.traverse();
            Condition.sleep();
        }

        if (NoobCrabs.location == Location.LEFT) {
            reset.reverse();
            reset.traverse();
        } else if (NoobCrabs.location == Location.RIGHT) {
            final GameObject THE_CRABS = ctx.objects.select().id(NoobCrabs.THE_CRABS_ID).nearest().poll();

            if (THE_CRABS.inViewport()) {
                THE_CRABS.interact("Enter");

                while (NoobCrabs.session.loggedIn() && !ctx.objects.select().id(NoobCrabs.THE_MINES_ID).nearest().poll().inViewport()) {
                    Condition.sleep();
                }
            }
        }
        NoobCrabs.resetting = false;
        NoobCrabs.status = "Finished resetting.";
    }
}

