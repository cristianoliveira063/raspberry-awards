package br.com.raspberryawards.listener;

import br.com.raspberryawards.model.MovieModel;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobListener extends JobExecutionListenerSupport {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public JobListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
             System.out.println("In Completion Listener ..");
            List<MovieModel> results = jdbcTemplate.query("SELECT * FROM  tb_movie",
                    (rs,rowNum)->{
                        return new MovieModel(rs.getString("title"),
                                rs.getString("winner"),rs.getString("producer"),rs.getInt("year"),
                                rs.getString("studio"));
                    }
            );
            results.forEach(System.out::println);
        }
    }
}