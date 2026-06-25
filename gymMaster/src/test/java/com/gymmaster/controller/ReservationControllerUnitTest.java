package com.gymmaster.controller;

import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.Reservation;
import com.gymmaster.entity.Venue;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.CustomerService;
import com.gymmaster.service.ReservationService;
import com.gymmaster.service.VenueService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationControllerUnitTest {

    @InjectMocks
    private ReservationController controller;

    @Mock
    private ReservationService reservationService;

    @Mock
    private CustomerService customerService;

    @Mock
    private VenueService venueService;

    @Mock
    private CurrentUserResolver currentUserResolver;

    @Mock
    private HttpServletRequest request;

    @Test
    void add_shouldRejectPeriodOutOfRange() {
        Reservation incoming = new Reservation();
        incoming.setRdate(Date.valueOf("2026-06-08"));
        incoming.setFacility(1);
        incoming.setVenue(1);
        incoming.setPeriod("9");
        incoming.setAmount(1);

        Venue venue = new Venue();
        venue.setCapacity(10);

        when(venueService.getOne(any())).thenReturn(venue);
        // 8 periods, all fully available — period 9 exceeds the array length
        when(venueService.remainingCapacity(any(), any()))
                .thenReturn(new int[]{10, 10, 10, 10, 10, 10, 10, 10});

        Assertions.assertThrows(BusinessException.class,
                () -> controller.add(incoming, request));
    }

    @Test
    void add_shouldRejectWhenCapacityIsNotEnough() {
        Reservation incoming = new Reservation();
        incoming.setRdate(Date.valueOf("2026-06-08"));
        incoming.setFacility(2);
        incoming.setVenue(3);
        incoming.setPeriod("1");
        incoming.setAmount(2);

        Venue venue = new Venue();
        venue.setCapacity(3);

        when(venueService.getOne(any())).thenReturn(venue);
        // period 1 has only 1 remaining spot; 2 are requested → capacity error
        when(venueService.remainingCapacity(any(), any()))
                .thenReturn(new int[]{1, 10, 10, 10, 10, 10, 10, 10});

        BusinessException ex = Assertions.assertThrows(BusinessException.class,
                () -> controller.add(incoming, request));
        Assertions.assertTrue(ex.getMessage().contains("capacity"));
    }
}
