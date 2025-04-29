package com.laioffer.staybooking.booking;


import com.laioffer.staybooking.model.BookingDto;
import com.laioffer.staybooking.model.BookingEntity;
import com.laioffer.staybooking.model.ListingEntity;
import com.laioffer.staybooking.repository.BookingRepository;
import com.laioffer.staybooking.repository.ListingRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;


@Service
public class BookingService {


    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;


    public BookingService(
            BookingRepository bookingRepository,
            ListingRepository listingRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.listingRepository = listingRepository;
    }

    // find all bookings for a given guest id
    // .stream() to convert List<BookingEntity> to List<BookingDto>
    // for (BookingEntity booking : bookings)
    public List<BookingDto> findBookingsByGuestId(long guestId) {
        return bookingRepository.findAllByGuestId(guestId)
                .stream()// .stream() replaced for loop
                .map(BookingDto::new) // new BookingDto(entity)
                .toList();
    }


    public List<BookingDto> findBookingsByListingId(long hostId, long listingId) {
        ListingEntity listing = listingRepository.getReferenceById(listingId);
        if (listing.getHostId() != hostId) {
            throw new ListingBookingsNotAllowedException(hostId, listingId);
        }
        return bookingRepository.findAllByListingId(listingId)
                .stream()
                .map(BookingDto::new)
                .toList();
    }

    // no need transaction because there is only 1 write operation
    public void createBooking(long guestId, long listingId, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) {
            throw new InvalidBookingException("Check-in date must be before check-out date.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Check-in date must be in the future.");
        }
        List<BookingEntity> overlappedBookings = bookingRepository.findOverlappedBookings(listingId, checkIn, checkOut);
        if (!overlappedBookings.isEmpty()) {
            throw new InvalidBookingException("Booking dates conflict, please select different dates.");
        }
        // passed all check then write
        bookingRepository.save(new BookingEntity(null, guestId, listingId, checkIn, checkOut));
    }


    public void deleteBooking(long guestId, long bookingId) {
        BookingEntity booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getGuestId() != guestId) { // only allow to delete the booking that belongs to the given guest id
            throw new DeleteBookingNotAllowedException(guestId, bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }


    public boolean existsActiveBookings(long listingId) {
        return bookingRepository.existsByListingIdAndCheckOutDateAfter(listingId, LocalDate.now());
    }
}
