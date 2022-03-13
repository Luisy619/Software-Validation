package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.validation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PetValidatorUnitTests {

	private PetValidator petValidator;
	private LocalDate birthDate;

	@BeforeEach
	void instantiate() {
		this.petValidator = new PetValidator();
		this.birthDate = LocalDate.of(2022, 1, 1);
	}


	@Test
	@DisplayName("Test validate() for when name is null")
	void ValidateName() {
		List<PetType> petTypes = new ArrayList<>();

		PetType petType = new PetType();

		Pet pet = new Pet();
		petTypes.add(petType);
		petType.setName("Dog");
		pet.setType(petType);
		pet.setBirthDate(birthDate);

		BindException errors = new BindException(pet, "name");
		ValidationUtils.invokeValidator(petValidator, pet, errors);
		assertThat(errors.hasErrors());
	}

	@Test
	@DisplayName("Test validate() for when type is null")
	void ValidateType() {
		List<PetType> petTypes = new ArrayList<>();

		PetType petType = new PetType();

		Pet pet = new Pet();
		pet.setName("Leo");
		pet.setBirthDate(birthDate);

		BindException errors = new BindException(pet, "type");
		ValidationUtils.invokeValidator(petValidator, pet, errors);
		assertThat(errors.hasErrors());
	}

	@Test
	@DisplayName("Test validate() for when birthdate is null")
	void ValidateBirthDate() {
		List<PetType> petTypes = new ArrayList<>();

		PetType petType = new PetType();

		Pet pet = new Pet();
		pet.setName("Leo");
		petTypes.add(petType);
		petType.setName("Dog");
		pet.setType(petType);

		BindException errors = new BindException(pet, "type");
		ValidationUtils.invokeValidator(petValidator, pet, errors);
		assertThat(errors.hasErrors());
	}

	@Test
	@DisplayName("Test supports()")
	void SupportTest() {
		assertTrue(petValidator.supports(Pet.class));
		assertFalse(petValidator.supports(Object.class));
	}
}
