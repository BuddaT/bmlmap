package net.bdew.wurm.bmlmap;

import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.MessagePolicy;

public class CommandHandler {
    public static MessagePolicy onPlayerMessage(Communicator communicator, String message, String title) {
        if (message.equals("#bmlmap")) {
            TestWindow.send(communicator.getPlayer());
            return MessagePolicy.DISCARD;
        }
        return MessagePolicy.PASS;
    }
}
