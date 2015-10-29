package oldschool.scripts.noobcrabs.tasks;

import oldschool.scripts.common.Task;
import oldschool.scripts.noobcrabs.NoobCrabs;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;
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
                && ctx.skills.level(Constants.SKILLS_HITPOINTS) < ctx.skills.realLevel(Constants.SKILLS_HITPOINTS) * eatAtPercentage
                && !ctx.inventory.select().select(new Filter<Item>() {
            @Override
            public boolean accept(Item gameObject) {
                return Arrays.asList(gameObject.actions()).contains("Eat");
            }
        }).isEmpty();
    }

    @Override
    public void execute() {
        NoobCrabs.status = "Nomming ...";

        if (ctx.game.tab(Game.Tab.INVENTORY)) {
            final Item foodz = ctx.inventory.select(new Filter<Item>() {
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
}
