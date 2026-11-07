package pe.edu.upeu.PharmaBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.upeu.PharmaBackend.dto.ProductoRequestDTO;
import pe.edu.upeu.PharmaBackend.dto.ProductoResponseDTO;
import pe.edu.upeu.PharmaBackend.entity.Categoria;
import pe.edu.upeu.PharmaBackend.entity.Producto;
import pe.edu.upeu.PharmaBackend.exception.RecursosNoEncontradoException;
import pe.edu.upeu.PharmaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.PharmaBackend.repository.CategoriaRepository;
import pe.edu.upeu.PharmaBackend.repository.ProductoRepository;
import pe.edu.upeu.PharmaBackend.service.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger LOG =
            LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository) {

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // CREAR PRODUCTO - INVALIDAR CACHE
    @Override
    @Transactional
    @CacheEvict(
            cacheNames = "productos",
            allEntries = true
    )
    public ProductoResponseDTO create(ProductoRequestDTO t) {

        String nombre = t.getNombre().trim();

        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException(
                    "Ya existe un producto con el nombre " + nombre
            );
        }

        Categoria categoria = buscarCategoria(t.getCategoriaId());

        Producto producto = new Producto();

        producto.setNombre(nombre);
        producto.setPrecio(t.getPrecio());
        producto.setStock(t.getStock());
        producto.setEstado(t.getEstado());
        producto.setCategoria(categoria);

        Producto prodCreado = productoRepository.save(producto);

        return convertirResponse(prodCreado);
    }

    // ACTUALIZAR PRODUCTO - INVALIDAR CACHE
    @Override
    @Transactional
    @CacheEvict(
            cacheNames = "productos",
            allEntries = true
    )
    public ProductoResponseDTO update(
            Long aLong,
            ProductoRequestDTO t) {

        Producto producto = productoRepository.findById(aLong)
                .orElseThrow(() ->
                        new RecursosNoEncontradoException(
                                "Producto no encontrado con id: " + aLong
                        )
                );

        String nombre = t.getNombre().trim();

        if (productoRepository.existsByNombreIgnoreCaseAndIdNot(
                nombre, aLong)) {

            throw new ReglaNegocioException(
                    "Ya existe un producto con el nombre " + nombre
            );
        }

        Categoria categoria = buscarCategoria(t.getCategoriaId());

        producto.setNombre(nombre);
        producto.setPrecio(t.getPrecio());
        producto.setStock(t.getStock());
        producto.setEstado(t.getEstado());
        producto.setCategoria(categoria);

        Producto prodActualizado =
                productoRepository.saveAndFlush(producto);

        return convertirResponse(prodActualizado);
    }

    // BUSCAR PRODUCTO POR ID - CACHE REDIS
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "productos",
            key = "'id-' + #p0"
    )
    public ProductoResponseDTO read(Long aLong) {

        Producto producto = productoRepository.findById(aLong)
                .orElseThrow(() ->
                        new RecursosNoEncontradoException(
                                "Producto no encontrado con id: " + aLong
                        )
                );

        return convertirResponse(producto);
    }

    // ELIMINAR PRODUCTO - INVALIDAR CACHE
    @Override
    @Transactional
    @CacheEvict(
            cacheNames = "productos",
            allEntries = true
    )
    public void delete(Long aLong) {

        Producto producto = productoRepository.findById(aLong)
                .orElseThrow(() ->
                        new RecursosNoEncontradoException(
                                "Producto no encontrado con id: " + aLong
                        )
                );

        productoRepository.delete(producto);
    }

    // LISTAR PRODUCTOS - CACHE REDIS
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "productos",
            key = "'todas'"
    )
    public Iterable<ProductoResponseDTO> readAll() {

        long inicio = System.currentTimeMillis();

        LOG.info("Inicio listar productos");

        try {
            return productoRepository.findAll()
                    .stream()
                    .map(this::convertirResponse)
                    .toList();
        } finally {
            LOG.info(
                    "Fin listar productos duracionMs={}",
                    System.currentTimeMillis() - inicio
            );
        }
    }

    // BUSCAR CATEGORIA ASOCIADA
    private Categoria buscarCategoria(Long categoriaId) {

        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() ->
                        new RecursosNoEncontradoException(
                                "Categoria no encontrada con id: " + categoriaId
                        )
                );
    }

    // CONVERTIR ENTIDAD A DTO
    private ProductoResponseDTO convertirResponse(Producto producto) {

        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getEstado(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getFechaCreacion(),
                producto.getFechaModificacion()
        );
    }
}
