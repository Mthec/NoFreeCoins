package mod.wurmunlimited;

import com.wurmonline.server.creatures.Creature;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class NoFreeCoinsMod implements WurmServerMod, Configurable, Initable {
    private boolean sellBlocked = false;

    @Override
    public void configure(Properties properties) {
        String val = properties.getProperty("allow_sell_at_token");
        if (val != null && !val.isEmpty()) {
            sellBlocked = !val.equals("true");
        }
    }

    @Override
    public void init() {
        HookManager manager = HookManager.getInstance();

        //noinspection SuspiciousInvocationHandlerImplementation
        manager.registerHook("com.wurmonline.server.players.Player",
                "checkCoinAward",
                "(I)Z",
                () -> (proxy, method, args) -> false);

        manager.registerHook("com.wurmonline.server.behaviours.Methods",
                "discardSellItem",
                "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/behaviours/Action;Lcom/wurmonline/server/items/Item;F)Z",
                () -> this::discardSellItem);
    }

    Object discardSellItem(Object o, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (sellBlocked) {
            Creature player = (Creature)args[0];
            player.getCommunicator().sendNormalServerMessage("You cannot sell items at the token on this server.");
            return true;
        } else {
            return method.invoke(o, args);
        }
    }
}
