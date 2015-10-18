package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import oldschool.scripts.NoobCrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.Arrays;

public class Eat extends Task<ClientContext> {
    public Eat(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return NoobCrabs.session.loggedIn()
                && !ctx.inventory.select().select(new Filter<Item>() {
            @Override
            public boolean accept(Item gameObject) {
                return Arrays.asList(gameObject.actions()).contains("Eat");
            }
        }).isEmpty()
                && ctx.players.local().health() < ctx.players.local().maxHealth() * 0.5;
    }

    @Override
    public void execute() {
        NoobCrabs.status = "Nomming ...";

        Item foodz = ctx.inventory.select(new Filter<Item>() {
            @Override
            public boolean accept(Item gameObject) {
                return Arrays.asList(gameObject.actions()).contains("Eat");
            }
        }).first().poll();

        if (foodz.valid())
            foodz.interact("Eat");

        Condition.sleep();
    }
}
