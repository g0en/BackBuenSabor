package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.ArticuloInsumoService;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.Categoria;
import com.entidades.buenSabor.domain.entities.ImagenArticulo;
import com.entidades.buenSabor.domain.entities.UnidadMedida;
import com.entidades.buenSabor.repositories.ArticuloInsumoRepository;
import com.entidades.buenSabor.repositories.CategoriaRepository;
import com.entidades.buenSabor.repositories.ImagenArticuloRepository;
import com.entidades.buenSabor.repositories.UnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ArticuloInsumoServiceImp extends BaseServiceImp<ArticuloInsumo,Long> implements ArticuloInsumoService {
    @Autowired
    private ImagenArticuloRepository imagenArticuloRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Override
    public ArticuloInsumo create(ArticuloInsumo articuloInsumo) {

        //Validaciones iniciales
        if(articuloInsumo.getStockActual() < articuloInsumo.getStockMinimo()){
            throw new RuntimeException("El stock actual debe ser mayor al stock minimo.");
        } else if (articuloInsumo.getStockMaximo() < 0 || articuloInsumo.getStockMinimo() < 0) {
            throw new RuntimeException("El stock minimo o maximo debe ser mayor a cero.");
        }

        //Validacion de categorias
        if(articuloInsumo.getCategoria() != null){
            Categoria categoria = categoriaRepository.getById(articuloInsumo.getCategoria().getId());
            if (categoria == null ) {
                throw new RuntimeException("La categoría con id: " + articuloInsumo.getCategoria().getId() + " no existe.");
            }else if(!categoria.isEsInsumo()){
                throw new RuntimeException("La categoria ingresada no es para insumos.");
            }

            articuloInsumo.setCategoria(categoria);
        }else{
            throw new RuntimeException("No se ingreso categoria para el insumo.");
        }

        Set<ImagenArticulo> imagenes = articuloInsumo.getImagenes();
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
            articuloInsumo.setImagenes(imagenesPersistidas);
        }

        //Validacion Unidad de Medida
        if(articuloInsumo.getUnidadMedida() != null){
            UnidadMedida unidadMedida = this.unidadMedidaRepository.findById(articuloInsumo.getUnidadMedida().getId()).get();
            articuloInsumo.setUnidadMedida(unidadMedida);
        }else{
            throw new RuntimeException("No se ingreso unidad de medida para el insumo.");
        }

        return super.create(articuloInsumo);
    }

    @Override
    public ArticuloInsumo update(ArticuloInsumo newArticuloInsumo, Long id) {
        ArticuloInsumo articuloInsumoExistente = this.articuloInsumoRepository.findById(id).get();

        //Validar el insumo a editar
        if (articuloInsumoExistente == null) {
            throw new RuntimeException("Insumo no encontrado: { id: " + id + " }");
        }

        //Validaciones iniciales
        if(newArticuloInsumo.getStockActual() < newArticuloInsumo.getStockMinimo()){
            throw new RuntimeException("El stock actual debe ser mayor al stock minimo.");
        } else if (newArticuloInsumo.getStockMaximo() < 0 || newArticuloInsumo.getStockMinimo() < 0) {
            throw new RuntimeException("El stock minimo o maximo debe ser mayor a cero.");
        }

        //Validacion de categorias
        if(newArticuloInsumo.getCategoria() != null){
            Categoria categoria = categoriaRepository.getById(newArticuloInsumo.getCategoria().getId());
            if (categoria == null ) {
                throw new RuntimeException("La categoría con id: " + newArticuloInsumo.getCategoria().getId() + " no existe.");
            }else if(!categoria.isEsInsumo()){
                throw new RuntimeException("La categoria ingresada no es para insumos.");
            }

            newArticuloInsumo.setCategoria(categoria);
        }else{
            throw new RuntimeException("No se ingreso categoria para el insumo.");
        }

        Set<ImagenArticulo> imagenes = newArticuloInsumo.getImagenes();
        Set<ImagenArticulo> imagenesPersistidas = new HashSet<>();

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
            newArticuloInsumo.setImagenes(imagenesPersistidas);
        }

        //Validacion Unidad de Medida
        if(newArticuloInsumo.getUnidadMedida() != null){
            UnidadMedida unidadMedida = this.unidadMedidaRepository.findById(newArticuloInsumo.getUnidadMedida().getId()).get();
            newArticuloInsumo.setUnidadMedida(unidadMedida);
        }else{
            throw new RuntimeException("No se ingreso unidad de medida para el insumo.");
        }

        return super.update(newArticuloInsumo, id);
    }


    @Override
    public void deleteById(Long id) {

        super.deleteById(id);
    }
}
