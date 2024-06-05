package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.entities.Pedido;

public interface PedidoService extends BaseService<Pedido, Long> {

    Pedido create(PedidoDto pedido);
}
