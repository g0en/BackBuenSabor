package com.entidades.buenSabor;

import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.domain.enums.*;
import com.entidades.buenSabor.repositories.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


@SpringBootApplication
public class BuenSaborApplication {
	private static final Logger logger = LoggerFactory.getLogger(BuenSaborApplication.class);

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ImagenPersonaRepository imagenPersonaRepository;
	@Autowired
	private PromocionDetalleRepository promocionDetalleRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private ProvinciaRepository provinciaRepository;

	@Autowired
	private LocalidadRepository localidadRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private SucursalRepository sucursalRepository;

	@Autowired
	private DomicilioRepository domicilioRepository;

	@Autowired
	private UnidadMedidaRepository unidadMedidaRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ArticuloInsumoRepository articuloInsumoRepository;

	@Autowired
	private ArticuloManufacturadoRepository articuloManufacturadoRepository;

	@Autowired
	private ImagenArticuloRepository imagenArticuloRepository;

	@Autowired
	private PromocionRepository promocionRepository;

	@Autowired
	private ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(BuenSaborApplication.class, args);
		logger.info("Estoy activo en el main");
	}

	@Bean
	@Transactional
	CommandLineRunner init(ClienteRepository clienteRepository,
						   ImagenPersonaRepository imagenPersonaRepository,
						   PromocionDetalleRepository promocionDetalleRepository,
						   UsuarioRepository usuarioRepository,
						   PaisRepository paisRepository,
						   ProvinciaRepository provinciaRepository,
						   LocalidadRepository localidadRepository,
						   EmpresaRepository empresaRepository,
						   SucursalRepository sucursalRepository,
						   DomicilioRepository domicilioRepository,
						   UnidadMedidaRepository unidadMedidaRepository,
						   CategoriaRepository categoriaRepository,
						   ArticuloInsumoRepository articuloInsumoRepository,
						   ArticuloManufacturadoRepository articuloManufacturadoRepository,
						   ImagenArticuloRepository imagenArticuloRepository,
						   PromocionRepository promocionRepository,
						   PedidoRepository pedidoRepository,
						   EmpleadoRepository empleadoRepository, FacturaRepository facturaRepository) {
		return args -> {
			logger.info("----------------ESTOY----FUNCIONANDO---------------------");
			Empresa empresa1 = Empresa.builder()
					.nombre("Empresa MCC")
					.razonSocial("Razón Social")
					.cuil(1324567890L)
					.build();

			empresaRepository.save(empresa1);

			/*Domicilio domicilioSucursal = Domicilio.builder()
					.calle("San Martín")
					.numero(123)
					.cp(5515)
					.piso(0)
					.nroDpto(0)
					.localidad(this.localidadRepository.findById(377L).get())
					.build();*/

			Sucursal sucursal1 = Sucursal.builder()
					.nombre("Sucursal 1 MCC")
					.horarioApertura(LocalTime.of(12,30,0))
					.horarioCierre(LocalTime.of(20,30,0))
					.esCasaMatriz(false)

					.empresa(empresa1)
					.build();

			sucursalRepository.save(sucursal1);

			Set<Sucursal> sucursales = new HashSet<>();
			sucursales.add(sucursal1);

			Categoria categoriaPizza = Categoria.builder()
					.denominacion("Pizza")
					.esInsumo(false)
					.sucursales(sucursales)
					.build();

			Categoria categoriaMolidos = Categoria.builder()
					.denominacion("Molidos")
					.esInsumo(true)
					.sucursales(sucursales)
					.build();

			Categoria categoriaLacteos = Categoria.builder()
					.denominacion("Lacteos")
					.esInsumo(true)
					.sucursales(sucursales)
					.build();

			Categoria categoriaSalsas = Categoria.builder()
					.denominacion("Salsas")
					.esInsumo(true)
					.sucursales(sucursales)
					.build();


			this.categoriaRepository.save(categoriaPizza);
			this.categoriaRepository.save(categoriaMolidos);
			this.categoriaRepository.save(categoriaLacteos);
			this.categoriaRepository.save(categoriaSalsas);

			UnidadMedida unidadMedidaGramos = UnidadMedida.builder()
					.denominacion("Gramos")
					.build();

			this.unidadMedidaRepository.save(unidadMedidaGramos);

			ImagenArticulo imgHarina = ImagenArticulo.builder()
					.url("https://st2.depositphotos.com/1000348/6439/i/450/depositphotos_64399139-stock-photo-flour-and-wheat-ears.jpg")
					.build();

			Set<ImagenArticulo> imgHarinas = new HashSet<>();
			imgHarinas.add(imgHarina);

			ArticuloInsumo harina = ArticuloInsumo.builder()
					.denominacion("Harina")
					.precioCompra(300.)
					.imagenes(imgHarinas)
					.unidadMedida(unidadMedidaGramos)
					.categoria(categoriaMolidos)
					.stockActual(20)
					.stockMaximo(100)
					.stockMinimo(1)
					.esParaElaborar(true)
					.build();

			ImagenArticulo imgQueso = ImagenArticulo.builder()
					.url("https://i0.wp.com/petroantillana.com/wp-content/uploads/2021/09/queso-gouda-acheese-1.jpg?fit=%2C&ssl=1")
					.build();

			Set<ImagenArticulo> imgQuesos = new HashSet<>();
			imgQuesos.add(imgQueso);

			ArticuloInsumo queso = ArticuloInsumo.builder()
					.denominacion("Queso")
					.precioCompra(500.)
					.imagenes(imgQuesos)
					.unidadMedida(unidadMedidaGramos)
					.categoria(categoriaLacteos)
					.stockActual(50)
					.stockMaximo(200)
					.stockMinimo(1)
					.esParaElaborar(true)
					.build();

			ImagenArticulo imgSalsa = ImagenArticulo.builder()
					.url("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsL8frGGGyuFiVUUHKimulAYhW5F2VJvXu-g&s")
					.build();

			Set<ImagenArticulo> imgSalsas = new HashSet<>();
			imgSalsas.add(imgSalsa);

			ArticuloInsumo salsaTomate = ArticuloInsumo.builder()
					.denominacion("Salsa de tomate")
					.precioCompra(250.)
					.imagenes(imgSalsas)
					.unidadMedida(unidadMedidaGramos)
					.categoria(categoriaSalsas)
					.stockActual(60)
					.stockMaximo(150)
					.stockMinimo(1)
					.esParaElaborar(true)
					.build();

			this.articuloInsumoRepository.save(harina);
			this.articuloInsumoRepository.save(queso);
			this.articuloInsumoRepository.save(salsaTomate);

			ImagenArticulo imgPizza = ImagenArticulo.builder()
					.url("https://media.istockphoto.com/id/1442417585/es/foto/persona-recibiendo-un-pedazo-de-pizza-de-pepperoni-con-queso.jpg?s=612x612&w=0&k=20&c=Uk4fj96OIDxE4v2S5sRRXRY_gZ899_TE6jGD-T-TysI=")
					.build();

			Set<ImagenArticulo> imgPizzas = new HashSet<>();
			imgPizzas.add(imgPizza);

			ArticuloManufacturadoDetalle detalle1 = ArticuloManufacturadoDetalle.builder()
					.cantidad(1)
					.articuloInsumo(harina)
					.build();
			ArticuloManufacturadoDetalle detalle2 = ArticuloManufacturadoDetalle.builder()
					.cantidad(2)
					.articuloInsumo(queso)
					.build();
			ArticuloManufacturadoDetalle detalle3 = ArticuloManufacturadoDetalle.builder()
					.cantidad(2)
					.articuloInsumo(salsaTomate)
					.build();

			Set<ArticuloManufacturadoDetalle> detalles = new HashSet<>();
			detalles.add(detalle1);
			detalles.add(detalle2);
			detalles.add(detalle3);

			ArticuloManufacturado articuloManufacturado = ArticuloManufacturado.builder()
					.denominacion("Pizza Napolitana")
					.precioVenta(5000.)
					.imagenes(imgPizzas)
					.unidadMedida(unidadMedidaGramos)
					.categoria(categoriaPizza)
					.descripcion("Pizza con abundante queso.")
					.tiempoEstimadoMinutos(15)
					.preparacion("Colocar abundante salsa y queso y dejar 10 minutos en el horno.")
					.articuloManufacturadoDetalles(detalles)
					.build();

			this.articuloManufacturadoRepository.save(articuloManufacturado);
		};
	}

}