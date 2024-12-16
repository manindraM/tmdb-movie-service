package com.example.tmdb.api;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.tmdb.model.Movie;
import com.example.tmdb.repo.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	MovieRepository movieRepository;
	
	@BeforeEach
	void cleanUp() {
		movieRepository.deleteAllInBatch();
	}
	
	@Test
	void givenMovie_whenCreateMovie_thenReturnSavedMovie() throws  Exception {
		
		//Given
		Movie movie = new Movie();
		movie.setName("RRR");
		movie.setActors(List.of("ramcharan","NTR"));
		movie.setDirector("Rajamouli");
		
		//when create movie
		var response = mockMvc.perform(post("/movies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(movie))
				
				);
		
		//Then verify saved movie
		response.andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.id", is(notNullValue())))
		            .andExpect(jsonPath("$.name", is(movie.getName())))
		            .andExpect(jsonPath("$.actors", is(movie.getActors())))
		            .andExpect(jsonPath("$.director", is(movie.getDirector())));
		
	}
	
	@Test
	void givenMovieId_whenFetchMovie_thenReturnMovie() throws Exception {
		//Given
		Movie movie = new Movie();
		movie.setName("RRR");
		movie.setActors(List.of("ramcharan","NTR"));
		movie.setDirector("Rajamouli");
		
		Movie savedMovie = movieRepository.save(movie);
		
		//When
		var response = mockMvc.perform(get("/movies/" + savedMovie.getId()));
		
		//Then verify saved movie
		response.andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.id", is(savedMovie.getId().intValue())))
		            .andExpect(jsonPath("$.name", is(movie.getName())))
		            .andExpect(jsonPath("$.actors", is(movie.getActors())))
		            .andExpect(jsonPath("$.director", is(movie.getDirector())));
		
	}
	
	@Test
	void givenSavedMovie_WhenUpdatedMovie_thenMovieUpdatedInDb() throws Exception{

		// Given
		Movie movie = new Movie();
		movie.setName("RRR");
		movie.setActors(List.of("ramcharan", "NTR"));
		movie.setDirector("Rajamouli");

		Movie savedMovie = movieRepository.save(movie);
		
		Long id = savedMovie.getId();
		
		//update movie
		movie.setActors(List.of("Ajay devagan"));
		
		var response = mockMvc.perform(put("/movies/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(movie))
				
				);
		
		//Then verify updated movie
				response.andDo(print())
				            .andExpect(status().isOk());
				
				//When
				var fetchResponse = mockMvc.perform(get("/movies/" + id));
				
				//Then verify saved movie
				fetchResponse.andDo(print())
				            .andExpect(status().isOk())
				            .andExpect(jsonPath("$.name", is(movie.getName())))
				            .andExpect(jsonPath("$.actors", is(movie.getActors())))
				            .andExpect(jsonPath("$.director", is(movie.getDirector())));
				

	}
	
	@Test
	void givenMovie_whenDeleteRequest_thenMovieRemovedFromDb() throws Exception {
		// Given
		Movie movie = new Movie();
		movie.setName("RRR");
		movie.setActors(List.of("ramcharan", "NTR"));
		movie.setDirector("Rajamouli");

		Movie savedMovie = movieRepository.save(movie);
		Long id = savedMovie.getId();

		// then
		var response = mockMvc.perform(delete("/movies/" + id))
				.andDo(print())
	            .andExpect(status().isOk());
		
		assertFalse(movieRepository.findById(id).isPresent());
		
		
	}
}
		
		

