package oldschool.scripts.noobcrabs.tasks;

import oldschool.scripts.common.Task;
import oldschool.scripts.noobcrabs.NoobCrabs;
import oldschool.scripts.noobcrabs.enums.Location;
import oldschool.scripts.noobcrabs.enums.Target;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.*;

import java.nio.BufferUnderflowException;
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
        final Npc nearbyRock = ctx.npcs.select().id(Target.ROCK.ids()).within(NoobCrabs.location.area()).nearest().peek();

        return NoobCrabs.resetting
                || (!NoobCrabs.hopping
                && !ctx.players.local().inMotion()
                && !ctx.players.local().inCombat()
                && nearbyRock.valid()
                && ctx.players.local().tile().distanceTo(nearbyRock) <= 1
                && (!nearbyRock.inCombat() || (!nearbyRock.interacting().valid() && nearbyRock.animation() == -1)));
    }

    @Override
    public void execute() {
        boolean ignore = Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat() || ctx.npcs.select().id(Target.ROCK.ids()).nearest().poll().inCombat();
            }
        }, 200, 10);

        if (!ignore) {
            NoobCrabs.resetting = true;
            NoobCrabs.status = "Resetting.";

            TilePath reset = ctx.movement.newTilePath(NoobCrabs.location.resetPath());

            if (!NoobCrabs.location.equals(Location.RIGHT)) {
                if (!walkBack) {
                    if (ctx.players.local().tile().equals(reset.end())) {
                        walkBack = true;
                    } else reset.traverse();
                } else {
                    reset = reset.reverse();

                    if (NoobCrabs.location.area().contains(ctx.players.local())) {
                        NoobCrabs.resetting = false;
                        walkBack = false;
                    } else reset.traverse();
                }
            } else {
                GameObject caveEntrancePoll = ctx.objects.nil();
                GameObject caveExitPoll = ctx.objects.nil();

                try {
                    caveEntrancePoll = ctx.objects.select(20).id(resetCaveId)
                            .each(Interactive.doSetBounds(new int[]{-74, 90, 26, 165, -182, 147})).poll();
                    caveExitPoll = ctx.objects.select(5).id(resetCaveExitId).poll();
                } catch (BufferUnderflowException e) {
                    log(e.getMessage());
                }

                final GameObject caveEntrance = caveEntrancePoll;
                final GameObject caveExit = caveExitPoll;

                if (!walkBack && caveEntrance.valid() && caveEntrance.inViewport()) {
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
}

