package acceptance;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = PetClinicApplication.class)
public class CucumberSpringConfiguration {

}
