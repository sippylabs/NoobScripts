package oldschool.scripts.noobcrabs.tasks;

import oldschool.scripts.common.utilities.Task;
import oldschool.scripts.noobcrabs.NoobCrabs;
import oldschool.scripts.noobcrabs.enums.Location;
import oldschool.scripts.noobcrabs.enums.Target;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.TilePath;

import java.util.concurrent.Callable;

public class Reset extends Task<ClientContext> {
    private boolean walkBack = false;
    private final int resetCaveId = 5008;
    private final int resetCaveExitId = 5014;

    public Reset(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        final Npc nearbyRock = ctx.npcs.select().id(Target.ROCK.ids()).within(NoobCrabs.location.area()).nearest().poll();

        return NoobCrabs.resetting
                || (nearbyRock.valid()
                && !ctx.players.local().inCombat()
                && ctx.players.local().tile().distanceTo(nearbyRock) <= 1
                && !ctx.players.local().inMotion()
                && !nearbyRock.inCombat());
    }

    @Override
    public void execute() {
        NoobCrabs.resetting = true;
        NoobCrabs.status = "Resetting.";

        TilePath reset = ctx.movement.newTilePath(NoobCrabs.location.resetPath());

        if (NoobCrabs.location == Location.LEFT) {
            if (!walkBack) {
                if (ctx.players.local().tile().equals(reset.end())) {
                    walkBack = true;
                } else reset.traverse();
            } else {
                reset = reset.reverse();

                if (ctx.players.local().tile().equals(reset.end())) {
                    NoobCrabs.resetting = false;
                    walkBack = false;
                } else reset.traverse();
            }
        } else if (NoobCrabs.location == Location.RIGHT) {
            final GameObject caveEntrance = ctx.objects.select().id(resetCaveId).poll();
            final GameObject caveExit = ctx.objects.select().id(resetCaveExitId).poll();

            if (!walkBack && caveEntrance.valid()) {
                if (ctx.players.local().tile().equals(reset.end())) {
                    boolean inside = false;

                    if (caveEntrance.inViewport()) {
                        caveEntrance.click(true);

                        inside = Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return !caveEntrance.inViewport();
                            }
                        }, 100, 20);
                    } else {
                        ctx.movement.step(caveEntrance);
                    }

                    if (inside) {
                        walkBack = true;
                    }
                } else reset.traverse();
            } else if (caveExit.valid()) {
                reset = reset.reverse();
                boolean outside = false;

                if (caveExit.inViewport()) {
                    caveExit.click(true);

                    outside = Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !caveExit.inViewport();
                        }
                    }, 100, 20);
                } else if (caveEntrance.valid() && caveEntrance.inViewport()) {
                    ctx.objects.id(resetCaveId).poll().click(true);
                } else {
                    ctx.movement.findPath(caveExit).traverse();
                }

                if (outside) {
                    reset.traverse();
                    NoobCrabs.resetting = false;
                    walkBack = false;
                }
            } else {
                reset.traverse();
            }
        }
    }
}

