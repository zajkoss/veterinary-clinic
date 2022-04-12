package pl.kurs.veterinaryclinic.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.veterinaryclinic.service.DoctorService;

@RestController
@RequestMapping(path = "/doctors")
public class DoctorController {

    private DoctorService doctorService;
    private ModelMapper mapper;

    public DoctorController(DoctorService doctorService, ModelMapper mapper) {
        this.doctorService = doctorService;
        this.mapper = mapper;
    }

}
