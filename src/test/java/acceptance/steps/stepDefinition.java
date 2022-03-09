package acceptance.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class stepDefinition extends AbstractStepsDefinition {

	Integer ownerID = 0;

	@Then("the error {string} shall be raised")
	public void theErrorShallBeRaised(String errorMessage) {
		assertThat(getLastPostResponse().getBody(), containsString(errorMessage));
	}

	@Given("the following owners exist in the system:")
	public void theFollowingOwnersExistInTheSystem(DataTable ownerTable) throws Exception {
		List<Map<String, String>> owners = ownerTable.asMaps(String.class, String.class);
		for (Map<String, String> owner : owners) {
			String createOwnerUrl = createOwnerPost(owner);
			post(createOwnerUrl, "");
			// Verify the creation was successful (triggered redirection to owner page).
			assertEquals(302, getLastStatusCode());
			// Get the last owner ID from the redirection header.
			String url = Objects.requireNonNull(getLastPostResponse().getHeaders().getLocation()).toString();
			String idStr = url.substring(url.lastIndexOf('/') + 1);
			// Last owner is set as owner ID for subsequent tests.
			ownerID = Integer.parseInt(idStr);
		}

	}

	@When("the following owner is created:")
	public void theFollowingOwnerIsCreated(DataTable ownerTable) throws Exception {
		Map<String, String> owner = ownerTable.asMaps(String.class, String.class).get(0);
		String createOwnerUrl = createOwnerPost(owner);
		post(createOwnerUrl, "");
	}

	private String createOwnerPost(Map<String, String> owner) {
		return "http://localhost:8080/owners/new?" + "lastName=" + owner.get("last_name") + "&firstName="
				+ owner.get("first_name") + "&address=" + owner.get("address") + "&city=" + owner.get("city")
				+ "&telephone=" + owner.get("telephone");
	}

	@Then("the following owner will exist {int} time in the system:")
	public void theFollowingOwnerWillExistTimeInTheSystem(int count, DataTable ownerTable) throws Exception {
		Map<String, String> owner = ownerTable.asMaps(String.class, String.class).get(0);
		assertOwnerExistNTimes(owner.get("first_name"), owner.get("last_name"), owner.get("address"), owner.get("city"),
				owner.get("telephone"), count);
	}

	@When("an owner with name {string} {string}, address {string} {string}, and telephone {string} is created")
	public void anOwnerWithNameAddressAndTelephoneIsCreated(String firstName, String lastName, String address,
			String city, String telephone) throws Exception {
		assertOwnerExistNTimes(firstName, lastName, address, city, telephone, 1);
	}

	private void assertOwnerExistNTimes(String firstName, String lastName, String address, String city,
			String telephone, int count) throws Exception {
		Pattern pattern = Pattern.compile(
				"<a href=\"/owners/([0-9]+)\">" + firstName + " " + lastName + "</a></a>\n" + "\\s*</td>\n" + "\\s*<td>"
						+ address + "</td>\n" + "\\s*<td>" + city + "</td>\n" + "\\s*<td>" + telephone + "</td>");

		// Fetch the owners page and assert no error.
		String httpGetUrl = getOwnersGet();
		get(httpGetUrl);
		assertEquals(200, getLastStatusCode());

		// Count the number of time the following user appears.
		Matcher matcher = pattern.matcher(Objects.requireNonNull(getLastGetResponse().getBody()));
		int match_count = 0;
		while (matcher.find()) {
			match_count++;
		}
		assertEquals(count, match_count);
	}

	private String getOwnersGet() {
		return "http://localhost:8080/owners";
	}

	@And("an owner with name {string} {string}, address {string} {string}, and telephone {string} will not exist")
	public void anOwnerWithNameAddressAndTelephoneWillNotExist(String firstName, String lastName, String address,
			String city, String telephone) throws Exception {
		assertOwnerExistNTimes(firstName, lastName, address, city, telephone, 0);
	}

	@Given("the following pets exist for owner {string}:")
	public void theFollowingPetsExistForOwner(String ownerLastName, DataTable petTable) throws Exception {
		List<Map<String, String>> pets = petTable.asMaps(String.class, String.class);

		get(getOwnerLast(ownerLastName));
		assertEquals(302, getLastStatusCode());
		String url = Objects.requireNonNull(getLastGetResponse().getHeaders().getLocation()).toString();
		assert url != null;
		String idStr = url.substring(url.lastIndexOf('/') + 1);
		// Last owner is set as owner ID for subsequent tests.
		Integer ownerIDbyLast = Integer.parseInt(idStr);

		for (Map<String, String> pet : pets) {
			String createPetUrl = createPetPost(ownerIDbyLast, pet.get("name"), pet.get("birth_date"), pet.get("type"));
			post(createPetUrl, "");
			assertEquals(302, getLastStatusCode());
		}
	}

	private String getOwnerLast(String ownerLastName) {
		return "/owners?lastName=" + ownerLastName;
	}

	private String createPetPost(Integer ownerID, String name, String birth_date, String type) {
		return "/owners/" + ownerID.toString() + "/pets/new?" + "id=" + "&name=" + name + "&birthDate=" + birth_date
				+ "&type=" + type;
	}

	@When("the following pet is added to the owner {string}:")
	public void theFollowingPetIsAddedToTheOwner(String ownerLastName, DataTable petTable) throws Exception {
		List<Map<String, String>> pets = petTable.asMaps(String.class, String.class);

		get(getOwnerLast(ownerLastName));
		assertEquals(302, getLastStatusCode());
		String url = Objects.requireNonNull(getLastGetResponse().getHeaders().getLocation()).toString();
		assert url != null;
		String idStr = url.substring(url.lastIndexOf('/') + 1);
		// Last owner is set as owner ID for subsequent tests.
		Integer ownerIDbyLast = Integer.parseInt(idStr);

		for (Map<String, String> pet : pets) {
			String createPetUrl = createPetPost(ownerIDbyLast, pet.get("name"), pet.get("birth_date"), pet.get("type"));
			post(createPetUrl, "");
		}
	}

	@Then("the following pet will exist for owner {string}:")
	public void theFollowingPetWillExistForOwner(String ownerLastName, DataTable petTable) throws Exception {
		Map<String, String> pet = petTable.asMaps(String.class, String.class).get(0);
		assertPetExistsNTimes(ownerLastName, pet.get("name"), pet.get("birth_date"), pet.get("type"), 1);
	}

	@And("the following pet will not exist for owner {string}:")
	public void theFollowingPetWillNotExistForOwner(String ownerLastName, DataTable petTable) throws Exception {
		Map<String, String> pet = petTable.asMaps(String.class, String.class).get(0);
		assertPetExistsNTimes(ownerLastName, pet.get("name"), pet.get("birth_date"), pet.get("type"), 0);
	}

	private void assertPetExistsNTimes(String ownerLastName, String name, String birth_date, String type, int count)
			throws Exception {
		Pattern pattern = Pattern
				.compile("<dt>Name</dt>\n" + "\\s*<dd>" + name + "</dd>\n" + "\\s*<dt>Birth Date</dt>\n" + "\\s*<dd>"
						+ birth_date + "</dd>\n" + "\\s*<dt>Type</dt>\n" + "\\s*<dd>" + type + "</dd>");

		// Fetch the owners page and assert no error.
		String httpGetUrl = getOwnerLast(ownerLastName);
		get(httpGetUrl);
		assertEquals(200, getLastStatusCode());

		// Count the number of time the pet appears.
		Matcher matcher = pattern.matcher(Objects.requireNonNull(getLastGetResponse().getBody()));
		int match_count = 0;
		while (matcher.find()) {
			match_count++;
		}
		assertEquals(count, match_count);
	}

	@When("a pet with name {string}, birthdate {string}, and type {string} is created for owner {string}")
	public void aPetWithNameBirthdateAndTypeIsCreatedForOwner(String petName, String petDOB, String petType,
			String ownerLastName) throws Exception {
		assertPetExistsNTimes(ownerLastName, petName, petDOB, petType, 1);
	}

	@And("a pet with name {string}, birthdate {string}, and type {string} will not exist for owner {string}")
	public void aPetWithNameBirthdateAndTypeWillNotExistForOwner(String petName, String petDOB, String petType,
			String ownerLastName) throws Exception {
		assertPetExistsNTimes(ownerLastName, petName, petDOB, petType, 0);
	}

	@When("a visit with description {string} and date {string} is created for pet {string} of owner {string}")
	public void aVisitWithDescriptionAndDateIsCreatedForPetOfOwner(String description, String visitDate, String petName,
			String ownerLastName) throws Exception {

		// Get the page of the owner.
		get(getOwnerLast(ownerLastName));
		assertEquals(302, getLastStatusCode());
		String url = Objects.requireNonNull(getLastGetResponse().getHeaders().getLocation()).toString();
		assert url != null;
		get(url);
		assertEquals(200, getLastStatusCode());

		// Get the url to add a visit to a pet.
		Pattern pattern = Pattern.compile("href=\"((\\d+)/pets/([\\d+])/visits/new)\">Add");
		Matcher matcher = pattern.matcher(Objects.requireNonNull(getLastGetResponse().getBody()));
		String createVisitUrl = "http://localhost:8080" + matcher.group(1);
		String petID = matcher.group(3);

		// Get verify the pet is correct.
		get(createVisitUrl);
		assertEquals(200, getLastStatusCode());
		assertThat(getLastGetResponse().getBody(), containsString(petName));

		// Create a new visit.
		String createVisitUrlWParameters = createVisitUrl + "?date=" + visitDate + "&description=" + description
				+ "&petId" + petID;
		post(createVisitUrlWParameters, "");
		assertEquals(302, getLastStatusCode());
	}

	@Then("a visit with description {string} and date {string} will exist for pet {string} of owner {string}")
	public void aVisitWithDescriptionAndDateWillExistForPetOfOwner(String description, String visitDate, String petName,
			String ownerLastName) throws Exception {
		// Get the page of the owner.
		get(getOwnerLast(ownerLastName));
		assertEquals(302, getLastStatusCode());
		String url = Objects.requireNonNull(getLastGetResponse().getHeaders().getLocation()).toString();
		assert url != null;
		get(url);
		assertEquals(200, getLastStatusCode());
		assertThat(getLastGetResponse().getBody(), containsString(description));
		assertThat(getLastGetResponse().getBody(), containsString(visitDate));
		assertThat(getLastGetResponse().getBody(), containsString(petName));
	}

	@And("no visit with description {string} and date {string} will exist for pet {string} of owner {string}")
	public void noVisitWillExistForPetOfOwner(String description, String visitDate, String petName,
			String ownerLastName) throws Exception {
		// Get the page of the owner.
		get(getOwnerLast(ownerLastName));
		assertEquals(302, getLastStatusCode());
		String url = Objects.requireNonNull(getLastGetResponse().getHeaders().getLocation()).toString();
		assert url != null;
		get(url);
		assertEquals(200, getLastStatusCode());
		assertThat(getLastGetResponse().getBody(), not(containsString(description)));
		assertThat(getLastGetResponse().getBody(), not(containsString(visitDate)));
		assertThat(getLastGetResponse().getBody(), containsString(petName));
	}

}
