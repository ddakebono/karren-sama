/*
 * Copyright (c) 2017 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        if(iLoggingEvent.getMessage().contains("D4J AudioThread | s.b.d.Discord4J | Discord4J Internal Exception") || iLoggingEvent.getMessage().contains("changed presence"))
            return FilterReply.DENY;
        return FilterReply.ACCEPT;
    }
}
