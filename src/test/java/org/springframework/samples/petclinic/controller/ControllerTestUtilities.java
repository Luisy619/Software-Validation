package org.springframework.samples.petclinic.controller;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;

import java.util.*;

public class ControllerTestUtilities {

	public Owner createTestOwner(Optional<String> lastName, Optional<String> firstName, Optional<String> address,
			Optional<String> telephone, Optional<String> city) {
		Owner owner = new Owner();
		owner.setId(new Random().nextInt());
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
		owner.setId(new Random().nextInt());
		owner.setLastName("Doe");
		return owner;
	}

	public Owner createTestOwnerWithPets() {
		Owner owner = new Owner();
		owner.setId(new Random().nextInt());
		owner.setLastName("Doe");
		owner.setPetsInternal(createPetSet());
		return owner;
	}

	public Set<Pet> createPetSet() {
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		pet1.setId(new Random().nextInt());
		pet2.setId(new Random().nextInt());
		Set<Pet> petSet = new HashSet<>();
		petSet.add(pet1);
		petSet.add(pet2);
		return petSet;
	}

	public List<Visit> createListOfVisits() {
		List<Visit> visitList = new ArrayList<>();
		Visit visit = new Visit();
		visitList.add(visit);
		return visitList;
	}

}
