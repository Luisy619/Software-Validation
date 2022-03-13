package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.*;

public class OwnerTests {

	static Owner owner_1 = new Owner();
	static Owner owner_2 = new Owner();
	static Owner owner_3 = new Owner();

	static Pet pet_1 = new Pet();
	static Pet pet_2 = new Pet();
	static Pet pet_3 = new Pet();

	@BeforeAll
	static void setup() {
		owner_1.setAddress("123 McGill Street");
		owner_1.setCity("Montreal");
		owner_1.setFirstName("Homer");
		owner_1.setLastName("Simpson");

		pet_1.setName("Pet 1");
		pet_1.setId(1);
		pet_2.setName("Pet 2");
		pet_2.setId(2);

		Set<Pet> owner_1_pets = new HashSet<Pet>();
		owner_1_pets.add(pet_1);
		owner_1_pets.add(pet_2);
		owner_1.setPetsInternal(owner_1_pets);

		owner_2.setAddress("123 Queens Street");
		owner_2.setCity("Kingston");
		owner_2.setFirstName("Bruce");
		owner_2.setLastName("Banner");

		pet_3.setName("Pet 3");
		pet_3.setId(3);

		Set<Pet> owner_2_pets = new HashSet<Pet>();
		owner_2_pets.add(pet_3);

		owner_3.setAddress("123 Brooklyn Street");
		owner_3.setFirstName("Tony");
		owner_3.setLastName("Stark");
	}

	@Test
	@Order(1)
	void getAllPets() {
		List<Pet> petList = new ArrayList<Pet>();
		petList.add(pet_1);
		petList.add(pet_2);
		assertEquals(petList, owner_1.getPets());
	}

	@Test
	@Order(2)
	void setPet() {
		owner_2.addPet(pet_3);
		assertEquals(owner_2, pet_3.getOwner());
	}

	@Test
	@Order(3)
	void getSinglePet() {
		assertEquals(pet_2, owner_1.getPet("Pet 2"));
	}

	@Test
	@Order(4)
	void getFakePet() {
		assertEquals(owner_2.getPet("Pet 78"), null);
	}

	@Test
	@Order(5)
	void addNewPet() {
		owner_2.addPet(pet_3);
		assertEquals(pet_3.getOwner(), owner_2);
	}

	@Test
	@Order(6)
	void getPetIgnoreNew() {
		assertEquals(owner_1.getPet("Pet 1", false), pet_1);
	}

	@Test
	@Order(7)
	void setCityTest() {
		owner_1.setCity("Tokyo");
		assertEquals("Tokyo", owner_1.getCity());
	}

	@Test
	@Order(8)
	void getCityTest() {
		assertEquals("Kingston", owner_2.getCity());
	}

	@Test
	@Order(9)
	void getAddressTest() {
		assertEquals("123 McGill Street", owner_1.getAddress());
	}

	@Test
	@Order(10)
	void setAddressTest() {
		owner_3.setAddress("123 Test");
		assertEquals("123 Test", owner_3.getAddress());
	}

	@AfterAll
	static void teardown() {
		owner_1 = null;
		owner_2 = null;
		owner_3 = null;

		pet_1 = null;
		pet_2 = null;
		pet_3 = null;

	}

}
