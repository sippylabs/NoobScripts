package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

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
        final GameObject THE_MINES = ctx.objects.select().id(NoobCrabs.THE_MINES_ID).nearest().poll();

        if (ctx.movement.step(THE_MINES)) {
            while (ctx.players.local().tile().distanceTo(THE_MINES) > 10) {
                if (ctx.players.local().tile().distanceTo(ctx.movement.destination()) > 15) {
                    Condition.sleep();
                } else {
                    Condition.sleep(3000);
                }

                ctx.movement.step(THE_MINES);
            }

            while (THE_MINES.inViewport()) {
                THE_MINES.interact("Enter"); //into the mines!
                Condition.sleep(2000);
            }

            final GameObject THE_CRABS = ctx.objects.select().id(NoobCrabs.THE_CRABS_ID).nearest().poll();

            if (THE_CRABS.inViewport()) {
                THE_CRABS.interact("Enter");

                while (!ctx.objects.select().id(NoobCrabs.THE_MINES_ID).nearest().poll().inViewport()) {
                    Condition.sleep();
                }

                NoobCrabs.resetting = false;
                NoobCrabs.status = "Finished resetting.";
            }
        }
    }
}
