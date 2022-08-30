package fr.cnam.stefangeorgesco.rnipp.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto;
import fr.cnam.stefangeorgesco.rnipp.service.RnippService;

/**
 * Le contrôleur RNIPP.
 * 
 * @author Stéfan Georgesco
 *
 */
@RestController
public class RnippController {

	@Autowired
	RnippService rnippService;

	/**
	 * Vérifie la validité des données fournies dans l'objet transmis dans le corps
	 * de la requête vis-à-vis du fichier RNIPP (existence d'un enregistrement avec
	 * l'identifiant donné et concordance des autres champs).
	 * 
	 * @param rnippRecordDto l'objet
	 *                       {@link fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto}
	 *                       comportant les données à vérifier.
	 * @return un objet
	 *         {@link org.springframework.http.ResponseEntity<RestResponse>} donnant
	 *         la réponse : résultat de la vérification dans le champ boolean
	 *         {@code result}, message dans le champ {@code message}.
	 */
	@PostMapping("/check")
	public ResponseEntity<RestResponse> checkData(@Valid @RequestBody RnippRecordDto rnippRecordDto) {

		boolean result = rnippService.checkData(rnippRecordDto);

		String message = result ? "vérification positive." : "vérification négative.";

		RestResponse response = new RestResponse(result, message);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
