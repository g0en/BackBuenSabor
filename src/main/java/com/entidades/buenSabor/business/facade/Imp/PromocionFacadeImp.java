package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.PromocionDto;
import com.entidades.buenSabor.domain.entities.Promocion;
import org.springframework.stereotype.Service;

@Service
public class PromocionFacadeImp extends BaseFacadeImp<Promocion, PromocionDto, PromocionDto, Long> {
    public PromocionFacadeImp(BaseService<Promocion, Long> baseService, BaseMapper<Promocion,PromocionDto,PromocionDto> baseMapper){
        super(baseService, baseMapper);
    }
}
