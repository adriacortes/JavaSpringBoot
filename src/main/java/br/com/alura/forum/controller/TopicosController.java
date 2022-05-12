package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/topicos") /*evitar repetir a URL em todos os métodos*/
public class TopicosController {

    /**
     * Para não repetir a anotação @ResponseBody em todos os métodos do controller,
     * devemos utilizar a anotação @RestController antes da declaração da Classe.
     */

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    //@ResponseBody
    public List<TopicoDto> lista(String nomeCurso) {/*Filtro por parametro*/
        if (nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDto.converter(topicos);
        } else {
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
            return TopicoDto.converter(topicos);
        }
    }

    //@RequestMapping(value = "/topicos",method = RequestMethod.POST)
    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoform, UriComponentsBuilder uriBuilder) {
        Topico topico = topicoform.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

   // @RequestMapping(value = "/topicos/{id}", method = RequestMethod.GET)
    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()){
            return  ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
       Optional<Topico> optional = topicoRepository.findById(id);
       if (optional.isPresent()) {
           Topico topico = form.atualizar(id, topicoRepository);
           return ResponseEntity.ok(new TopicoDto(topico));
       }
       return ResponseEntity.notFound().build();
    }

   @DeleteMapping("/{id}")
   @Transactional
   public ResponseEntity<?> remover(@PathVariable Long id){
    Optional<Topico> optional = topicoRepository.findById(id);
    if (optional.isPresent()){
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
   }


    /* ResponseEntity = Para montar uma resposta a ser devolvida ao cliente da API.
     *  Ferramenta teste post = Postman
     *  Para o Spring disparar as validações do Bean Validation usar anotação @Valid
     * */

}
