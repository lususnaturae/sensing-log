package com.ylitormatech.sensinglog.data.component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.ylitormatech.sensinglog.service.ApiKeyService;
import com.ylitormatech.sensinglog.service.SensorService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

//import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

/**
 * Created by mika on 10.6.2016.
 *
 * SaverThread is used by MessagingEndpoint for save sensor messages to Mongo database.
 * Message data arrives as json-formatted string containing (possibly) several sets of
 * datatypes. To ease up the further data quering the json string is parsed so that all
 * datatypes are saved as a separate row in the database. To help timing this process the
 * separation is made in avseparate thread (per message).
 * Also the following adjustments are made:
 * - the apikey identifying (and sent by) the sensor is replaced by the unique (database)
 *   id of the sensor (given by the system at the time of sensor creation).
 * - Sensor message is not saved if the sensor is not set as 'active'.
 *
 * @param: sensServ     Service to initiate the database actions
 * @param: msg          Sensor message, json-formatted.
 */
public class SaverThread implements Runnable {
    private String message;
    private ApiKeyService apiKeyService;
    private SensorService sensorService;
    private final String strApikeyField = "apikey";
    private final String strDataField = "data";
    private final String strSensorIdField = "sensorid";

    Logger logger = Logger.getLogger(this.getClass().getName());
    public static final boolean DEV_MODE = true;

    public SaverThread(ApiKeyService apiServ, SensorService sensServ, String msg) {
        this.apiKeyService = apiServ;
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
    {"apikey":"adsfdsafdsf",
     "timestamp":1234567890,
     "data":[{"datatype":"ACC","value":{"x": 0.4,"y":0.3,"z":0.3}},{"datatype":"HUM","value":20}]}
    --->
    {"apikey":"adsfdsafdsf","timestamp":1234567890,"datatype":"ACC","value":{"x":0.4,"y":0.3,"z":0.3}}
    {"apikey":"adsfdsafdsf","timestamp":1234567890,"datatype":"HUM","value":20}
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
            StringBuilder valueString = new StringBuilder();
            StringBuilder currFieldName = new StringBuilder();
            boolean fCheckValidity = false;
            boolean fNotValid = false;

            // This will give all jsonTokens one at a time, like:
            // START_OBJECT,FIELD_NAME,VALUE_STRING,...,START_OBJECT,...,END_OBJECT,END_OBJECT.
            // We must keep count on starts and ends, and handle the content accordingly.
            while (!jsonOldParser.isClosed() || fNotValid) {
                JsonToken jsonToken = jsonOldParser.nextToken();
                if (jsonToken == null) {
                    continue;   // Comes here when no tokens are left!
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
                        if (strDataField.equals(currFieldName.toString())) {
                            // DO NOT write "data" into the new json at all!
                            currFieldName.setLength(0);
                            break;
                        }
                        else {
                            if (strApikeyField.equals(currFieldName.toString())) {
                                fCheckValidity = true;
                                // This will trigger the validity check for the sensor
                                // on 'VALUE_STRING' next round!
                                currFieldName.setLength(0);
                                currFieldName.append(strSensorIdField);   // Change the id to sensorid.
                            }

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
                        valueString.setLength(0);
                        valueString.append(jsonOldParser.getValueAsString());
                        if (fCheckValidity) {
                            // Check the validity of the apikey, and if passed replace it
                            // with the database id of the sensor.
                            if (!checkSensorValidity(valueString)) {
                                fNotValid = true;
                                break;
                            }
                            fCheckValidity = false;
                            // NOTICE: valueString contains here the database id, not the apikey any longer!
                            currNewMsg.append(valueString.toString());
                        }
                        else {
                            currNewMsg.append("\"" + valueString.toString() + "\"");
                        }
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
                            currNewMsg.append("}"); // Close multi-value item.
                        }
                        iStartObject--;
                        break;
                }
                //System.out.println("jsonToken = " + jsonToken);
            }

            jsonOldParser.close();

            if (!fNotValid) {
                // Now just save the newly created jsons:
                for (StringBuilder myMsg : newMsgs) {
                    //System.out.println(myMsg.toString());
                    sensorService.saveMessage(myMsg.toString());
                }
            }
        }
        catch (IOException ioe) {
            logger.error("processSave(): " + ioe.getMessage());
        }
        catch (NullPointerException npe) {
            logger.error("processSave(): " + npe.getMessage());
        }
    }


    private boolean checkSensorValidity(StringBuilder apikey) {
        boolean fRet = false;

        // Test sensor validity: apikey existence, active status.
        // Returns the databse id of the sensor, if found. Otherwise zero value.

        if (apikey.length() > 0) {
            int id = apiKeyService.checkSensorValidityByApiKey(apikey.toString());
            if (id > 0) {
                apikey.setLength(0);
                apikey.append(id);
                fRet = true;
            }
        }
        return fRet;
    }
}
