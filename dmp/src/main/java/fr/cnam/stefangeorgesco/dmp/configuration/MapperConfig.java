package fr.cnam.stefangeorgesco.dmp.configuration;



import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

@Configuration
public class MapperConfig {
	
	@Bean
	public Provider<Doctor> doctorProvider() {
		return new Provider<Doctor>() {
			public Doctor get(ProvisionRequest<Doctor> request) {
				Doctor doctor = new Doctor();
				doctor.setId(((String) request.getSource()));
				return doctor;
			}
		};
	}

	@Bean
	public ModelMapper commonModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ModelMapper doctorDTOModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<DoctorDTO, Doctor> typeMap = modelMapper.createTypeMap(DoctorDTO.class, Doctor.class);
		typeMap.addMapping(src -> src.getSpecialtiesDTO(), Doctor::setSpecialties);
		typeMap.addMapping(src -> src.getAddressDTO(), Doctor::setAddress);

		return modelMapper;
	}

	@Bean
	public ModelMapper doctorModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Doctor, DoctorDTO> typeMap = modelMapper.createTypeMap(Doctor.class, DoctorDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		typeMap.addMappings(mapper -> mapper.skip(DoctorDTO::setSecurityCode));
		typeMap.addMapping(src -> src.getSpecialties(), DoctorDTO::setSpecialtiesDTO);
		typeMap.addMapping(src -> src.getAddress(), DoctorDTO::setAddressDTO);

		return modelMapper;
	}

	@Bean
	public ModelMapper patientFileDTOModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<PatientFileDTO, PatientFile> typeMap = modelMapper.createTypeMap(PatientFileDTO.class,
				PatientFile.class);
		typeMap.addMappings(mapper -> mapper.with(doctorProvider()).map(PatientFileDTO::getReferringDoctorId, PatientFile::setReferringDoctor));
		typeMap.addMapping(src -> src.getAddressDTO(), PatientFile::setAddress);

		return modelMapper;
	}

	@Bean
	public ModelMapper patientFileModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<PatientFile, PatientFileDTO> typeMap = modelMapper.createTypeMap(PatientFile.class,
				PatientFileDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		typeMap.addMappings(mapper -> mapper.skip(PatientFileDTO::setSecurityCode));
		typeMap.addMapping(src -> src.getReferringDoctor().getId(), PatientFileDTO::setReferringDoctorId);
		typeMap.addMapping(src -> src.getAddress(), PatientFileDTO::setAddressDTO);

		return modelMapper;
	}

	@Bean
	public ModelMapper userModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<User, UserDTO> typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		typeMap.addMappings(mapper -> mapper.skip(UserDTO::setPassword));
		typeMap.addMappings(mapper -> mapper.skip(UserDTO::setSecurityCode));

		return modelMapper;
	}

}
