package com.svalero.comicreactive.service;

import com.svalero.comicreactive.domain.Comic;
import com.svalero.comicreactive.exception.ComicNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ComicService {

    Flux<Comic> findAll(); //Flux es cuando va a devolver una lista de elementos
//    Mono<Comic> findById(String id) throws ComicNotFoundException; // Mono es un elemento suelto, un único elemento (parecido al optional)
    Mono<Comic> findByReference(String reference) throws ComicNotFoundException;
    Mono<Comic> addComic(Comic comic);
    Mono<Comic> deleteComic(String reference) throws ComicNotFoundException;
}
