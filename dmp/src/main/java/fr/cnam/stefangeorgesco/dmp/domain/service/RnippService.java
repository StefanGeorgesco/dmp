package fr.cnam.stefangeorgesco.dmp.domain.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import reactor.core.publisher.Mono;

/**
 * Classe de service d'interrogation du service REST externe RNIPP.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class RnippService {

	@Autowired
	private WebClient rnippClient;

	/**
	 * Service vérifiant que les données identifiant, nom, prénom et date de
	 * naissance d'un dossier patient correspondent à un enregistrement du RNIPP. La
	 * vérification est positive si aucune exception n'est levée.
	 * 
	 * @param patientFileDTO l'objet
	 *                       {@link fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO}
	 *                       représentant le dossier patient à vérifier.
	 * @throws CheckException les données du dossier patient ne correspondent pas à
	 *                        un enregistrement du RNIPP.
	 */
	public void checkPatientData(@Valid PatientFileDTO patientFileDTO) throws CheckException {

		RnippRecord record = new RnippRecord(patientFileDTO.getId(), patientFileDTO.getFirstname(),
				patientFileDTO.getLastname(), patientFileDTO.getDateOfBirth());

		RnippResponse response = rnippClient.post().uri("/check").bodyValue(record).retrieve()
				.onStatus(HttpStatus::is5xxServerError,
						clientResponse -> Mono.error(new CheckException("Impossible de joindre le service RNIPP.")))
				.onStatus(HttpStatus::is4xxClientError,
						clientResponse -> Mono.error(new CheckException("Requête RNIPP incorrecte.")))
				.bodyToMono(RnippResponse.class).block();

		if (!response.getResult()) {
			throw new CheckException("Les données fournies sont incorrectes. Pas d'enregistrement RNIPP.");
		}
	}

}
