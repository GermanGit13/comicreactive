package com.svalero.comicreactive.service;

import com.svalero.comicreactive.domain.Comic;
import com.svalero.comicreactive.exception.ComicNotFoundException;
import com.svalero.comicreactive.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ComicServiceImpl implements ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Override
    public Flux<Comic> findAll() {
        return comicRepository.findAll();
    }

//    @Override
//    public Mono<Comic> findById(String id) throws ComicNotFoundException {
//        return comicRepository.findById(id);
//    }

    @Override
    public Mono<Comic> findByReference(String reference) throws ComicNotFoundException {
        Mono<Comic> comic = comicRepository.findByReference(reference);
        return comic;
    }

    @Override
    public Mono<Comic> addComic(Comic comic) {
        return comicRepository.save(comic);
    }

    @Override
    public Mono<Comic> deleteComic(String reference) throws ComicNotFoundException {
        return this.comicRepository
                .findByReference(reference)
                .flatMap(p -> this.comicRepository.deleteById(p.getId()).thenReturn(p));
    }

//    @Override
//    public Mono<Comic> deleteComic(String reference) throws ComicNotFoundException {
//        Mono<Comic> comic = comicRepository.findByReference(reference).onErrorReturn(new Comic());
//        comicRepository.delete(comic.block());
//        return comic;
//    }

//    @Override
//    public Mono<Comic> deleteComic(String reference) throws ComicNotFoundException {
//        Mono<Comic> comic = comicRepository.findByReference(reference);
//        comicRepository.delete(comic.block());
//        return comic;
//    }
}
