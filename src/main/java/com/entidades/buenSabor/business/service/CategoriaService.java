package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.entities.Categoria;

public interface CategoriaService extends BaseService<Categoria, Long> {
    void deleteInSucursales(Long idCategoria, Long idSucursal);
}
