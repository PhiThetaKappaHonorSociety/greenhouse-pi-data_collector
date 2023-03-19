package com.thomasrokicki.sensor_data_collector.features.sensor_reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sputnikdev.bluetooth.URL;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.CharacteristicGovernor;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

import com.thomasrokicki.sensor_data_collector.features.sensor_data_message.SensorDataMessageSample;

public class SensorReaderService {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String sensorMacId;

	 private final BluetoothManager bluetoothManager = new BluetoothManagerBuilder()
	            .withTinyBTransport(true)
	            .withBlueGigaTransport("^*.$")
	            .withIgnoreTransportInitErrors(true)
	            .withDiscovering(true)
	            .withRediscover(true)
	            .build();
	
	public SensorReaderService(String sensorMacId) {
		this.sensorMacId = sensorMacId;
	}

	public SensorDataMessageSample readSensor() {
		final String methodLabel = "In readSensor(): ";

		bluetoothManager.addDeviceDiscoveryListener(discoveredDevice -> {
            System.out.println(String.format("Discovered: %s [%s]", discoveredDevice.getName(),
                    discoveredDevice.getURL().getDeviceAddress()));
        });
		
		String sensorValue = read();
		if (sensorValue == null) {
			logger.error(methodLabel + "Could not get sensor value.");
			return null;
		}

		SensorDataMessageSample sensorDataMessageSample = new SensorDataMessageSample();
		sensorDataMessageSample.setSampledAt(ZonedDateTime.now(ZoneOffset.UTC).toString());
		sensorDataMessageSample.setSampleValue(sensorValue);

		return sensorDataMessageSample;
	}

	private String read() {
		final String methodLabel = "In read(): ";
		
		String dataPointValue = null;
		try {
			dataPointValue = new BluetoothManagerBuilder()
            .withTinyBTransport(true)
//            .withBlueGigaTransport("^*.$")
            .build()
            .getCharacteristicGovernor(new URL(sensorMacId), true)
            .whenReady(CharacteristicGovernor::read)
            .thenAccept(dataBytes -> {
            	return new String(dataBytes, StandardCharsets.UTF_8);
            }).get();
			
			logger.debug(methodLabel + "Read value from sensor: " + dataPointValue);

		} catch (Exception e) {
			logger.error(methodLabel + "Could not read from sensor: " + sensorMacId, e);
			return null;
		}
		return dataPointValue;
	}
}
