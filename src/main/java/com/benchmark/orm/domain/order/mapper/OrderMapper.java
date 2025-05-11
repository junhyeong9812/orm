package com.benchmark.orm.domain.order.mapper;

import com.benchmark.orm.domain.order.dto.OrderSearchDto;
import com.benchmark.orm.domain.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 매퍼 인터페이스
 */
@Mapper
public interface OrderMapper {
    /**
     * 주문 저장
     *
     * @param order 저장할 주문 정보
     */
    void insert(Order order);

    /**
     * 주문 수정
     *
     * @param order 수정할 주문 정보
     */
    void update(Order order);

    /**
     * 주문 상태 수정
     *
     * @param id 주문 ID
     * @param status 변경할 상태
     */
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 주문 삭제
     *
     * @param id 삭제할 주문 ID
     */
    void deleteById(Long id);

    /**
     * 주문 조회
     *
     * @param id 조회할 주문 ID
     * @return 주문 정보
     */
    Order findById(Long id);

    /**
     * 사용자 ID로 주문 조회
     *
     * @param userId 사용자 ID
     * @return 주문 목록
     */
    List<Order> findByUserId(Long userId);

    /**
     * 주문 상태로 주문 조회
     *
     * @param status 주문 상태
     * @return 주문 목록
     */
    List<Order> findByStatus(String status);

    /**
     * 사용자 ID와 주문 상태로 주문 조회
     *
     * @param userId 사용자 ID
     * @param status 주문 상태
     * @return 주문 목록
     */
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 주문 날짜 범위로 주문 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 주문 목록
     */
    List<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * 모든 주문 조회
     *
     * @return 주문 목록
     */
    List<Order> findAll();

    /**
     * 페이징된 주문 조회
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return 주문 목록
     */
    List<Order> findAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 정렬된 주문 조회
     *
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 주문 목록
     */
    List<Order> findAllWithSorting(@Param("sortColumn") String sortColumn, @Param("sortDirection") String sortDirection);

    /**
     * 페이징 및 정렬된 주문 조회
     *
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 주문 목록
     */
    List<Order> findAllWithPagingAndSorting(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection);

    /**
     * 사용자별 주문 총 금액 계산
     *
     * @param userId 사용자 ID
     * @return 총 주문 금액
     */
    Integer calculateTotalOrderAmountByUserId(Long userId);

    /**
     * 검색 조건을 이용한 주문 검색
     *
     * @param searchDto 검색 조건 DTO
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @param sortColumn 정렬 컬럼
     * @param sortDirection 정렬 방향 (asc/desc)
     * @return 검색된 주문 리스트
     */
    List<Order> searchOrders(
            @Param("searchDto") OrderSearchDto searchDto,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection);

    /**
     * 검색 조건을 이용한 주문 총 개수 조회
     *
     * @param searchDto 검색 조건 DTO
     * @return 검색된 주문 총 개수
     */
    int countBySearchDto(@Param("searchDto") OrderSearchDto searchDto);

    /**
     * 주문과 주문 상품 정보 함께 조회
     *
     * @param id 주문 ID
     * @return 주문 정보
     */
    Order findOrderWithOrderItems(Long id);

    /**
     * 최근 주문 목록 조회
     *
     * @param limit 최대 개수
     * @return 주문 목록
     */
    List<Order> findRecentOrders(int limit);
}