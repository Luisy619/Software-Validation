package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import io.cucumber.java.Before;

import java.time.LocalDate;
import java.util.*;

public class PetTests {

	static Pet pet_1 = new Pet();
	static Pet pet_2 = new Pet();

	static Visit visit_1 = new Visit();
	static Visit visit_2 = new Visit();

	static Owner owner_1 = new Owner();

	@BeforeAll
	static void setup() {
		pet_1.setName("Pablo");
		pet_1.setOwner(owner_1);
		pet_1.setBirthDate(LocalDate.of(2022, 03, 03));
		pet_1.setId(1);

		pet_2.setName("Luke");
		pet_2.setOwner(owner_1);
		pet_2.setId(2);

		visit_1.setDate(LocalDate.of(2022, 04, 10));
		visit_2.setDate(LocalDate.of(2023, 04, 10));
		Set<Visit> visit_list = new HashSet<Visit>();
		visit_list.add(visit_1);
		visit_list.add(visit_2);
		pet_2.setVisitsInternal(visit_list);

	}

	@Test
	@Order(1)
	void getBirthdayTest() {
		assertEquals(LocalDate.of(2022, 03, 03), pet_1.getBirthDate());
	}

	@Test
	@Order(2)
	void setBirthdayTest() {
		pet_2.setBirthDate(LocalDate.of(2023, 03, 03));
		assertEquals(LocalDate.of(2023, 03, 03), pet_2.getBirthDate());
	}

	@Test
	@Order(3)
	void getVisitTest() {
		List<Visit> visit_list = new ArrayList<Visit>();
		visit_list.add(visit_1);
		visit_list.add(visit_2);
		PropertyComparator.sort(visit_list, new MutableSortDefinition("date", false, false));
		assertEquals(visit_list, pet_2.getVisits());
	}

	@Test
	@Order(4)
	void getOwnerTest() {
		assertEquals(owner_1, pet_1.getOwner());
	}

	@Test
	@Order(5)
	void getIdTest() {

		assertEquals(2, pet_2.getId());
	}

	@Test
	@Order(6)
	void addVisitTest() {
		pet_1.addVisit(visit_1);
		assertEquals(visit_1.getPetId(), pet_1.getId());
	}

	@AfterAll
	static void teardown() {
		pet_1 = null;
		pet_2 = null;

		visit_1 = null;
		visit_2 = null;

		owner_1 = null;
	}

}
