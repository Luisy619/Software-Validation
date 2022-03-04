package acceptance.steps;

import io.cucumber.java.en.Given;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;

public class stepDefinition {
	@Given("the following owners exist in the system:")
	public void theFollowingOwnersExistInTheSystem(DataTable ownerTable) {
		List<Map<String, String>> owners = ownerTable.asMaps(String.class, String.class);
	}
}
