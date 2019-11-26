package br.com.raspberryawards.config;

import br.com.raspberryawards.listener.JobListener;
import br.com.raspberryawards.model.MovieInput;
import br.com.raspberryawards.model.MovieModel;
import br.com.raspberryawards.processor.MovieProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<MovieInput> reader() {
        FlatFileItemReader<MovieInput> csvFileReader = new FlatFileItemReader<>();
        csvFileReader.setStrict(false);
        csvFileReader.setResource(new ClassPathResource("movielist.csv"));
        csvFileReader.setLinesToSkip(1);

        LineMapper<MovieInput> movieLineMapper = createMovieLineMapper();
        csvFileReader.setLineMapper(movieLineMapper);

        return csvFileReader;
    }

    private LineMapper<MovieInput> createMovieLineMapper() {
        DefaultLineMapper<MovieInput> movieLineMapper = new DefaultLineMapper<>();

        LineTokenizer movieLineTokenizer = createMovieLineTokenizer();
        movieLineMapper.setLineTokenizer(movieLineTokenizer);

        FieldSetMapper<MovieInput> movieInformationMapper = createMovieInformationMapper();
        movieLineMapper.setFieldSetMapper(movieInformationMapper);

        return movieLineMapper;
    }

    private LineTokenizer createMovieLineTokenizer() {
        DelimitedLineTokenizer movieLineTokenizer = new DelimitedLineTokenizer();
        movieLineTokenizer.setDelimiter(";");
        movieLineTokenizer.setNames(new String[] { "year", "title","studios","producers","winner" });
        return movieLineTokenizer;
    }

    private FieldSetMapper<MovieInput> createMovieInformationMapper() {
        BeanWrapperFieldSetMapper<MovieInput> movieInformationMapper = new BeanWrapperFieldSetMapper<>();
        movieInformationMapper.setTargetType(MovieInput.class);
        return movieInformationMapper;
    }

    @Bean
    public MovieProcessor processor() {
        return new MovieProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<MovieModel> writer() {
        JdbcBatchItemWriter<MovieModel> writer = new JdbcBatchItemWriter<MovieModel>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO tb_movie (title,winner,producer,studio,year) " +
                "VALUES (:title, :winner, :producer, :studio, :year)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(JobListener listener) {
        return jobBuilderFactory.get("importMovieJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<MovieInput,MovieModel> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
