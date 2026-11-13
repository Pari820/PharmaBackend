package pe.edu.upeu.PharmaBackend.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.PharmaBackend.auditoria.EventoAuditoria;
import pe.edu.upeu.PharmaBackend.dto.AuditoriaEventoDTO;
import pe.edu.upeu.PharmaBackend.dto.PaginaResponseDTO;
import pe.edu.upeu.PharmaBackend.entity.AuditoriaEvento;
import pe.edu.upeu.PharmaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.PharmaBackend.repository.AuditoriaEventoRepository;
import pe.edu.upeu.PharmaBackend.service.service.AuditoriaService;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {
    private static final Logger log = LoggerFactory.getLogger(AuditoriaServiceImpl.class);
    private final AuditoriaEventoRepository repository;

    public AuditoriaServiceImpl(AuditoriaEventoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(EventoAuditoria evento) {
        AuditoriaEvento a = new AuditoriaEvento();
        a.setFecha(LocalDateTime.now());
        a.setUsuario(evento.usuario() == null ? "sistema" : evento.usuario());
        a.setAccion(evento.accion());
        a.setEntidad(evento.entidad());
        a.setEntidadId(evento.entidadId());
        a.setDetalle(evento.detalle());
        a.setRequestId(evento.requestId());
        repository.save(a);
        log.info("Auditoria {} | {} {} | usuario={}", a.getAccion(),
                a.getEntidad(), a.getEntidadId(), a.getUsuario());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<AuditoriaEventoDTO> buscar(String usuario, String accion,
            LocalDate desde, LocalDate hasta, int pagina, int tamanio) {
        if (pagina < 0 || tamanio < 1 || tamanio > 100) {
            throw new ReglaNegocioException("Pagina invalida o tamanio fuera de 1 a 100");
        }
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ReglaNegocioException("Rango de fechas invalido");
        }
        Page<AuditoriaEventoDTO> p = repository.buscar(
                vacioANulo(usuario), vacioANulo(accion),
                desde == null ? null : desde.atStartOfDay(),
                hasta == null ? null : hasta.plusDays(1).atStartOfDay(),
                PageRequest.of(pagina, tamanio,
                        Sort.by(Sort.Direction.DESC, "fecha").and(Sort.by("id")))
        ).map(AuditoriaEventoDTO::de);
        return new PaginaResponseDTO<>(p.getContent(), p.getNumber(), p.getSize(),
                p.getTotalElements(), p.getTotalPages(), p.isLast());
    }

    private static String vacioANulo(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
