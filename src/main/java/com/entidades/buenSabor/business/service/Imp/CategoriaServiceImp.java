package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.CategoriaService;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.ArticuloManufacturado;
import com.entidades.buenSabor.domain.entities.Categoria;
import com.entidades.buenSabor.domain.entities.Sucursal;
import com.entidades.buenSabor.repositories.ArticuloInsumoRepository;
import com.entidades.buenSabor.repositories.ArticuloManufacturadoRepository;
import com.entidades.buenSabor.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoriaServiceImp extends BaseServiceImp<Categoria, Long> implements CategoriaService {
    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Override
    public Categoria create(Categoria categoria) {
        Set<Sucursal> sucursales = new HashSet<>();

        if (categoria.getSucursales() != null && !categoria.getSucursales().isEmpty()) {
            for (Sucursal sucursal : categoria.getSucursales()) {
                Sucursal sucursalAux = this.sucursalService.getById(sucursal.getId());
                if (sucursalAux == null) {
                    throw new RuntimeException("La sucursal con el id " + sucursal.getId() + " no existe.");
                }
                sucursalAux.getCategorias().add(categoria);
                sucursales.add(sucursalAux);
            }
        }

        categoria.setSucursales(sucursales);

        //categoria.setCategoriaPadre(categoria);
        if (!categoria.getSubCategorias().isEmpty()) {
            mapearSubcategorias(categoria, sucursales);
        }

        return super.create(categoria);
    }

    private void mapearSubcategorias(Categoria categoria, Set<Sucursal> sucursales) {
        if (!categoria.getSubCategorias().isEmpty()) {
            for (Categoria subcategoria : categoria.getSubCategorias()) {
                subcategoria.setCategoriaPadre(categoria);
                subcategoria.setSucursales(sucursales);
                for (Sucursal sucursal : sucursales) {
                    sucursal.getCategorias().add(subcategoria);
                }
                if (!subcategoria.getSubCategorias().isEmpty()) {
                    mapearSubcategorias(subcategoria, sucursales);
                }
            }
        }
    }

    @Override
    public Categoria update(Categoria newCategoria, Long id) {
        Categoria categoriaExistente = this.categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoria con el id: " + id + " no existe."));

        // Actualizar detalles básicos de la categoría
        categoriaExistente.setDenominacion(newCategoria.getDenominacion());
        categoriaExistente.setEsInsumo(newCategoria.isEsInsumo());

        // Actualizar sucursales
        Set<Sucursal> sucursales = new HashSet<>();
        if (newCategoria.getSucursales() != null && !newCategoria.getSucursales().isEmpty()) {
            for (Sucursal sucursal : newCategoria.getSucursales()) {
                Sucursal sucursalBd = this.sucursalService.getById(sucursal.getId());
                if (sucursalBd == null) {
                    throw new RuntimeException("La sucursal con el id " + sucursal.getId() + " no existe.");
                }
                boolean categoriaExists = sucursalBd.getCategorias().stream()
                        .anyMatch(cat -> cat.getId() != null && cat.getId().equals(newCategoria.getId()));

                if (!categoriaExists) {
                    sucursalBd.getCategorias().add(newCategoria);
                }
                sucursales.add(sucursalBd);
            }
        }

        categoriaExistente.setSucursales(sucursales);

        // Eliminar cualquier referencia de la categoría existente a sí misma en sus subcategorías
        categoriaExistente.getSubCategorias().removeIf(subcat -> subcat.getId().equals(categoriaExistente.getId()));

        // Actualizar subcategorías
        actualizarSubcategorias(categoriaExistente, newCategoria, sucursales);

        return super.update(categoriaExistente, id);
    }

    private void actualizarSubcategorias(Categoria categoriaExistente, Categoria newCategoria, Set<Sucursal> sucursales) {
        if (!newCategoria.getSubCategorias().isEmpty()) {
            for (Categoria subcategoriaNueva : newCategoria.getSubCategorias()) {
                // Evitar agregar la propia categoría como subcategoría
                if (subcategoriaNueva.getId().equals(categoriaExistente.getId())) {
                    continue;
                }

                Optional<Categoria> subcategoriaExistenteOpt = categoriaExistente.getSubCategorias().stream()
                        .filter(sc -> sc.getId().equals(subcategoriaNueva.getId()))
                        .findFirst();

                if (subcategoriaExistenteOpt.isPresent()) {
                    Categoria subcategoriaExistente = subcategoriaExistenteOpt.get();
                    subcategoriaExistente.setDenominacion(subcategoriaNueva.getDenominacion());
                    subcategoriaExistente.setEsInsumo(subcategoriaNueva.isEsInsumo());
                    subcategoriaExistente.setSucursales(sucursales);
                    for (Sucursal sucursal : sucursales) {
                        boolean categoriaExists = sucursal.getCategorias().stream()
                                .anyMatch(cat -> cat.getId() != null && cat.getId().equals(subcategoriaExistente.getId()));

                        if (!categoriaExists) {
                            sucursal.getCategorias().add(subcategoriaExistente);
                        }
                    }
                    actualizarSubcategorias(subcategoriaExistente, subcategoriaNueva, sucursales);
                } else {
                    subcategoriaNueva.setCategoriaPadre(categoriaExistente);
                    subcategoriaNueva.setSucursales(sucursales);
                    categoriaExistente.getSubCategorias().add(subcategoriaNueva);

                    for (Sucursal sucursal : sucursales) {
                        sucursal.getCategorias().add(subcategoriaNueva);
                    }
                    actualizarSubcategorias(subcategoriaNueva, subcategoriaNueva, sucursales);
                }
            }
        }
    }

    public void deleteInSucursales(Long idCategoria, Long idSucursal) {
        Categoria categoriaExistente = this.categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("La categoría con el id: " + idCategoria + " no existe."));

        Sucursal sucursal = this.sucursalService.getById(idSucursal);

        //Eliminar realcion entre sucursal y categoria
        sucursal.getCategorias().remove(categoriaExistente);
        categoriaExistente.getSucursales().remove(sucursal);

        this.categoriaRepository.save(categoriaExistente);
    }


    @Override
    public void deleteById(Long id) {
        Categoria categoria = this.categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría con el id: " + id + " no existe."));

        //Filtrar articulos por la categoria
        List<ArticuloInsumo> articuloInsumos = this.articuloInsumoRepository.getByCategoria(categoria);
        List<ArticuloManufacturado> articuloManufacturados = this.articuloManufacturadoRepository.getByCategoria(categoria);

        //Verificar si los articulos estan eliminados
        boolean hayArticulos = false;
        for(ArticuloInsumo insumo : articuloInsumos){
            if(insumo.isEliminado()){
                hayArticulos = true;
                break;
            }
        }

        for(ArticuloManufacturado manufacturado : articuloManufacturados){
            if(manufacturado.isEliminado()){
                hayArticulos = true;
                break;
            }
        }


        //Verificar que no tenga sucursales ni articulos asociados
        if(!categoria.getSucursales().isEmpty()){
            throw new RuntimeException("No se puede eliminar la categoría, tiene sucursales asociadas.");
        }else if(!hayArticulos) {
            throw new RuntimeException("No se puede eliminar la categoría, tiene articulos asociados.");
        }else{
            super.deleteById(id);
        }
    }
}