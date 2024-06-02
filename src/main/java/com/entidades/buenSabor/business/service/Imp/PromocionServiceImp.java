package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.ImagenPromocionService;
import com.entidades.buenSabor.business.service.PromocionDetalleService;
import com.entidades.buenSabor.business.service.PromocionService;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.entities.ImagenPromocion;
import com.entidades.buenSabor.domain.entities.Promocion;
import com.entidades.buenSabor.domain.entities.PromocionDetalle;
import com.entidades.buenSabor.domain.entities.Sucursal;
import com.entidades.buenSabor.repositories.PromocionDetalleRepository;
import com.entidades.buenSabor.repositories.PromocionRepository;
import com.entidades.buenSabor.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class PromocionServiceImp extends BaseServiceImp<Promocion, Long> implements PromocionService {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private PromocionDetalleService promocionDetalleService;

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private ImagenPromocionService imagenPromocionService;

    @Autowired
    private SucursalService sucursalService;

    @Override
    public Promocion create(Promocion promocion) {

        Set<Sucursal> sucursales = new HashSet<>();
        Sucursal sucursalAux;
        Set<PromocionDetalle> promocionDetalles = new HashSet<>();

        //Añadir Sucursales
        if(!promocion.getSucursales().isEmpty() && promocion.getSucursales() != null){
            for(Sucursal sucursal : promocion.getSucursales()){
                sucursalAux = this.sucursalRepository.findById(sucursal.getId())
                        .orElseThrow(() -> new RuntimeException("La sucursal id: " + sucursal.getId() + " no exite."));
                sucursalAux.getPromociones().add(promocion);
                sucursales.add(sucursalAux);
            }

            promocion.setSucursales(sucursales);
        }

        //Añadir detalles de la promocion
        if(!promocion.getPromocionDetalles().isEmpty() && promocion.getPromocionDetalles() != null){
            for(PromocionDetalle detalle : promocion.getPromocionDetalles()){
                if(detalle != null){
                    promocionDetalles.add(detalle);
                }else{
                    throw new RuntimeException("El detalle de la promocion es null.");
                }
            }

            promocion.setPromocionDetalles(promocionDetalles);
        }

        return super.create(promocion);
    }


    @Override
    public Promocion update(Promocion newPromocion, Long id) {
        //Verificar que la promocion exista
        Promocion promocionExistente = this.promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La promocion id: " + id + " no se encontro."));

        // Actualizar detalles básicos de la promoción
        promocionExistente.setDenominacion(newPromocion.getDenominacion());
        promocionExistente.setFechaDesde(newPromocion.getFechaDesde());
        promocionExistente.setFechaHasta(newPromocion.getFechaHasta());
        promocionExistente.setHoraDesde(newPromocion.getHoraDesde());
        promocionExistente.setHoraHasta(newPromocion.getHoraHasta());
        promocionExistente.setDescripcionDescuento(newPromocion.getDescripcionDescuento());
        promocionExistente.setPrecioPromocional(newPromocion.getPrecioPromocional());
        promocionExistente.setTipoPromocion(newPromocion.getTipoPromocion());

        //Actualizar imagenes
        Set<ImagenPromocion> imagenes = newPromocion.getImagenes();
        Set<ImagenPromocion> imagenesEliminadas = new HashSet<>();

        for (ImagenPromocion imagenExistente : promocionExistente.getImagenes()) {
            if (!imagenes.contains(imagenExistente)) {
                imagenesEliminadas.add(imagenExistente);
            }
        }

        promocionExistente.getImagenes().removeAll(imagenesEliminadas);

        //Eliminar las imagenes eliminadas
        for(ImagenPromocion imagen : imagenesEliminadas){
            this.imagenPromocionService.deleteById(imagen.getId());
        }

        // Actualizar sucursales
        Set<Sucursal> sucursalesActualizadas = new HashSet<>();
        if (newPromocion.getSucursales() != null && !newPromocion.getSucursales().isEmpty()) {
            for (Sucursal sucursal : newPromocion.getSucursales()) {
                Sucursal sucursalBd = this.sucursalService.getById(sucursal.getId());
                if (sucursalBd == null) {
                    throw new RuntimeException("La sucursal con el id " + sucursal.getId() + " no existe.");
                }
                sucursalBd.getPromociones().add(promocionExistente);
                sucursalesActualizadas.add(sucursalBd);
            }
        }

        // Eliminar las sucursales que ya no están asociadas a la promoción
        for (Sucursal sucursalExistente : promocionExistente.getSucursales()) {
            if (!sucursalesActualizadas.contains(sucursalExistente)) {
                sucursalExistente.getPromociones().remove(promocionExistente);
            }
        }

        promocionExistente.setSucursales(sucursalesActualizadas);

        //Actualizar detalles de la promocion
        Set<PromocionDetalle> detalles = newPromocion.getPromocionDetalles();
        Set<PromocionDetalle> detallesEliminados = new HashSet<>();

        for (PromocionDetalle detalleExistente : promocionExistente.getPromocionDetalles()) {
            if (!detalles.contains(detalleExistente)) {
                detallesEliminados.add(detalleExistente);
            }
        }

        promocionExistente.getPromocionDetalles().removeAll(detallesEliminados);

        //Eliminar las detalles eliminados
        for(PromocionDetalle detalle : detallesEliminados){
            this.promocionDetalleService.deleteById(detalle.getId());
        }

        return super.update(newPromocion, id);
    }


    @Override
    public void deleteById(Long id) {
        Promocion promocion = this.promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La promocion id: " + id + " no existe."));

        //Eliminar detalles
        for(PromocionDetalle detalle : promocion.getPromocionDetalles()){
            this.promocionDetalleService.deleteById(detalle.getId());
        }

        //Eliminar imagenes
        for(ImagenPromocion imagen : promocion.getImagenes()){
            this.imagenPromocionService.deleteById(imagen.getId());
        }

        //Quitar relacion con sucursales
        promocion.getSucursales().removeAll(promocion.getSucursales());

        super.deleteById(id);
    }
}
