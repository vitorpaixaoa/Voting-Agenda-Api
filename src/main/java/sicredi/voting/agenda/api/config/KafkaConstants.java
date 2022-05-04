package sicredi.voting.agenda.api.config;

import lombok.Getter;

@Getter
public class KafkaConstants {

    private KafkaConstants(){}

    public static  final String BOOTSTRAP_SERVER = "127.0.0.1:9092";

    public static final String RESULT_TOPIC = "vote-result";

    public static final String GROUP_ID ="my-group-id";
}
