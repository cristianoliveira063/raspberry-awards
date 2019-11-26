package br.com.raspberryawards.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieInput {
    private  String year;
    private String title;
    private String studios;
    private  String producers;
    private  String winner;

    public MovieInput() {
    }

}
