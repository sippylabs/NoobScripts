package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Reset extends Task<ClientContext> {

    public Reset(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.players.local().inCombat()
                && ctx.players.local().tile().distanceTo(NoobCrabs.nearestRock) < 1
                && !ctx.players.local().inMotion();
    }

    @Override
    public void execute() {
        final GameObject THE_MINES = ctx.objects.select().id(NoobCrabs.THE_MINES_ID).nearest().poll();
        final GameObject THE_CRABS = ctx.objects.select().id(NoobCrabs.THE_CRABS_ID).nearest().poll();

        if (ctx.movement.step(THE_MINES)) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().tile().distanceTo(THE_MINES) < 1;
                }
            }, 100, 100);

            if (THE_MINES.inViewport()) {
                THE_MINES.interact("Enter"); //into the mines!

                final boolean crabs_visible = Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return THE_CRABS.inViewport();
                    }
                }, 300, 18);

                if (crabs_visible) {
                    THE_CRABS.interact("Enter");
                }
            }
        }
    }
}
