package org.springframework.samples.petclinic.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.service.PetTypeFormatter;
import org.springframework.samples.petclinic.service.PetValidator;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
public class PetControllerTests extends ControllerTestUtilities {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetRepository petRepository;

	@MockBean
	private OwnerRepository ownerRepository;

	@Mock
	private PetValidator petValidator;

	@Mock
	private PetTypeFormatter petTypeFormatter;

	@Test
	void initCreationFormSuccess() throws Exception {
		Owner testOwner = createTestOwnerWithLastNameNonNull();
		when(ownerRepository.findById(any())).thenReturn(testOwner);
		mockMvc.perform(get("/owners/{ownerId}/pets/new", testOwner.getId()))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	// has name, null id, duplication
	@Test // true, true, true
	void processCreationFormSuccessRejectsDuplicatePet_NonNullPetname_NullPetId_ResultError() throws Exception {
		// Already has a pet with the name "pet2"
		Owner testOwner = createTestOwnerWithPets();
		Pet testPet = new Pet();
		// Then tries to add pet with same name
		testPet.setName("pet1");
		when(ownerRepository.findById(any())).thenReturn(testOwner);
		mockMvc.perform(post("/owners/{ownerId}/pets/new", testOwner.getId()).param("name", testPet.getName()))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	// no name, has id, duplication
	@Test // false, false, true
	void processCreationFormSuccess_DuplicatePet_NullPetName_NonNullPetId_ResultError() throws Exception {
		// Already has a pet with the name "pet1"
		Owner testOwner = createTestOwnerWithPets();
		Pet testPet = testOwner.getPets().get(0);
		// Then tries to add pet with same name
		when(ownerRepository.findById(any())).thenReturn(testOwner);
		mockMvc.perform(
				post("/owners/{ownerId}/pets/new", testOwner.getId()).param("type", testPet.getType().toString())
						.param("id", testPet.getId().toString()).param("birthDate", testPet.getBirthDate().toString()))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test // true, false, true
	void processCreationFormSuccess_DuplicatePet_NonNullPetName_NonNullPetId_ResultError() throws Exception {
		// Already has a pet with the name "pet1"
		Owner testOwner = createTestOwnerWithPets();
		Pet testPet = testOwner.getPets().get(0);
		// Then tries to add pet with same name
		testPet.setName("pet1");
		when(ownerRepository.findById(any())).thenReturn(testOwner);
		mockMvc.perform(post("/owners/{ownerId}/pets/new", testOwner.getId())
				.param("type", testPet.getType().toString()).param("name", testPet.getName())
				.param("id", testPet.getId().toString()).param("birthDate", testPet.getBirthDate().toString()))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test //
	void processCreationFormSuccessUniquePetNonNullPetNameNonNullPetIdResultError() throws Exception {
		// Already has a pet with the name "pet1"
		Owner testOwner = createTestOwnerWithPets();
		Pet testPet = testOwner.getPets().get(0);
		when(ownerRepository.findById(any())).thenReturn(testOwner);
		mockMvc.perform(post("/owners/{ownerId}/pets/new", testOwner.getId()).param("id", testPet.getId().toString())
				.param("name", testPet.getName()).param("type", testPet.getType().toString())
				.param("birthDate", testPet.getBirthDate().toString()))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void processCreationFormSuccessUniquePetNoResultError() throws Exception {
		Owner testOwner = createTestOwnerWithPets();
		Pet testPet = testOwner.getPets().get(0);
		when(ownerRepository.findById(any())).thenReturn(testOwner);
		mockMvc.perform(post("/owners/{ownerId}/pets/new", testOwner.getId()).param("id", testPet.getId().toString())
				.param("name", testPet.getName()).param("birthDate", testPet.getBirthDate().toString()))
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void initUpdateFromSuccess() throws Exception {
		Owner testOwner = createTestOwnerWithPets();
		Pet testPet = testOwner.getPets().get(0);
		when(petRepository.findById(any())).thenReturn(testPet);
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", testOwner.getId(), testPet.getId()))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void processUpdateFromSuccessResultErrors() throws Exception {
		Owner testOwner = createTestOwnerWithPets();
		Pet testPet = testOwner.getPets().get(0);
		when(ownerRepository.findById(any())).thenReturn(testOwner);
		when(petRepository.findById(any())).thenReturn(testPet);
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", testOwner.getId(), testPet.getId())
				.param("name", testPet.getName())
				// null type field
				.param("birthDate", testPet.getBirthDate().toString()))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void processUpdateFromSuccessNoResultError() throws Exception {
		Owner testOwner = createTestOwnerWithPets();
		Pet testPet = testOwner.getPets().get(0);
		when(ownerRepository.findById(any())).thenReturn(testOwner);
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", testOwner.getId(), testPet.getId())
				.param("id", testPet.getId().toString()).param("name", testPet.getName())
				.param("birthDate", testPet.getBirthDate().toString()))
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

}
