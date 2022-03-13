package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.persistence.PetRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class PetTypeFormatterUnitTests {

	@Mock
	private PetRepository petRepo;

	private PetTypeFormatter petTypeFormatter;

	@BeforeEach
	void instantiate() {
		this.petTypeFormatter = new PetTypeFormatter(petRepo);
	}


	@Test
	@DisplayName("Test print()")
	void PrintTest() {
		PetType petType = new PetType();

		petType.setName("Leo");
		String petName = this.petTypeFormatter.print(petType, Locale.ENGLISH);

		assertThat(petName).isEqualTo("Leo");
	}

	@Test
	@DisplayName("Test parse() with no exception")
	void ParseTestNoException() throws ParseException {
		List<PetType> petTypes = new ArrayList<>();

		PetType petType = new PetType();

		petType.setName("Dog");
		petTypes.add(petType);

		given(this.petRepo.findPetTypes()).willReturn(petTypes);
		assertThat(petTypeFormatter.parse("Dog", Locale.ENGLISH).getName()).isEqualTo("Dog");
	}

	@Test
	@DisplayName("Test parse() with an exception")
	void ParseTestWithException() {
		List<PetType> petTypes = new ArrayList<>();

		given(this.petRepo.findPetTypes()).willReturn(petTypes);
		Assertions.assertThrows(ParseException.class, () -> {
			petTypeFormatter.parse("Cat", Locale.ENGLISH);
		});
	}
}
