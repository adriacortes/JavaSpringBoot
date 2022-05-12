package br.com.alura.forum.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice /*Para interceptar as exceptions que forem lançadas nos métodos das classes controller*/
public class ErroDeValidacaoHandler {

    /*
    * Para tratar os erros de validação do Bean Validation e personalizar o JSON,
    * que será devolvido ao cliente da API, com as mensagens de erro, devemos criar
    * um método na classe @RestControllerAdvice e anotá-lo com @ExceptionHandler e @ResponseStatus.
   
    * */
    @Autowired
    private MessageSource messageSource;
    @ResponseStatus(code= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception){
       List<ErroDeFormularioDto> dto = new ArrayList<>();

       List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

       fieldErrors.forEach(e ->{
           String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
           ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(),mensagem) ;
           dto.add(erro);
       });
       return dto;
    }
}
