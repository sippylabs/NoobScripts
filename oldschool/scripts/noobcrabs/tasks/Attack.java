package oldschool.scripts.noobcrabs.tasks;

import oldschool.scripts.common.utilities.Task;
import oldschool.scripts.noobcrabs.NoobCrabs;
import oldschool.scripts.noobcrabs.enums.Target;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class Attack extends Task<ClientContext> {
    private Npc nearestCrab;
    private boolean killSteal;

    public Attack(ClientContext ctx, boolean killSteal) {
        super(ctx);
        this.killSteal = killSteal;
    }

    @Override
    public boolean activate() {
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

            boolean dismissed = Arrays.asList(nearestNpc.actions()).contains("Dismiss")
                    ? nearestNpc.interact("Dismiss") : nearestNpc.interact("Attack");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return nearestNpc.interacting().name().equals(ctx.players.local().name());
                }
            }, 200, 10);
        } else {
            nearestCrab = ctx.npcs.select().id(Target.CRAB.ids()).within(NoobCrabs.location.area()).select(new Filter<Npc>() {
                @Override
                public boolean accept(Npc npc) {
                    //if npc not in combat OR npc not interacting OR npcs target not interacting with npc
                    return killSteal || !npc.inCombat() || !npc.interacting().valid() || !npc.interacting().interacting().tile().equals(npc.tile());
                }
            }).nearest().poll();

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