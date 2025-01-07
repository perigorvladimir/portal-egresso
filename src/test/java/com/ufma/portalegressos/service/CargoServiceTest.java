package com.ufma.portalegressos.service;

import com.ufma.portalegressos.application.services.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("test")
public class CargoServiceTest {
    @Autowired
    private CargoService cargoService;

}
