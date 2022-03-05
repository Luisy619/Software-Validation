package acceptance.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

public class stepDefinition {

	@Then("the error {string} shall be raised")
	public void theErrorShallBeRaised(String errorMessage) {
	}

	@Given("the following owners exist in the system:")
	public void theFollowingOwnersExistInTheSystem(DataTable ownerTable) {
		List<Map<String, String>> owners = ownerTable.asMaps(String.class, String.class);
	}

	@When("the following owner is created:")
	public void theFollowingOwnerIsCreated(DataTable ownerTable) {
		Map<String, String> owner = ownerTable.asMap(String.class, String.class);
	}

	@Then("the following owner will exist {int} time in the system:")
	public void theFollowingOwnerWillExistTimeInTheSystem(int count, DataTable ownerTable) {
		Map<String, String> owner = ownerTable.asMap(String.class, String.class);
	}

	@When("an owner with name {string} {string}, address {string} {string}, and telephone {string} is created")
	public void anOwnerWithNameAddressAndTelephoneIsCreated(String firstName, String lastName, String address, String city, String telephone) {
	}

	@And("an owner with name {string} {string}, address {string} {string}, and telephone {string} will not exist")
	public void anOwnerWithNameAddressAndTelephoneWillNotExist(String firstName, String lastName, String address, String city, String telephone) {
	}

	@Given("the following pets exist for owner {string}:")
	public void theFollowingPetsExistForOwner(String ownerLastName, DataTable petTable) {
		List<Map<String, String>> pets = petTable.asMaps(String.class, String.class);
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
	public void aPetWithNameBirthdateAndTypeIsCreatedForOwner(String petName, String petDOB, String petType, String ownerLastName) {
	}

	@And("a pet with name {string}, birthdate {string}, and type {string} will not exist for owner {string}")
	public void aPetWithNameBirthdateAndTypeWillNotExistForOwner(String petName, String petDOB, String petType, String ownerLastName) {
	}

	@When("a visit with description {string} and date {string} is created for pet {string} of owner {string}")
	public void aVisitWithDescriptionAndDateIsCreatedForPetOfOwner(String description, String visitDate, String petName, String ownerLastName) {
	}

	@Then("a visit with description {string} and date {string} will exist for pet {string} of owner {string}")
	public void aVisitWithDescriptionAndDateWillExistForPetOfOwner(String description, String visitDate, String petName, String ownerLastName) {
	}

	@And("no visit will exist for pet {string} of owner {string}")
	public void noVisitWillExistForPetOfOwner(String petName, String ownerLastName) {
	}
}
