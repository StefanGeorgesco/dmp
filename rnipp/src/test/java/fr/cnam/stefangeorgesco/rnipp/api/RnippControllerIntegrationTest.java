package fr.cnam.stefangeorgesco.rnipp.api;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-records.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-records.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class RnippControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private RnippRecordDto rnippRecordDto;

	@BeforeEach
	public void setup() {
		rnippRecordDto.setId("P001");
		rnippRecordDto.setFirstname("Eric");
		rnippRecordDto.setLastname("Martin");
		rnippRecordDto.setDateOfBirth(LocalDate.of(1995, 5, 15));
	}

	@Test
	public void testCheckDataSuccess() throws Exception {
		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.result", is(true)))
				.andExpect(jsonPath("$.message", is("vérification positive.")));
	}

	@Test
	public void testCheckDataFailureRecordNotFound() throws Exception {
		rnippRecordDto.setId("P01");

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.result", is(false)))
				.andExpect(jsonPath("$.message", is("vérification négative.")));
	}

	@Test
	public void testCheckDataFailureRecordFoundFirstNameDifferent() throws Exception {
		rnippRecordDto.setFirstname("Alain");

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.result", is(false)))
				.andExpect(jsonPath("$.message", is("vérification négative.")));
	}

	@Test
	public void testCheckDataFailureRecordFoundLastNameDifferent() throws Exception {
		rnippRecordDto.setLastname("Durand");

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.result", is(false)))
				.andExpect(jsonPath("$.message", is("vérification négative.")));
	}

	@Test
	public void testCheckDataFailureRecordFoundDateOfBirthDifferent() throws Exception {
		rnippRecordDto.setDateOfBirth(LocalDate.of(1975, 5, 24));

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.result", is(false)))
				.andExpect(jsonPath("$.message", is("vérification négative.")));
	}

	@Test
	public void testCheckDataFailureBadIdNull() throws Exception {
		rnippRecordDto.setId(null);

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is("L'identifiant est obligatoire.")));
	}

	@Test
	public void testCheckDataFailureBadIdBlank() throws Exception {
		rnippRecordDto.setId("");

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is("L'identifiant est obligatoire.")));
	}

	@Test
	public void testCheckDataFailureBadFirstnameNull() throws Exception {
		rnippRecordDto.setFirstname(null);

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Le prénom est obligatoire.")));
	}

	@Test
	public void testCheckDataFailureBadFirstnameBlank() throws Exception {
		rnippRecordDto.setFirstname("");

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Le prénom est obligatoire.")));
	}

	@Test
	public void testCheckDataFailureBadLastnameNull() throws Exception {
		rnippRecordDto.setLastname(null);

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.lastname", is("Le nom est obligatoire.")));
	}

	@Test
	public void testCheckDataFailureBadLastnameBlank() throws Exception {
		rnippRecordDto.setLastname("");

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.lastname", is("Le nom est obligatoire.")));
	}

	@Test
	public void testCheckDataFailureBadDateOfBirthNull() throws Exception {
		rnippRecordDto.setDateOfBirth(null);

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.dateOfBirth", is("La date de naissance est obligatoire.")));
	}

	@Test
	public void testCheckDataFailureBadDateOfBirthFuture() throws Exception {
		rnippRecordDto.setDateOfBirth(LocalDate.now().plusDays(1));

		mockMvc.perform(post("/check").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rnippRecordDto))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(
						jsonPath("$.dateOfBirth", is("La date de naissance doit être dans le passé ou aujourd'hui.")));
	}

}
