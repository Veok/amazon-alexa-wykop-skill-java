package wykop;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by L on 06.09.2016.
 */
public final class WykopSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;

    static {
        supportedApplicationIds = new HashSet<String>();
    }

    public WykopSpeechletRequestStreamHandler() {
        super(new WykopSpeechlet(), supportedApplicationIds);
    }


}
