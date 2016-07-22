package com.ylitormatech.sensinglog.data.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylitormatech.sensinglog.data.entity.SensorData;
import com.ylitormatech.sensinglog.service.ApiKeyService;
import com.ylitormatech.sensinglog.service.SensorService;
import com.ylitormatech.sensinglog.utils.headerAgentInterceptor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mika on 22/07/16.
 */
@RestController
@RequestMapping("/api/sensordata")
public class SensingLogRestController {

    Logger logger = Logger.getLogger(this.getClass().getName());
    public final String uri = "http://localhost:8080/user/id";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private headerAgentInterceptor headerAgentInterceptor;

    @Autowired
    ApiKeyService apiKeyService;

    @Autowired
    SensorService sensorService;


    /*
    @RequestMapping(value = "/create", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<String> createSensor(@RequestHeader("Authorization") String header, @RequestBody @Valid SensorForm sensorForm, BindingResult bindingResult){
        Integer userId;
        ResponseEntity<String> jmsResponse;

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("{\"error\":\"sensor.create.error.missing.value\"}");
        }

        userId = getUserInfo(header);
        if (userId == -1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"sensor.create.error.invalid.user\"}");
        }

        if(sensorService.restFindIsSensorNameExist(sensorForm.getName(),userId)) {
            if(sensorDatatypeService.isDatatypesValid(sensorForm.getSensordatatypes())) {
                SensorEntity sensorEntity = sensorService.restAdd(sensorForm.getName(), sensorForm.getSensordatatypes(), userId);
                jmsResponse = jmsService.newSensor(sensorEntity);
                if (jmsResponse == null) {
                    sensorService.restUpdateOnlog(sensorEntity);
                    return ResponseEntity.ok("sensor create");
                } else {
                    return jmsResponse;
                }
            }
            return  ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("{\"error\":\"sensor.create.error.invalid.datatype\"}");
        }
        return  ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("{\"error\":\"sensor.create.error.sensor.name.exist\"}");
    }*/

    /*
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> showSensor(@RequestHeader("Authorization") String header, @PathVariable("id") Integer id) {
        logger.debug("Show SensorEntity Controller - GET");
        String json;
        Integer userId = getUserInfo(header);
        ObjectMapper objectMapper = new ObjectMapper();

        if (userId == -1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"sensor.get.error.invalid.user\"}");
        }

        SensorEntity sensor = sensorService.restFind(id,userId);

        if (sensor==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        try {
            json = objectMapper.writeValueAsString(sensor);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("{\"error\":\"sensor.get.error.json.parsing\"}");
        }
        return ResponseEntity.ok(json);
    }*/


    @RequestMapping(value = "/deletedata/{apikey}&{starttime}&{endtime}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSensorData(@RequestHeader("Authorization") String header, @PathVariable("apikey") String apikey,
                                               @PathVariable("starttime") Long starttime, @PathVariable("endtime") Long endtime) {
        logger.debug("REST 'Delete sensor data' Controller - DELETE");
        String json;
        ObjectMapper objectMapper = new ObjectMapper();
        //Integer userId = getUserInfo(header);

        /*if (userId == -1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"sensor.delete.error.invalid.user\"}");
        }*/

        int sensorId = apiKeyService.getSensorIdByApiKey(apikey);
        if ( sensorId <= 0 ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        int iCount = sensorService.removeMessages(sensorId, starttime, endtime);
        try {
            json = objectMapper.writeValueAsString(iCount);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("{\"error\":\"sensor.get.error.json.parsing\"}");
        }
        return ResponseEntity.ok(json);
    }


    @RequestMapping(value = "/getdata24h/{apikey}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDataLast24h(@RequestHeader("Authorization") String header, @PathVariable("apikey") String apikey) {
        logger.debug("REST 'Get sensor data for last 24h' Controller - GET");
        String json;
        //Integer userid = getUserInfo(header);
        ObjectMapper objectMapper = new ObjectMapper();

        /*if (userid == -1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"sensor.list.error.invalid.user\"}");
        }*/

        int sensorId = apiKeyService.getSensorIdByApiKey(apikey);
        if ( sensorId <= 0 ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Get yesterdays date:
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        // Get yesterday's date in milliseconds:
        long start = cal.getTime().getTime();

        // Get current time in millis:
        Date date = new Date();
        long end =  date.getTime() ;

        List<SensorData> dataList = sensorService.getMessages(sensorId, start, end);
        if(dataList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        try {
            json = objectMapper.writeValueAsString(dataList);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("{\"error\":\"sensing-log.list.error.json.parsing\"}");
        }
        return ResponseEntity.ok(json);
    }



    /*
    private Integer getUserInfo(String header){
        UserInfo userInfo;

        headerAgentInterceptor.setBearer(header);

        try {
            userInfo = restTemplate.getForObject(uri, UserInfo.class);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
        System.out.println(userInfo.getId());
        return userInfo.getId();
    }

    @RequestMapping(value = "/datatypelist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listSensorDatatypes(@RequestHeader("Authorization") String header) {
        logger.debug("REST List SensorDatatypeEntity Controller - GET");
        String json;
        Integer userid = getUserInfo(header);
        ObjectMapper objectMapper = new ObjectMapper();

        if (userid == -1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        List<SensorDatatypeEntity> sensorDatatypes = sensorDatatypeService.findAll();
        if(sensorDatatypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        try {
            json = objectMapper.writeValueAsString(sensorDatatypes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("error.json.parsing");
        }
        return ResponseEntity.ok(json);
    }*/



}