package com.benchmark.orm.domain.order.service;

import com.benchmark.orm.domain.order.dto.*;
import com.benchmark.orm.domain.order.entity.Order;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.mapper.OrderMapper;
import com.benchmark.orm.global.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderMyBatisService {

    private final OrderMapper orderMapper;

    /**
     * 모든 주문 조회 (간단 버전)
     */
    public List<OrderSimpleDto> findAll() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderMapper.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findAll - 실행시간: {}ms, 결과 수: {}", endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 모든 주문 조회 (상세 버전)
     */
    public List<OrderResponseDto> findAllDetailed() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderMapper.findAll();
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findAllDetailed - 실행시간: {}ms, 결과 수: {}", endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 주문 조회
     */
    public OrderResponseDto findById(Long id) {
        long startTime = System.currentTimeMillis();
        Order order = orderMapper.findById(id);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findById - 실행시간: {}ms", endTime - startTime);

        if (order == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + id);
        }
        return OrderResponseDto.fromEntity(order);
    }

    /**
     * 사용자 ID로 주문 조회
     */
    public List<OrderSimpleDto> findByUserId(Long userId) {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderMapper.findByUserId(userId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findByUserId - 실행시간: {}ms, 결과 수: {}",
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
        List<Order> orders = orderMapper.findByStatus(status.name());
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findByStatus - 실행시간: {}ms, 결과 수: {}",
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
        List<Order> orders = orderMapper.findByUserIdAndStatus(userId, status.name());
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findByUserIdAndStatus - 실행시간: {}ms, 결과 수: {}",
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
        List<Order> orders = orderMapper.findByOrderDateBetween(startDate, endDate);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findByOrderDateBetween - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 조회
     */
    public PageDto<OrderSimpleDto> findAllWithPaging(int page, int size, String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();

        int offset = page * size;
        List<Order> orders = orderMapper.findAllWithPagingAndSorting(offset, size, sortBy, sortDirection);

        // 전체 개수 조회
        List<Order> allOrders = orderMapper.findAll();
        long totalElements = allOrders.size();

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findAllWithPaging - 실행시간: {}ms, 결과 수: {}/{}",
                endTime - startTime, orders.size(), totalElements);

        List<OrderSimpleDto> content = orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());

        return PageDto.of(content, page, size, totalElements);
    }

    /**
     * 정렬 조회
     */
    public List<OrderSimpleDto> findAllWithSorting(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderMapper.findAllWithSorting(sortBy, sortDirection);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findAllWithSorting - 실행시간: {}ms, 결과 수: {}",
                endTime - startTime, orders.size());

        return orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 주문 상품 정보와 함께 주문 조회
     */
    public OrderResponseDto findOrderWithOrderItems(Long orderId) {
        long startTime = System.currentTimeMillis();
        Order order = orderMapper.findOrderWithOrderItems(orderId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findOrderWithOrderItems - 실행시간: {}ms", endTime - startTime);

        if (order == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + orderId);
        }
        return OrderResponseDto.fromEntityWithOrderItems(order);
    }

    /**
     * 최근 주문 목록 조회
     */
    public List<OrderSimpleDto> findRecentOrders(int limit) {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderMapper.findRecentOrders(limit);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order findRecentOrders - 실행시간: {}ms, 결과 수: {}",
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
        Integer totalAmount = orderMapper.calculateTotalOrderAmountByUserId(userId);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order calculateTotalOrderAmountByUserId - 실행시간: {}ms, 결과: {}",
                endTime - startTime, totalAmount);

        return totalAmount != null ? totalAmount : 0;
    }

    /**
     * 검색 조건으로 주문 검색
     */
    public PageDto<OrderSimpleDto> searchOrders(OrderSearchDto searchDto, int page, int size) {
        long startTime = System.currentTimeMillis();

        int offset = page * size;
        String sortBy = searchDto.getSortBy() != null ? searchDto.getSortBy() : "id";
        String sortDirection = searchDto.getSortDirection() != null ? searchDto.getSortDirection() : "asc";

        List<Order> orders = orderMapper.searchOrders(searchDto, offset, size, sortBy, sortDirection);
        int totalCount = orderMapper.countBySearchDto(searchDto);

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order searchOrders - 실행시간: {}ms, 결과 수: {}/{}",
                endTime - startTime, orders.size(), totalCount);

        List<OrderSimpleDto> content = orders.stream()
                .map(OrderSimpleDto::from)
                .collect(Collectors.toList());

        return PageDto.of(content, page, size, totalCount);
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
        orderMapper.insert(order);

        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order createOrder - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntity(order);
    }

    /**
     * 주문 수정
     */
    @Transactional
    public OrderResponseDto updateOrder(Long id, OrderRequestDto requestDto) {
        long startTime = System.currentTimeMillis();

        Order existingOrder = orderMapper.findById(id);
        if (existingOrder == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + id);
        }

        // 주문 정보 업데이트
        if (requestDto.getStatus() != null) {
            existingOrder.changeStatus(requestDto.getStatus());
        }
        if (requestDto.getOrderDate() != null) {
            existingOrder.changeOrderDate(requestDto.getOrderDate());
        }

        orderMapper.update(existingOrder);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order updateOrder - 실행시간: {}ms", endTime - startTime);

        return OrderResponseDto.fromEntity(existingOrder);
    }

    /**
     * 주문 상태 변경
     */
    @Transactional
    public void updateOrderStatus(Long id, OrderStatus status) {
        long startTime = System.currentTimeMillis();

        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + id);
        }

        orderMapper.updateStatus(id, status.name());
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order updateOrderStatus - 실행시간: {}ms", endTime - startTime);
    }

    /**
     * 주문 삭제
     */
    @Transactional
    public void deleteOrder(Long id) {
        long startTime = System.currentTimeMillis();

        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new RuntimeException("주문을 찾을 수 없습니다. ID: " + id);
        }

        orderMapper.deleteById(id);
        long endTime = System.currentTimeMillis();

        log.info("[MyBatis] Order deleteOrder - 실행시간: {}ms", endTime - startTime);
    }
}