package wykop;


import com.google.gson.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;


import java.io.*;

import java.net.URL;

import java.util.List;



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
        //String speechOutput = "Welcome to the wykop mirkoblog";
        //String repromptText = "I can read random or last entries for you. All of them are translated from polish to english";
        return getWelcomeResponse();
    }


    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if("Wykop".equals(intentName)) return getWelcomeResponse();
        else if("LastEntry".equals(intentName)){

                return getLastEntry();

        }
            else if("AMAZON.StopIntent".equals(intentName)){
                PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");
            return  SpeechletResponse.newTellResponse(outputSpeech);
        }
        else{
            throw  new SpeechletException("Invalid intent");
        }

    }



    @Override
    public void onSessionEnded(final SessionEndedRequest sessionEndedRequest,final Session session) throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", sessionEndedRequest.getRequestId(),
                session.getSessionId());
    }

    private SpeechletResponse getWelcomeResponse(){

        String speechTest= "Welcome to the wykop mirkoblog";
        // String repromtText = "I can read random or last entries for you. All of them are translated from polish to english";

        SimpleCard card= new SimpleCard();
        card.setTitle("Hello");
        card.setContent(speechTest);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechTest);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newTellResponse(speech,card);
    }

    private SpeechletResponse getLastEntry()  {

        try{
            Reader reader = new InputStreamReader(new URL("http://a.wykop.pl/stream/hot/appkey,NPeaINJAQH").openStream()); //Read the json output
            Gson gson = new GsonBuilder().create();
            DataObject[] obj = gson.fromJson(reader, DataObject[].class);
            readEntry(obj[0].toString());

        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }




    private SpeechletResponse readEntry(String entry){


        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ReadEntry");

        card.setContent(entry);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
       speech.setText(entry);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private class DataObject{ //This class should match your json object structure
        private String body;
        private List<Item> item;
        @Override
        public String toString() {
            return body + item;
        }
    }

    private class Item{ //This is the inner array class
        public String body;

        @Override
        public String toString() {
            return body;
        }}

    public static void main(String[] args) {

        WykopSpeechlet w = new WykopSpeechlet();

            w.getLastEntry();

    }
    private SpeechletResponse newAskResponse(String stringOutput, String repromptText) {
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(stringOutput);

        PlainTextOutputSpeech repromptOutputSpeech = new PlainTextOutputSpeech();
        repromptOutputSpeech.setText(repromptText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);

        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }}

