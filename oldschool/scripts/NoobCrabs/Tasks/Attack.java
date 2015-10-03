package oldschool.scripts.NoobCrabs.Tasks;

import oldschool.scripts.Common.Utilities.Task;
import org.powerbot.script.rt4.ClientContext;

public class Attack extends Task<ClientContext> {
    private final int[] Rocks = {101,103};
    private final int[] Crabs = {100, 102};

    public Attack(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void execute() {

    }
}
