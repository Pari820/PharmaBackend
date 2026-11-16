package pe.edu.upeu.PharmaBackend.service.service;

import pe.edu.upeu.PharmaBackend.dto.ProductoRequestDTO;
import pe.edu.upeu.PharmaBackend.dto.ProductoResponseDTO;
import pe.edu.upeu.PharmaBackend.service.generic.CrudService;

public interface ProductoService extends CrudService<ProductoRequestDTO, ProductoResponseDTO, Long> {

    pe.edu.upeu.PharmaBackend.dto.PaginaResponseDTO<
        pe.edu.upeu.PharmaBackend.dto.ProductoResponseDTO
    > listarPaginado(
        int pagina,
        int tamanio,
        String ordenarPor,
        String direccion
    );
}
