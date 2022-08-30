package fr.cnam.stefangeorgesco.dmp.domain.service;

import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;

/**
 * Classe de service d'interrogation du service REST externe RNIPP.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class RNIPPService {

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
	public void checkPatientData(PatientFileDTO patientFileDTO) throws CheckException {

	}

}
