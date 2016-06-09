package com.ylitormatech.sensinglog.data.repository;

import com.ylitormatech.sensinglog.data.entity.SensorLogDataEntity;

/**
 * Created by mika on 9.6.2016.
 */
public interface SensorDataRepository {
    public void add(SensorLogDataEntity slde);

    /*
    public List<String> getGroupByDate(sensoridid, start, end);

    public delete(sensorid, boolean all = false, start, end);


     */
}
