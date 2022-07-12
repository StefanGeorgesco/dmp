package fr.cnam.stefangeorgesco.dmp.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;

@Configuration
public class MapperConfig {
	
	@Bean
	public ModelMapper commonModelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public ModelMapper doctorDTOModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<DoctorDTO, Doctor> typeMap = modelMapper.createTypeMap(DoctorDTO.class, Doctor.class);
		typeMap.addMapping(src -> src.getSpecialtyDTOs(), Doctor::setSpecialties);
		typeMap.addMapping(src -> src.getAddressDTO(), Doctor::setAddress);
		
		return modelMapper;
		
	}

}
