package oldschool.scripts.noobcrabs.tasks;

import oldschool.scripts.common.Task;
import oldschool.scripts.common.World;
import oldschool.scripts.common.utils.CollectionUtils;
import oldschool.scripts.noobcrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Widget;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Hop extends Task<ClientContext> {
    private boolean enabled;

    final private ArrayList<World> worlds = new ArrayList<World>();
    final private ArrayList<Integer> worldsTried = new ArrayList<Integer>();
    final private Widget worldHop = ctx.widgets.widget(69);
    final private Component btnWorldSwitch = ctx.widgets.widget(182).component(5);
    final private Component switchHighRisk = ctx.widgets.widget(219).component(0).component(2);

    private int currentWorldId = 0;
    private Component[] worldList;
    private int hopLimit;
    private int maxPlayers;

    public Hop(final ClientContext ctx, final boolean enabled, final int hopLimit, final int maxPlayers) {
        super(ctx);

        this.enabled = enabled;
        this.hopLimit = hopLimit;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public boolean activate() {
        return (ctx.game.loggedIn() && enabled && !NoobCrabs.resetting)
                && (!ctx.players.local().interacting().valid() || !ctx.players.local().inCombat())
                && (ctx.players.select().within(NoobCrabs.location.area()).size() >= maxPlayers
                || !ctx.objects.select(50).id(6).isEmpty());
    }

    @Override
    public void execute() {
        NoobCrabs.hopping = true;
        NoobCrabs.status = "Hopping worlds...";

        if (ctx.game.tab(Game.Tab.LOGOUT)) {
            if (worldHop.component(7).visible()) {
                final String currentWorldText = ctx.widgets.widget(429).component(1).text();
                currentWorldId = Integer.parseInt(currentWorldText.substring(
                        Math.max(currentWorldText.length() - 2, 0)));

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !worldHop.component(1).text().equals("Loading...");
                    }
                }, 250, 20);

                worldList = worldHop.component(7).components();

                if (worldList.length > 0) {
                    parseWorlds();

                    if (!sorted()) {
                        if (worldHop.component(12).click()) {
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    parseWorlds();
                                    return sorted();
                                }
                            }, 500, 10);
                        }
                    }

                    World next = nextValidHop();

                    ctx.input.move(worldHop.component(7).centerPoint());
                    if (worldHop.component(7).boundingRect().contains(next.component.boundingRect())) {
                        if (!worldsTried.contains(currentWorldId)) {
                            worldsTried.add(currentWorldId);
                        }

                        next.component.click();
                        if (next.comment.contains("High Risk")) {
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return switchHighRisk.visible();
                                }
                            }, 500, 6);

                            if (switchHighRisk.visible()) {
                                switchHighRisk.click();
                            }
                            Condition.sleep();
                            NoobCrabs.hopping = false;
                        } else if (next.component.boundingRect().y > worldHop.component(7).boundingRect().getCenterY()) {
                            ctx.input.scroll(true);
                        } else {
                            ctx.input.scroll(false);
                        }
                    }
                }
            } else {
                btnWorldSwitch.click(true);
            }
            Condition.sleep(2000);
        }
    }

    public void parseWorlds() {
        try {
            worlds.clear();
            for (int i = 0; i < worldList.length; i += 6) {
                worlds.add(new World(
                        worldList[i],
                        worldList[i + 1].textureId() == 1131,
                        Integer.parseInt(worldList[i + 2].text()),
                        worldList[i + 3].textureId(),
                        Integer.parseInt(worldList[i + 4].text()),
                        worldList[i + 5].text(),
                        worldList[i].textColor() != 0
                ));
            }

            worlds.removeAll(CollectionUtils.where(worlds, new Filter<World>() {
                @Override
                public boolean accept(World world) {
                    return !world.enabled || world.comment.contains("PVP") || !world.membersOnly;
                }
            }));
        } catch (ArrayIndexOutOfBoundsException e) {
            Condition.sleep();
        }
    }

    public boolean sorted() {
        for (World world : worlds) {
            if (worlds.indexOf(world) > 0 && worlds.get(worlds.indexOf(world) - 1).population > world.population) {
                return false;
            }
        }
        return true;
    }

    public World nextValidHop() {
        if (worldsTried.size() > hopLimit) {
            worldsTried.clear();
        }

        worlds.removeAll(CollectionUtils.where(worlds, new Filter<World>() {
            @Override
            public boolean accept(World world) {
                return worldsTried.contains(world.worldNumber);
            }
        }));

        return worlds.get(0);
    }
}