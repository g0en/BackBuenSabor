package com.entidades.buenSabor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ArticuloDto extends BaseDto{
    protected String denominacion;
    protected Double precioVenta;
    protected Set<ImagenArticuloDto> imagenes = new HashSet<>();
    protected UnidadMedidaDto unidadMedida;
    protected CategoriaShortDto categoria;
}
