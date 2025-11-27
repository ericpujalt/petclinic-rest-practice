package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTests {

    private Owner createOwnerWithPets() {

        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("Street 1");
        owner.setCity("City");
        owner.setTelephone("123456789");

        Pet pet1 = new Pet();
        pet1.setId(10);
        pet1.setName("Fluffy");
        pet1.setBirthDate(LocalDate.of(2020, 1, 1));
        pet1.setOwner(owner);

        Pet pet2 = new Pet();
        pet2.setName("NewPet");
        pet2.setBirthDate(LocalDate.of(2022, 6, 1));
        pet2.setOwner(owner);

        owner.addPet(pet1);
        owner.addPet(pet2);
        return owner;
    }

    @Test
    void testToStringCoversAllFields() {
        Owner owner = new Owner();
        owner.setId(5);
        owner.setFirstName("Anna");
        owner.setLastName("Smith");
        owner.setAddress("Road 9");
        owner.setCity("Berlin");
        owner.setTelephone("222222222");

        String result = owner.toString();

        assertTrue(result.contains("5"));
        assertTrue(result.contains("Anna"));
        assertTrue(result.contains("Smith"));
        assertTrue(result.contains("Road 9"));
        assertTrue(result.contains("Berlin"));
        assertTrue(result.contains("222222222"));
    }

    @Test
    void testGetPetIgnoreNewFalseReturnsNewAndExisting() {
        Owner owner = createOwnerWithPets();

        Pet found = owner.getPet("newpet", false);

        assertNotNull(found);
        assertEquals("NewPet", found.getName());
    }

    @Test
    void testGetPetIgnoreNewTrueSkipsNewPets() {
        Owner owner = createOwnerWithPets();

        Pet found = owner.getPet("newpet", true);

        assertNull(found);
    }

    @Test
    void testGetPetReturnsExistingPetCaseInsensitive() {
        Owner owner = createOwnerWithPets();

        Pet found = owner.getPet("fLUffy", false);

        assertNotNull(found);
        assertEquals("Fluffy", found.getName());
    }

    @Test
    void testGetPetReturnsNullWhenNotFound() {
        Owner owner = createOwnerWithPets();

        Pet found = owner.getPet("unknown", false);

        assertNull(found);
    }

    @Test
    void testSetPetsInternalAndGetPets() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Bobby");
        pet.setOwner(owner);

        owner.addPet(pet);

        assertEquals(1, owner.getPets().size());
        assertTrue(owner.getPets().stream().anyMatch(p -> p.getName().equals("Bobby")));
    }
}
