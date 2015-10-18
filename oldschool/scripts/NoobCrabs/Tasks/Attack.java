package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Attack extends Task<ClientContext> {
    private Npc nearestCrab;

    public Attack(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        nearestCrab = ctx.npcs.select().id(NoobCrabs.Crabs).within(NoobCrabs.location.area()).nearest().poll();

        return !NoobCrabs.resetting
                && !ctx.players.local().inMotion()
                && !ctx.players.local().interacting().valid();
    }

    @Override
    public void execute() {
        if (!ctx.npcs.select().select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return npc.interacting().name().equals(ctx.players.local().name());
            }
        }).isEmpty()) {
            final Npc nearestNpc = ctx.npcs.nearest().poll();

            nearestNpc.interact("Attack");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return nearestNpc.interacting().name().equals(ctx.players.local().name());
                }
            }, 200, 10);
        } else {
            if (nearestCrab.valid() && nearestCrab.interact("Attack")) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return nearestCrab.interacting().name().equals(ctx.players.local().name());
                    }
                }, 100, 30);
            }
        }
    }
}
