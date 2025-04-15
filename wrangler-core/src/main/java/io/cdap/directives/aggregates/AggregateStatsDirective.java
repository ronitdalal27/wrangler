package io.cdap.directives.aggregates;


import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.List;

/**
 * Directive for aggregating byte sizes and time durations.
 */
public class AggregateStatsDirective implements Directive {
    private String byteSizeColumn;
    private String timeDurationColumn;
    private String totalSizeColumn;
    private String totalTimeColumn;
    private long totalBytes = 0;
    private long totalMillis = 0;
    @SuppressWarnings("unused")
    private int rowCount = 0;

    @Override
    public UsageDefinition define() {
        UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
        builder.define("byteSizeColumn", TokenType.COLUMN_NAME);
        builder.define("timeDurationColumn", TokenType.COLUMN_NAME);
        builder.define("totalSizeColumn", TokenType.COLUMN_NAME);
        builder.define("totalTimeColumn", TokenType.COLUMN_NAME);
        return builder.build();
    }

    @Override
    public void initialize(Arguments args) {
        byteSizeColumn = args.value("byteSizeColumn");
        timeDurationColumn = args.value("timeDurationColumn");
        totalSizeColumn = args.value("totalSizeColumn");
        totalTimeColumn = args.value("totalTimeColumn");
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        for (Row row : rows) {
            ByteSize byteSize = (ByteSize) row.getValue(byteSizeColumn);
            TimeDuration timeDuration = (TimeDuration) row.getValue(timeDurationColumn);

            if (byteSize != null) totalBytes += Long.parseLong(byteSize.getBytes());
            if (timeDuration != null) totalMillis += Long.parseLong(timeDuration.getMillis());

            rowCount++;
        }
        return finalizeExecution();
    }

    @Override
    public void destroy() {
        // Cleanup resources if necessary
    }

    public List<Row> finalizeExecution() {
        Row resultRow = new Row();
        resultRow.add(totalSizeColumn, totalBytes / (1024 * 1024)); // Convert bytes to MB
        resultRow.add(totalTimeColumn, totalMillis / 1000); // Convert milliseconds to seconds
        return List.of(resultRow);
    }
}