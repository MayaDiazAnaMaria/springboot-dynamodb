package com.demo.entity;

import lombok.Data;
import java.util.List;

@Data
public class Franquicia {
    private Long idFranquicia;
    private String nombre;
    private List<Sucursal> sucursales;
}
