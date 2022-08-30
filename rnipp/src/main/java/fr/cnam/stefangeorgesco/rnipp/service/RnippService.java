package fr.cnam.stefangeorgesco.rnipp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto;
import fr.cnam.stefangeorgesco.rnipp.model.RnippRecord;
import fr.cnam.stefangeorgesco.rnipp.repository.RnippRecordRepository;

/**
 * Classe de sercice.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class RnippService {

	@Autowired
	private RnippRecordRepository rnippRecordRepository;

	/**
	 * Vérifie la validité des données fournies dans un objet
	 * {@link fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto} vis-à-vis du fichier
	 * RNIPP (existence d'un enregistrement avec l'identifiant donné et concordance
	 * des autres champs).
	 * 
	 * @param rnippRecordDto l'objet
	 *                       {@link fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto}
	 *                       comportant les données à vérifier.
	 * @return un booléen, égal à {@code true} si la vérification est positive,
	 *         {@code false} sinon.
	 */
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
