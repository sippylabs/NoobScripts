package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;

import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Find extends Task<ClientContext> {
    private final int[] Rocks = {101, 103};
    private final int[] Crabs = {100, 102};

    public Find(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if (NoobCrabs.nearestRock == null)
            return true;
        return (!ctx.npcs.select().id(Crabs).isEmpty()
                || !ctx.npcs.select().id(Rocks).isEmpty())
                && (!ctx.players.local().inCombat()
                || NoobCrabs.nearestRock.health() < 1)
                && ctx.players.local().tile().distanceTo(ctx.npcs.nearest().poll()) > 1;
    }

    @Override
    public void execute() {
        NoobCrabs.nearestRock = ctx.npcs.select().id(Rocks).nearest().poll();
        if (ctx.movement.step(NoobCrabs.nearestRock)) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inMotion()
                            && (ctx.movement.destination().distanceTo(ctx.players.local()) < 1 || ctx.players.local().tile().distanceTo(NoobCrabs.nearestRock) < 1);
                }
            }, 1000, 10);
        }
    }
}
