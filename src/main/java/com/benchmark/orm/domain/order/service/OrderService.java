package com.benchmark.orm.domain.order.service;

import com.benchmark.orm.domain.order.dto.OrderRequestDto;
import com.benchmark.orm.domain.order.dto.OrderResponseDto;
import com.benchmark.orm.domain.order.dto.OrderSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 주문 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface OrderService {

    /**
     * JPA를 사용하여 주문 저장
     * @param orderDto 저장할 주문 DTO
     * @return 저장된 주문 응답 DTO
     */
    OrderResponseDto saveOrderJpa(OrderRequestDto orderDto);

    /**
     * MyBatis를 사용하여 주문 저장
     * @param orderDto 저장할 주문 DTO
     * @return 결과 메시지
     */
    String saveOrderMyBatis(OrderRequestDto orderDto);

    /**
     * JPA를 사용하여 ID로 주문 조회
     * @param id 주문 ID
     * @return 주문 응답 DTO Optional 객체
     */
    Optional<OrderResponseDto> findOrderByIdJpa(Long id);

    /**
     * MyBatis를 사용하여 ID로 주문 조회
     * @param id 주문 ID
     * @return 주문 응답 DTO
     */
    OrderResponseDto findOrderByIdMyBatis(Long id);

    /**
     * JPA를 사용하여 모든 주문 조회
     * @return 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findAllOrdersJpa();

    /**
     * MyBatis를 사용하여 모든 주문 조회
     * @return 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findAllOrdersMyBatis();

    /**
     * JPQL을 사용하여 사용자 ID로 주문 조회
     * @param userId 사용자 ID
     * @return 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersByUserIdJpql(Long userId);

    /**
     * QueryDSL을 사용하여 사용자 ID로 주문 조회
     * @param userId 사용자 ID
     * @return 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersByUserIdQueryDsl(Long userId);

    /**
     * JPQL을 사용하여 상품 ID로 주문 조회
     * @param productId 상품 ID
     * @return 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersByProductIdJpql(Long productId);

    /**
     * QueryDSL을 사용하여 상품 ID로 주문 조회
     * @param productId 상품 ID
     * @return 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersByProductIdQueryDsl(Long productId);

    /**
     * JPQL을 사용하여 주문 날짜 범위로 주문 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersByOrderDateBetweenJpql(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * QueryDSL을 사용하여 주문 날짜 범위로 주문 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersByOrderDateBetweenQueryDsl(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * JPA를 사용하여 페이징으로 주문 조회
     * @param pageable 페이징 정보
     * @return 페이징된 주문 응답 DTO 객체
     */
    Page<OrderResponseDto> findOrdersWithPagingJpa(Pageable pageable);

    /**
     * QueryDSL을 사용하여 페이징으로 주문 조회
     * @param pageable 페이징 정보
     * @return 페이징된 주문 응답 DTO 객체
     */
    Page<OrderResponseDto> findOrdersWithPagingQueryDsl(Pageable pageable);

    /**
     * MyBatis를 사용하여 페이징으로 주문 조회
     * @param offset 시작 위치
     * @param limit 데이터 개수
     * @return 페이징된 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersWithPagingMyBatis(int offset, int limit);

    /**
     * JPA를 사용하여 정렬로 주문 조회
     * @param sort 정렬 정보
     * @return 정렬된 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersWithSortingJpa(Sort sort);

    /**
     * QueryDSL을 사용하여 정렬로 주문 조회
     * @param sort 정렬 정보
     * @return 정렬된 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersWithSortingQueryDsl(Sort sort);

    /**
     * MyBatis를 사용하여 정렬로 주문 조회
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향
     * @return 정렬된 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersWithSortingMyBatis(String sortColumn, String sortDirection);

    /**
     * JPA를 사용하여 페이징 및 정렬로 주문 조회
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징 및 정렬된 주문 응답 DTO 객체
     */
    Page<OrderResponseDto> findOrdersWithPagingAndSortingJpa(Pageable pageable);

    /**
     * MyBatis를 사용하여 페이징 및 정렬로 주문 조회
     * @param offset 시작 위치
     * @param limit 데이터 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향
     * @return 페이징 및 정렬된 주문 응답 DTO 리스트
     */
    List<OrderResponseDto> findOrdersWithPagingAndSortingMyBatis(int offset, int limit, String sortColumn, String sortDirection);

    /**
     * JPQL을 사용하여 사용자 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 응답 DTO Optional 객체
     */
    Optional<OrderResponseDto> findOrderWithUserJpql(Long orderId);

    /**
     * QueryDSL을 사용하여 사용자 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 응답 DTO Optional 객체
     */
    Optional<OrderResponseDto> findOrderWithUserQueryDsl(Long orderId);

    /**
     * JPQL을 사용하여 상품 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 응답 DTO Optional 객체
     */
    Optional<OrderResponseDto> findOrderWithProductJpql(Long orderId);

    /**
     * QueryDSL을 사용하여 상품 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 응답 DTO Optional 객체
     */
    Optional<OrderResponseDto> findOrderWithProductQueryDsl(Long orderId);

    /**
     * JPQL을 사용하여 사용자 및 상품 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 응답 DTO Optional 객체
     */
    Optional<OrderResponseDto> findOrderWithUserAndProductJpql(Long orderId);

    /**
     * QueryDSL을 사용하여 사용자 및 상품 정보와 함께 주문 조회
     * @param orderId 주문 ID
     * @return 주문 응답 DTO Optional 객체
     */
    Optional<OrderResponseDto> findOrderWithUserAndProductQueryDsl(Long orderId);

    /**
     * QueryDSL을 사용하여 사용자 ID로 주문을 페이징하여 조회
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 페이징된 주문 응답 DTO 객체
     */
    Page<OrderResponseDto> findOrdersByUserIdWithPagingQueryDsl(Long userId, Pageable pageable);

    /**
     * JPQL을 사용한 주문 검색
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 페이징된 주문 응답 DTO 객체
     */
    Page<OrderResponseDto> searchOrdersJpql(OrderSearchDto searchDto, Pageable pageable);

    /**
     * QueryDSL을 사용한 주문 검색
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 페이징된 주문 응답 DTO 객체
     */
    Page<OrderResponseDto> searchOrdersQueryDsl(OrderSearchDto searchDto, Pageable pageable);

    /**
     * MyBatis를 사용한 주문 검색
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 데이터 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향
     * @return 페이징 및 정렬된 주문 응답 DTO 리스트와 총 개수
     */
    Page<OrderResponseDto> searchOrdersMyBatis(OrderSearchDto searchDto, int offset, int limit,
                                               String sortColumn, String sortDirection);

    /**
     * JPA를 사용하여 주문 정보 업데이트
     * @param id 주문 ID
     * @param orderDto 업데이트할 주문 DTO
     * @return 업데이트된 주문 응답 DTO
     */
    OrderResponseDto updateOrderJpa(Long id, OrderRequestDto orderDto);

    /**
     * MyBatis를 사용하여 주문 정보 업데이트
     * @param id 주문 ID
     * @param orderDto 업데이트할 주문 DTO
     * @return 결과 메시지
     */
    String updateOrderMyBatis(Long id, OrderRequestDto orderDto);

    /**
     * JPA를 사용하여 주문 삭제
     * @param id 삭제할 주문 ID
     * @return 결과 메시지
     */
    String deleteOrderJpa(Long id);

    /**
     * MyBatis를 사용하여 주문 삭제
     * @param id 삭제할 주문 ID
     * @return 결과 메시지
     */
    String deleteOrderMyBatis(Long id);
}