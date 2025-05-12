package com.benchmark.orm.domain.order.service;

import com.benchmark.orm.domain.order.dto.OrderRequestDto;
import com.benchmark.orm.domain.order.dto.OrderResponseDto;
import com.benchmark.orm.domain.order.dto.OrderSearchDto;
import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.OrderItem;
import com.benchmark.orm.domain.order.mapper.OrderItemMapper;
import com.benchmark.orm.domain.order.mapper.OrderMapper;
import com.benchmark.orm.domain.order.repository.OrderItemRepository;
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
import java.util.ArrayList;
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
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponseDto saveOrderJpa(OrderRequestDto orderDto) {
        // 사용자 엔티티 조회
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + orderDto.getUserId()));

        // 주문 엔티티 생성
        Order order = orderDto.toEntity(user);

        // 주문 상품 처리
        if (orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
            for (OrderRequestDto.OrderItemRequestDto itemDto : orderDto.getOrderItems()) {
                // 상품 조회
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + itemDto.getProductId()));

                // 주문 상품 생성 및 관계 설정
                OrderItem orderItem = itemDto.toEntity(product);
                order.addOrderItem(orderItem);
            }
        }

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 응답 DTO 반환
        return OrderResponseDto.fromEntityWithUserAndOrderItems(savedOrder);
    }

    @Override
    @Transactional
    public String saveOrderMyBatis(OrderRequestDto orderDto) {
        // 사용자 엔티티 조회
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + orderDto.getUserId()));

        // 주문 엔티티 생성
        Order order = orderDto.toEntity(user);

        // MyBatis를 통해 주문 저장
        orderMapper.insert(order);

        // 주문 상품 처리
        if (orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
            for (OrderRequestDto.OrderItemRequestDto itemDto : orderDto.getOrderItems()) {
                // 상품 조회
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + itemDto.getProductId()));

                // 주문 상품 생성
                OrderItem orderItem = itemDto.toEntity(product);
                orderItem.assignOrder(order);

                // MyBatis를 통해 주문 상품 저장
                orderItemMapper.insert(orderItem);
            }
        }

        return "주문이 MyBatis를 통해 성공적으로 생성되었습니다.";
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
        return orderRepository.findByUserId(userId).stream()
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
    public List<OrderResponseDto> findOrdersByProductIdQueryDsl(Long productId) {
        return orderRepository.findOrdersContainingProduct(productId).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersByOrderDateBetweenJpql(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
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
    public Optional<OrderResponseDto> findOrderWithOrderItemsJpql(Long orderId) {
        return orderRepository.findOrderWithOrderItemsJpql(orderId)
                .map(OrderResponseDto::fromEntityWithOrderItems);
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithOrderItemsQueryDsl(Long orderId) {
        return orderRepository.findOrderWithOrderItems(orderId)
                .map(OrderResponseDto::fromEntityWithOrderItems);
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithUserAndOrderItemsJpql(Long orderId) {
        return orderRepository.findOrderWithUserAndOrderItemsJpql(orderId)
                .map(OrderResponseDto::fromEntityWithUserAndOrderItems);
    }

    @Override
    public Optional<OrderResponseDto> findOrderWithUserAndOrderItemsQueryDsl(Long orderId) {
        return orderRepository.findOrderWithUserAndOrderItems(orderId)
                .map(OrderResponseDto::fromEntityWithUserAndOrderItems);
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
    public Page<OrderResponseDto> searchOrdersJpql(OrderSearchDto searchDto, Pageable pageable) {
        // JPQL 방식으로 검색
        Page<Order> orderPage = orderRepository.searchOrdersJpql(
                searchDto.getUserId(),
                searchDto.getStatus(),
                searchDto.getStartDate(),
                searchDto.getEndDate(),
                pageable);

        List<OrderResponseDto> orderDtos = orderPage.getContent().stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDtos, pageable, orderPage.getTotalElements());
    }

    @Override
    public Page<OrderResponseDto> searchOrdersQueryDsl(OrderSearchDto searchDto, Pageable pageable) {
        // QueryDSL 방식으로 검색
        Page<Order> orderPage = orderRepository.searchOrders(searchDto, pageable);

        List<OrderResponseDto> orderDtos = orderPage.getContent().stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDtos, pageable, orderPage.getTotalElements());
    }

    @Override
    public Page<OrderResponseDto> searchOrdersMyBatis(OrderSearchDto searchDto, int offset, int limit,
                                                      String sortColumn, String sortDirection) {
        // MyBatis 방식으로 검색
        List<Order> orders = orderMapper.searchOrders(searchDto, offset, limit, sortColumn, sortDirection);

        List<OrderResponseDto> orderDtos = orders.stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());

        // 전체 개수 조회
        int total = orderMapper.countBySearchDto(searchDto);

        return new PageImpl<>(orderDtos, Pageable.ofSize(limit).withPage(offset / limit), total);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderJpa(Long id, OrderRequestDto orderDto) {
        // 기존 주문 조회
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID: " + id));

        // 사용자 엔티티 조회
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + orderDto.getUserId()));

        // 기존 주문 상품 제거 (영속성 전이로 자동 삭제)
        // 주문 엔티티가 orderItems의 변경을 관리하므로 여기서는 관계만 제거
        List<OrderItem> existingOrderItems = new ArrayList<>(existingOrder.getOrderItems());
        for (OrderItem item : existingOrderItems) {
            existingOrder.removeOrderItem(item);
        }

        // 주문 기본 정보 업데이트
        existingOrder.changeUser(user)
                .changeOrderDate(orderDto.getOrderDate() != null ? orderDto.getOrderDate() : existingOrder.getOrderDate())
                .changeStatus(orderDto.getStatus() != null ? orderDto.getStatus() : existingOrder.getStatus());

        // 새 주문 상품 추가
        if (orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
            for (OrderRequestDto.OrderItemRequestDto itemDto : orderDto.getOrderItems()) {
                // 상품 조회
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + itemDto.getProductId()));

                // 주문 상품 생성 및 관계 설정
                OrderItem orderItem = itemDto.toEntity(product);
                existingOrder.addOrderItem(orderItem);
            }
        }

        // 주문 저장
        Order updatedOrder = orderRepository.save(existingOrder);

        return OrderResponseDto.fromEntityWithUserAndOrderItems(updatedOrder);
    }

    @Override
    @Transactional
    public String updateOrderMyBatis(Long id, OrderRequestDto orderDto) {
        // 기존 주문 조회
        Order existingOrder = orderMapper.findById(id);
        if (existingOrder == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + id);
        }

        // 사용자 엔티티 조회
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + orderDto.getUserId()));

        // 주문 기본 정보 업데이트
        Order updatedOrder = Order.builder()
                .id(id)
                .user(user)
                .orderDate(orderDto.getOrderDate() != null ? orderDto.getOrderDate() : existingOrder.getOrderDate())
                .status(orderDto.getStatus() != null ? orderDto.getStatus() : existingOrder.getStatus())
                .build();

        // MyBatis를 통해 주문 업데이트
        orderMapper.update(updatedOrder);

        // 기존 주문 상품 삭제 (MyBatis에서는 수동으로 관계 처리 필요)
        orderItemMapper.deleteByOrderId(id);

        // 새 주문 상품 추가
        if (orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
            for (OrderRequestDto.OrderItemRequestDto itemDto : orderDto.getOrderItems()) {
                // 상품 조회
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + itemDto.getProductId()));

                // 주문 상품 생성
                OrderItem orderItem = itemDto.toEntity(product);
                orderItem.assignOrder(updatedOrder);

                // MyBatis를 통해 주문 상품 저장
                orderItemMapper.insert(orderItem);
            }
        }

        return "주문이 MyBatis를 통해 성공적으로 업데이트되었습니다.";
    }

    @Override
    @Transactional
    public String deleteOrderJpa(Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderRepository.deleteById(id);
                    return "주문이 JPA를 통해 성공적으로 삭제되었습니다.";
                })
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID: " + id));
    }

    @Override
    @Transactional
    public String deleteOrderMyBatis(Long id) {
        Order existingOrder = orderMapper.findById(id);
        if (existingOrder == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + id);
        }

        // 주문 상품 삭제 (MyBatis에서는 수동으로 관계 처리 필요)
        orderItemMapper.deleteByOrderId(id);

        // 주문 삭제
        orderMapper.deleteById(id);

        return "주문이 MyBatis를 통해 성공적으로 삭제되었습니다.";
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order updatedOrder = orderRepository.updateOrderStatus(orderId, status);
        if (updatedOrder == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + orderId);
        }
        return OrderResponseDto.fromEntity(updatedOrder);
    }

    @Override
    public List<OrderResponseDto> findRecentOrders(int limit) {
        return orderRepository.findRecentOrders(limit).stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}