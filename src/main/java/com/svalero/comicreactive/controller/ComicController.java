package com.svalero.comicreactive.controller;

import com.svalero.comicreactive.domain.Comic;
import com.svalero.comicreactive.exception.ComicNotFoundException;
import com.svalero.comicreactive.exception.ErrorMessage;
import com.svalero.comicreactive.service.ComicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ComicController {
    @Autowired
    private ComicService comicService;  //Instanciamos el ComicService
    private final Logger logger = LoggerFactory.getLogger(ComicController.class); //Creamos el objeto capaz de pintar las trazas en el log y lo asociamos a la clase que queremos controlar


    @GetMapping(value = "/comics", produces = MediaType.TEXT_EVENT_STREAM_VALUE) //Ya no devuelve un Json ahora es un flujo de datos según lo que nos vaya enviando la BBDD podemos ir cargando mientras seguimos recibiendo
    public Flux<Comic> getComic() {
        return comicService.findAll();
//        return busService.findAll().delayElements(Duration.ofSeconds(3)); //Opción para meter un retraso entre elemento y elemento que recibimos por si queremos procesar algo antes de recibir el siguiente
    }

    @GetMapping(value = "/comics/{reference}")
    public ResponseEntity<Mono<Comic>> getComicReference(@PathVariable("reference") String reference) throws ComicNotFoundException {
        Mono<Comic> comic = comicService.findByReference(reference);
        return ResponseEntity.ok(comic);
    }

    @PostMapping(value = "/comics")
    public ResponseEntity<Mono<Comic>> addComic(@RequestBody Comic comic) {
        Mono<Comic> newComic = comicService.addComic(comic);
        return ResponseEntity.ok(newComic);
    }

//    @DeleteMapping(value = "/comics/{reference}")
//    public ResponseEntity<Mono<Comic>> deleteComic(@PathVariable String reference) throws ComicNotFoundException {
//        Mono<Comic> comic = comicService.deleteComic(reference);
////        return ResponseEntity.noContent().build();
//        return ResponseEntity.ok(comic);
//    }
    @DeleteMapping("/comics/{reference}")
    private Mono<ResponseEntity<Comic>> deleteComic(@PathVariable("reference") String reference) throws ComicNotFoundException {
        return this.comicService.deleteComic(reference)
                .flatMap(comic -> Mono.just(ResponseEntity.ok(comic)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * @ExceptionHandler(ComicNotFoundException.class): manejador de excepciones, recoge la que le pasamos por parametro en este caso ComicNotFoundException.class
     * ResponseEntity<?>: Con el interrogante porque no sabe que nos devolver
     * @return
     */
    @ExceptionHandler(ComicNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleComicNotFoundException(ComicNotFoundException cnfe) {
        logger.error(cnfe.getMessage(), cnfe); //Mandamos la traza de la exception al log, con su mensaje y su traza
        cnfe.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(404, cnfe.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // le pasamos el error y el 404 de not found
    }

    /** Capturamos la excepcion para las validaciones y así devolvemos un 400 Bad Request alguien llama a la API de forma incorrecta
     *@ExceptionHandler(MethodArgumentNotValidException.class) Para capturar la excepcion de las validaciones que hacemos al dar de alta un objeto
     * le pasamos un mensaje personalizado de ErrorMessage
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(MethodArgumentNotValidException manve) {
        logger.error(manve.getMessage(), manve); //Mandamos la traza de la exception al log, con su mensaje y su traza
        manve.printStackTrace(); //Para la trazabilidad de la exception
        /**
         * Código que extrae que campos no han pasado la validación
         */
        Map<String, String> errors = new HashMap<>(); //Montamos un Map de errores
        manve.getBindingResult().getAllErrors().forEach(error -> { //para la exception manve recorremos todos los campos
            String fieldName = ((FieldError) error).getField(); //Extraemos con getField el nombre del campo que no ha pasado la validación
            String message = error.getDefaultMessage(); // y el mensaje asociado
            errors.put(fieldName, message);
        });
        /**
         * FIN Código que extrae que campos no han pasado la validación
         */

        ErrorMessage errorMessage = new ErrorMessage(400, "Bad Request", errors); //Podemos pasarle código y mensaje o añadir los códigos de error del Map sacamos los campos que han fallado
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // le pasamos el error y el 400 de not found
    }

    /**
     * Lo usamos para contralar las excepciones en general para pillar los errors 500
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        logger.error(exception.getMessage(), exception); //Mandamos la traza de la exception al log, con su mensaje y su traza
        //exception.printStackTrace(); //Para la trazabilidad de la exception
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error"); //asi no damos pistas de como está programa como si pasaba usando e.getMessage()
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR); // le pasamos el error y el 500 error en el servidor no controlado, no sé que ha pasado jajaja
    }



}
