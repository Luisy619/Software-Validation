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
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.persistence.VisitRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest(classes = PetClinicApplication.class)
class VisitRepositoryTests {
	
	@Autowired
	VisitRepository visits;

	@Test
	void findByPetIdTest() {
		List<Visit> actual = visits.findByPetId(8);
		assertEquals(2, actual.size());
	}
	
	@Test
	void saveTest() {
		// Note that this test depends on findByPetId
		Visit visit = new Visit();
		LocalDate date = LocalDate.of(2022, 4, 1);
		visit.setId(5);
		visit.setPetId(1);
		visit.setDate(date);
		visit.setDescription("routine check up");
		
		visits.save(visit);
		List<Visit> actual = visits.findByPetId(1);
		assertEquals(5, actual.get(0).getId());
	}

}
