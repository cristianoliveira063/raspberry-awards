package br.com.raspberryawards.processor;

import br.com.raspberryawards.model.MovieInput;
import br.com.raspberryawards.model.MovieModel;
import org.springframework.batch.item.ItemProcessor;

public class MovieProcessor implements ItemProcessor<MovieInput, MovieModel> {
    @Override
    public MovieModel process(MovieInput movieInput) throws Exception {
        final MovieModel movieModel = new MovieModel(movieInput.getTitle(),movieInput.getWinner(),
                movieInput.getProducers(),Integer.parseInt(movieInput.getYear()),
                movieInput.getStudios() != null ? movieInput.getStudios() : null);
        return movieModel;
    }
}
