package com.svalero.comicreactive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "comic") //Anotación para crear de la colección en MongoDB (Como si fuera una tabla)
public class comic {

    @Id
    private String id; //Las claves en MongoBd son cadenas de texto la compone con parámetros únicos
    @Field
    private String name; //Field son campos antes deciamos que eran columnas
    @Field
    private String category;
    @Field
    private String format;
    @Field
    private int pages;
    @Field
    private String publisher;
    @Field
    private int yearPublitacion;
    @Field
    private String reference;
    @Field
    private int stock;
    @Field
    private String author;




}
