package com.svalero.comicreactive.repository;

import com.svalero.comicreactive.domain.Comic;
import com.svalero.comicreactive.exception.ComicNotFoundException;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Extendemos de ReactiveMongoRepository
 * Un objeto Comic y la clave de String
 */
@Repository
public interface ComicRepository extends ReactiveMongoRepository<Comic, String> {
    
    Flux<Comic> findAll(); //Flux es cuando va a devolver una lista de elementos
//    Mono<Comic> findById(String id); // Mono es un elemento suelto, un único elemento (parecido al optional)
    Mono<Comic> findByReference(String reference) throws ComicNotFoundException;
//    Mono<Comic> deleteComic(String reference) throws ComicNotFoundException;
}
