package pe.edu.upeu.PharmaBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.PharmaBackend.dto.ClienteRequestDTO;
import pe.edu.upeu.PharmaBackend.dto.ClienteResponseDTO;
import pe.edu.upeu.PharmaBackend.dto.PaginaResponseDTO;
import pe.edu.upeu.PharmaBackend.entity.Cliente;
import pe.edu.upeu.PharmaBackend.exception.RecursosNoEncontradoException;
import pe.edu.upeu.PharmaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.PharmaBackend.repository.ClienteRepository;
import pe.edu.upeu.PharmaBackend.service.service.ClienteService;

import java.util.List;
import java.util.Set;

@Service
public class ClienteServiceImpl implements ClienteService {

    private static final Logger log =
            LoggerFactory.getLogger(ClienteServiceImpl.class);

    private static final Set<String> CAMPOS_ORDEN_VALIDOS =
            Set.of("id", "dni", "nombres", "apellidos", "email");

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteRequestDTO request) {

        log.info("Registrando cliente con DNI={}", request.getDni());

        String dni = request.getDni().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (clienteRepository.existsByDni(dni)) {
            throw new ReglaNegocioException(
                    "Ya existe un cliente con el DNI: " + dni
            );
        }

        if (clienteRepository.existsByEmailIgnoreCase(email)) {
            throw new ReglaNegocioException(
                    "Ya existe un cliente con el correo: " + email
            );
        }

        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        cliente.setNombres(request.getNombres().trim());
        cliente.setApellidos(request.getApellidos().trim());
        cliente.setEmail(email);
        cliente.setTelefono(normalizar(request.getTelefono()));
        cliente.setDireccion(normalizar(request.getDireccion()));
        cliente.setEstado(request.getEstado());

        Cliente guardado = clienteRepository.save(cliente);

        log.info("Cliente registrado correctamente id={}", guardado.getId());
        return convertirResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO read(Long id) {

        log.info("Buscando cliente id={}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursosNoEncontradoException(
                        "Cliente no encontrado con id: " + id
                ));

        return convertirResponse(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> readAll() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<ClienteResponseDTO> listar(
            int pagina,
            int tamanio,
            String ordenarPor,
            String direccion) {

        if (pagina < 0) {
            throw new ReglaNegocioException("La página no puede ser negativa");
        }

        if (tamanio <= 0) {
            throw new ReglaNegocioException("El tamaño de página debe ser mayor que cero");
        }

        String campoOrden = ordenarPor == null
                ? "apellidos"
                : ordenarPor.trim().toLowerCase();

        if (!CAMPOS_ORDEN_VALIDOS.contains(campoOrden)) {
            throw new ReglaNegocioException(
                    "El campo ordenarPor no es válido. Use id, dni, nombres, apellidos o email"
            );
        }

        String dir = direccion == null
                ? "asc"
                : direccion.trim().toLowerCase();

        if (!dir.equals("asc") && !dir.equals("desc")) {
            throw new ReglaNegocioException(
                    "La dirección debe ser asc o desc"
            );
        }

        Sort sort = dir.equals("desc")
                ? Sort.by(campoOrden).descending()
                : Sort.by(campoOrden).ascending();

        Pageable pageable = PageRequest.of(pagina, tamanio, sort);
        Page<Cliente> resultado = clienteRepository.findAll(pageable);

        List<ClienteResponseDTO> contenido = resultado.getContent()
                .stream()
                .map(this::convertirResponse)
                .toList();

        return new PaginaResponseDTO<>(
                contenido,
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.isLast()
        );
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(Long id, ClienteRequestDTO request) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursosNoEncontradoException(
                        "Cliente no encontrado con id: " + id
                ));

        String dni = request.getDni().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (clienteRepository.existsByDniAndIdNot(dni, id)) {
            throw new ReglaNegocioException(
                    "Ya existe otro cliente con el DNI: " + dni
            );
        }

        if (clienteRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ReglaNegocioException(
                    "Ya existe otro cliente con el correo: " + email
            );
        }

        cliente.setDni(dni);
        cliente.setNombres(request.getNombres().trim());
        cliente.setApellidos(request.getApellidos().trim());
        cliente.setEmail(email);
        cliente.setTelefono(normalizar(request.getTelefono()));
        cliente.setDireccion(normalizar(request.getDireccion()));
        cliente.setEstado(request.getEstado());

        Cliente actualizado = clienteRepository.save(cliente);

        log.info("Cliente id={} actualizado correctamente", id);
        return convertirResponse(actualizado);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursosNoEncontradoException(
                        "Cliente no encontrado con id: " + id
                ));

        if (Boolean.FALSE.equals(cliente.getEstado())) {
            throw new ReglaNegocioException(
                    "El cliente ya se encuentra inactivo"
            );
        }

        cliente.setEstado(false);
        clienteRepository.save(cliente);

        log.info("Cliente id={} dado de baja correctamente", id);
    }

    private ClienteResponseDTO convertirResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getDni(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getEstado(),
                cliente.getFechaCreacion(),
                cliente.getFechaModificacion()
        );
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
