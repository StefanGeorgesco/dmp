package fr.cnam.stefangeorgesco.rnipp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto;
import fr.cnam.stefangeorgesco.rnipp.model.RnippRecord;
import fr.cnam.stefangeorgesco.rnipp.repository.RnippRecordRepository;

@Service
public class RnippService {

	@Autowired
	private RnippRecordRepository rnippRecordRepository;

	public boolean checkData(RnippRecordDto rnippRecordDto) {

		Optional<RnippRecord> optionalRnippRecord = rnippRecordRepository.findById(rnippRecordDto.getId());

		if (optionalRnippRecord.isPresent()) {
			RnippRecord rnippRecord = optionalRnippRecord.get();
			return rnippRecord.getFirstname().equals(rnippRecordDto.getFirstname())
					&& rnippRecord.getLastname().equals(rnippRecordDto.getLastname())
					&& rnippRecord.getDateOfBirth().equals(rnippRecordDto.getDateOfBirth());
		} else {
			return false;
		}
	}

}
