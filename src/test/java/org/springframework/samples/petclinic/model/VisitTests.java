package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.*;

public class VisitTests {

	static Visit visit_1 = new Visit();
	static LocalDate date_1 = LocalDate.of(2023, 03, 03);
	static String message_1 = "Test...1";
	static Integer id = 1;
	static Integer petId = 90;

	@Test
	@Order(1)
	void getPetIdTest() {
		visit_1.setPetId(petId);
		assertEquals(90, visit_1.getPetId());
	}

	@Test
	@Order(2)
	void getIdTest() {
		visit_1.setId(id);
		assertEquals(1, visit_1.getId());
	}

	@Test
	@Order(3)
	void getDateTest() {
		visit_1.setDate(date_1);
		assertEquals(date_1, visit_1.getDate());
	}

	@Test
	@Order(4)
	void getDescTest() {
		visit_1.setDescription(message_1);
		assertEquals(message_1, visit_1.getDescription());
	}

}
