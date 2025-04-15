package io.cdap.directives.aggregates;


import io.cdap.wrangler.TestingRig;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AggregateStatsDirectiveTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testAggregation() throws Exception {
        List<Row> rows = List.of(
            new Row("data_transfer_size", new ByteSize("10MB"))
                .add("response_time", new TimeDuration("150ms")),
            new Row("data_transfer_size", new ByteSize("5MB"))
                .add("response_time", new TimeDuration("250ms")),
            new Row("data_transfer_size", new ByteSize("15MB"))
                .add("response_time", new TimeDuration("350ms"))
        );

        String[] recipe = {
            "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        List<Row> result = TestingRig.execute(recipe, rows);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(30, result.get(0).getValue("total_size_mb")); // 10MB + 5MB + 15MB
        Assert.assertEquals(0.75, 0.001); // (150ms + 250ms + 350ms) / 1000
    }
}
