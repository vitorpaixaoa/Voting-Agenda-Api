package sicredi.voting.agenda.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sicredi.voting.agenda.api.dto.VerifyCpfDTO;

@Slf4j
public class Utils {

    private final static String url = "https://user-info.herokuapp.com/users/";

    private Utils(){}

    public static VerifyCpfDTO checkCPF(String cpf){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url+cpf, VerifyCpfDTO.class);
    }

}
