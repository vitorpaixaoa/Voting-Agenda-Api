package sicredi.voting.agenda.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableKafka
@SpringBootApplication
public class VotingAgendaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotingAgendaApiApplication.class, args);
    }

}
