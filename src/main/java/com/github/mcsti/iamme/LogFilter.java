package com.github.mcsti.iamme;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class LogFilter extends AbstractFilter {private static Result validateMessage(Message message) {
    if (message == null) {
        return Result.NEUTRAL;
    }
    return validateMessage(message.getFormattedMessage());
}
    
    /**
     * Validates a message and returns the {@link Result} value depending
     * on whether the message contains sensitive AuthMe data.
     *
     * @param message The message to verify
     *
     * @return The Result value
     */
    private static Result validateMessage(String message) {
        return message.contains("/login") || message.contains("/register")
                ? Result.DENY
                : Result.NEUTRAL;
    }
    
    @Override
    public Result filter(LogEvent event) {
        Message candidate = null;
        if (event != null) {
            candidate = event.getMessage();
        }
        return validateMessage(candidate);
    }
    
    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg);
    }
    
    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }
    
    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        String candidate = null;
        if (msg != null) {
            candidate = msg.toString();
        }
        return validateMessage(candidate);
    }
}
