package com.ylitormatech.sensinglog.data.component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.ylitormatech.sensinglog.service.SensorService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

//import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

/**
 * Created by mika on 10.6.2016.
 *
 * SaverThread is used by MessagingEndpoint for saving sensor message to Mongo database.
 * Message data arrives as json-formatted string containing (possibly) several sets of
 * datatypes. To ease up the further data quering the json string is parsed so that all
 * datatypes are saved as a separate row in database. To help timing this process the
 * separation is made in separate threads (by this class).
 *
 * @param: sensServ     Service to initiate the database actions
 * @param: msg          Sensor message, json-formatted.
 */
public class SaverThread implements Runnable {
    private String message;
    private SensorService sensorService;

    Logger logger = Logger.getLogger(this.getClass().getName());
    public static final boolean DEV_MODE = true;

    public SaverThread(SensorService sensServ, String msg) {
        this.sensorService = sensServ;
        this.message = msg;
    }


    @Override
    public void run() {
        long lStartTime, lEndTime = 0;
        if (DEV_MODE) {
            lStartTime = System.currentTimeMillis();
        }
        logger.debug("run(): STARTED");
        processSave();

        if (DEV_MODE) {
            lEndTime = System.currentTimeMillis();
        }
        logger.debug("run(): ENDED: Elapsed millisecs: " + (lEndTime - lStartTime));
    }


    /*
    E.g.:
    {"token":"adsfdsafdsf",
            "timestamp":1234567890,
            "data":[{"datatype":"ACC","value":{"x": 0.4,"y":0.3,"z":0.3}},{"datatype":"HUM","value":20}]}
    --->
    {"token":"adsfdsafdsf","timestamp":1234567890,"datatype":"ACC","value":{"x":0.4,"y":0.3,"z":0.3}}
    {"token":"adsfdsafdsf","timestamp":1234567890,"datatype":"HUM","value":20}
    */
    private void processSave() {
        if ( sensorService == null || message.isEmpty() ) {
            logger.error("processSave(): Invalid SensorService or empty message detected!");
            return;
        }

        JsonFactory jsonOldFactory = new JsonFactory();     // json factory for the original message (to be parsed out).
        JsonParser jsonOldParser = null;

        StringBuilder strBaseJson = new StringBuilder();        // Builder for the static part of the new json message (token, timestamp...).
        StringBuilder currNewMsg = null;                        // Placeholder for the json currently under consturction...
        ArrayList<StringBuilder> newMsgs = new ArrayList();     // Collection of new jsons.

        try {
            // Load the original message to parser:
            jsonOldParser = jsonOldFactory.createParser(message);
            int iStartObject = 0;    // Start token counter.
            boolean fComma = false;
            StringBuilder currFieldName = new StringBuilder();

            // This will give all jsonTokens one at a time, like:
            // START_OBJECT,FIELD_NAME,VALUE_STRING,...,START_OBJECT,...,END_OBJECT,END_OBJECT.
            // We must keep count on starts and ends, and handle the content accordingly.
            while (!jsonOldParser.isClosed()) {
                JsonToken jsonToken = jsonOldParser.nextToken();
                if (jsonToken == null) {
                    continue;
                }

                switch (jsonToken) {
                    case START_OBJECT:
                        iStartObject++;
                        if ( iStartObject == 1 ) {  // At the beginning of the static part...
                            currNewMsg = strBaseJson;
                            currNewMsg.append("{");
                        }
                        else if (iStartObject == 2) {
                            // This bracket is removed from the string, since data-level is removed
                            // from the new jsons. However, this is the end of the base json string:
                            // start always a new json to store the data part individually (base + data).
                            currNewMsg = new StringBuilder(strBaseJson.toString());
                            newMsgs.add(currNewMsg);             // Add to collection for saving...
                        }
                        else {
                            // We arrive here only if there are multiple 'value' items. In that case
                            // just enclose them with '{}'.
                            currNewMsg.append("{");
                            fComma = false;
                        }
                        currFieldName.setLength(0);
                        break;
                    case FIELD_NAME:    // This is field name, always string.
                        currFieldName.setLength(0);
                        currFieldName.append(jsonOldParser.getCurrentName());
                        if ("data".equals(currFieldName.toString())) {
                            // DO NOT write "data" into the new json at all!
                            currFieldName.setLength(0);
                            break;
                        }
                        else {
                            if (fComma) {
                                currNewMsg.append(",\"" + currFieldName.toString() + "\":");
                            }
                            else {
                                currNewMsg.append("\"" + currFieldName.toString() + "\":");
                            }
                            fComma = true;
                        }
                        break;
                    case VALUE_STRING:  // String value for the last field name.
                        currNewMsg.append("\"" + jsonOldParser.getValueAsString() + "\"");
                        break;
                    case VALUE_NUMBER_INT:  // Integer formatted value for the last field name.
                        if ("timestamp".equals(currFieldName.toString())) {
                            currNewMsg.append( jsonOldParser.getValueAsLong());
                        }
                        else {
                            currNewMsg.append( jsonOldParser.getValueAsInt());
                        }
                        break;
                    case VALUE_NUMBER_FLOAT:    // Float formatted value for the last field name.
                        currNewMsg.append( jsonOldParser.getValueAsDouble());
                        break;
                    case VALUE_FALSE:
                    case VALUE_TRUE:
                        currNewMsg.append( jsonOldParser.getValueAsBoolean() );
                        break;
                    case START_ARRAY:
                        // Just ignore this: We must combine each
                        // following datatype part separately with the static start part of the new json
                        // (and create as many individual jsons as there are datatype parts).
                        // The array itself is not visible in the new jsons!
                        // Each json is stored on newMsg array for the database saving.
                        break;
                    case END_ARRAY:
                        // Ignored, see above.
                        break;
                    case END_OBJECT:
                        if (iStartObject == 1) {
                            // Close all new jsons:
                            for ( StringBuilder myMsg : newMsgs ) {
                                myMsg.append("}");

                                System.out.println(myMsg.toString());
                            }
                        }
                        else if (iStartObject == 2) {
                            // This is skipped for the new json!!!
                        }
                        else {
                            currNewMsg.append("}");
                        }
                        iStartObject--;
                        break;
                }
                //System.out.println("jsonToken = " + jsonToken);
            }

            jsonOldParser.close();

            // Now just save the newly created jsons:
            for ( StringBuilder myMsg : newMsgs ) {
                //System.out.println(myMsg.toString());
                sensorService.saveMessage(myMsg.toString());
            }
        }
        catch (IOException ioe) {
            logger.error("processSave(): " + ioe.getMessage());
        }
        catch (NullPointerException npe) {
            logger.error("processSave(): " + npe.getMessage());
        }
    }
}
