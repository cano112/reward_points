package com.noster.rewardpoints.infra.rest;

import com.noster.rewardpoints.api.Api;
import com.noster.rewardpoints.domain.entities.GroupedPoints;
import com.noster.rewardpoints.domain.values.Points;
import com.noster.rewardpoints.domain.values.SummaryGrouping;
import com.noster.rewardpoints.usecases.GetSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration;

import java.time.Instant;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

@WebMvcTest(value = GetSummaryController.class)
@Import(LogbookAutoConfiguration.class)
public class GetSummaryControllerTest {

    private static final UUID USER_ID = UUID.fromString("f6941dd8-6e08-4c56-b945-34d17112e0e5");

    private final Api api;

    @MockBean
    private GetSummary getSummary;

    @Autowired
    GetSummaryControllerTest(MockMvc mockMvc) {
        this.api = new Api(mockMvc);
    }

    // TODO: Validation tests & implementation missing

    @Test
    void shouldGetMonthlySummary() {
        final var request = new GetSummary.Request(
                USER_ID,
                Instant.parse("2020-10-01T00:00:00.000Z"),
                Instant.parse("2020-12-31T23:59:59.999Z"),
                SummaryGrouping.MONTH
        );
        final var response = new GetSummary.Response(
                new GroupedPoints(
                        SummaryGrouping.MONTH,
                        List.of(
                                new GroupedPoints.Group(Month.OCTOBER.getValue(), new Points(50)),
                                new GroupedPoints.Group(Month.NOVEMBER.getValue(), new Points(50)),
                                new GroupedPoints.Group(Month.DECEMBER.getValue(), new Points(40))
                        )
                ),
                new Points(140)
        );
        given(getSummary.with(request)).willReturn(response);

        api
                .getSummary(USER_ID, Map.of(
                        "start", "2020-10-01T00:00:00.000Z",
                        "end", "2020-12-31T23:59:59.999Z",
                        "group_by", "MONTH"
                ))
                .then()
                .status(HttpStatus.OK)
                .body(sameJSONAs(
                        // language=JSON
                        """
                                                     
                                {
                                    "points_summary": {
                                        "grouped": {
                                            "by": "MONTH",
                                            "entries": [
                                                {
                                                    "group": 10,
                                                    "points": 50
                                                },
                                                {
                                                    "group": 11,
                                                    "points": 50
                                                },
                                                {
                                                    "group": 12,
                                                    "points": 40
                                                }
                                            ]
                                        },
                                        "total": {
                                            "points": 140
                                        }
                                    }
                                }
                                 """));
    }
}