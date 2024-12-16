package com.example.tmdb.service;

import com.example.tmdb.exception.InvalidDataException;
import com.example.tmdb.exception.NotFoundException;
import com.example.tmdb.model.Movie;
import com.example.tmdb.repo.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional //Operational will perform fully or stop fully.
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    //CRUD Operations=CREATE UPDATE READ DELETE

    public Movie create(Movie movie){

        if(movie == null){
            throw new InvalidDataException("Invalid Movie: null");
        }
        return movieRepository.save(movie);

    }

    public Movie read(Long id){
       return movieRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Movie not found with id : " +id));
    }

    public void update(Long id, Movie update){
        if(update == null || id ==null){
            throw new InvalidDataException("Invalid Movie:null");
        }

        //check if exists
        if(movieRepository.existsById(id)){
            Movie movie = movieRepository.getReferenceById(id);
            movie.setName(update.getName());
            movie.setDirector(update.getDirector());
            movie.setActors(update.getActors());
            movieRepository.save(movie);
        }
        else{
            throw new NotFoundException("Movie not found with id : " +id);
        }

    }

    public void delete(Long id){
        if(movieRepository.existsById(id)){
            movieRepository.deleteById(id);
        }
        else{
            throw new NotFoundException("Movie not found with id : " +id);
        }
    }
}
