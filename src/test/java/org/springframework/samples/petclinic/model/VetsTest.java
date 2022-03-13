package org.springframework.samples.petclinic.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.*;
public class VetsTest {
    static Vets vets_list=new Vets();
    static List<Vet> list=new ArrayList <Vet> ();
    @Test
    @Order(1)
    void getVetListTest(){
        assertEquals(list, vets_list.getVetList());
    }


    @AfterAll
   static void teardown() {
    vets_list=null;
    list=null;
    
   
}
}