package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long userId) {
        log.info("Создание бронирования для пользователя {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        validateBookingCreation(item, userId);

        Booking booking = bookingMapper.toBooking(bookingRequestDto, userId);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        validateBookingTime(booking);
        validateBookingDatesNotOverlap(booking);

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Бронирование создано: {}", savedBooking.getId());
        return bookingMapper.toBookingResponseDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto approveBooking(Long bookingId, Boolean approved, Long userId) {
        log.info("Подтверждение бронирования {} пользователем {}", bookingId, userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new NotOwnerException("Подтверждать бронирование может только владелец вещи");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new AlreadyProcessedException("Бронирование уже было обработано");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingResponseDto(updatedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!isUserOwnerOrBooker(booking, userId)) {
            throw new NotAuthorizedException("Просмотр бронирования доступен только автору или владельцу вещи");
        }

        return bookingMapper.toBookingResponseDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getUserBookings(String state, Long userId, Integer from, Integer size) {
        validatePaginationParams(from, size);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        BookingState bookingState = BookingState.fromString(state)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + state));

        PageRequest page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        Page<Booking> bookingsPage = getBookingsPageForUser(userId, bookingState, page);

        return bookingsPage.getContent().stream()
                .map(bookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getOwnerBookings(String state, Long userId, Integer from, Integer size) {
        validatePaginationParams(from, size);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        BookingState bookingState = BookingState.fromString(state)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + state));

        PageRequest page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        Page<Booking> bookingsPage = getBookingsPageForOwner(userId, bookingState, page);

        return bookingsPage.getContent().stream()
                .map(bookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    private void validateBookingCreation(Item item, Long userId) {
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Вещь недоступна для бронирования");
        }
        if (item.getOwnerId().equals(userId)) {
            throw new SelfBookingException("Нельзя забронировать свою вещь");
        }
    }

    private void validateBookingTime(Booking booking) {
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new InvalidBookingTimeException("Дата начала бронирования должна быть раньше даты окончания");
        }
        if (booking.getStart().equals(booking.getEnd())) {
            throw new InvalidBookingTimeException("Даты начала и окончания бронирования не могут совпадать");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new InvalidBookingTimeException("Дата начала бронирования не может быть в прошлом");
        }
    }

    private void validateBookingDatesNotOverlap(Booking booking) {
        boolean hasOverlapping = bookingRepository.existsByItemIdAndDateRange(
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd(),
                BookingStatus.APPROVED);
        if (hasOverlapping) {
            throw new ConflictException("Вещь уже забронирована на указанные даты");
        }
    }

    private boolean isUserOwnerOrBooker(Booking booking, Long userId) {
        return booking.getBooker().getId().equals(userId) ||
                booking.getItem().getOwnerId().equals(userId);
    }

    private void validatePaginationParams(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Некорректные параметры пагинации");
        }
    }

    private Page<Booking> getBookingsPageForUser(Long userId, BookingState state, PageRequest page) {
        return switch (state) {
            case CURRENT -> bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(
                    userId, LocalDateTime.now(), LocalDateTime.now(), page);
            case PAST -> bookingRepository.findByBookerIdAndEndBefore(
                    userId, LocalDateTime.now(), page);
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfter(
                    userId, LocalDateTime.now(), page);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(
                    userId, BookingStatus.WAITING, page);
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(
                    userId, BookingStatus.REJECTED, page);
            default -> // ALL
                    bookingRepository.findByBookerId(userId, page);
        };
    }

    private Page<Booking> getBookingsPageForOwner(Long ownerId, BookingState state, PageRequest page) {
        return switch (state) {
            case CURRENT -> bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(
                    ownerId, LocalDateTime.now(), LocalDateTime.now(), page);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBefore(
                    ownerId, LocalDateTime.now(), page);
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartAfter(
                    ownerId, LocalDateTime.now(), page);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatus(
                    ownerId, BookingStatus.WAITING, page);
            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatus(
                    ownerId, BookingStatus.REJECTED, page);
            default -> // ALL
                    bookingRepository.findByItemOwnerId(ownerId, page);
        };
    }
}