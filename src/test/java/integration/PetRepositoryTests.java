package integration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest(classes = PetClinicApplication.class)
class PetRepositoryTests {

	@Autowired
	PetRepository pets;

	@Autowired
	OwnerRepository owners;

	@Test
	@Transactional
	void findPetTypesTest() {
		List<PetType> actual = pets.findPetTypes();

		assertEquals(6, actual.size());
	}

	@Test
	@Transactional
	void findByIdTest() {
		Pet actual = pets.findById(1);
		assertEquals("Leo", actual.getName());
	}

	@Test
	@Transactional
	void saveTest() {
		// Note that this test is dependent on findByID()
		// and on OwnerRepository.findById()
		Pet pet = new Pet();
		LocalDate birthDate = LocalDate.of(2021, 10, 1);
		Owner owner = owners.findById(1);
		PetType type = new PetType();

		type.setId(1);
		type.setName("Mammoth");

		pet.setBirthDate(birthDate);
		pet.setId(14);
		pet.setName("Charlie");
		pet.setOwner(owner);
		pet.setType(type);

		pets.save(pet);
		Pet actual = pets.findById(14);

		assertEquals("Charlie", actual.getName());

	}

}
