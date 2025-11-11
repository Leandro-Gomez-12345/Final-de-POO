package co.com.poo.shoppingcart.services;

import co.com.poo.shoppingcart.entities.OrderEntity;
import co.com.poo.shoppingcart.model.Order;
import co.com.poo.shoppingcart.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Persiste un resumen de la orden (sin items) en la tabla orders.
     * Usa el total final y la fecha actual. Se puede ampliar luego.
     */
    public OrderEntity saveOrderSummary(String customerName, Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomerName(customerName != null ? customerName : "Default Customer");
        entity.setTotal(order.getFinalTotal());
        entity.setOrderDate(LocalDateTime.now());
        return orderRepository.save(entity);
    }

    public OrderEntity getOrderEntity(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}