package com.benchmark.orm.domain.order.service;

import com.benchmark.orm.domain.order.dto.*;
import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderJpaService {

    private final OrderRepository orderRepository;

    /**
     * 모든 주문 조회 (간단 버전)
     */
    public List<OrderSimpleDto> findAll() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderRepository.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findAll - 실행시간: {}ms, 결과 수: {}", endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 모든 주문 조회 (상세 버전)
     */
    public List<OrderResponseDto> findAllDetailed() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderRepository.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findAllDetailed - 실행시간: {}ms, 결과 수: {}", endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 주문 조회
     */
    public OrderResponseDto findById(Long id) {
        long startTime = System.currentTimeMillis();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID: " + id));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findById - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntity(order);
    }

    /**
     * 사용자 ID로 주문 조회
     */
    public List<OrderSimpleDto> findByUserId(Long userId) {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderRepository.findByUserId(userId);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findByUserId - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 주문 상태로 주문 조회
     */
    public List<OrderSimpleDto> findByStatus(OrderStatus status) {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderRepository.findByStatus(status);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findByStatus - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 ID와 주문 상태로 주문 조회
     */
    public List<OrderSimpleDto> findByUserIdAndStatus(Long userId, OrderStatus status) {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderRepository.findByUserIdAndStatus(userId, status);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findByUserIdAndStatus - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 주문 날짜 범위로 주문 조회
     */
    public List<OrderSimpleDto> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findByOrderDateBetween - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 조회
     */
    public Page<OrderSimpleDto> findAllWithPaging(int page, int size, String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findAll(pageable);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findAllWithPaging - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.getTotalElements());

        return orders.map(OrderSimpleDto::from);
    }

    /**
     * 정렬 조회
     */
    public List<OrderSimpleDto> findAllWithSorting(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        List<Order> orders = orderRepository.findAll(sort);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findAllWithSorting - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 정보와 함께 주문 조회
     */
    public OrderResponseDto findOrderWithUser(Long orderId) {
        long startTime = System.currentTimeMillis();
        Order order = orderRepository.findOrderWithUser(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID: " + orderId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findOrderWithUser - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntityWithUser(order);
    }

    /**
     * 주문 상품 정보와 함께 주문 조회
     */
    public OrderResponseDto findOrderWithOrderItems(Long orderId) {
        long startTime = System.currentTimeMillis();
        Order order = orderRepository.findOrderWithOrderItems(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID: " + orderId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findOrderWithOrderItems - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntityWithOrderItems(order);
    }

    /**
     * 사용자 및 주문 상품 정보와 함께 주문 조회
     */
    public OrderResponseDto findOrderWithUserAndOrderItems(Long orderId) {
        long startTime = System.currentTimeMillis();
        Order order = orderRepository.findOrderWithUserAndOrderItems(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID: " + orderId));
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findOrderWithUserAndOrderItems - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntityWithUserAndOrderItems(order);
    }

    /**
     * 최근 주문 목록 조회
     */
    public List<OrderSimpleDto> findRecentOrders(int limit) {
        long startTime = System.currentTimeMillis();
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "orderDate"));
        List<Order> orders = orderRepository.findAll(pageable).getContent();
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order findRecentOrders - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자별 총 주문 금액 계산
     */
    public Integer calculateTotalOrderAmountByUserId(Long userId) {
        long startTime = System.currentTimeMillis();
        Integer totalAmount = orderRepository.calculateTotalOrderAmountByUserId(userId);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order calculateTotalOrderAmountByUserId - 실행시간: {}ms, 결과: {}",
                endTime - startTime, totalAmount);

        return totalAmount != null ? totalAmount : 0;
    }

    /**
     * 검색 조건으로 주문 검색
     */
    public Page<OrderSimpleDto> searchOrders(OrderSearchDto searchDto, int page, int size) {
        long startTime = System.currentTimeMillis();

        String sortBy = searchDto.getSortBy() != null ? searchDto.getSortBy() : "id";
        String sortDirection = searchDto.getSortDirection() != null ? searchDto.getSortDirection() : "asc";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.searchOrders(searchDto, pageable);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order searchOrders - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.getTotalElements());

        return orders.map(OrderSimpleDto::from);
    }

    /**
     * 주문 생성
     */
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        // 사용자 정보 확인 후 주문 생성 로직 구현 필요
        // 여기서는 간단하게 처리
        Order order = requestDto.toEntity(null); // 실제로는 사용자 조회 필요
        Order savedOrder = orderRepository.save(order);

        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order createOrder - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntity(savedOrder);
    }

    /**
     * 주문 수정
     */
    @Transactional
    public OrderResponseDto updateOrder(Long id, OrderRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID: " + id));

        // 주문 정보 업데이트
        if (requestDto.getStatus() != null) {
            order.changeStatus(requestDto.getStatus());
        }
        if (requestDto.getOrderDate() != null) {
            order.changeOrderDate(requestDto.getOrderDate());
        }

        Order savedOrder = orderRepository.save(order);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order updateOrder - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntity(savedOrder);
    }

    /**
     * 주문 상태 변경
     */
    @Transactional
    public OrderResponseDto updateOrderStatus(Long id, OrderStatus status) {
        long startTime = System.currentTimeMillis();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID: " + id));

        order.changeStatus(status);
        Order savedOrder = orderRepository.save(order);

        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order updateOrderStatus - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntity(savedOrder);
    }

    /**
     * 주문 삭제
     */
    @Transactional
    public void deleteOrder(Long id) {
        long startTime = System.currentTimeMillis();

        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + id);
        }

        orderRepository.deleteById(id);
        long endTime = System.currentTimeMillis();

        log.info("[JPA] Order deleteOrder - 실행시간: {}ms", endTime - startTime);
    }
}