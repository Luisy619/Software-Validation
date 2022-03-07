package org.springframework.samples.petclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
public class OwnerControllerTests extends ControllerTestUtilities {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OwnerRepository ownerRepository;

	@Mock
	private BindingResult bindingResult;

	@MockBean
	private VisitRepository visitRepository;

	@Test
	void initCreationFormSuccess() throws Exception {
		mockMvc.perform(get("/owners/new")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void processCreationFormWithBindingResultErrors() throws Exception {
		mockMvc.perform(post("/owners/new").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		verify(ownerRepository, never()).save(any());
	}

	@Test
	void processCreationFormWithNoBindingResultErrorSaveOwner() throws Exception {
		Owner owner = createTestOwner(Optional.of("Doe"), Optional.of("John"), Optional.of("1234 canada street"),
				Optional.of("1234567890"), Optional.of("Montreal"));
		mockMvc.perform(post("/owners/new").param("lastName", owner.getLastName())
				.param("firstName", owner.getFirstName()).param("address", owner.getAddress())
				.param("telephone", owner.getTelephone()).param("city", owner.getCity())
				// not sure 302 is the status we are looking for
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isFound());
		verify(ownerRepository, times(1)).save(any());
	}

	@Test
	void initFindFormSuccess() throws Exception {
		mockMvc.perform(get("/owners/find")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/findOwners"));
	}

	@Test
	void processFindFormNullLastNameReturnsEmptyResult() throws Exception {
		Owner owner = createTestOwner(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
				Optional.empty());
		when(ownerRepository.findByLastName(owner.getLastName())).thenReturn(null);
		mockMvc.perform(get("/owners")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/findOwners"));
	}

	@Test
	void processFindFormNonNullLastNameWithOneOwnerFound() throws Exception {
		Owner owner = createTestOwnerWithLastNameNonNull();
		Collection<Owner> list = new LinkedList<>();
		list.add(owner);
		when(ownerRepository.findByLastName(owner.getLastName())).thenReturn(list);
		mockMvc.perform(get("/owners").param("lastName", owner.getLastName())).andExpect(status().isFound())
				.andExpect(view().name("redirect:/owners/" + owner.getId()));
	}

	@Test
	void processFindFormNonNullLastNameWithMultipleOwnersFound() throws Exception {
		Owner owner1 = createTestOwnerWithLastNameNonNull();
		Owner owner2 = createTestOwnerWithLastNameNonNull();
		Collection<Owner> list = new LinkedList<>();
		list.add(owner1);
		list.add(owner2);
		when(ownerRepository.findByLastName(any())).thenReturn(list);
		mockMvc.perform(get("/owners"))
				// .param("lastName", owner.getLastName()))
				.andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/ownersList"));
	}

	@Test
	void initUpdateOwnerFormSuccess() throws Exception {
		Owner owner = createTestOwnerWithLastNameNonNull();
		when(ownerRepository.findById(any())).thenReturn(owner);
		mockMvc.perform(get("/owners/{ownerId}/edit", owner.getId())).andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void processUpdateOwnerFormWithBindingResultError() throws Exception {
		Owner owner = createTestOwnerWithLastNameNonNull();
		mockMvc.perform(post("/owners/{ownerId}/edit", owner.getId()).param("lastName", owner.getLastName())
				// not sure 302 is the status we are looking for
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void processUpdateOwnerFormWithNoBindingResultError() throws Exception {
		Owner owner = createTestOwner(Optional.of("Doe"), Optional.of("John"), Optional.of("1234 canada street"),
				Optional.of("1234567890"), Optional.of("Montreal"));
		mockMvc.perform(post("/owners/{ownerId}/edit", owner.getId()).param("lastName", owner.getLastName())
				.param("firstName", owner.getFirstName()).param("address", owner.getAddress())
				.param("telephone", owner.getTelephone()).param("city", owner.getCity())
				// not sure 302 is the status we are looking for
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isFound())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
		verify(ownerRepository, times(1)).save(any());

	}

	@Test
	void showOwnerSuccess() throws Exception {
		Owner owner = createTestOwnerWithPets();
		when(ownerRepository.findById(any())).thenReturn(createTestOwnerWithPets());
		when(visitRepository.findByPetId(any())).thenReturn(createListOfVisits());
		mockMvc.perform(get("/owners/{ownerId}", owner.getId())).andExpect(status().isOk())
				.andExpect(view().name("owners/ownerDetails"));
		verify(visitRepository, times(2)).findByPetId(any());
	}

}
