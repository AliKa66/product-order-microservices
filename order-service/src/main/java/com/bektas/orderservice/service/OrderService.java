package com.bektas.orderservice.service;

import com.bektas.orderservice.dto.InventoryResponse;
import com.bektas.orderservice.dto.OrderLineItemsDto;
import com.bektas.orderservice.dto.OrderRequest;
import com.bektas.orderservice.model.Order;
import com.bektas.orderservice.model.OrderLineItems;
import com.bektas.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest
                .getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToOrderLineItems)
                .toList();
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] response = webClientBuilder.build()
                .get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allInStock = Arrays.stream(response).allMatch(InventoryResponse::isInStock);

        if (allInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto dto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setId(dto.getId());
        orderLineItems.setPrice(dto.getPrice());
        orderLineItems.setQuantity(dto.getQuantity());
        orderLineItems.setSkuCode(dto.getSkuCode());
        return orderLineItems;
    }
}
