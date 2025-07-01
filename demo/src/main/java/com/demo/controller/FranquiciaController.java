package com.demo.controller;

import com.demo.entity.Franquicia;
import com.demo.entity.Sucursal;
import com.demo.entity.Producto;
import com.demo.repository.FranquiciaRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/franquicia")
public class FranquiciaController {

    @Autowired
    private FranquiciaRepository repository;

    @PostMapping
    public Franquicia create(@RequestBody Franquicia franquicia) {
        return repository.saveFranquicia(franquicia);
    }

    @PostMapping("/sucursal")
    public Franquicia addSucursalesByName(@RequestBody AddSucursalByNameRequest request) {
        return repository.addSucursalesToFranquiciaByName(request.getNombreFranquicia(), request.getSucursales());
    }

    @PostMapping("/sucursal/producto")
    public Franquicia addProductosByName(@RequestBody AddProductoByNameRequest request) {
        return repository.addProductosToSucursalByName(
                request.getNombreFranquicia(),
                request.getNombreSucursal(),
                request.getProductos());
    }

    @DeleteMapping("/sucursal/producto/delete")
    public Franquicia deleteProductoByName(@RequestBody DeleteProductoByNameRequest request) {
        return repository.deleteProductoFromSucursalByName(
                request.getNombreFranquicia(),
                request.getNombreSucursal(),
                request.getNombreProducto());
    }

    @PatchMapping("/nombre")
    public Franquicia updateFranquiciaName(@RequestBody UpdateFranquiciaNombreRequest request) {
        return repository.updateFranquiciaNameById(request.getIdFranquicia(), request.getNombre());
    }

    @PatchMapping("/sucursal/nombre")
    public Franquicia updateSucursalNameSimple(@RequestBody UpdateSucursalNombreRequest request) {
        return repository.updateSucursalNameSimple(request.getIdSucursal(), request.getNombre());
    }

    @PatchMapping("/sucursal/producto/nombre")
    public Franquicia updateProductoNameSimple(@RequestBody UpdateProductoNombreSimpleRequest request) {
        return repository.updateProductoNameSimple(request.getIdProducto(), request.getNombre());
    }

    @PatchMapping("/sucursal/producto/cantidad")
    public Franquicia updateProductoCantidadSimple(@RequestBody UpdateProductoCantidadSimpleRequest request) {
        return repository.updateProductoCantidadSimple(request.getIdProducto(), request.getCantidad());
    }

    @GetMapping("/max-stock")
    public List<FranquiciaRepository.MaxStockResponse> getMaxStockPorSucursal(
            @RequestParam String nombreFranquicia) {
        return repository.getMaxStockProductosByFranquicia(nombreFranquicia);
    }

    @Data static class AddSucursalByNameRequest {
        private String nombreFranquicia;
        private List<Sucursal> sucursales;
    }

    @Data static class AddProductoByNameRequest {
        private String nombreFranquicia;
        private String nombreSucursal;
        private List<Producto> productos;
    }

    @Data static class DeleteProductoByNameRequest {
        private String nombreFranquicia;
        private String nombreSucursal;
        private String nombreProducto;
    }

    @Data static class UpdateFranquiciaNombreRequest {
        private Long idFranquicia;
        private String nombre;
    }

    @Data static class UpdateSucursalNombreRequest {
        private Long idSucursal;
        private String nombre;
    }

    @Data static class UpdateProductoNombreSimpleRequest {
        private Long idProducto;
        private String nombre;
    }

    @Data static class UpdateProductoCantidadSimpleRequest {
        private Long idProducto;
        private Integer cantidad;
    }
}
