package acceptance.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.test.annotation.DirtiesContext;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// This annotation is required to reset the
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class stepDefinition {

	static HttpEntity responseEntity = null;
	static String ownerURL = null;

	@Before
	public void resetResponseEntity() {
		// Reset the value of the responseEntity static variable to null before each
		// scenario.
		responseEntity = null;
	}

	@Before
	public void resetOwnerID() {
		// Reset the value of the previously created ownerID.
		ownerURL = null;
	}

	@Then("the error {string} shall be raised")
	public void theErrorShallBeRaised(String errorMessage) throws IOException {
		assertNotNull(responseEntity);
		assertThat(EntityUtils.toString(responseEntity), containsString(errorMessage));
	}

	@Given("the following owners exist in the system:")
	public void theFollowingOwnersExistInTheSystem(DataTable ownerTable) throws IOException {
		List<Map<String, String>> owners = ownerTable.asMaps(String.class, String.class);
		for (Map<String, String> owner : owners) {

			try (CloseableHttpClient httpClient = HttpClientBuilder.create()
					.setRedirectStrategy(new LaxRedirectStrategy()).build()) {
				HttpPost httpPost = createOwnerPost(owner);
				HttpContext context = new BasicHttpContext();
				HttpResponse response = httpClient.execute(httpPost, context);
				// Assert that no error happened.
				assertEquals(200, response.getStatusLine().getStatusCode());
				// Make sure that the system reacted by checking if we navigated to the
				// owner information page.
				assertThat(EntityUtils.toString(response.getEntity()), containsString("Owner Information"));
				// Get the URL of the current owner for subsequent checks and operations.
				HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
				HttpHost currentHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				ownerURL = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString()
						: (currentHost.toURI() + currentReq.getURI());
			}
		}

	}

	@When("the following owner is created:")
	public void theFollowingOwnerIsCreated(DataTable ownerTable) throws IOException {
		Map<String, String> owner = ownerTable.asMap(String.class, String.class);

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
				.build()) {
			HttpPost httpPost = createOwnerPost(owner);
			HttpResponse response = httpClient.execute(httpPost);
			assertEquals(200, response.getStatusLine().getStatusCode());
			responseEntity = response.getEntity();
		}
	}

	private HttpPost createOwnerPost(Map<String, String> owner) {
		List<NameValuePair> form = new ArrayList<>();
		form.add(new BasicNameValuePair("lastName", owner.get("last_name")));
		form.add(new BasicNameValuePair("firstName", owner.get("first_name")));
		form.add(new BasicNameValuePair("address", owner.get("address")));
		form.add(new BasicNameValuePair("city", owner.get("city")));
		form.add(new BasicNameValuePair("telephone", owner.get("telephone")));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

		HttpPost httpPost = new HttpPost("http://localhost:8080/owners/new");
		httpPost.setEntity(entity);
		return httpPost;
	}

	@Then("the following owner will exist {int} time in the system:")
	public void theFollowingOwnerWillExistTimeInTheSystem(int count, DataTable ownerTable) throws IOException {
		Map<String, String> owner = ownerTable.asMap(String.class, String.class);
		assertOwnerExistNTimes(owner.get("first_name"), owner.get("last_name"), owner.get("address"), owner.get("city"),
				owner.get("telephone"), count);
	}

	@When("an owner with name {string} {string}, address {string} {string}, and telephone {string} is created")
	public void anOwnerWithNameAddressAndTelephoneIsCreated(String firstName, String lastName, String address,
			String city, String telephone) throws IOException {
		assertOwnerExistNTimes(firstName, lastName, address, city, telephone, 1);
	}

	private String assertOwnerExistNTimes(String firstName, String lastName, String address, String city,
			String telephone, int count) throws IOException {
		Pattern pattern = Pattern.compile(
				"<a href=\"/owners/([0-9]+)\">" + firstName + lastName + "</a></a>\n" + "\t*</td>\n" + "\t*<td>"
						+ address + "</td>\n" + "\t*<td>" + city + "</td>\n" + "\t*<td>" + telephone + "</td>");

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			// Fetch the owners page and assert no error.
			HttpGet httpGet = getOwnersGet();
			HttpResponse response = httpClient.execute(httpGet);
			assertEquals(200, response.getStatusLine().getStatusCode());

			// Count the owner matches in the HTML document.
			String body = response.getEntity().toString();
			Matcher matcher = pattern.matcher(body);
			int match_count = 0;
			while (matcher.find()) {
				match_count++;
			}
			assertEquals(count, match_count);
			return match_count == 1 ? matcher.group(1) : "";
		}
	}

	private HttpGet getOwnersGet() {
		return new HttpGet("http://localhost:8080/owners");
	}

	@And("an owner with name {string} {string}, address {string} {string}, and telephone {string} will not exist")
	public void anOwnerWithNameAddressAndTelephoneWillNotExist(String firstName, String lastName, String address,
			String city, String telephone) throws IOException {
		assertOwnerExistNTimes(firstName, lastName, address, city, telephone, 0);
	}

	@Given("the following pets exist for owner {string}:")
	public void theFollowingPetsExistForOwner(String ownerLastName, DataTable petTable) throws IOException {
		List<Map<String, String>> pets = petTable.asMaps(String.class, String.class);
		for (Map<String, String> pet : pets) {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				HttpPost httpPost = createPetPost(pet);
				HttpResponse response = httpClient.execute(httpPost);
				assertEquals(200, response.getStatusLine().getStatusCode());
				responseEntity = response.getEntity();
			}
		}
	}

	private HttpPost createPetPost(Map<String, String> owner) {
		List<NameValuePair> form = new ArrayList<>();
		form.add(new BasicNameValuePair("lastName", owner.get("last_name")));
		form.add(new BasicNameValuePair("firstName", owner.get("first_name")));
		form.add(new BasicNameValuePair("address", owner.get("address")));
		form.add(new BasicNameValuePair("city", owner.get("city")));
		form.add(new BasicNameValuePair("telephone", owner.get("telephone")));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

		HttpPost httpPost = new HttpPost("http://localhost:8080/owners/new");
		httpPost.setEntity(entity);
		return httpPost;
	}

	@When("the following pet is added to the owner {string}:")
	public void theFollowingPetIsAddedToTheOwner(String ownerLastName, DataTable petTable) {
		Map<String, String> pet = petTable.asMap(String.class, String.class);
	}

	@Then("the following pet will exist for owner {string}:")
	public void theFollowingPetWillExistForOwner(String ownerLastName, DataTable petTable) {
		Map<String, String> pet = petTable.asMap(String.class, String.class);
	}

	@And("the following pet will not exist for owner {string}:")
	public void theFollowingPetWillNotExistForOwner(String ownerLastName, DataTable petTable) {
		Map<String, String> pet = petTable.asMap(String.class, String.class);
	}

	@When("a pet with name {string}, birthdate {string}, and type {string} is created for owner {string}")
	public void aPetWithNameBirthdateAndTypeIsCreatedForOwner(String petName, String petDOB, String petType,
			String ownerLastName) {
	}

	@And("a pet with name {string}, birthdate {string}, and type {string} will not exist for owner {string}")
	public void aPetWithNameBirthdateAndTypeWillNotExistForOwner(String petName, String petDOB, String petType,
			String ownerLastName) {
	}

	@When("a visit with description {string} and date {string} is created for pet {string} of owner {string}")
	public void aVisitWithDescriptionAndDateIsCreatedForPetOfOwner(String description, String visitDate, String petName,
			String ownerLastName) {
	}

	@Then("a visit with description {string} and date {string} will exist for pet {string} of owner {string}")
	public void aVisitWithDescriptionAndDateWillExistForPetOfOwner(String description, String visitDate, String petName,
			String ownerLastName) {
	}

	@And("no visit will exist for pet {string} of owner {string}")
	public void noVisitWillExistForPetOfOwner(String petName, String ownerLastName) {
	}

	/**
	 * This method sends a request to the petClinic server and verifies the response.
	 * @param request The REST API endpoint to exercise.
	 * @param expectedResponse String which is expected to be part of a correct response.
	 * @param expectedCode The expected HTTP response code
	 */
	private void httpRequestHelper(String request, String expectedResponse, int expectedCode) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/" + request);
		HttpResponse response = httpclient.execute(httpGet);
		assertEquals(expectedCode, response.getStatusLine().getStatusCode());
		assertThat(EntityUtils.toString(response.getEntity()), containsString(expectedResponse));
	}

}
