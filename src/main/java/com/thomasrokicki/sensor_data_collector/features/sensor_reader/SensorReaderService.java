package com.thomasrokicki.sensor_data_collector.features.sensor_reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thomasrokicki.sensor_data_collector.features.sensor_data_message.SensorDataMessageSample;

public class SensorReaderService {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String sensorMacId;

	public SensorReaderService(String sensorMacId) {
		this.sensorMacId = sensorMacId;
	}

	public SensorDataMessageSample readSensor() {
		final String methodLabel = "In readSensor(): ";

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
			StreamConnection streamConnection = (StreamConnection) Connector.open(sensorMacId);
			OutputStream os = streamConnection.openOutputStream();
			InputStream is = streamConnection.openInputStream();

			PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(os));
			pWriter.write("ready\n\n");
			pWriter.flush();
			pWriter.close();
			Thread.sleep(200);

			BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
			dataPointValue = bReader.readLine();
			logger.debug(methodLabel + "Read value from sensor: " + dataPointValue);

			bReader.close();
			is.close();
			os.close();
			streamConnection.close();

		} catch (ConnectionNotFoundException e) {
			logger.error(methodLabel + "Could not establish connection to sensor: " + sensorMacId);
			return null;
		} catch (Exception e) {
			logger.error(methodLabel + "Could not read from sensor: " + sensorMacId, e);
			return null;
		}
		return dataPointValue;
	}
}
