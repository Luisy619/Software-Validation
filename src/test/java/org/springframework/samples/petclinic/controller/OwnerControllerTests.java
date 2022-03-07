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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
public class OwnerControllerTests {

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
		mockMvc.perform(post("/owners/new").param("lastName", "Doe").param("firstName", "John")
				.param("address", "1234 canada street").param("telephone", "1234567890").param("city", "Montreal")
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
		Owner owner = new Owner();
		owner.setLastName(null);
		when(ownerRepository.findByLastName(owner.getLastName())).thenReturn(null);
		mockMvc.perform(get("/owners")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/findOwners"));
	}

}
