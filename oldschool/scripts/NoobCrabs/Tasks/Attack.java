package oldschool.scripts.Noobcrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.Noobcrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Attack extends Task<ClientContext> {
    private Npc nearbyCrab;

    public Attack(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        nearbyCrab = ctx.npcs.select().id(NoobCrabs.Crabs).nearest().poll();

        if (ctx.players.local().interacting().valid()) {
            return ctx.players.local().interacting().health() < 1
                    && ctx.players.local().tile().distanceTo(nearbyCrab) < 15
                    && !nearbyCrab.inCombat()
                    && !NoobCrabs.resetting;
        }
        return !ctx.players.local().inCombat()
                && ctx.players.local().tile().distanceTo(nearbyCrab) < 15
                && !nearbyCrab.inCombat()
                && !NoobCrabs.resetting;
    }

    @Override
    public void execute() {
        NoobCrabs.status = "Attacking crab";

        if (!nearbyCrab.equals(ctx.npcs.nil()) && nearbyCrab.interact("Attack")) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return nearbyCrab.interacting().equals(ctx.players.local());
                }
            }, 50, 20);
        }
    }
}
