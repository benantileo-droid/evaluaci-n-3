package com.example.productos;

import com.example.productos.model.Producto;
import com.example.productos.repository.ProductoRepository;
import com.example.productos.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @Test
    void deberiaRetornarListaDeProductos() {

        Producto p1 = new Producto(1L, "Laptop", 999.99, 10);
        Producto p2 = new Producto(2L, "Mouse", 29.99, 50);

        Mockito.when(repository.findAll())
                .thenReturn(List.of(p1, p2));

        List<Producto> resultado = service.listar();

        assertEquals(2, resultado.size());
        assertEquals("Laptop", resultado.get(0).getNombre());

        verify(repository).findAll();
    }

    @Test
    void deberiaRetornarProductoCuandoExiste() {

        Producto producto = new Producto(1L, "Laptop", 999.99, 10);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(producto));

        Optional<Producto> resultado = service.obtener(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Laptop", resultado.get().getNombre());
        assertEquals(999.99, resultado.get().getPrecio());

        verify(repository).findById(1L);
    }

    @Test
    void deberiaRetornarVacioCuandoProductoNoExiste() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Producto> resultado = service.obtener(99L);

        assertFalse(resultado.isPresent());

        verify(repository).findById(99L);
    }

    @Test
    void deberiaDescontarStockCorrectamente() {

        Producto producto = new Producto(1L, "Laptop", 999.99, 10);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(producto));

        Mockito.when(repository.save(any(Producto.class)))
                .thenReturn(producto);

        boolean resultado = service.descontarStock(1L, 3);

        assertTrue(resultado);
        assertEquals(7, producto.getStock());

        verify(repository).findById(1L);
        verify(repository).save(producto);
    }

    @Test
    void deberiaRetornarFalseCuandoStockInsuficiente() {

        Producto producto = new Producto(1L, "Laptop", 999.99, 2);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(producto));

        boolean resultado = service.descontarStock(1L, 5);

        assertFalse(resultado);

        verify(repository).findById(1L);
    }

    @Test
    void deberiaRetornarFalseCuandoProductoNoExisteAlDescontar() {

        Mockito.when(repository.findById(99L))
                .thenReturn(Optional.empty());

        boolean resultado = service.descontarStock(99L, 1);

        assertFalse(resultado);

        verify(repository).findById(99L);
    }
}
