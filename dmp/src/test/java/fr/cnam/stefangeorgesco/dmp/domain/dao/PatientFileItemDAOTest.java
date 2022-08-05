package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Prescription;
import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-diseases.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-medical-acts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-patient-file-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-patient-file-items.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-diseases.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-medical-acts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class PatientFileItemDAOTest {

	@Autowired
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private Doctor authoringDoctor;

	@Autowired
	private Doctor recipientDoctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private MedicalAct medicalAct;

	@Autowired
	private Act act;

	@Autowired
	private Disease disease;

	@Autowired
	private Diagnosis diagnosis;

	@Autowired
	private Mail mail;

	@Autowired
	private Prescription prescription;

	@Autowired
	private Symptom symptom;

	private LocalDate date;

	private long count;

	@BeforeEach
	public void setUp() {
		authoringDoctor.setId("D001");
		recipientDoctor.setId("D002");
		patientFile.setId("P001");
		date = LocalDate.now();

		medicalAct.setId("HBSD001");
		act.setDate(date);
		act.setComments("a comment on this act");
		act.setAuthoringDoctor(authoringDoctor);
		act.setPatientFile(patientFile);
		act.setMedicalAct(medicalAct);

		disease.setId("J019");
		diagnosis.setDate(date);
		diagnosis.setComments("a comment on this diagnosis");
		diagnosis.setAuthoringDoctor(authoringDoctor);
		diagnosis.setPatientFile(patientFile);
		diagnosis.setDisease(disease);

		mail.setDate(date);
		mail.setComments("a comment on this mail");
		mail.setAuthoringDoctor(authoringDoctor);
		mail.setPatientFile(patientFile);
		mail.setText("This is a mail to doctor...");
		mail.setRecipientDoctor(recipientDoctor);

		prescription.setDate(date);
		prescription.setComments("a comment on this prescription");
		prescription.setAuthoringDoctor(authoringDoctor);
		prescription.setPatientFile(patientFile);
		prescription.setDescription("this prescription...");

		symptom.setDate(date);
		symptom.setComments("a comment on this prescription");
		symptom.setAuthoringDoctor(authoringDoctor);
		symptom.setPatientFile(patientFile);
		symptom.setDescription("this prescription...");
	}

	@Test
	public void testPatientFileItemDAOSaveCreateActSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(act));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateDiagnosisSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(diagnosis));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateMailSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(mail));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreatePrescriptionSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(prescription));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	public void testPatientFileItemDAOSaveCreateSymptomSuccess() {

		count = patientFileItemDAO.count();

		assertDoesNotThrow(() -> patientFileItemDAO.save(symptom));

		assertEquals(count + 1, patientFileItemDAO.count());
	}
	
	@Test
	public void testPatientFileItemDAOSaveCreateActFailureMedicalActDoesNotExist() {
		
		medicalAct.setId("ID");
		
		count = patientFileItemDAO.count();
		
		assertThrows(RuntimeException.class, () -> patientFileItemDAO.save(act));
		
		assertEquals(count, patientFileItemDAO.count());
	}
	
	@Test
	public void testPatientFileItemDAOSaveCreateMailFailureRecipientDoctorDoesNotExist() {
		
		recipientDoctor.setId("ID");
		
		count = patientFileItemDAO.count();
		
		assertThrows(RuntimeException.class, () -> patientFileItemDAO.save(mail));
		
		assertEquals(count, patientFileItemDAO.count());
	}
	
	@Test
	public void testPatientFileItemDAOSaveCreatePrescriptionFailureAuthoringDoctorDoesNotExist() {
		
		authoringDoctor.setId("ID");
		
		count = patientFileItemDAO.count();
		
		assertThrows(RuntimeException.class, () -> patientFileItemDAO.save(prescription));
		
		assertEquals(count, patientFileItemDAO.count());
	}
	
	@Test
	public void testPatientFileItemDAOSaveCreateSymptomFailurePatientFileDoesNotExist() {
		
		patientFile.setId("ID");
		
		count = patientFileItemDAO.count();
		
		assertThrows(RuntimeException.class, () -> patientFileItemDAO.save(symptom));
		
		assertEquals(count, patientFileItemDAO.count());
	}
	
}
