package net.bdew.wurm.bmlmap;

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
                        .addImage(HttpHandler.addr.resolve("test.png").toString(), 512, 512)
        );
        responder.getCommunicator().sendBml(300, 370, true, false, bml.toString(), 200, 200, 200, question.getTitle());
    }

    private static Stream<ServerEntry> getServers(Player player) {
        return Arrays.stream(Servers.getAllServers())
                .filter(server -> !server.entryServer && server != Servers.localServer && server.isAvailable(player.getPower(), true));
    }

    public static boolean send(Creature creature) {
        ModQuestions.createQuestion(creature, "Test image", "", -10, new TestWindow(creature)).sendQuestion();
        return true;
    }
}
