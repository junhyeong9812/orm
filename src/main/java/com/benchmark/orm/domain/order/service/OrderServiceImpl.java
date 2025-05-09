package com.benchmark.orm.domain.order.service;

import com.benchmark.orm.domain.order.dto.OrderRequestDto;
import com.benchmark.orm.domain.order.dto.OrderResponseDto;
import com.benchmark.orm.domain.order.dto.OrderSearchDto;
import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.mapper.OrderMapper;
import com.benchmark.orm.domain.order.repository.OrderRepository;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.repository.ProductRepository;
import com.benchmark.orm.domain.user.entity.User;
import com.benchmark.orm.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OrderService 인터페이스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponseDto saveOrderJpa(OrderRequestDto orderDto) {
        // 사용자와 상품 엔티티 조회
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDto.getUserId()));

        Product product = productRepository.findById(orderDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderDto.getProductId()));

        // DTO를 엔티티로 변환
        Order order = orderDto.toEntity(user, product);

        // 엔티티 저장
        Order savedOrder = orderRepository.save(order);

        // 응답 DTO 반환
        return OrderResponseDto.fromEntityWithUserAndProduct(savedOrder);
    }

    @Override
    @Transactional
    public String saveOrderMyBatis(OrderRequestDto orderDto) {
        // 사용자와 상품 엔티티 조회
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDto.getUserId()));

        Product product = productRepository.findById(orderDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderDto.getProductId()));

        // DTO를 엔티티로 변환
        Order order = orderDto.toEntity(user, product);

        // MyBatis를 통해 엔티티 저장
        orderMapper.insert(order);

        return "Order created successfully with MyBatis";
    }

    @Override
    public Optional<OrderResponseDto> findOrderByIdJpa(Long id) {
        return orderRepository.findById(id)
                .map(OrderResponseDto::fromEntity);
    }

    @Override
    public OrderResponseDto findOrderByIdMyBatis(Long id) {
        Order order = orderMapper.findById(id);
        return order != null ? OrderResponseDto.fromEntity(order) : null;
    }

    @Override
    public List<OrderResponseDto> findAllOrdersJpa() {
        return orderRepository.findAll().stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findAllOrdersMyBatis() {
        return orderMapper.findAll().stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersByUserIdJpql(Long userId) {
        return orderRepository.findByUserIdJpql(userId).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersByUserIdQueryDsl(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersByProductIdJpql(Long productId) {
        return orderRepository.findByProductIdJpql(productId).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersByProductIdQueryDsl(Long productId) {
        return orderRepository.findByProductId(productId).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersByOrderDateBetweenJpql(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetweenJpql(startDate, endDate).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersByOrderDateBetweenQueryDsl(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponseDto> findOrdersWithPagingJpa(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderResponseDto> orderDtos = orderPage.getContent().stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDtos, pageable, orderPage.getTotalElements());
    }

    @Override
    public Page<OrderResponseDto> findOrdersWithPagingQueryDsl(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAllWithPaging(pageable);

        List<OrderResponseDto> orderDtos = orderPage.getContent().stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDtos, pageable, orderPage.getTotalElements());
    }

    @Override
    public List<OrderResponseDto> findOrdersWithPagingMyBatis(int offset, int limit) {
        return orderMapper.findAllWithPaging(offset, limit).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersWithSortingJpa(Sort sort) {
        return orderRepository.findAll(sort).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersWithSortingQueryDsl(Sort sort) {
        return orderRepository.findAllWithSorting(sort).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersWithSortingMyBatis(String sortColumn, String sortDirection) {
        return orderMapper.findAllWithSorting(sortColumn, sortDirection).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponseDto> findOrdersWithPagingAndSortingJpa(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderResponseDto> orderDtos = orderPage.getContent().stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDtos, pageable, orderPage.getTotalElements());
    }

    @Override
    public List<OrderResponseDto> findOrdersWithPagingAndSortingMyBatis(int offset, int limit, String sortColumn, String sortDirection) {
        return orderMapper.findAllWithPagingAndSorting(offset, limit, sortColumn, sortDirection).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithUserJpql(Long orderId) {
        return orderRepository.findOrderWithUserJpql(orderId)
                .map(OrderResponseDto::fromEntityWithUser);
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithUserQueryDsl(Long orderId) {
        return orderRepository.findOrderWithUser(orderId)
                .map(OrderResponseDto::fromEntityWithUser);
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithProductJpql(Long orderId) {
        return orderRepository.findOrderWithProductJpql(orderId)
                .map(OrderResponseDto::fromEntityWithProduct);
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithProductQueryDsl(Long orderId) {
        return orderRepository.findOrderWithProduct(orderId)
                .map(OrderResponseDto::fromEntityWithProduct);
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithUserAndProductJpql(Long orderId) {
        return orderRepository.findOrderWithUserAndProductJpql(orderId)
                .map(OrderResponseDto::fromEntityWithUserAndProduct);
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithUserAndProductQueryDsl(Long orderId) {
        return orderRepository.findOrderWithUserAndProduct(orderId)
                .map(OrderResponseDto::fromEntityWithUserAndProduct);
    }

    @Override
    public Page<OrderResponseDto> findOrdersByUserIdWithPagingQueryDsl(Long userId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByUserIdWithPaging(userId, pageable);

        List<OrderResponseDto> orderDtos = orderPage.getContent().stream()
                .map(OrderResponseDto::fromEntityWithUser)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDtos, pageable, orderPage.getTotalElements());
    }

    @Override
    public List<OrderResponseDto> searchOrders(OrderSearchDto searchDto) {
        // 검색 조건에 따라 동적으로 조회
        if (searchDto.getUserId() != null) {
            return findOrdersByUserIdQueryDsl(searchDto.getUserId());
        } else if (searchDto.getProductId() != null) {
            return findOrdersByProductIdQueryDsl(searchDto.getProductId());
        } else if (searchDto.getStartDate() != null && searchDto.getEndDate() != null) {
            return findOrdersByOrderDateBetweenQueryDsl(searchDto.getStartDate(), searchDto.getEndDate());
        } else {
            // 기본적으로 모든 주문 반환
            return findAllOrdersJpa();
        }
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderJpa(Long id, OrderRequestDto orderDto) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    // 사용자와 상품 엔티티 조회
                    User user = userRepository.findById(orderDto.getUserId())
                            .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDto.getUserId()));

                    Product product = productRepository.findById(orderDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderDto.getProductId()));

                    // DTO를 엔티티로 변환 (ID 설정)
                    Order updatedOrder = orderDto.toEntity(user, product);

                    // ID 설정을 위한 Reflection 사용
                    try {
                        java.lang.reflect.Field idField = Order.class.getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(updatedOrder, id);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to set Order ID", e);
                    }

                    // 업데이트된 주문 저장
                    Order savedOrder = orderRepository.save(updatedOrder);
                    return OrderResponseDto.fromEntityWithUserAndProduct(savedOrder);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public String updateOrderMyBatis(Long id, OrderRequestDto orderDto) {
        Order existingOrder = orderMapper.findById(id);

        if (existingOrder == null) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        // 사용자와 상품 엔티티 조회
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDto.getUserId()));

        Product product = productRepository.findById(orderDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderDto.getProductId()));

        // DTO를 엔티티로 변환 (ID 설정)
        Order updatedOrder = orderDto.toEntity(user, product);

        // ID 설정을 위한 Reflection 사용
        try {
            java.lang.reflect.Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(updatedOrder, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set Order ID", e);
        }

        // MyBatis를 통해 주문 업데이트
        orderMapper.update(updatedOrder);

        return "Order updated successfully with MyBatis";
    }

    @Override
    @Transactional
    public String deleteOrderJpa(Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderRepository.deleteById(id);
                    return "Order deleted successfully with JPA";
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public String deleteOrderMyBatis(Long id) {
        Order existingOrder = orderMapper.findById(id);

        if (existingOrder == null) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        orderMapper.deleteById(id);
        return "Order deleted successfully with MyBatis";
    }
}