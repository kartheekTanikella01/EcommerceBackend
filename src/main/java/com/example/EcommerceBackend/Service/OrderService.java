package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.DTO.OrderDto;
import com.example.EcommerceBackend.DTO.OrderItemDto;
import com.example.EcommerceBackend.Entity.Cart;
import com.example.EcommerceBackend.Entity.CartItem;
import com.example.EcommerceBackend.Entity.Order;
import com.example.EcommerceBackend.Entity.OrderItem;
import com.example.EcommerceBackend.Entity.User;
import com.example.EcommerceBackend.Repository.CartRepository;
import com.example.EcommerceBackend.Repository.OrderRepository;
import com.example.EcommerceBackend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepository;

    public OrderDto createOrderFromCart(int userId) {
        // Retrieve the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Get the user's cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user ID: " + userId));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());

        // Calculate total price and add items to order
        double totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        // Save the order
        orderRepository.save(order);

        // Clear the cart after moving items to order
        cart.getItems().clear();
        cartRepository.save(cart);

        // Send confirmation email
        String subject = "Order Confirmation - Order #" + order.getId();
        String body = "Dear " + user.getName() + ",\n\n"
                + "Thank you for your order! Your order has been received and is being processed.\n"
                + "Order ID: " + order.getId() + "\n"
                + "Total Price: $" + totalPrice + "\n"
                + "Order Date: " + order.getOrderDate() + "\n\n"
                + "We will notify you once your order has been shipped.\n\n"
                + "Thank you for shopping with us!";

        emailService.sendOrderConfirmationEmail(user.getEmail(), subject, body);

        // Convert order to DTO and return
        return convertToOrderDTO(order);
    }

    public List<OrderDto> getOrdersByUserId(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDto> orderDTOs = new ArrayList<>();

        // Convert each order to OrderDTO
        for (Order order : orders) {
            orderDTOs.add(convertToOrderDTO(order));
        }
        return orderDTOs;
    }

    public String cancelOrder(int orderId, int userId) {
        // Retrieve the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Verify if the order belongs to the user
        if (order.getUser().getId() != userId) {
            throw new RuntimeException("Order does not belong to the user with ID: " + userId);
        }

        // Get the user's cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(order.getUser());
                    return cartRepository.save(newCart);
                });

        // Move products from order back to cart
        for (OrderItem orderItem : order.getItems()) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(orderItem.getProduct());
            cartItem.setQuantity(orderItem.getQuantity());
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }

        // Save updated cart and delete the order
        cartRepository.save(cart);
        orderRepository.delete(order);

        return "Order has been canceled and items returned to cart.";
    }

    private OrderDto convertToOrderDTO(Order order) {
        OrderDto orderDTO = new OrderDto();
        orderDTO.setId(order.getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setOrderDate(order.getOrderDate());

        List<OrderItemDto> items = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemDto itemDTO = new OrderItemDto();
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setQuantity(item.getQuantity());
            items.add(itemDTO);
        }
        orderDTO.setItems(items);

        return orderDTO;
    }
}
