package br.com.raspberryawards.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieModel {

    private String title;
    private  String  winner;
    private  String producer;
    private  int year;
    private  String studio;

    public MovieModel() {
    }
}
