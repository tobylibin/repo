
package com.continuent.tungsten.replicator.mqfilter;

import java.util.ArrayList;
import java.util.List;

public class FilterManager
{
    private final static String key     = "key";
    public static FilterManager instace;
    private List<Filter>        filters = new ArrayList<Filter>();

    public static FilterManager getInstance()
    {
        if (instace == null)
        {
            synchronized (key)
            {
                if (instace == null)
                {
                    instace = new FilterManager();
                    return instace;
                }
            }
        }
        return instace;
    }

    public boolean invoke(RuleObject rule)
    {
        for (Filter filter : filters)
        {
            if (filter.filter(rule) == RuleStatus.failed)
            {
                return false;
            }
        }
        return true;
    }

    public List<Filter> getFilters()
    {
        return filters;
    }

    public void setFilters(List<Filter> filters)
    {
        this.filters = filters;
    }

}
