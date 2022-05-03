package sicredi.voting.agenda.api.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import sicredi.voting.agenda.api.dto.VotingAgendaDTO;
import sicredi.voting.agenda.api.model.VotingAgendaModel;
import sicredi.voting.agenda.api.util.BaseMapper;

@Component
public class VotingAgendaMapper extends BaseMapper<VotingAgendaModel, VotingAgendaDTO> {
    @Override
    protected void configure(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<VotingAgendaModel, VotingAgendaDTO>() {
            @Override
            protected void configure() {
                // TODO document why this method is empty
            }
        });

        modelMapper.addMappings(new PropertyMap<VotingAgendaDTO, VotingAgendaModel>() {
            @Override
            protected void configure() {
                // TODO document why this method is empty
            }
        });
    }
}
