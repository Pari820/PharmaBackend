
package pe.edu.upeu.PharmaBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.upeu.PharmaBackend.dto.CategoriaRequestDTO;
import pe.edu.upeu.PharmaBackend.dto.CategoriaResponseDTO;
import pe.edu.upeu.PharmaBackend.entity.Categoria;
import pe.edu.upeu.PharmaBackend.exception.RecursosNoEncontradoException;
import pe.edu.upeu.PharmaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.PharmaBackend.repository.CategoriaRepository;
import pe.edu.upeu.PharmaBackend.service.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private static final Logger LOG =
            LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // CREAR CATEGORIA - INVALIDAR CACHE
    @Override
    @Transactional
    @CacheEvict(cacheNames = "categorias", allEntries = true)
    public CategoriaResponseDTO create(CategoriaRequestDTO t) {

        String nombre = t.getNombre().trim();

        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException(
                    "Ya existe una categoria con el nombre " + nombre
            );
        }

        Categoria categoria = new Categoria();

        categoria.setNombre(nombre);
        categoria.setDescripcion(t.getDescripcion());
        categoria.setEstado(t.getEstado());

        Categoria catCreada = categoriaRepository.save(categoria);

        return convertirResponse(catCreada);
    }

    // ACTUALIZAR - INVALIDAR CATEGORIAS Y PRODUCTOS
    @Override
    @Transactional
    @CacheEvict(
            cacheNames = {"categorias", "productos"},
            allEntries = true
    )
    public CategoriaResponseDTO update(
            Long aLong,
            CategoriaRequestDTO t) {

        Categoria categoria = categoriaRepository.findById(aLong)
                .orElseThrow(() ->
                        new RecursosNoEncontradoException(
                                "Categoria no encontrada con id: " + aLong
                        )
                );

        categoria.setNombre(t.getNombre());
        categoria.setDescripcion(t.getDescripcion());
        categoria.setEstado(t.getEstado());

        Categoria catActualizada =
                categoriaRepository.save(categoria);

        return convertirResponse(catActualizada);
    }

    // BUSCAR POR ID - GUARDAR EN REDIS
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "categorias",
            key = "'id-' + #p0"
    )
    public CategoriaResponseDTO read(Long aLong) {

        Categoria categoria = categoriaRepository.findById(aLong)
                .orElseThrow(() ->
                        new RecursosNoEncontradoException(
                                "Categoria no encontrada con id: " + aLong
                        )
                );

        return convertirResponse(categoria);
    }

    // ELIMINAR - INVALIDAR CACHE
    @Override
    @Transactional
    @CacheEvict(cacheNames = "categorias", allEntries = true)
    public void delete(Long aLong) {

        Categoria categoria = categoriaRepository.findById(aLong)
                .orElseThrow(() ->
                        new RecursosNoEncontradoException(
                                "Categoria no encontrada con id: " + aLong
                        )
                );

        categoriaRepository.delete(categoria);
    }

    // LISTAR TODAS - GUARDAR EN REDIS
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "categorias",
            key = "'todas'"
    )
    public Iterable<CategoriaResponseDTO> readAll() {

        long inicio = System.currentTimeMillis();

        LOG.info("Inicio listar categorias");

        try {
            return categoriaRepository.findAll()
                    .stream()
                    .map(this::convertirResponse)
                    .toList();
        } finally {
            LOG.info(
                    "Fin listar categorias duracionMs={}",
                    System.currentTimeMillis() - inicio
            );
        }
    }

    // CONVERTIR ENTIDAD A DTO
    private CategoriaResponseDTO convertirResponse(Categoria categoria) {

        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getEstado(),
                categoria.getFechaCreacion(),
                categoria.getFechaModificacion()
        );
    }
}
