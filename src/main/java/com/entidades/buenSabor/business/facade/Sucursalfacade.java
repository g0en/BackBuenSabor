package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.SucursalDto;

import java.util.List;

public interface Sucursalfacade extends BaseFacade<SucursalDto, SucursalDto, Long> {
    List<SucursalDto> sucursalByEmpresaId(Long idEmpresa);
}
