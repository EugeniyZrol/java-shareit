package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequestorIdOrderByCreatedDesc(Long requestorId);

    @Query("SELECT r FROM Request r WHERE r.requestor.id != :userId ORDER BY r.created DESC")
    Page<Request> findAllByRequestorIdNot(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.requestor.id != :userId ORDER BY r.created DESC")
    List<Request> findAllByRequestorIdNot(@Param("userId") Long userId);
}