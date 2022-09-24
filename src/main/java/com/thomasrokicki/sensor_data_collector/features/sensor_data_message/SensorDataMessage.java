package com.thomasrokicki.sensor_data_collector.features.sensor_data_message;

import java.util.List;

public class SensorDataMessage {

	private String sensorUUID;
	private List<SensorDataMessageSample> samples;

	public String getSensorUUID() {
		return sensorUUID;
	}

	public void setSensorUUID(String sensorUUID) {
		this.sensorUUID = sensorUUID;
	}

	public List<SensorDataMessageSample> getSamples() {
		return samples;
	}

	public void setSamples(List<SensorDataMessageSample> samples) {
		this.samples = samples;
	}

}
