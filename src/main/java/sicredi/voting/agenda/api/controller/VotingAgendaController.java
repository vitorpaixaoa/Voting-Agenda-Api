package sicredi.voting.agenda.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sicredi.voting.agenda.api.dto.VerifyCpfDTO;
import sicredi.voting.agenda.api.dto.VoteCountingDTO;
import sicredi.voting.agenda.api.dto.VoteDTO;
import sicredi.voting.agenda.api.dto.VotingAgendaDTO;
import sicredi.voting.agenda.api.model.VoteModel;
import sicredi.voting.agenda.api.model.VotingAgendaModel;
import sicredi.voting.agenda.api.service.VotingAgendaService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1.0/voting-agenda")
@RequiredArgsConstructor
public class VotingAgendaController {

    private final VotingAgendaService service;

    @PostMapping
    public ResponseEntity<VotingAgendaDTO> addNewVotingAgenda(@RequestBody @Valid VotingAgendaDTO dto){
        return new ResponseEntity<>(service.createVotingAgenda(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotingAgendaModel> findVotingAgenda(@PathVariable Long id){
        return new ResponseEntity<>(service.findVotingAgendaById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<VotingAgendaDTO>> listAll(Pageable pageable){
        return new ResponseEntity<>(service.allVotingAgenda(pageable), HttpStatus.OK);
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<VoteModel> addNewVotingAgenda(@PathVariable Long id, @RequestBody @Valid VoteDTO dto){
        return new ResponseEntity<>(service.addVote(id, dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/votes")
    public ResponseEntity<VoteCountingDTO> getVotes(@PathVariable Long id){
        return new ResponseEntity<>(service.getVotes(id), HttpStatus.OK);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<VerifyCpfDTO> verifyCpf(@PathVariable String cpf){
        return new ResponseEntity<>(service.verifyCpf(cpf), HttpStatus.OK);
    }
}
