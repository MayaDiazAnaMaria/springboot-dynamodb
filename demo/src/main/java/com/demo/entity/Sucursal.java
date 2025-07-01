package com.demo.entity;

import lombok.Data;
import java.util.List;

@Data
public class Sucursal {
    private Long idSucursal;
    private String nombre;
    private List<Producto> productos;
}
