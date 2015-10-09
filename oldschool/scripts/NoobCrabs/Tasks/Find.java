package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;

public class Find extends Task<ClientContext> {
    public Find(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return NoobCrabs.nearestRock == null
                || (!ctx.npcs.select().id(NoobCrabs.Rocks).within(NoobCrabs.location.area()).isEmpty()
                && (!ctx.players.local().inCombat() || NoobCrabs.nearestRock.health() < 1)
                && ctx.players.local().tile().distanceTo(ctx.npcs.nearest().poll()) > 1)
                && ctx.players.local().interacting().combatLevel() == -1;
    }

    @Override
    public void execute() {
        System.out.println(NoobCrabs.location.side());
        NoobCrabs.status = "Finding crab...";
        NoobCrabs.nearestRock = ctx.npcs.nearest().poll();

        if (ctx.movement.step(NoobCrabs.nearestRock)) {
            //wait for movement
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println(new Timestamp(new Date().getTime()) + ": Waiting for movement.");
                    return !ctx.players.local().inMotion();
                }
            });

            //wait for the crab to wake the fuck up
            final boolean attacking = Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println(new Timestamp(new Date().getTime()) + ": Waiting for crab to wake the fuck up.");
                    return NoobCrabs.nearestRock.inCombat() || NoobCrabs.nearestRock.animation() > -1;
                }
            }, 200, 10);

            //for scrubs who don't use auto-attack
            if (attacking && NoobCrabs.nearestRock.inViewport() && ctx.players.local().animation() == -1) {
                NoobCrabs.nearestRock.interact("Attack");
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
