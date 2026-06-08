package com.gymmaster.controller;

import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Bill;
import com.gymmaster.service.AccountService;
import com.gymmaster.service.BillService;
import com.gymmaster.service.FacilityService;
import com.gymmaster.service.ReservationService;
import com.gymmaster.service.VenueService;
import com.gymmaster.utils.RedisCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillControllerUnitTest {

    @InjectMocks
    private BillController controller;

    @Mock
    private BillService billService;

    @Mock
    private FacilityService facilityService;

    @Mock
    private VenueService venueService;

    @Mock
    private RedisCache redisCache;

    @Mock
    private AccountService accountService;

    @Mock
    private ReservationService reservationService;

    @Test
    void pagePeriod_shouldRejectInvalidTimeRange() {
        Timestamp start = Timestamp.valueOf("2026-06-10 00:00:00");
        Timestamp end = Timestamp.valueOf("2026-06-01 00:00:00");

        BackMsg<Map> result = controller.pagePeriod(start, end);

        Assertions.assertEquals(0, result.getCode());
        Assertions.assertEquals("wrong date entered!", result.getMsg());
    }

    @Test
    void pagePeriod_shouldAggregateByFacilityName() {
        Bill b1 = new Bill();
        b1.setFname("Pool");
        b1.setFigure(new BigDecimal("30.00"));
        b1.setBdate(Timestamp.valueOf("2026-06-02 10:00:00"));

        Bill b2 = new Bill();
        b2.setFname("Pool");
        b2.setFigure(new BigDecimal("20.00"));
        b2.setBdate(Timestamp.valueOf("2026-06-03 10:00:00"));

        Bill b3 = new Bill();
        b3.setFname("Yoga");
        b3.setFigure(new BigDecimal("40.00"));
        b3.setBdate(Timestamp.valueOf("2026-06-20 10:00:00"));

        when(billService.list(any())).thenReturn(new ArrayList<>(Arrays.asList(b1, b2, b3)));

        Timestamp start = Timestamp.valueOf("2026-06-01 00:00:00");
        Timestamp end = Timestamp.valueOf("2026-06-10 00:00:00");

        BackMsg<Map> result = controller.pagePeriod(start, end);

        Assertions.assertEquals(1, result.getCode());
        Map<String, BigDecimal> stats = result.getData();
        Assertions.assertEquals(new BigDecimal("50.00"), stats.get("Pool"));
        Assertions.assertFalse(stats.containsKey("Yoga"));
    }
}
