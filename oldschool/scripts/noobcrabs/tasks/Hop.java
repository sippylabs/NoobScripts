package oldschool.scripts.noobcrabs.tasks;

import oldschool.scripts.common.utilities.Task;
import oldschool.scripts.noobcrabs.NoobCrabs;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Widget;

public class Hop extends Task<ClientContext> {
    private boolean activate = true;

    private Widget worldHop;
    private Component btnWorldSwitch;

    public Hop(ClientContext ctx) {
        super(ctx);

        worldHop = ctx.widgets.widget(69);
        btnWorldSwitch = ctx.widgets.widget(182).component(5);
    }

    @Override
    public boolean activate() {
        return activate;
    }

    @Override
    public void execute() {
        activate = false;
        NoobCrabs.hopping = true;

        if (ctx.game.tab(Game.Tab.LOGOUT)) {
            if (worldHop.component(1).visible()) {

            } else {
                btnWorldSwitch.click(true);
            }
        }
    }
}