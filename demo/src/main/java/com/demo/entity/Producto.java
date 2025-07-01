package com.demo.entity;

import lombok.Data;

@Data
public class Producto {
    private Long idProducto;
    private String nombre;
    private Integer cantidad;
}
