package com.thomasrokicki.sensor_data_collector.features.http;

import java.lang.invoke.MethodHandles;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thomasrokicki.sensor_data_collector.features.sensor_data_message.SensorDataMessage;

public class HttpService {
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String apiUrl;
	private String apiKey;

	private ObjectMapper objectMapper;

	public HttpService(String apiUrl, String apiKey) {
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	public Integer send(SensorDataMessage sensorDataMessage) {
		final String methodLabel = "In send(): ";

		int timeout = 20;
		int CONNECTION_TIMEOUT_MS = timeout * 1000; // Timeout in millis.
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
				.setConnectTimeout(CONNECTION_TIMEOUT_MS).setSocketTimeout(CONNECTION_TIMEOUT_MS).build();

		HttpClient httpClient = HttpClientBuilder.create().build(); // Use this instead

		try {
			String postBody = objectMapper.writeValueAsString(sensorDataMessage);
			logger.debug(methodLabel + "Post Body: " + postBody);

//            HttpPost request = new HttpPost("http://10.66.3.1:25561/sensorData");

			HttpPost request = new HttpPost(apiUrl);
			request.setConfig(requestConfig);
			StringEntity params = new StringEntity(postBody);
			request.addHeader("X-API-KEY", apiKey);
			request.addHeader("Content-Type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			logger.debug(methodLabel + "Server Response: " + response.toString());

			return response.getStatusLine().getStatusCode();

		} catch (Exception ex) {
			// handle exception here
			logger.error(methodLabel + "Exception while sending data to server: " + ex.getMessage());
			return -1;
		}
	}

}
