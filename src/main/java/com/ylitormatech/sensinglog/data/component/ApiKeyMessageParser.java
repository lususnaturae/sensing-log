package com.ylitormatech.sensinglog.data.component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mika on 16.7.2016.
 */
public class ApiKeyMessageParser {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private String origMessage;
    private StringBuilder apiKey;
    private int sensorId;
    private List<String> dataTypes;

    public ApiKeyMessageParser(String message) {
        this.origMessage = message;
        this.apiKey = new StringBuilder();
        sensorId = 0;
        dataTypes = new ArrayList<String>();
    }

    public String getApiKey() {
        return apiKey.toString();
    }

    public int getSensorId() {
        return sensorId;
    }

    public List<String> getDataTypes() {
        return dataTypes;
    }

    public boolean breakMessage() {
        boolean fRet = false;

        return processMessage();
    }


    /*
    E.g.:
    {"apikey":"04FC9A2B1F80222E534998EC1F028B251CA613D80B24781A27055A0923C1C783",
     "id":7,"datatype":[{"id":1,"name":"TMP","labeltoken":"sensor.type.checkbox.label.tmp"},{<possible next dt>}]}
    */
    private boolean processMessage() {
        boolean fRet = false;

        if ( origMessage.isEmpty() ) {
            logger.error("ApiKeyMessageParser::processMessage(): Empty message detected!");
            return fRet;
        }

        JsonFactory jsonOldFactory = new JsonFactory();     // json factory for the original message (to be parsed out).
        JsonParser jsonOldParser = null;

        try {
            // Load the original message to parser:
            jsonOldParser = jsonOldFactory.createParser(origMessage);
            int iStartObject = 0;   // Start token counter.
            int iArrayObject = 0;   // Array token counter.
            StringBuilder valueString = new StringBuilder();
            StringBuilder currFieldName = new StringBuilder();
            boolean fNotValid = false;
            boolean fDatatypeValid = false;    // if 'true', id refers to datatype (at the following loops).
            int iDatatypeAO = 0;                // Helper for fDatatypeValid controlling.

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
                        break;
                    case FIELD_NAME:    // This is field name, always string.
                        currFieldName.setLength(0);
                        currFieldName.append(jsonOldParser.getCurrentName());
                        break;
                    case VALUE_STRING:  // String value for the last field name.
                        if ("apikey".equals(currFieldName.toString())) {
                            this.apiKey.setLength(0);
                            this.apiKey.append(jsonOldParser.getValueAsString());
                            fRet = true;
                        }
                        if ("name".equals(currFieldName.toString())) {
                            if (fDatatypeValid) {
                                // TODO: validity check for datatype ???
                                dataTypes.add(jsonOldParser.getValueAsString());
                            }
                            fRet = true;
                        }
                        break;
                    case VALUE_NUMBER_INT:  // Integer formatted value for the last field name.
                        if ("id".equals(currFieldName.toString())) {
                            if (!fDatatypeValid) {
                                this.sensorId = jsonOldParser.getValueAsInt();
                            }
                            fRet = true;
                        }
                        break;
                    case VALUE_NUMBER_FLOAT:    // Float formatted value for the last field name.
                        break;
                    case VALUE_FALSE:
                    case VALUE_TRUE:
                        break;
                    case START_ARRAY:
                        iArrayObject++;
                        if ("datatype".equals(currFieldName.toString())) {
                            fDatatypeValid = true;
                            iDatatypeAO = iArrayObject;
                        }
                        break;
                    case END_ARRAY:
                        if (iDatatypeAO == iArrayObject) {
                            fDatatypeValid = false;
                            iDatatypeAO = 0;
                        }
                        iArrayObject--;
                        break;
                    case END_OBJECT:
                        iStartObject--;
                        break;
                }
                //System.out.println("jsonToken = " + jsonToken);
            }

            jsonOldParser.close();
        }
        catch (IOException ioe) {
            logger.error("processSave(): " + ioe.getMessage());
        }
        catch (NullPointerException npe) {
            logger.error("processSave(): " + npe.getMessage());
        }
        return fRet;
    }

}
