package com.gymmaster.controller;

import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Reservation;
import com.gymmaster.entity.Venue;
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
import java.util.Arrays;
import java.util.Collections;

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
    private HttpServletRequest request;

    @Test
    void add_shouldRejectPeriodOutOfRange() throws Exception {
        Reservation incoming = new Reservation();
        incoming.setRdate(Date.valueOf("2026-06-08"));
        incoming.setFacility(1);
        incoming.setVenue(1);
        incoming.setPeriod("9");
        incoming.setAmount(1);

        Venue venue = new Venue();
        venue.setCapacity(10);

        when(reservationService.list(any())).thenReturn(Collections.emptyList());
        when(venueService.getOne(any())).thenReturn(venue);

        BackMsg<String> result = controller.add(incoming, request);

        Assertions.assertEquals(0, result.getCode());
        Assertions.assertEquals("wrong period!", result.getMsg());
    }

    @Test
    void add_shouldRejectWhenCapacityIsNotEnough() throws Exception {
        Reservation existing = new Reservation();
        existing.setPeriod("1");
        existing.setAmount(2);

        Reservation incoming = new Reservation();
        incoming.setRdate(Date.valueOf("2026-06-08"));
        incoming.setFacility(2);
        incoming.setVenue(3);
        incoming.setPeriod("1");
        incoming.setAmount(2);

        Venue venue = new Venue();
        venue.setCapacity(3);

        when(reservationService.list(any())).thenReturn(Arrays.asList(existing));
        when(venueService.getOne(any())).thenReturn(venue);

        BackMsg<String> result = controller.add(incoming, request);

        Assertions.assertEquals(0, result.getCode());
        Assertions.assertTrue(result.getMsg().contains("capacity"));
    }
}
