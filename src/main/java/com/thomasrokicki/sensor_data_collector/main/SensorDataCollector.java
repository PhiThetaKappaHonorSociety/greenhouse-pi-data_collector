package com.thomasrokicki.sensor_data_collector.main;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thomasrokicki.sensor_data_collector.features.http.HttpService;
import com.thomasrokicki.sensor_data_collector.features.sensor_data_message.SensorDataMessage;
import com.thomasrokicki.sensor_data_collector.features.sensor_data_message.SensorDataMessageSample;
import com.thomasrokicki.sensor_data_collector.features.sensor_reader.SensorReaderService;
import com.thomasrokicki.sensor_data_collector.utilities.RuntimeConstants;

public class SensorDataCollector {
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		final String methodLabel = "In main(): ";

		RuntimeConstants runtimeConstants = new RuntimeConstants();
		if (RuntimeConstants.checkValidity(runtimeConstants) == true) {
			run(runtimeConstants);
		} else {
			logger.error(methodLabel + "Required env variables were not found. Cannot Proceed. Shutting down.");
		}

	}

	private static void run(RuntimeConstants runtimeConstants) {
		final String methodLabel = "In run(): ";

		// Store list in memory to allow retry sending if server failure
		List<SensorDataMessageSample> samples = new ArrayList<>();

		while (true) {
			try {
				// Get new value from sensor
				SensorReaderService sensorReaderService = new SensorReaderService(
						runtimeConstants.getBluetoothMac());
				SensorDataMessageSample sensorDataSample = sensorReaderService.readSensor();
				
				if(sensorDataSample != null) {
					logger.debug(methodLabel + "Saving sample in memory");
					samples.add(sensorDataSample);
				}
				
				if(!samples.isEmpty()) {
					logger.debug(methodLabel + "Sending " + samples.size() + " samples to server.");
					
					// Create Payload
					SensorDataMessage sensorDataMessage = new SensorDataMessage();
					sensorDataMessage.setSensorUUID(runtimeConstants.getSensorUuid());
					sensorDataMessage.setSamples(samples);

					// Send Payload
					HttpService httpService = new HttpService(runtimeConstants.getApiUrl(),
							runtimeConstants.getApiKey());
					Integer statusCode = httpService.send(sensorDataMessage);

					// Remove in-memory samples if successfully sent
					if (statusCode == 200) {
						samples.removeAll(samples);
					}
				}

				// 300000 = 5 minutes
				Thread.sleep(300000);
			} catch (Exception e) {
				logger.error(methodLabel + "Exception occured in main loop", e);
			}
		}
	}
}
