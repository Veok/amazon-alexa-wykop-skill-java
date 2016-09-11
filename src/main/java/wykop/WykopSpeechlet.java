package wykop;


import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memetix.mst.translate.Translate;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import com.memetix.mst.language.Language;



/**
 * Created by L on 06.09.2016.
 */
public class WykopSpeechlet implements Speechlet {


    private static final Logger log = LoggerFactory.getLogger(WykopSpeechlet.class);


    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        return getWelcomeResponse();
    }


    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("Wykop".equals(intentName)) return getWelcomeResponse();
        else if ("LastEntry".equals(intentName)) {
            return getEntries("LastEntry");
        } else if ("AllEntries".equals(intentName)) {
            return getEntries("AllEntries");

        }
        else if("TranslatedLastEntry".equals(intentName)){
            return getEntries("TranslatedLastEntry");
        }
        else if("TranslatedAllEntries".equals(intentName)){
            return getEntries("TranslatedAllEntries");
        }
        else if ("AMAZON.StopIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");
            return SpeechletResponse.newTellResponse(outputSpeech);
        } else {
            throw new SpeechletException("Invalid intent");
        }

    }


    @Override
    public void onSessionEnded(final SessionEndedRequest sessionEndedRequest, final Session session) throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", sessionEndedRequest.getRequestId(),
                session.getSessionId());
    }

    private SpeechletResponse getWelcomeResponse() {

        String speechText = "Welcome to the wykop mirkoblog";

        SimpleCard card = new SimpleCard();
        card.setTitle("Hello");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private SpeechletResponse getEntries(String choice) {

        try {
            Reader reader = new InputStreamReader(new URL("http://a.wykop.pl/stream/hot/appkey,NPeaINJAQH").openStream()); //Read the json output
            Gson gson = new GsonBuilder().create();
            DataObject[] obj = gson.fromJson(reader, DataObject[].class);


            if (choice.equals("LastEntry")) {
                return readEntry(Jsoup.parse(obj[0].toString()).text());
            } else if (choice.equals("AllEntries")) {
                return readEntry(Jsoup.parse(Arrays.toString(obj)).text());
            }
            else if(choice.equals("TranslatedAllEntries")){
                return readEntry(getTranslatedText(Arrays.toString(obj)));
            }
            else if(choice.equals("TranslatedLastEntry")){
                return readEntry(getTranslatedText(obj[0].toString()));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("We've got a problem with connecting to wykop entries database. Try again later");
        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse readEntry(String entry) {


        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ReadEntry");
        card.setContent(entry);

        String result = entry.replaceAll("null", " End of entry");
        System.out.println(result);
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(result);

        return SpeechletResponse.newTellResponse(speech);
    }

    private String getTranslatedText(String txt) throws Exception{

        Translate.setClientId("Trebboe");
        Translate.setClientSecret("it2BSt9WjdlmpdN5VyHPTlxOBQU0zrSna8RCqLCJGvM=");

        return Translate.execute(Jsoup.parse(txt).text(), Language.POLISH, Language.ENGLISH);
    }
    public static void main(String[] args) {

        WykopSpeechlet w = new WykopSpeechlet();
        w.getEntries("LastEntry");
    }
}

