package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.mapper.PedidoMapper;
import com.entidades.buenSabor.domain.dto.PedidoDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.entities.PreferenceMp;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Autowired
    private PedidoMapper pedidoMapper;

    public PreferenceMp getPreferenciaIdMercadoPago(PedidoDto pedidoDto) {
        Pedido pedido = pedidoMapper.toEntity(pedidoDto);
        try {
            // Configuración del Access Token
            MercadoPagoConfig.setAccessToken("TEST-6868831087152965-060612-f20879d73b51d173129c43f57c44c205-1844671669");

            // Crear item de la preferencia
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(String.valueOf(1234))
                    .title("ASD")
                    .description("Pedido realizado desde el carrito de compras")
                    .pictureUrl("http://picture.com/PS5")
                    .categoryId("art") // Ejemplo de categoría correcta, ajusta según sea necesario
                    .quantity(1)
                    .currencyId("ARS") // Asegúrate de usar el código de moneda correcto
                    .unitPrice(new BigDecimal(1))
                    .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            // URLs de redirección
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:5173/productos")
                    .failure("http://localhost:5173/productos")
                    .pending("http://localhost:5173/productos")
                    .build();

            // Crear la solicitud de preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .build();

            // Cliente de Mercado Pago
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            // Crear y devolver el objeto PreferenceMp
            PreferenceMp mpPreference = new PreferenceMp();
            mpPreference.setStatusCode(preference.getResponse().getStatusCode());
            mpPreference.setId(preference.getId());

            return mpPreference;

        } catch (MPApiException apiException) {
            System.err.println("Error de API: " + apiException.getApiResponse().getContent());
            apiException.printStackTrace();
            return null;
        } catch (MPException e) {
            e.printStackTrace();
            return null;
        }
    }
}
