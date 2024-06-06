package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido,Long>{
    @Query(value = "SELECT p FROM Pedido p ORDER BY p.id DESC LIMIT 1")
    Pedido findLastPedido();
}
