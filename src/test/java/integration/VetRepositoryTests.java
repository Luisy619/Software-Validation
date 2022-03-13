package integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.persistence.VetRepository;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest(classes = PetClinicApplication.class)
class VetRepositoryTests {

	@Autowired
	VetRepository vets;

	@Test
	@Transactional
	void findAllTest() {
		Collection<Vet> actual = vets.findAll();
		assertEquals(6, actual.size());
	}

}
