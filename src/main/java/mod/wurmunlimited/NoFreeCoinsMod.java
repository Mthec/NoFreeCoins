package mod.wurmunlimited;

import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.*;

public class NoFreeCoinsMod implements WurmServerMod, Initable {
    @Override
    public void init() {
        HookManager manager = HookManager.getInstance();

        //noinspection SuspiciousInvocationHandlerImplementation
        manager.registerHook("com.wurmonline.server.players.Player",
                "checkCoinAward",
                "(I)Z",
                () -> (proxy, method, args) -> false);
    }
}
