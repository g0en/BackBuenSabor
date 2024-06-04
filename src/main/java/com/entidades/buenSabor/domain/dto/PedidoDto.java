package com.entidades.buenSabor.domain.dto;

import com.entidades.buenSabor.domain.enums.FormaPago;
import com.entidades.buenSabor.domain.enums.TipoEnvio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoDto extends BaseDto{
    private Double total;
    private TipoEnvio tipoEnvio;
    private FormaPago formaPago;

    // private DomicilioDto domicilio;
    private SucursalShortDto sucursal;

    // Factura, cliente
    private Set<PedidoDetalleDto> pedidoDetalles = new HashSet<>();
    //private EmpleadoDto empleado;
}
