package br.com.raspberryawards.repository;

import br.com.raspberryawards.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    @Query(value = "SELECT min(year) FROM Movie  where  producer like %:producer%")
    public BigDecimal min(String  producer );

    @Query(value = "SELECT min(year) FROM Movie  where  producer like %:producer%")
    public BigDecimal max(String  producer );


}
