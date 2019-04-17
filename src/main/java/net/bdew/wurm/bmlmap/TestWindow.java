package net.bdew.wurm.bmlmap;

import com.wurmonline.server.Server;
import com.wurmonline.server.ServerEntry;
import com.wurmonline.server.Servers;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.Question;
import com.wurmonline.server.utils.BMLBuilder;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

public class TestWindow implements ModQuestion {
    private Creature responder;

    public TestWindow(Creature responder) {
        this.responder = responder;
    }

    @Override
    public void answer(Question question, Properties answers) {
    }

    @Override
    public void sendQuestion(Question question) {
        BMLBuilder bml = BMLBuilder.createNoQuestionWindow(String.valueOf(question.getId()),
                BMLBuilder.createGenericBuilder()
                        .addText("")
                        .addString(
                                BMLBuilder.createCenteredNode(
                                        BMLBuilder.createGenericBuilder()
                                                .addImage(HttpHandler.addr.resolve(String.format("%d.png", Server.rand.nextInt())).toString(), 256, 256)
                                ).toString()
                        )
        );
        responder.getCommunicator().sendBml(300, 320, true, true, bml.toString(), 200, 200, 200, question.getTitle());
    }

    private static Stream<ServerEntry> getServers(Player player) {
        return Arrays.stream(Servers.getAllServers())
                .filter(server -> !server.entryServer && server != Servers.localServer && server.isAvailable(player.getPower(), true));
    }

    public static void send(Creature creature) {
        ModQuestions.createQuestion(creature, "Test image", "", -10, new TestWindow(creature)).sendQuestion();
    }
}
