package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBookerId(Long bookerId, Pageable pageable);

    Page<Booking> findByBookerIdAndStartBeforeAndEndAfter(
            Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBookerIdAndEndBefore(
            Long bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBookerIdAndStartAfter(
            Long bookerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findByBookerIdAndStatus(
            Long bookerId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItemOwnerId(Long ownerId, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(
            Long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndBefore(
            Long ownerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartAfter(
            Long ownerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatus(
            Long ownerId, BookingStatus status, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.item.id = :itemId " +
            "AND b.end < :currentTime " +
            "AND b.status = 'APPROVED'")
    boolean existsByBookerIdAndItemIdAndEndBefore(
            @Param("bookerId") Long bookerId,
            @Param("itemId") Long itemId,
            @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.status = :status " +
            "AND ((b.start BETWEEN :start AND :end) OR " +
            "(b.end BETWEEN :start AND :end) OR " +
            "(b.start <= :start AND b.end >= :end))")
    boolean existsByItemIdAndDateRange(
            @Param("itemId") Long itemId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") BookingStatus status);

    List<Booking> findByItemIdIn(List<Long> itemIds);

    List<Booking> findByItemIdAndStatus(Long itemId, BookingStatus status);


}