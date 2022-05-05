package sicredi.voting.agenda.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableKafka
@SpringBootApplication
public class VotingAgendaApiApplication {
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }

    public static void main(String[] args) {
        SpringApplication.run(VotingAgendaApiApplication.class, args);
    }

}
