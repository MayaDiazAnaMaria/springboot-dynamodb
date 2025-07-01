package com.demo.repository;

import com.demo.entity.Franquicia;
import com.demo.entity.Sucursal;
import com.demo.entity.Producto;
import com.demo.exception.ResourceNotFoundException;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Repository
public class FranquiciaRepository {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    private final String tableName = "franquicia";
    private final Gson gson = new Gson();

    public Franquicia saveFranquicia(Franquicia franquicia) {
        franquicia.setIdFranquicia(nextId("franquicia"));
        if (franquicia.getSucursales() != null) {
            for (Sucursal s : franquicia.getSucursales()) {
                s.setIdSucursal(nextId("sucursal"));
            }
        }
        putItem(franquicia);
        return franquicia;
    }

    public Franquicia addSucursalesToFranquiciaByName(String nombreFranquicia, List<Sucursal> sucursales) {
        Franquicia franquicia = findFranquiciaByName(nombreFranquicia);
        if (franquicia.getSucursales() == null) franquicia.setSucursales(new ArrayList<>());
        for (Sucursal s : sucursales) {
            s.setIdSucursal(nextId("sucursal"));
        }
        franquicia.getSucursales().addAll(sucursales);
        putItem(franquicia);
        return franquicia;
    }

    public Franquicia addProductosToSucursalByName(String nombreFranquicia, String nombreSucursal, List<Producto> productos) {
        Franquicia franquicia = findFranquiciaByName(nombreFranquicia);
        boolean found = false;
        if (franquicia.getSucursales() != null) {
            for (Sucursal s : franquicia.getSucursales()) {
                if (s.getNombre().equalsIgnoreCase(nombreSucursal)) {
                    found = true;
                    if (s.getProductos() == null) s.setProductos(new ArrayList<>());
                    for (Producto p : productos) {
                        p.setIdProducto(nextId("producto"));
                    }
                    s.getProductos().addAll(productos);
                    break;
                }
            }
        }
        if (!found) throw new ResourceNotFoundException("Sucursal con nombre '" + nombreSucursal + "' no encontrada en franquicia '" + nombreFranquicia + "'");
        putItem(franquicia);
        return franquicia;
    }

    public Franquicia deleteProductoFromSucursalByName(String nombreFranquicia, String nombreSucursal, String nombreProducto) {
        Franquicia franquicia = findFranquiciaByName(nombreFranquicia);
        boolean foundSucursal = false;
        if (franquicia.getSucursales() != null) {
            for (Sucursal s : franquicia.getSucursales()) {
                if (s.getNombre().equalsIgnoreCase(nombreSucursal)) {
                    foundSucursal = true;
                    if (s.getProductos() != null && s.getProductos().removeIf(p -> p.getNombre().equalsIgnoreCase(nombreProducto))) {
                        putItem(franquicia);
                        return franquicia;
                    } else {
                        throw new ResourceNotFoundException("Producto con nombre '" + nombreProducto + "' no encontrado en sucursal '" + nombreSucursal + "'");
                    }
                }
            }
        }
        if (!foundSucursal) throw new ResourceNotFoundException("Sucursal con nombre '" + nombreSucursal + "' no encontrada en franquicia '" + nombreFranquicia + "'");
        return franquicia;
    }

    public Franquicia updateFranquiciaNameById(Long idFranquicia, String nombre) {
        Franquicia franquicia = getFranquicia(idFranquicia);
        franquicia.setNombre(nombre);
        putItem(franquicia);
        return franquicia;
    }

    public Franquicia updateSucursalNameSimple(Long idSucursal, String nombre) {
        Franquicia franquicia = findFranquiciaBySucursalId(idSucursal);
        if (franquicia.getSucursales() != null) {
            for (Sucursal s : franquicia.getSucursales()) {
                if (s.getIdSucursal().equals(idSucursal)) {
                    s.setNombre(nombre);
                    break;
                }
            }
        }
        putItem(franquicia);
        return franquicia;
    }

    public Franquicia updateProductoNameSimple(Long idProducto, String nombre) {
        Franquicia franquicia = findFranquiciaByProductoId(idProducto);
        if (franquicia.getSucursales() != null) {
            for (Sucursal s : franquicia.getSucursales()) {
                if (s.getProductos() != null) {
                    for (Producto p : s.getProductos()) {
                        if (p.getIdProducto().equals(idProducto)) {
                            p.setNombre(nombre);
                            break;
                        }
                    }
                }
            }
        }
        putItem(franquicia);
        return franquicia;
    }

    public Franquicia updateProductoCantidadSimple(Long idProducto, Integer cantidad) {
        Franquicia franquicia = findFranquiciaByProductoId(idProducto);
        if (franquicia.getSucursales() != null) {
            for (Sucursal s : franquicia.getSucursales()) {
                if (s.getProductos() != null) {
                    for (Producto p : s.getProductos()) {
                        if (p.getIdProducto().equals(idProducto)) {
                            p.setCantidad(cantidad);
                            break;
                        }
                    }
                }
            }
        }
        putItem(franquicia);
        return franquicia;
    }

    public List<MaxStockResponse> getMaxStockProductosByFranquicia(String nombreFranquicia) {
        Franquicia franquicia = findFranquiciaByName(nombreFranquicia);
        List<MaxStockResponse> result = new ArrayList<>();
        if (franquicia.getSucursales() != null) {
            for (Sucursal s : franquicia.getSucursales()) {
                if (s.getProductos() != null && !s.getProductos().isEmpty()) {
                    List<Producto> productosConCantidad = s.getProductos().stream()
                            .filter(p -> p.getCantidad() != null)
                            .toList();
                    if (!productosConCantidad.isEmpty()) {
                        Producto maxProducto = Collections.max(productosConCantidad, Comparator.comparing(Producto::getCantidad));
                        result.add(new MaxStockResponse(s.getNombre(), maxProducto.getNombre(), maxProducto.getCantidad()));
                    }
                }
            }
        }
        return result;
    }

    @Data
    @AllArgsConstructor
    public static class MaxStockResponse {
        private String sucursal;
        private String producto;
        private Integer cantidad;
    }

    private Franquicia findFranquiciaByName(String nombreFranquicia) {
        ScanRequest request = ScanRequest.builder().tableName(tableName).build();
        ScanResponse response = dynamoDbClient.scan(request);
        for (Map<String, AttributeValue> item : response.items()) {
            Franquicia f = gson.fromJson(item.get("data").s(), Franquicia.class);
            if (f.getNombre().equalsIgnoreCase(nombreFranquicia)) {
                return f;
            }
        }
        throw new ResourceNotFoundException("Franquicia con nombre '" + nombreFranquicia + "' no encontrada");
    }

    private Franquicia getFranquicia(Long idFranquicia) {
        Map<String, AttributeValue> key = Map.of("id_franquicia", AttributeValue.builder().n(idFranquicia.toString()).build());
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName).key(key).build());
        if (response.hasItem()) {
            return gson.fromJson(response.item().get("data").s(), Franquicia.class);
        }
        throw new ResourceNotFoundException("Franquicia con ID " + idFranquicia + " no encontrada");
    }

    private Franquicia findFranquiciaBySucursalId(Long idSucursal) {
        ScanRequest request = ScanRequest.builder().tableName(tableName).build();
        ScanResponse response = dynamoDbClient.scan(request);
        for (Map<String, AttributeValue> item : response.items()) {
            Franquicia f = gson.fromJson(item.get("data").s(), Franquicia.class);
            if (f.getSucursales() != null) {
                for (Sucursal s : f.getSucursales()) {
                    if (s.getIdSucursal().equals(idSucursal)) return f;
                }
            }
        }
        throw new ResourceNotFoundException("Sucursal con ID " + idSucursal + " no encontrada");
    }

    private Franquicia findFranquiciaByProductoId(Long idProducto) {
        ScanRequest request = ScanRequest.builder().tableName(tableName).build();
        ScanResponse response = dynamoDbClient.scan(request);
        for (Map<String, AttributeValue> item : response.items()) {
            Franquicia f = gson.fromJson(item.get("data").s(), Franquicia.class);
            if (f.getSucursales() != null) {
                for (Sucursal s : f.getSucursales()) {
                    if (s.getProductos() != null) {
                        for (Producto p : s.getProductos()) {
                            if (p.getIdProducto().equals(idProducto)) return f;
                        }
                    }
                }
            }
        }
        throw new ResourceNotFoundException("Producto con ID " + idProducto + " no encontrado");
    }

    private void putItem(Franquicia franquicia) {
        Map<String, AttributeValue> item = Map.of(
                "id_franquicia", AttributeValue.builder().n(franquicia.getIdFranquicia().toString()).build(),
                "data", AttributeValue.builder().s(gson.toJson(franquicia)).build()
        );
        dynamoDbClient.putItem(PutItemRequest.builder().tableName(tableName).item(item).build());
    }

    private Long nextId(String entity) {
        Map<String, AttributeValue> key = Map.of("entity", AttributeValue.builder().s(entity).build());
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName("counters")
                .key(key)
                .updateExpression("ADD last_id :inc")
                .expressionAttributeValues(Map.of(":inc", AttributeValue.builder().n("1").build()))
                .returnValues(ReturnValue.UPDATED_NEW)
                .build();
        return Long.valueOf(dynamoDbClient.updateItem(request).attributes().get("last_id").n());
    }
}
