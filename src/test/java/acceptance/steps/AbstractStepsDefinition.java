/*
 *  Code adapted from: https://github.com/albanoj2/order-rest-backend
 *  Original author: Justin Albano
 *
 * 	This abstract class provides HTTP utilities for interacting with the Spring application.
 */

package acceptance.steps;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractStepsDefinition {

	private RestTemplate restTemplate = new RestTemplate();

	private ResponseEntity<String> lastGetResponse;

	private ResponseEntity<String> lastPostResponse;

	private int lastStatusCode;

	protected void get(String url) throws Exception {
		lastGetResponse = restTemplate.getForEntity(url, String.class);
		lastStatusCode = lastGetResponse.getStatusCodeValue();
	}

	protected void post(String url, String body, Object... urlVariables) throws Exception {
		lastPostResponse = restTemplate.postForEntity(url, new HttpEntity<>(body), String.class);
		lastStatusCode = lastPostResponse.getStatusCodeValue();
	}

	protected ResponseEntity<String> getLastGetResponse() {
		return lastGetResponse;
	}

	protected ResponseEntity<String> getLastPostResponse() {
		return lastPostResponse;
	}

	public int getLastStatusCode() {
		return lastStatusCode;
	}

}
