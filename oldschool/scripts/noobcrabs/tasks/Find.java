package oldschool.scripts.noobcrabs.tasks;

import oldschool.scripts.common.utilities.Task;
import oldschool.scripts.noobcrabs.NoobCrabs;
import oldschool.scripts.noobcrabs.enums.Target;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Find extends Task<ClientContext> {
    public Find(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        final Npc nearestCrab = ctx.npcs.select().id(Target.CRAB.ids()).within(NoobCrabs.location.area()).nearest().poll();

        return (ctx.game.loggedIn() && !NoobCrabs.resetting)
                && ((ctx.players.local().interacting().valid() && ctx.players.local().interacting().health() < 1)
                || !ctx.players.local().interacting().valid())
                && !ctx.npcs.select().id(Target.ROCK.ids()).within(NoobCrabs.location.area()).isEmpty()
                && (ctx.npcs.select().select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return npc.interacting().name().equals(ctx.players.local().name());
            }
        }).isEmpty())
                && (nearestCrab.inCombat() || ctx.players.local().tile().distanceTo(nearestCrab) > 4);
    }

    @Override
    public void execute() {
        NoobCrabs.status = "Finding crab...";

        final Npc nearestRock = ctx.npcs.select().select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.tile().equals(ctx.players.local().interacting().tile());
            }
        }).id(Target.ROCK.ids()).within(NoobCrabs.location.area()).nearest().poll();

        if (!ctx.movement.running() && ctx.movement.energyLevel() > 50)
            ctx.movement.running(true);

        if (ctx.players.local().tile().distanceTo(nearestRock) > 1) {
            boolean rockInView = nearestRock.tile().matrix(ctx).inViewport()
                    ? nearestRock.tile().matrix(ctx).interact("Walk here") : ctx.movement.step(nearestRock);

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().tile().distanceTo(ctx.movement.destination()) < 4;
                }
            });
        }

        switch (Random.nextInt(0, 6)) {
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
