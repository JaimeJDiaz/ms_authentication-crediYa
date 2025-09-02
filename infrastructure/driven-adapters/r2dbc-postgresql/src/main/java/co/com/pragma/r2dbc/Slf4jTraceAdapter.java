package co.com.pragma.r2dbc;

import co.com.pragma.model.user.gateways.TracePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jTraceAdapter implements TracePort {
    private static final Logger logger = LoggerFactory.getLogger(Slf4jTraceAdapter.class);

    @Override
    public void trace(String message) {
        logger.trace(message);
    }
}
