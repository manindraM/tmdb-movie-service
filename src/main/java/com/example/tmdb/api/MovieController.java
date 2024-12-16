package com.example.tmdb.api;

import com.example.tmdb.model.Movie;
import com.example.tmdb.service.MovieService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")  //Here defining the api path here
@Slf4j
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id){
        Movie movie = movieService.read(id);
        log.info("Returned movie with id:{}",+id);
        return ResponseEntity.ok(movie);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        Movie createdMovie = movieService.create(movie);
        log.info("Created movie with id:{}",+createdMovie.getId());
        return ResponseEntity.ok(createdMovie);

    }

    @PutMapping("/{id}")
    public void updateMovie(@PathVariable Long id , @RequestBody Movie movie){
        movieService.update(id,movie);
        log.info("Updated movie with id:{}",+id);

    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id){
        movieService.delete(id);
        log.info("D movie with id:{}",+id);
    }
}
