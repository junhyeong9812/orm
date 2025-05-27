package com.benchmark.orm.domain.order.controller;

import com.benchmark.orm.domain.order.dto.*;
import com.benchmark.orm.domain.order.entity.Order.OrderStatus;
import com.benchmark.orm.domain.order.service.OrderMyBatisService;
import com.benchmark.orm.global.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/mybatis/order")
@RequiredArgsConstructor
public class OrderMyBatisController {

    private final OrderMyBatisService orderMyBatisService;

    /**
     * 모든 주문 조회 (간단 버전)
     */
    @GetMapping
    public ResponseEntity<List<OrderSimpleDto>> getAllOrders() {
        log.info("[MyBatis] GET /api/mybatis/order - 모든 주문 조회 요청");
        List<OrderSimpleDto> orders = orderMyBatisService.findAll();
        return ResponseEntity.ok(orders);
    }

    /**
     * 모든 주문 조회 (상세 버전)
     */
    @GetMapping("/detailed")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersDetailed() {
        log.info("[MyBatis] GET /api/mybatis/order/detailed - 모든 주문 상세 조회 요청");
        List<OrderResponseDto> orders = orderMyBatisService.findAllDetailed();
        return ResponseEntity.ok(orders);
    }

    /**
     * ID로 주문 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        log.info("[MyBatis] GET /api/mybatis/order/{} - ID로 주문 조회 요청", id);
        OrderResponseDto order = orderMyBatisService.findById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * 사용자 ID로 주문 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderSimpleDto>> getOrdersByUserId(@PathVariable Long userId) {
        log.info("[MyBatis] GET /api/mybatis/order/user/{} - 사용자 ID로 주문 조회 요청", userId);
        List<OrderSimpleDto> orders = orderMyBatisService.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 상태로 주문 조회
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderSimpleDto>> getOrdersByStatus(@PathVariable OrderStatus status) {
        log.info("[MyBatis] GET /api/mybatis/order/status/{} - 주문 상태로 조회 요청", status);
        List<OrderSimpleDto> orders = orderMyBatisService.findByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * 사용자 ID와 주문 상태로 주문 조회
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<OrderSimpleDto>> getOrdersByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable OrderStatus status) {
        log.info("[MyBatis] GET /api/mybatis/order/user/{}/status/{} - 사용자 ID와 상태로 조회 요청", userId, status);
        List<OrderSimpleDto> orders = orderMyBatisService.findByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 날짜 범위로 주문 조회
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<OrderSimpleDto>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("[MyBatis] GET /api/mybatis/order/date-range - 날짜 범위 조회 요청: {} ~ {}", startDate, endDate);
        List<OrderSimpleDto> orders = orderMyBatisService.findByOrderDateBetween(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * 페이징 조회
     */
    @GetMapping("/paging")
    public ResponseEntity<PageDto<OrderSimpleDto>> getOrdersWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[MyBatis] GET /api/mybatis/order/paging - 페이징 조회 요청: page={}, size={}, sortBy={}, sortDirection={}",
                page, size, sortBy, sortDirection);
        PageDto<OrderSimpleDto> orders = orderMyBatisService.findAllWithPaging(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(orders);
    }

    /**
     * 정렬 조회
     */
    @GetMapping("/sorting")
    public ResponseEntity<List<OrderSimpleDto>> getOrdersWithSorting(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("[MyBatis] GET /api/mybatis/order/sorting - 정렬 조회 요청: sortBy={}, sortDirection={}",
                sortBy, sortDirection);
        List<OrderSimpleDto> orders = orderMyBatisService.findAllWithSorting(sortBy, sortDirection);
        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 상품 정보와 함께 주문 조회
     */
    @GetMapping("/{id}/with-order-items")
    public ResponseEntity<OrderResponseDto> getOrderWithOrderItems(@PathVariable Long id) {
        log.info("[MyBatis] GET /api/mybatis/order/{}/with-order-items - 주문 상품 정보와 함께 조회 요청", id);
        OrderResponseDto order = orderMyBatisService.findOrderWithOrderItems(id);
        return ResponseEntity.ok(order);
    }

    /**
     * 최근 주문 목록 조회
     */
    @GetMapping("/recent")
    public ResponseEntity<List<OrderSimpleDto>> getRecentOrders(
            @RequestParam(defaultValue = "10") int limit) {
        log.info("[MyBatis] GET /api/mybatis/order/recent - 최근 주문 조회 요청: limit={}", limit);
        List<OrderSimpleDto> orders = orderMyBatisService.findRecentOrders(limit);
        return ResponseEntity.ok(orders);
    }

    /**
     * 사용자별 총 주문 금액 계산
     */
    @GetMapping("/user/{userId}/total-amount")
    public ResponseEntity<Integer> getTotalOrderAmountByUserId(@PathVariable Long userId) {
        log.info("[MyBatis] GET /api/mybatis/order/user/{}/total-amount - 사용자별 총 주문 금액 계산 요청", userId);
        Integer totalAmount = orderMyBatisService.calculateTotalOrderAmountByUserId(userId);
        return ResponseEntity.ok(totalAmount);
    }

    /**
     * 검색 조건으로 주문 검색
     */
    @GetMapping("/search")
    public ResponseEntity<PageDto<OrderSimpleDto>> searchOrders(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        log.info("[MyBatis] GET /api/mybatis/order/search - 검색 요청: userId={}, status={}, startDate={}, endDate={}",
                userId, status, startDate, endDate);

        OrderSearchDto searchDto = OrderSearchDto.builder()
                .userId(userId)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        PageDto<OrderSimpleDto> orders = orderMyBatisService.searchOrders(searchDto, page, size);
        return ResponseEntity.ok(orders);
    }

    /**
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto requestDto) {
        log.info("[MyBatis] POST /api/mybatis/order - 주문 생성 요청: userId={}", requestDto.getUserId());
        OrderResponseDto order = orderMyBatisService.createOrder(requestDto);
        return ResponseEntity.ok(order);
    }

    /**
     * 주문 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDto requestDto) {
        log.info("[MyBatis] PUT /api/mybatis/order/{} - 주문 수정 요청", id);
        OrderResponseDto order = orderMyBatisService.updateOrder(id, requestDto);
        return ResponseEntity.ok(order);
    }

    /**
     * 주문 상태 변경
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        log.info("[MyBatis] PATCH /api/mybatis/order/{}/status - 주문 상태 변경 요청: {}", id, status);
        orderMyBatisService.updateOrderStatus(id, status);
        return ResponseEntity.ok().build();
    }

    /**
     * 주문 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("[MyBatis] DELETE /api/mybatis/order/{} - 주문 삭제 요청", id);
        orderMyBatisService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}