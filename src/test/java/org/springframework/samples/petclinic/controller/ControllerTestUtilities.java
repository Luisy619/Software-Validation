package org.springframework.samples.petclinic.controller;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;

import java.time.LocalDate;
import java.util.*;

public class ControllerTestUtilities {

	public Owner createTestOwner(Optional<String> lastName, Optional<String> firstName, Optional<String> address,
			Optional<String> telephone, Optional<String> city) {
		Owner owner = new Owner();
		owner.setId(new Random().nextInt() & Integer.MAX_VALUE);
		if (lastName.isPresent())
			owner.setLastName(lastName.get());
		if (firstName.isPresent())
			owner.setFirstName(firstName.get());
		if (address.isPresent())
			owner.setAddress(address.get());
		if (telephone.isPresent())
			owner.setTelephone(telephone.get());
		if (city.isPresent())
			owner.setCity(city.get());
		return owner;
	}

	public Owner createTestOwnerWithLastNameNonNull() {
		Owner owner = new Owner();
		owner.setId(new Random().nextInt() & Integer.MAX_VALUE);
		owner.setLastName("Doe");
		return owner;
	}

	public Owner createTestOwnerWithPets() {
		Owner owner = new Owner();
		owner.setId(new Random().nextInt() & Integer.MAX_VALUE);
		owner.setLastName("Doe");
		owner.setPetsInternal(createPetSetWithPets());
		return owner;
	}

	public Set<Pet> createPetSetWithPets() {
		Pet pet1 = new Pet();
		PetType petType1 = new PetType();
		petType1.setName("Dog");
		petType1.setId(1);
		pet1.setName("pet1");
		pet1.setType(petType1);
		pet1.setBirthDate(LocalDate.of(1999, 5, 12));
		// Pet pet2 = new Pet();
		// pet2.setName("pet2");
		// PetType petType2 = new PetType();
		// petType2.setName("cat");
		// pet1.setName("pet2");
		// pet1.setType(petType2);
		// pet2.setBirthDate(LocalDate.of(2001,5,13));
		pet1.setId(new Random().nextInt() & Integer.MAX_VALUE);
		// pet2.setId(new Random().nextInt() & Integer.MAX_VALUE);
		Set<Pet> petSet = new HashSet<>();
		petSet.add(pet1);
		// petSet.add(pet2);
		return petSet;
	}

	public List<Visit> createListOfVisits() {
		List<Visit> visitList = new ArrayList<>();
		Visit visit = new Visit();
		visitList.add(visit);
		return visitList;
	}

}
