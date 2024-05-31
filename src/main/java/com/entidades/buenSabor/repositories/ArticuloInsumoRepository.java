package com.entidades.buenSabor.repositories;


import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.Categoria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloInsumoRepository extends BaseRepository<ArticuloInsumo,Long> {
    List<ArticuloInsumo> getByCategoria(Categoria categoria);
}
