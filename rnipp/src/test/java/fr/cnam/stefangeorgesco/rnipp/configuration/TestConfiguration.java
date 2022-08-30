package fr.cnam.stefangeorgesco.rnipp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto;
import fr.cnam.stefangeorgesco.rnipp.model.RnippRecord;

@Configuration
public class TestConfiguration {

	@Bean(name = "RnippRecord")
	@Scope(value = "prototype")
	RnippRecord getRNIPPRecord() {
		return new RnippRecord();
	}

	@Bean(name = "RnippRecordDto")
	@Scope(value = "prototype")
	RnippRecordDto getRnippRecordDto() {
		return new RnippRecordDto();
	}

}
