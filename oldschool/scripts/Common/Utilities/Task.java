package oldschool.scripts.Common.Utilities;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

import java.sql.Timestamp;
import java.util.Date;

public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {
    public Task(C ctx) {
        super(ctx);
    }

    public abstract boolean activate();

    public abstract void execute();

    public void log(Object msg) {
        System.out.println("[" + new Timestamp(new Date().getTime()) + "]: " + String.valueOf(msg));
    }
}
