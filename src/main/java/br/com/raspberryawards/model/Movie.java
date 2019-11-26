package br.com.raspberryawards.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@Entity
public class Movie {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private  String  winner;
    private  String producer;
    private  int year;
    private  String studio;



}
