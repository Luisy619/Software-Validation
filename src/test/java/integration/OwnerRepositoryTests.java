package integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.transaction.annotation.Transactional;


@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest(classes = PetClinicApplication.class)
class OwnerRepositoryTests {

	@Autowired
	OwnerRepository owners;
	
	@Test
	@Transactional
	void findByIdTest() {
		Owner actual = owners.findById(1);
		assertEquals("George", actual.getFirstName());
	}
	
	@Test
	@Transactional
	void findByLastNameTest() {
		Collection<Owner> actual = owners.findByLastName("Davis");
		assertEquals(2, actual.size());
	}
	
	@Test
	@Transactional
	void saveTest() {
		// Note that this test is dependent on findByID()
		Owner owner = new Owner();
		
		owner.setFirstName("Kevin");
		owner.setLastName("Wang");
		owner.setId(11);
		owner.setAddress("1 Test ave.");
		owner.setCity("Montreal");
		owner.setTelephone("1111111111");
		
		owners.save(owner);
		
		Owner actual = owners.findById(11);
		
		assertEquals("Kevin", actual.getFirstName());
	}


}
