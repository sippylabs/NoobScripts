package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.NoobCrabs.NoobCrabs;
import oldschool.scripts.common.utilities.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.Arrays;

public class Eat extends Task<ClientContext> {
    private double eatAtPercentage;

    public Eat(ClientContext ctx, double eatAtPercentage) {
        super(ctx);
        this.eatAtPercentage = eatAtPercentage;
    }

    @Override
    public boolean activate() {
        return ctx.game.loggedIn()
                && !ctx.inventory.select().select(new Filter<Item>() {
            @Override
            public boolean accept(Item gameObject) {
                return Arrays.asList(gameObject.actions()).contains("Eat");
            }
        }).isEmpty()
                && ctx.players.local().health() < ctx.players.local().maxHealth() * eatAtPercentage;
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
