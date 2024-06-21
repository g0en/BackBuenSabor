package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.CategoriaFacadeImp;
import com.entidades.buenSabor.domain.dto.CategoriaDto;
import com.entidades.buenSabor.domain.dto.CategoriaGetDto;
import com.entidades.buenSabor.domain.entities.Categoria;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
@CrossOrigin("*")
public class CategoriaController extends BaseControllerImp<Categoria, CategoriaDto, CategoriaGetDto, Long, CategoriaFacadeImp> {
    public CategoriaController(CategoriaFacadeImp facade) {
        super(facade);
    }

    @DeleteMapping("/baja/{idCategoria}/{idSucursal}")
    public void deleteInSucursales(@PathVariable("idCategoria") Long idCategoria, @PathVariable("idSucursal") Long idSucursal){
        facade.deleteInSucursales(idCategoria, idSucursal);
    }

    @GetMapping("/findByEmpresa/{idEmpresa}")
    public ResponseEntity<?> findByEmpresa(@PathVariable("idEmpresa") Long idEmpresa){
        return ResponseEntity.ok(facade.findByEmpresa(idEmpresa));
    }
}
