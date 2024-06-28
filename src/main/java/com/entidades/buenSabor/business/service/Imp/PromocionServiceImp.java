package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.ImagenPromocionService;
import com.entidades.buenSabor.business.service.PromocionDetalleService;
import com.entidades.buenSabor.business.service.PromocionService;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.dto.PromocionDto;
import com.entidades.buenSabor.domain.dto.SucursalShortDto;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
import java.util.List;

@Service
public class PromocionServiceImp extends BaseServiceImp<Promocion, Long> implements PromocionService {

    @Autowired
    ArticuloRepository articuloRepository;

    @Autowired
    ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    PromocionDetalleRepository promocionDetalleRepository;

    @Autowired
    PromocionRepository promocionRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    private ImagenPromocionService imagenPromocionService;

    @Transactional
    public Promocion create(Promocion promocion) {
        Set<Sucursal> sucursales = new HashSet<>();

        for (Sucursal sucursal : promocion.getSucursales()) {
            Sucursal sucursalBd = sucursalRepository.findById(sucursal.getId())
                    .orElseThrow(() -> new RuntimeException("La sucursal con id " + sucursal.getId() + " no se ha encontrado"));

            sucursalBd.getPromociones().add(promocion);
            sucursales.add(sucursalBd);
        }
        Promocion nuevaPromocion = createPromocion(promocion, sucursales);
        return promocionRepository.save(nuevaPromocion);
    }

    private Promocion createPromocion(Promocion promocion, Set<Sucursal> sucursales) {
        Promocion nuevaPromocion = new Promocion();
        nuevaPromocion.setSucursales(sucursales);
        nuevaPromocion.setTipoPromocion(promocion.getTipoPromocion());
        nuevaPromocion.setPrecioPromocional(promocion.getPrecioPromocional());
        nuevaPromocion.setDenominacion(promocion.getDenominacion());
        nuevaPromocion.setFechaDesde(promocion.getFechaDesde());
        nuevaPromocion.setFechaHasta(promocion.getFechaHasta());
        nuevaPromocion.setHoraDesde(promocion.getHoraDesde());
        nuevaPromocion.setHoraHasta(promocion.getHoraHasta());
        nuevaPromocion.setDescripcionDescuento(promocion.getDescripcionDescuento());

        nuevaPromocion.setImagenes(new HashSet<>(promocion.getImagenes()));

        Set<PromocionDetalle> detalles = promocion.getPromocionDetalles();
        Set<PromocionDetalle> nuevosDetalles = new HashSet<>();

        //Ingresar Articulos Manufacturados a detalles
        if (detalles != null && !detalles.isEmpty()) {
            for (PromocionDetalle detalle : detalles) {
                Articulo articuloExistente = articuloRepository.findById(detalle.getArticulo().getId())
                        .orElseThrow(() -> new RuntimeException("Uno de los artículos enviados no es válido."));
                if (articuloExistente instanceof ArticuloInsumo)
                    articuloExistente = articuloInsumoRepository.findById(detalle.getArticulo().getId())
                            .orElseThrow(() -> new RuntimeException("No se encontro el insumo."));
                else if (articuloExistente instanceof ArticuloManufacturado)
                    articuloExistente = articuloManufacturadoRepository.findById(detalle.getArticulo().getId())
                            .orElseThrow(() -> new RuntimeException("No se encontro el manufacturado."));
                else
                    throw new RuntimeException("El artículo " + detalle.getArticulo().getDenominacion() + " no se ha encontrado.");

                if (articuloExistente == null) {
                    throw new RuntimeException("El artículo " + detalle.getArticulo().getDenominacion() + " no se ha encontrado.");
                }
                PromocionDetalle nuevoDetalle = new PromocionDetalle();
                nuevoDetalle.setCantidad(detalle.getCantidad());
                nuevoDetalle.setArticulo(articuloExistente);
                nuevosDetalles.add(nuevoDetalle);
            }
            nuevaPromocion.setPromocionDetalles(nuevosDetalles);
        } else {
            throw new RuntimeException("La promoción debe tener al menos un detalle.");
        }

        return nuevaPromocion;
    }

    @Transactional
    @Override
    public Promocion update(Promocion promocion, Long id) {
        Promocion promocionExistente = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La promoción con id " + id + " no se ha encontrado"));

        updateImagenes(promocion, promocionExistente);

        // Eliminar detalles antiguos
        promocionDetalleRepository.deleteAll(promocionExistente.getPromocionDetalles());

        Set<PromocionDetalle> nuevosDetalles = promocion.getPromocionDetalles();
        if (nuevosDetalles == null || nuevosDetalles.isEmpty()) {
            throw new RuntimeException("La promoción debe tener al menos un detalle.");
        }

        Set<PromocionDetalle> detallesActualizados = new HashSet<>();
        for (PromocionDetalle detalle : nuevosDetalles) {
            Articulo articulo = articuloRepository.findById(detalle.getArticulo().getId())
                    .orElseThrow(() -> new RuntimeException("El artículo con id " + detalle.getArticulo().getId() + " no se ha encontrado"));
            PromocionDetalle nuevoDetalle = new PromocionDetalle();
            nuevoDetalle.setCantidad(detalle.getCantidad());
            nuevoDetalle.setArticulo(articulo);
            detallesActualizados.add(nuevoDetalle);
        }

        promocion.setPromocionDetalles(detallesActualizados);

        return promocionRepository.save(promocion);
    }

    private void updateImagenes(Promocion promocion, Promocion promocionExistente) {
        Set<ImagenPromocion> imagenes = promocion.getImagenes();
        Set<ImagenPromocion> imagenesEliminadas = new HashSet<>(promocionExistente.getImagenes());
        imagenesEliminadas.removeAll(imagenes);

        for (ImagenPromocion imagen : imagenesEliminadas) {
            String publicId = imagen.getUrl().substring(imagen.getUrl().lastIndexOf("/") + 1);
            imagenPromocionService.deleteImage(publicId, imagen.getId().toString());
        }

        promocionExistente.setImagenes(imagenes);
    }

    @Override
    public List<Promocion> duplicateInOtherSucursales(Long id, Set<SucursalShortDto> sucursales) {
        return null;
    }

    @Override
    public List<Promocion> findBySucursal(Long idSucursal) {
        return this.promocionRepository.findAllWithSucursales(idSucursal);
    }
}
