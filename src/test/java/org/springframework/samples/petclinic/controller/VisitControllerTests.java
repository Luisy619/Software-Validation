package org.springframework.samples.petclinic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
public class VisitControllerTests extends ControllerTestUtilities {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VisitRepository visitRepository;

	@MockBean
	private PetRepository petRepository;

	@Test
	void testInitNewVisitFormSuccess() throws Exception {
		Pet pet = new Pet();
		pet.setId(new Random().nextInt());
		when(petRepository.findById(any())).thenReturn(pet);
		when(visitRepository.findByPetId(any())).thenReturn(createListOfVisits());
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", pet.getId())).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@Test
	void processNewVisitFormBindingResultHasErrors() throws Exception {
		Owner owner = createTestOwnerWithLastNameNonNull();
		Pet pet = new Pet();
		pet.setId(new Random().nextInt());
		when(petRepository.findById(any())).thenReturn(pet);
		when(visitRepository.findByPetId(any())).thenReturn(createListOfVisits());
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", owner.getId(), pet.getId())
				// not sure 302 is the status we are looking for
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
		verify(visitRepository, never()).save(any());
	}

	@Test
	void processNewVisitFormBindingResultHasNoErrors() throws Exception {
		Owner owner = createTestOwner(Optional.of("Doe"), Optional.of("John"), Optional.of("1234 canada street"),
				Optional.of("1234567890"), Optional.of("Montreal"));
		Pet pet = new Pet();
		pet.setId(new Random().nextInt());
		when(petRepository.findById(any())).thenReturn(pet);
		when(visitRepository.findByPetId(any())).thenReturn(createListOfVisits());
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", owner.getId(), pet.getId())
				.param("lastName", owner.getLastName()).param("firstName", owner.getFirstName())
				.param("address", owner.getAddress()).param("telephone", owner.getTelephone())
				.param("city", owner.getCity()).param("description", "description sample")
				// not sure 302 is the status we are looking for
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isFound())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
		verify(visitRepository, times(1)).save(any());
	}

}
