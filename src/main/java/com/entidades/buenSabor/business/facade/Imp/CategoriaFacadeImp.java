package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.CategoriaFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.CategoriaService;
import com.entidades.buenSabor.domain.dto.CategoriaDto;
import com.entidades.buenSabor.domain.dto.CategoriaGetDto;
import com.entidades.buenSabor.domain.entities.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaFacadeImp extends BaseFacadeImp<Categoria, CategoriaDto, CategoriaGetDto, Long> implements CategoriaFacade {
    public CategoriaFacadeImp(BaseService<Categoria, Long> baseService, BaseMapper<Categoria, CategoriaDto, CategoriaGetDto> baseMapper){
        super(baseService, baseMapper);
    }

    @Autowired
    private CategoriaService categoriaService;

    public void deleteInSucursales (Long idCategoria, Long idSucursal) {
        this.categoriaService.deleteInSucursales(idCategoria, idSucursal);
    }
}
