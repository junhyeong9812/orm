package com.benchmark.orm.domain.order.controller;

import com.benchmark.orm.domain.order.dto.OrderRequestDto;
import com.benchmark.orm.domain.order.dto.OrderResponseDto;
import com.benchmark.orm.domain.order.dto.OrderSearchDto;
import com.benchmark.orm.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 주문 관련 API 컨트롤러
 * 각 ORM 기술별 성능 비교를 위한 엔드포인트 제공
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * JPA로 주문 생성
     * @param orderRequestDto 주문 요청 DTO
     * @return 생성된 주문 정보
     */
    @PostMapping("/jpa")
    public ResponseEntity<OrderResponseDto> createOrderJpa(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto savedOrder = orderService.saveOrderJpa(orderRequestDto);
        return ResponseEntity.ok(savedOrder);
    }

    /**
     * MyBatis로 주문 생성
     * @param orderRequestDto 주문 요청 DTO
     * @return 결과 메시지
     */
    @PostMapping("/mybatis")
    public ResponseEntity<Map<String, String>> createOrderMyBatis(@RequestBody OrderRequestDto orderRequestDto) {
        String result = orderService.saveOrderMyBatis(orderRequestDto);
        return ResponseEntity.ok(Map.of("message", result));
    }

    /**
     * JPA로 ID별 주문 조회
     * @param id 주문 ID
     * @return 주문 정보
     */
    @GetMapping("/jpa/{id}")
    public ResponseEntity<OrderResponseDto> getOrderByIdJpa(@PathVariable Long id) {
        return orderService.findOrderByIdJpa(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * MyBatis로 ID별 주문 조회
     * @param id 주문 ID
     * @return 주문 정보
     */
    @GetMapping("/mybatis/{id}")
    public ResponseEntity<OrderResponseDto> getOrderByIdMyBatis(@PathVariable Long id) {
        OrderResponseDto order = orderService.findOrderByIdMyBatis(id);
        return order != null
                ? ResponseEntity.ok(order)
                : ResponseEntity.notFound().build();
    }

    /**
     * JPA로 모든 주문 조회
     * @return 주문 목록
     */
    @GetMapping("/jpa")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersJpa() {
        return ResponseEntity.ok(orderService.findAllOrdersJpa());
    }

    /**
     * MyBatis로 모든 주문 조회
     * @return 주문 목록
     */
    @GetMapping("/mybatis")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersMyBatis() {
        return ResponseEntity.ok(orderService.findAllOrdersMyBatis());
    }

    /**
     * JPQL로 사용자 ID별 주문 조회
     * @param userId 사용자 ID
     * @return 주문 목록
     */
    @GetMapping("/jpql/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserIdJpql(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.findOrdersByUserIdJpql(userId));
    }

    /**
     * QueryDSL로 사용자 ID별 주문 조회
     * @param userId 사용자 ID
     * @return 주문 목록
     */
    @GetMapping("/querydsl/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserIdQueryDsl(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.findOrdersByUserIdQueryDsl(userId));
    }

    /**
     * JPQL로 상품 ID별 주문 조회
     * @param productId 상품 ID
     * @return 주문 목록
     */
    @GetMapping("/jpql/product/{productId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByProductIdJpql(@PathVariable Long productId) {
        return ResponseEntity.ok(orderService.findOrdersByProductIdJpql(productId));
    }

    /**
     * QueryDSL로 상품 ID별 주문 조회
     * @param productId 상품 ID
     * @return 주문 목록
     */
    @GetMapping("/querydsl/product/{productId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByProductIdQueryDsl(@PathVariable Long productId) {
        return ResponseEntity.ok(orderService.findOrdersByProductIdQueryDsl(productId));
    }

    /**
     * JPQL로 주문 날짜 범위별 주문 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 목록
     */
    @GetMapping("/jpql/date-range")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByOrderDateRangeJpql(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(orderService.findOrdersByOrderDateBetweenJpql(startDate, endDate));
    }

    /**
     * QueryDSL로 주문 날짜 범위별 주문 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 목록
     */
    @GetMapping("/querydsl/date-range")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByOrderDateRangeQueryDsl(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(orderService.findOrdersByOrderDateBetweenQueryDsl(startDate, endDate));
    }

    /**
     * JPA로 페이징된 주문 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 주문 정보
     */
    @GetMapping("/jpa/paging")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersWithPagingJpa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.findOrdersWithPagingJpa(PageRequest.of(page, size)));
    }

    /**
     * QueryDSL로 페이징된 주문 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 주문 정보
     */
    @GetMapping("/querydsl/paging")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersWithPagingQueryDsl(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.findOrdersWithPagingQueryDsl(PageRequest.of(page, size)));
    }

    /**
     * MyBatis로 페이징된 주문 조회
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 페이징된 주문 정보
     */
    @GetMapping("/mybatis/paging")
    public ResponseEntity<List<OrderResponseDto>> getOrdersWithPagingMyBatis(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(orderService.findOrdersWithPagingMyBatis(offset, limit));
    }

    /**
     * JPA로 정렬된 주문 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 주문 정보
     */
    @GetMapping("/jpa/sorting")
    public ResponseEntity<List<OrderResponseDto>> getOrdersWithSortingJpa(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return ResponseEntity.ok(orderService.findOrdersWithSortingJpa(sort));
    }

    /**
     * QueryDSL로 정렬된 주문 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 주문 정보
     */
    @GetMapping("/querydsl/sorting")
    public ResponseEntity<List<OrderResponseDto>> getOrdersWithSortingQueryDsl(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return ResponseEntity.ok(orderService.findOrdersWithSortingQueryDsl(sort));
    }

    /**
     * MyBatis로 정렬된 주문 조회
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 정렬된 주문 정보
     */
    @GetMapping("/mybatis/sorting")
    public ResponseEntity<List<OrderResponseDto>> getOrdersWithSortingMyBatis(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(orderService.findOrdersWithSortingMyBatis(sortBy, direction));
    }

    /**
     * JPA로 페이징 및 정렬된 주문 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 페이징 및 정렬된 주문 정보
     */
    @GetMapping("/jpa/paging-sorting")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersWithPagingAndSortingJpa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        return ResponseEntity.ok(orderService.findOrdersWithPagingAndSortingJpa(
                PageRequest.of(page, size, Sort.by(sortDirection, sortBy))));
    }

    /**
     * MyBatis로 페이징 및 정렬된 주문 조회
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 페이징 및 정렬된 주문 정보
     */
    @GetMapping("/mybatis/paging-sorting")
    public ResponseEntity<List<OrderResponseDto>> getOrdersWithPagingAndSortingMyBatis(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(orderService.findOrdersWithPagingAndSortingMyBatis(
                offset, limit, sortBy, direction));
    }

    /**
     * JPQL로 사용자 정보와 함께 주문 조회
     * @param id 주문 ID
     * @return 사용자 정보가 포함된 주문 정보
     */
    @GetMapping("/jpql/{id}/with-user")
    public ResponseEntity<OrderResponseDto> getOrderWithUserJpql(@PathVariable Long id) {
        return orderService.findOrderWithUserJpql(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 사용자 정보와 함께 주문 조회
     * @param id 주문 ID
     * @return 사용자 정보가 포함된 주문 정보
     */
    @GetMapping("/querydsl/{id}/with-user")
    public ResponseEntity<OrderResponseDto> getOrderWithUserQueryDsl(@PathVariable Long id) {
        return orderService.findOrderWithUserQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 상품 정보와 함께 주문 조회
     * @param id 주문 ID
     * @return 상품 정보가 포함된 주문 정보
     */
    @GetMapping("/jpql/{id}/with-product")
    public ResponseEntity<OrderResponseDto> getOrderWithProductJpql(@PathVariable Long id) {
        return orderService.findOrderWithProductJpql(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 상품 정보와 함께 주문 조회
     * @param id 주문 ID
     * @return 상품 정보가 포함된 주문 정보
     */
    @GetMapping("/querydsl/{id}/with-product")
    public ResponseEntity<OrderResponseDto> getOrderWithProductQueryDsl(@PathVariable Long id) {
        return orderService.findOrderWithProductQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * JPQL로 사용자 및 상품 정보와 함께 주문 조회
     * @param id 주문 ID
     * @return 사용자 및 상품 정보가 포함된 주문 정보
     */
    @GetMapping("/jpql/{id}/with-user-product")
    public ResponseEntity<OrderResponseDto> getOrderWithUserAndProductJpql(@PathVariable Long id) {
        return orderService.findOrderWithUserAndProductJpql(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 사용자 및 상품 정보와 함께 주문 조회
     * @param id 주문 ID
     * @return 사용자 및 상품 정보가 포함된 주문 정보
     */
    @GetMapping("/querydsl/{id}/with-user-product")
    public ResponseEntity<OrderResponseDto> getOrderWithUserAndProductQueryDsl(@PathVariable Long id) {
        return orderService.findOrderWithUserAndProductQueryDsl(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * QueryDSL로 사용자 ID별 페이징된 주문 조회
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 주문 정보
     */
    @GetMapping("/querydsl/user/{userId}/paging")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByUserIdWithPagingQueryDsl(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(orderService.findOrdersByUserIdWithPagingQueryDsl(
                userId, PageRequest.of(page, size)));
    }

    /**
     * 검색 조건으로 JPQL을 사용한 주문 검색
     * @param searchDto 검색 조건 DTO
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 검색된 주문 목록
     */
    @PostMapping("/jpql/search")
    public ResponseEntity<Page<OrderResponseDto>> searchOrdersJpql(
            @RequestBody OrderSearchDto searchDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return ResponseEntity.ok(orderService.searchOrdersJpql(searchDto, PageRequest.of(page, size, sort)));
    }

    /**
     * 검색 조건으로 QueryDSL을 사용한 주문 검색
     * @param searchDto 검색 조건 DTO
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 검색된 주문 목록
     */
    @PostMapping("/querydsl/search")
    public ResponseEntity<Page<OrderResponseDto>> searchOrdersQueryDsl(
            @RequestBody OrderSearchDto searchDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return ResponseEntity.ok(orderService.searchOrdersQueryDsl(searchDto, PageRequest.of(page, size, sort)));
    }

    /**
     * 검색 조건으로 MyBatis를 사용한 주문 검색
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 제한
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return 검색된 주문 목록
     */
    @PostMapping("/mybatis/search")
    public ResponseEntity<Page<OrderResponseDto>> searchOrdersMyBatis(
            @RequestBody OrderSearchDto searchDto,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(orderService.searchOrdersMyBatis(searchDto, offset, limit, sortBy, direction));
    }

    /**
     * JPA로 주문 정보 업데이트
     * @param id 주문 ID
     * @param orderRequestDto 업데이트할 주문 DTO
     * @return 업데이트된 주문 정보
     */
    @PutMapping("/jpa/{id}")
    public ResponseEntity<OrderResponseDto> updateOrderJpa(@PathVariable Long id, @RequestBody OrderRequestDto orderRequestDto) {
        try {
            OrderResponseDto updatedOrder = orderService.updateOrderJpa(id, orderRequestDto);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * MyBatis로 주문 정보 업데이트
     * @param id 주문 ID
     * @param orderRequestDto 업데이트할 주문 DTO
     * @return 결과 메시지
     */
    @PutMapping("/mybatis/{id}")
    public ResponseEntity<Map<String, String>> updateOrderMyBatis(@PathVariable Long id, @RequestBody OrderRequestDto orderRequestDto) {
        try {
            String result = orderService.updateOrderMyBatis(id, orderRequestDto);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * JPA로 주문 삭제
     * @param id 주문 ID
     * @return 결과 메시지
     */
    @DeleteMapping("/jpa/{id}")
    public ResponseEntity<Map<String, String>> deleteOrderJpa(@PathVariable Long id) {
        try {
            String result = orderService.deleteOrderJpa(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * MyBatis로 주문 삭제
     * @param id 주문 ID
     * @return 결과 메시지
     */
    @DeleteMapping("/mybatis/{id}")
    public ResponseEntity<Map<String, String>> deleteOrderMyBatis(@PathVariable Long id) {
        try {
            String result = orderService.deleteOrderMyBatis(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}