package br.com.gympoint;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import br.com.gympoint.dto.UserCredentialsDto;
import br.com.gympoint.utils.Response;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@Transactional
@SpringBootTest(classes = GymPointApplication.class)
public class RegistrationControllerTest {

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	private MockMvc mvc;
	
	@Before
	public void setup() {
		this.mvc = MockMvcBuilders
				.webAppContextSetup(this.wac)
				.addFilter(springSecurityFilterChain)
				.build();
	}
	
	@Test
	public void store_unauthorized() throws Exception {

		
		String json = "{\n" + 
				"	\"student\" : {\n" + 
				"		\"id\" : \"4\"\n" + 
				"	},\n" + 
				"	\"contract\": {\n" + 
				"		\"id\" : \"6\"\n" + 
				"	},\n" + 
				"	\"startDate\" : \"2020-01-17T13:00:00.000+0000\"\n" + 
				"	\n" + 
				"}";
		
		//Token vencido, deve falhar
		doPostRegistration(
				json, 
				"la@l.com", 
				"asasa23232",
				"eyJhbGciOiJIUzUxMiJ9.eyJpc1Byb3ZpZGVyIjp0cnVlLCJzdWIi"
				+ "OiJsYUBsLmNvbSIsImV4cCI6MTU3OTA1Mzc3MCwidXNlcklkIjoxMSwiaWF0IjoxNTc5MDM1NzcwfQ."
				+ "4ZH0OeTCDfCBc94iJCC4C20rBiaGjJemm8_5SPIx85w8oJskqNQwOoEmNXnIAQmTcQ1ZTOhJ09cO67pzYmtd7Q",
				status().isUnauthorized());
		
	}
	
	@Test
	public void store_created() throws Exception {

		
		String json = "{\n" + 
				"	\"student\" : {\n" + 
				"		\"id\" : \"4\"\n" + 
				"	},\n" + 
				"	\"contract\": {\n" + 
				"		\"id\" : \"6\"\n" + 
				"	},\n" + 
				"	\"startDate\" : \"2020-01-17T13:00:00.000+0000\"\n" + 
				"	\n" + 
				"}";
		
		//Tudo valido, deve passar
		doPostRegistration(json, "la@l.com", "12345678", null, status().isCreated());
		
	}
	
	@Test
	public void store_bad_request_contract() throws Exception {

		
		String json = "{\n" + 
				"	\"contract\": {\n" + 
				"		\"id\" : \"6\"\n" + 
				"	},\n" + 
				"	\"startDate\" : \"2020-01-17T13:00:00.000+0000\"\n" + 
				"	\n" + 
				"}";
		
		//Contrato nao informado, deve falhar com bad request
		doPostRegistration(json, "la@l.com", "12345678", null, status().isBadRequest());
		
	}
	
	@Test
	public void store_bad_request_student() throws Exception {

		
		String json = "{\n" + 
				"	\"student\" : {\n" + 
				"		\"id\" : \"4\"\n" + 
				"	},\n" + 
				"	\"startDate\" : \"2020-01-17T13:00:00.000+0000\"\n" + 
				"	\n" + 
				"}";
		
		//Estudante nao informado, deve falhar com bad request
		doPostRegistration(json, "la@l.com", "12345678", null, status().isBadRequest());
		
	}
	
	@Test
	public void store_bad_request_date() throws Exception {

		
		String json = "{\n" + 
				"	\"student\" : {\n" + 
				"		\"id\" : \"4\"\n" + 
				"	},\n" + 
				"	\"contract\": {\n" + 
				"		\"id\" : \"6\"\n" + 
				"	},\n" + 
				"}";
		
		//Data de inicio nao informada, deve falhar com bad request
		doPostRegistration(json, "la@l.com", "12345678", null, status().isBadRequest());
		
	}
	
	@Test
	public void store_forbidden_student_already_exist_in_contract() throws Exception {

		
		String json = "{\n" + 
				"	\"student\" : {\n" + 
				"		\"id\" : \"1\"\n" + 
				"	},\n" + 
				"	\"contract\": {\n" + 
				"		\"id\" : \"6\"\n" + 
				"	},\n" + 
				"	\"startDate\" : \"2020-01-17T13:00:00.000+0000\"\n" + 
				"	\n" + 
				"}";
		
		//tenta salvar um registro com um usuario ja existente em outro contrato
		doPostRegistration(json, "la@l.com", "12345678", null, status().isForbidden());
		
	}
	
	@Test
	public void index_ok() throws Exception {
		doGetRegistration("", "la@l.com", "12345678", null, status().isOk());
	}
	
	@Test
	public void index_unauthorized() throws Exception {
		
		doGetRegistration(
				"",
				"la@l.com", 
				"12345678", 
				"eyJhbGciOiJIUzUxMiJ9.eyJpc1Byb3ZpZGVyIjp0cnVlLCJzdWIi"
				+ "OiJsYUBsLmNvbSIsImV4cCI6MTU3OTA1Mzc3MCwidXNlcklkIjoxMSwiaWF0IjoxNTc5MDM1NzcwfQ."
				+ "4ZH0OeTCDfCBc94iJCC4C20rBiaGjJemm8_5SPIx85w8oJskqNQwOoEmNXnIAQmTcQ1ZTOhJ09cO67pzYmtd7Q",
				 status().isUnauthorized());
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void show_ok() throws Exception {
		
	    ResultActions result =  doGetRegistration("/student/1", "la@l.com", "12345678", null, status().isOk());
		
		Gson gson = new Gson();
		String resultString = result.andReturn().getResponse().getContentAsString();
		Response response =  gson.fromJson(resultString,Response.class);
		
		assertNotNull(response.getData());
		
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void show_ok_empty() throws Exception {
		
	    ResultActions result =  doGetRegistration("/student/999", "la@l.com", "12345678", null, status().isOk());
		
		Gson gson = new Gson();
		String resultString = result.andReturn().getResponse().getContentAsString();
		Response response =  gson.fromJson(resultString,Response.class);
		
		assertNull(response.getData());
		
	}
	
	private ResultActions doGetRegistration(
			String pathParams,
			String email, 
			String password, 
			String token,
			ResultMatcher matcher) throws Exception {
		
		String jwtToken = token != null ? token : getJwt(email, password);
		
		return mvc.perform(get("/api/v1/registration".concat(pathParams))
				.header("Authorization", "Bearer " + jwtToken)
				.accept(APPLICATION_JSON))
				.andExpect(matcher);
	}
	
	private void doPostRegistration(
			String json, 
			String email, 
			String password, 
			String token,
			ResultMatcher matcher) throws Exception {
		
		String jwtToken = token != null ? token : getJwt(email, password);
		
		mvc.perform(post("/api/v1/registration")
				.header("Authorization", "Bearer " + jwtToken)
				.contentType(APPLICATION_JSON)
				.content(json)
				.accept(APPLICATION_JSON))
				.andExpect(matcher);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getJwt(String userName, String password) throws Exception {
		
		UserCredentialsDto userC = new UserCredentialsDto(userName, password);
		Gson gson = new Gson();
		
		ResultActions result  = mvc.perform(post("/api/v1/login")
				.contentType(APPLICATION_JSON)
				.content(gson.toJson(userC))
				.accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON));
		
		String resultString = result.andReturn().getResponse().getContentAsString();
		Response response =  gson.fromJson(resultString,Response.class);

		LinkedTreeMap<String, String> map = (LinkedTreeMap<String, String>) response.getData();
		
		return map.get("token").toString();
		
	}
	
}
