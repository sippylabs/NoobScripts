package oldschool.scripts.noobcrabs.tasks;

import oldschool.scripts.common.Task;
import oldschool.scripts.noobcrabs.NoobCrabs;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Widget;

import java.util.ArrayList;

public class Hop extends Task<ClientContext> {
    private boolean activate = true;
    final ArrayList<World> worlds = new ArrayList<World>();

    final private Widget worldHop = ctx.widgets.widget(69);
    final private Component btnWorldSwitch = ctx.widgets.widget(182).component(5);
    final Component[] worldList = worldHop.component(7).components();


    public Hop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return activate;
    }

    @Override
    public void execute() {
        NoobCrabs.hopping = true;

        if (ctx.game.tab(Game.Tab.LOGOUT)) {
            if (worldHop.component(1).visible()) {
                final String currentWorldText = worldHop.component(2).text();
                final int currentWorld = Integer.parseInt(currentWorldText.substring(
                        Math.max(currentWorldText.length() - 2, 0)));

                //parse world list from components
                for (int i = 0; i < worldList.length; i += 6) {
                    worlds.add(new World(
                            worldList[i],
                            worldList[i + 1].textureId() == 1131,
                            Integer.parseInt(worldList[i + 2].text()),
                            worldList[i + 3].textureId(),
                            Integer.parseInt(worldList[i + 4].text()),
                            worldList[i + 5].text(),
                            worldList[i].textColor() == 0
                    ));
                }

                if (!sorted()) {
                    worldHop.component(12).click();
                }

            } else {
                btnWorldSwitch.click(true);
            }
        }
    }

    public boolean sorted() {
        for (int i = 1; i < worlds.size(); i++)
            if (worlds.get(i).population < worlds.get(i - 1).population)
                return false;
        return true;
    }

    public World nextValidHop() {
        return null;
    }

    public class World {
        public Component component;
        public boolean membersOnly;
        public int worldNumber;
        public int countryId;
        public int population;
        public String comment;
        public boolean enabled;

        public World() {

        }

        public World(Component world, boolean membersOnly, int worldNumber,
                     int countryId, int population, String comment, boolean enabled) {
            this.component = world;
            this.membersOnly = membersOnly;
            this.worldNumber = worldNumber;
            this.countryId = countryId;
            this.population = population;
            this.comment = comment;
            this.enabled = enabled;
        }

        public String print() {
            return "World " + this.worldNumber + " / "
                    + "Members: " + this.membersOnly + " / "
                    + "Country ID: " + this.countryId + " / "
                    + "Population: " + this.population + " / "
                    + "Comment: " + this.comment;
        }
    }
}