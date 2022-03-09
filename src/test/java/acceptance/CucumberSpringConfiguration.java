package acceptance;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = { PetClinicApplication.class, CucumberRunnerTest.class },
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberSpringConfiguration {

}
