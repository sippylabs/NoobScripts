package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Find extends Task<ClientContext> {
    public Find(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return NoobCrabs.nearestRock == null
                || (!ctx.npcs.select().id(NoobCrabs.Rocks).isEmpty()
                && (!ctx.players.local().inCombat() || NoobCrabs.nearestRock.health() < 1)
                && ctx.players.local().tile().distanceTo(ctx.npcs.nearest().poll()) > 1)
                && ctx.players.local().interacting().combatLevel() == -1;
    }

    @Override
    public void execute() {
        NoobCrabs.nearestRock = ctx.npcs.nearest().poll();

        if (ctx.movement.step(NoobCrabs.nearestRock)) {
            //wait for movement
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inMotion()
                            && ctx.players.local().tile().distanceTo(NoobCrabs.nearestRock) < 2;
                }
            }, 500, 12);

            //wait for the crab to wake the fuck up
            final boolean attacking = Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().interacting().inCombat();
                }
            }, 200, 10);

            //for scrubs who don't use auto-attack
            if (attacking && NoobCrabs.nearestRock.inViewport()) {
                NoobCrabs.nearestRock.interact("Attack");
            }
        }
    }
}
