package wykop;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by L on 06.09.2016.
 */
public final class WykopSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */

        supportedApplicationIds = new HashSet<String>();
        //supportedApplicationIds.add("amzn1.echo-sdk-ams.app.[amzn1.ask.skill.345aec2e-5fb7-4150-95b9-85fde40e9e4e]");
    }
    public WykopSpeechletRequestStreamHandler() {
        super(new WykopSpeechlet(), supportedApplicationIds);
    }


}
