package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.ArticuloInsumoFacadeImp;
import com.entidades.buenSabor.business.facade.Imp.DomicilioFacadeImp;
import com.entidades.buenSabor.domain.dto.ArticuloInsumoDto;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articuloInsumo")
@CrossOrigin("*")
public class ArticuloInsumoController extends BaseControllerImp<ArticuloInsumo, ArticuloInsumoDto, ArticuloInsumoDto, Long, ArticuloInsumoFacadeImp> {
    public ArticuloInsumoController(ArticuloInsumoFacadeImp facade) {
        super(facade);
    }

    @GetMapping("/findBySucursal/{idSucursal}")
    public ResponseEntity<?> findBySucursales(@PathVariable("idSucursal") Long idSucursal) {
        return ResponseEntity.ok(facade.findBySucursales(idSucursal));
    }

    @GetMapping("/paraVenta/{idSucursal}")
    public ResponseEntity<?> paraVenta(@PathVariable("idSucursal") Long idSucursal) {
        return ResponseEntity.ok(facade.paraVenta(idSucursal));
    }

    @DeleteMapping("/baja/{idInusmo}")
    public ResponseEntity<?> bajaInsumo(@PathVariable("idInusmo") Long idInusmo) {
        return ResponseEntity.ok(facade.bajaInsumo(idInusmo));
    }
}