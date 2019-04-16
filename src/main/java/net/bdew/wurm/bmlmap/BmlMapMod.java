package net.bdew.wurm.bmlmap;

import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.MessagePolicy;
import org.gotti.wurmunlimited.modloader.interfaces.PlayerMessageListener;
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

public class BmlMapMod implements WurmServerMod, ServerStartedListener, PlayerMessageListener {
    @Override
    public void onServerStarted() {
        HttpHandler.start(this);
    }

    @Override
    @Deprecated
    public boolean onPlayerMessage(Communicator communicator, String message) {
        return false;
    }

    @Override
    public MessagePolicy onPlayerMessage(Communicator communicator, String message, String title) {
        if (message.startsWith("#") && communicator.getPlayer().getPower() > 0)
            return CommandHandler.onPlayerMessage(communicator, message, title);
        return MessagePolicy.PASS;
    }
}
