package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.PedidoFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.PedidoService;
import com.entidades.buenSabor.business.service.ProvinciaService;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.dto.PedidoGetDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoFacadeImp extends BaseFacadeImp<Pedido, PedidoDto, PedidoGetDto, Long> implements PedidoFacade {
    public PedidoFacadeImp(BaseService<Pedido,Long> baseService, BaseMapper<Pedido,PedidoDto,PedidoGetDto> baseMapper){
        super(baseService, baseMapper);
    }
    @Autowired
    PedidoService pedidoService;

    public PedidoGetDto creation(PedidoDto pedido){
        return baseMapper.toDTO(pedidoService.create(pedido));
    }
}