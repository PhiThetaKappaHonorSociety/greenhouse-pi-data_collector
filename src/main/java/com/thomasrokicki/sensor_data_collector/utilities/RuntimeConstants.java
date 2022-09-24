package com.thomasrokicki.sensor_data_collector.utilities;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RuntimeConstants {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private final String ENV_PREFIX = "ptk_greenhouse.";

	private final String ENV_API_KEY = "api_key";
	private final String ENV_API_URL = "api_url";
	private final String ENV_BLUETOOTH_MAC = "bluetooth_mac";
	private final String ENV_SENSOR_UUID = "sensor_uuid";

	private String apiKey;
	private String apiUrl;
	private String bluetoothMac;
	private String sensorUuid;

	public RuntimeConstants() {
		apiKey = getEnvVariable(ENV_PREFIX + ENV_API_KEY);
		apiUrl = getEnvVariable(ENV_PREFIX + ENV_API_URL);
		bluetoothMac = getEnvVariable(ENV_PREFIX + ENV_BLUETOOTH_MAC);
		sensorUuid = getEnvVariable(ENV_PREFIX + ENV_SENSOR_UUID);
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getBluetoothMac() {
		return bluetoothMac;
	}

	public void setBluetoothMac(String bluetoothMac) {
		this.bluetoothMac = bluetoothMac;
	}

	public String getSensorUuid() {
		return sensorUuid;
	}

	public void setSensorUuid(String sensorUuid) {
		this.sensorUuid = sensorUuid;
	}

	public static Boolean checkValidity(RuntimeConstants runtimeConstants) {
		if (!isValid(runtimeConstants.getApiKey())) {
			return false;
		}
		if (!isValid(runtimeConstants.getApiUrl())) {
			return false;
		}
		if (!isValid(runtimeConstants.getBluetoothMac())) {
			return false;
		}
		if (!isValid(runtimeConstants.getSensorUuid())) {
			return false;
		}

		return true;
	}

	private static boolean isValid(String envValue) {
		if (envValue == null || envValue.isBlank()) {
			return false;
		}
		return true;
	}

	private String getEnvVariable(String envKey) {
		try {
			String envValue = System.getenv(envKey);
			if (!isValid(envValue)) {
				return null;
			}

			return envValue;

		} catch (Exception e) {
			logger.error("Couldnt get env var: " + envKey, e);
		}
		return null;
	}
}
