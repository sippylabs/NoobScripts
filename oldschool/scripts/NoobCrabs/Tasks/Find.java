package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.Random;
import java.util.concurrent.Callable;

public class Find extends Task<ClientContext> {
    public Find(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if (!NoobCrabs.session.loggedIn())
            return false;
        if (ctx.players.local().interacting().valid())
            return (!ctx.players.local().inCombat()
                    || ctx.players.local().interacting().health() < 1)
                    && !ctx.npcs.select().id(NoobCrabs.Rocks).within(NoobCrabs.location.area()).isEmpty()
                    && !NoobCrabs.resetting;
        return !ctx.players.local().inCombat()
                && !ctx.npcs.select().id(NoobCrabs.Rocks).within(NoobCrabs.location.area()).isEmpty()
                && !NoobCrabs.resetting;
    }

    @Override
    public void execute() {
        NoobCrabs.status = "Finding crab...";
        final Npc nearestRock = ctx.npcs.nearest().poll();

        if (ctx.players.local().tile().distanceTo(nearestRock) > 1) {
            ctx.movement.step(nearestRock);

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < 10;
                }
            });
        } else if (ctx.players.local().tile().distanceTo(nearestRock) <= 1) {
            //wait for the crab to wake the fuck up
            final boolean attacking = Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().inCombat() || nearestRock.inCombat();
                }
            }, 200, 2);

            //for scrubs who don't use auto-attack
            if (ctx.players.local().animation() < 1 && nearestRock.interacting().equals(ctx.players.local())) {
                nearestRock.interact("Attack");
            }

            switch (new Random().nextInt(6)) {
                case 0:
                    NoobCrabs.status = "Calling crab a punk.";
                    break;
                case 1:
                    NoobCrabs.status = "Ruining crab's day.";
                    break;
                case 2:
                    NoobCrabs.status = "Insulting crab's mom.";
                    break;
                case 3:
                    NoobCrabs.status = "Fucking up crab.";
                    break;
                case 4:
                    NoobCrabs.status = "Mugging crab.";
                    break;
                case 5:
                    NoobCrabs.status = "Breaking crab's legs.";
                    break;
            }
        }
    }
}
