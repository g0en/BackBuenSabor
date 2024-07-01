package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.ArticuloManufacturado;
import com.entidades.buenSabor.domain.entities.Promocion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromocionRepository extends BaseRepository<Promocion,Long>{
    @Query("SELECT p FROM Promocion p JOIN p.sucursales s WHERE s.id = :idSucursal")
    List<Promocion> findAllWithSucursales(@Param("idSucursal") Long idSucursal);

    @Query("SELECT p FROM Promocion p JOIN p.promocionDetalles det WHERE det.articulo.id = :idInsumo")
    List<Promocion> findByArticuloInsumoId(@Param("idInsumo") Long idInsumo);

    @Query("SELECT p FROM Promocion p JOIN p.promocionDetalles det WHERE det.articulo.id = :idManufacturado")
    List<Promocion> findByArticuloManufacturadoId(@Param("idManufacturado") Long idManufacturado);

}
