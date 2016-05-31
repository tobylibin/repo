
package com.continuent.tungsten.replicator.mqutil;

import org.apache.log4j.Logger;

public abstract class LogerEvent   
{
    private static Logger logger = Logger.getLogger(LogerEvent.class);

    public static void logEventID(String eventId, boolean log)
    {
        if (log)
        {
            logger.info(eventId);
        }
    }
}
