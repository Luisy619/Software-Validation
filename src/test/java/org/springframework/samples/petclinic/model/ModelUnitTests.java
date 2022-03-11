package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.SerializationUtils;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ModelUnitTests {
	@Test
	@DisplayName("Test serialization()")
	void Serialization() {
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Doe");

		Person other = (Person) SerializationUtils.deserialize(SerializationUtils.serialize(person));
		assertThat(other.getFirstName()).isEqualTo(person.getFirstName());
		assertThat(other.getLastName()).isEqualTo(person.getLastName());
	}
}
