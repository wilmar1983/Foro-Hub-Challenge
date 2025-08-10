package com.aluracursos.foro_hub_challenge.controller;

import com.aluracursos.foro_hub_challenge.domain.Topico;
import com.aluracursos.foro_hub_challenge.dto.*;
import com.aluracursos.foro_hub_challenge.repository.CursoRepository;
import com.aluracursos.foro_hub_challenge.repository.TopicoRepository;
import com.aluracursos.foro_hub_challenge.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Operation(summary = "Listar tópicos activos",
            description = "Retorna una lista paginada de los tópicos activos en el foro.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listar(
            @Parameter(description = "Parámetros de paginación")
            @PageableDefault(size = 10) Pageable paginacion
    ) {
        var pagina = topicoRepository.findByEstadoTrue(paginacion)
                .map(DatosListadoTopico::new);
        return ResponseEntity.ok(pagina);
    }

    @Operation(
            summary = "Registrar nuevo tópico",
            description = "Crea un nuevo tópico en el foro especificando título, mensaje, autor y curso.")
    @ApiResponse(responseCode = "201", description = "Tópico creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid
            @Parameter(description = "Datos del nuevo tópico", required = true)
            DatosRegistroTopico datos,
            UriComponentsBuilder uriBuilder
    ) {
        var autor = usuarioRepository.findById(datos.autor())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        var curso = cursoRepository.findById(datos.curso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        var topico = new Topico(
                null,
                datos.titulo(),
                datos.mensaje(),
                LocalDateTime.now(),
                true,
                autor,
                curso
        );
        topicoRepository.save(topico);

        var datosRespuesta = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                autor.getNombre(),
                curso.getNombre()
        );

        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuesta);
    }

    @Operation(
            summary = "Actualizar tópico existente",
            description = "Actualiza el título y mensaje de un tópico dado su ID.")
    @ApiResponse(responseCode = "200", description = "Tópico actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Tópico no encontrado", content = @Content)
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @Parameter(description = "ID del tópico a actualizar", required = true)
            @PathVariable Long id,
            @RequestBody @Valid
            @Parameter(description = "Nuevos datos del tópico")
            DatosActualizarTopico datos
    ) {
        var topico = topicoRepository.getReferenceById(id);
        topico.actualizar(datos);

        var datosRespuesta = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre()
        );

        return ResponseEntity.ok(datosRespuesta);
    }

    @Operation(
            summary = "Obtener un tópico por ID",
            description = "Devuelve los detalles completos de un tópico activo a partir de su ID.")
    @ApiResponse(responseCode = "200", description = "Tópico encontrado")
    @ApiResponse(responseCode = "404", description = "Tópico no encontrado o inactivo", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> obtenerTopico(
            @Parameter(description = "ID del tópico a consultar", required = true)
            @PathVariable Long id
    ) {
        var topico = topicoRepository.findById(id)
                .filter(Topico::getEstado)
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado o inactivo"));

        var detalle = new DatosDetalleTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre()
        );

        return ResponseEntity.ok(detalle);
    }

    @Operation(
            summary = "Eliminar un tópico (eliminación lógica)",
            description = "Marca como inactivo un tópico existente sin eliminarlo físicamente de la base de datos.")
    @ApiResponse(responseCode = "204", description = "Tópico eliminado lógicamente")
    @ApiResponse(responseCode = "404", description = "Tópico no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del tópico a eliminar", required = true)
            @PathVariable Long id
    ) {
        var topico = topicoRepository.getReferenceById(id);
        topico.desactivar();
        return ResponseEntity.noContent().build();
    }
}
