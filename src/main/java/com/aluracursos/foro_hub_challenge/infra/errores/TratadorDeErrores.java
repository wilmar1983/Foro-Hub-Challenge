package com.aluracursos.foro_hub_challenge.infra.errores;

import com.aluracursos.foro_hub_challenge.dto.DatosError;
import com.aluracursos.foro_hub_challenge.dto.DatosErrorValidacion;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DatosError> tratarError404(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new DatosError("Recurso no encontrado", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DatosErrorValidacion>> tratarError400(MethodArgumentNotValidException ex) {
        var errores = ex.getFieldErrors().stream()
                .map(error -> new DatosErrorValidacion(error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<DatosError> tratarValidacionGenerica(ValidationException ex) {
        return ResponseEntity.badRequest().body(new DatosError("Error de validación", ex.getMessage()));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<DatosError> tratarErrorPersonalizado(ErrorResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new DatosError("Error personalizado", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DatosError> tratarError500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new DatosError("Error interno del servidor", ex.getMessage()));
    }
}
