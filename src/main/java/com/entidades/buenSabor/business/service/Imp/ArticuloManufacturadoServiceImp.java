package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.ArticuloInsumoService;
import com.entidades.buenSabor.business.service.ArticuloManufacturadoService;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.ArticuloManufacturadoRepository;
import com.entidades.buenSabor.repositories.CategoriaRepository;
import com.entidades.buenSabor.repositories.ImagenArticuloRepository;
import com.entidades.buenSabor.repositories.UnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ArticuloManufacturadoServiceImp extends BaseServiceImp<ArticuloManufacturado, Long> implements ArticuloManufacturadoService {

    @Autowired
    private ArticuloInsumoService articuloInsumoService;

    @Autowired
    private ImagenArticuloRepository imagenArticuloRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;


    @Override
    @Transactional
    public ArticuloManufacturado create(ArticuloManufacturado articuloManufacturado) {

        //Validar tiempo de estimacion
        if(articuloManufacturado.getTiempoEstimadoMinutos() < 0){
            throw new RuntimeException("No se admite tiempo negativo");
        }else if(articuloManufacturado.getTiempoEstimadoMinutos() > 45){
            throw new RuntimeException("No se admite tiempo mayor a 45 minutos.");
        }

        //Validacion de categorias
        if(articuloManufacturado.getCategoria() != null){
            Categoria categoria = categoriaRepository.getById(articuloManufacturado.getCategoria().getId());
            if (categoria == null ) {
                throw new RuntimeException("La categoría con id: " + articuloManufacturado.getCategoria().getId() + " no existe.");
            }else if(!categoria.isEsInsumo()){
                throw new RuntimeException("La categoria ingresada no es para insumos.");
            }

            articuloManufacturado.setCategoria(categoria);
        }else{
            throw new RuntimeException("No se ingreso categoria para el insumo.");
        }

        Set<ImagenArticulo> imagenes = articuloManufacturado.getImagenes();
        Set<ImagenArticulo> imagenesPersistidas = new HashSet<>();

        //Validacion de Imagenes
        if(!imagenes.isEmpty()){
            for (ImagenArticulo imagen : imagenes) {
                if (imagen.getId() != null) {
                    Optional<ImagenArticulo> imagenBd = imagenArticuloRepository.findById(imagen.getId());
                    imagenBd.ifPresent(imagenesPersistidas::add);
                } else {
                    imagen.setEliminado(false);
                    ImagenArticulo savedImagen = imagenArticuloRepository.save(imagen);
                    imagenesPersistidas.add(savedImagen);
                }
            }
        }

        if (!imagenesPersistidas.isEmpty()) {
            articuloManufacturado.setImagenes(imagenesPersistidas);
        }

        //Validacion Unidad de Medida
        if(articuloManufacturado.getUnidadMedida() != null){
            UnidadMedida unidadMedida = this.unidadMedidaRepository.findById(articuloManufacturado.getUnidadMedida().getId()).get();
            articuloManufacturado.setUnidadMedida(unidadMedida);
        }else{
            throw new RuntimeException("No se ingreso unidad de medida para el insumo.");
        }

        //Validar Detalles del articulo manufacturado
        if(!articuloManufacturado.getArticuloManufacturadoDetalles().isEmpty()){
            //Agregar Detalles al articulo manufacturado
            Set<ArticuloManufacturadoDetalle> articuloManufacturadoDetalles = new HashSet<>();

            for(ArticuloManufacturadoDetalle detalle : articuloManufacturado.getArticuloManufacturadoDetalles()){
                detalle.setArticuloInsumo(this.articuloInsumoService.getById(detalle.getArticuloInsumo().getId()));
                articuloManufacturadoDetalles.add(detalle);
            }

            articuloManufacturado.setArticuloManufacturadoDetalles(articuloManufacturadoDetalles);

        }else{
            throw new RuntimeException("No se enviaron articulos manufacturados.");
        }

        return super.create(articuloManufacturado);
    }


    @Override
    @Transactional
    public ArticuloManufacturado update(ArticuloManufacturado articuloManufacturado, Long id) {
        ArticuloManufacturado articuloManufacturadoExistente = this.articuloManufacturadoRepository.findById(id).get();
        if(articuloManufacturadoExistente == null){
            throw new RuntimeException("Manufacturado no encontrado: { id: " + id + " }");
        }

        //Validar tiempo de estimacion
        if(articuloManufacturado.getTiempoEstimadoMinutos() < 0){
            throw new RuntimeException("No se admite tiempo negativo");
        }else if(articuloManufacturado.getTiempoEstimadoMinutos() > 45){
            throw new RuntimeException("No se admite tiempo mayor a 45 minutos.");
        }

        //Validacion de categorias
        if(articuloManufacturado.getCategoria() != null){
            Categoria categoria = categoriaRepository.getById(articuloManufacturado.getCategoria().getId());
            if (categoria == null ) {
                throw new RuntimeException("La categoría con id: " + articuloManufacturado.getCategoria().getId() + " no existe.");
            }else if(!categoria.isEsInsumo()){
                throw new RuntimeException("La categoria ingresada no es para insumos.");
            }

            articuloManufacturado.setCategoria(categoria);
        }else{
            throw new RuntimeException("No se ingreso categoria para el insumo.");
        }

        Set<ImagenArticulo> imagenes = articuloManufacturado.getImagenes();
        Set<ImagenArticulo> imagenesPersistidas = new HashSet<>();

        //Validacion de Imagenes
        if(!imagenes.isEmpty()){
            for (ImagenArticulo imagen : imagenes) {
                if (imagen.getId() != null) {
                    Optional<ImagenArticulo> imagenBd = imagenArticuloRepository.findById(imagen.getId());
                    imagenBd.ifPresent(imagenesPersistidas::add);
                } else {
                    imagen.setEliminado(false);
                    ImagenArticulo savedImagen = imagenArticuloRepository.save(imagen);
                    imagenesPersistidas.add(savedImagen);
                }
            }
        }

        if (!imagenesPersistidas.isEmpty()) {
            articuloManufacturado.setImagenes(imagenesPersistidas);
        }

        //Validacion Unidad de Medida
        if(articuloManufacturado.getUnidadMedida() != null){
            UnidadMedida unidadMedida = this.unidadMedidaRepository.findById(articuloManufacturado.getUnidadMedida().getId()).get();
            articuloManufacturado.setUnidadMedida(unidadMedida);
        }else{
            throw new RuntimeException("No se ingreso unidad de medida para el insumo.");
        }

        //Validar Detalles del articulo manufacturado
        if(!articuloManufacturado.getArticuloManufacturadoDetalles().isEmpty()){
            //Agregar Detalles al articulo manufacturado
            Set<ArticuloManufacturadoDetalle> articuloManufacturadoDetalles = new HashSet<>();

            for(ArticuloManufacturadoDetalle detalle : articuloManufacturado.getArticuloManufacturadoDetalles()){
                detalle.setArticuloInsumo(this.articuloInsumoService.getById(detalle.getArticuloInsumo().getId()));
                articuloManufacturadoDetalles.add(detalle);
            }

            articuloManufacturado.setArticuloManufacturadoDetalles(articuloManufacturadoDetalles);

        }else {
            throw new RuntimeException("No se enviaron articulos manufacturados.");
        }

        return super.update(articuloManufacturado, id);
    }
}
