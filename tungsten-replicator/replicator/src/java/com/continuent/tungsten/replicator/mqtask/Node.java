package com.continuent.tungsten.replicator.mqtask;

import com.continuent.tungsten.replicator.mqexception.ApplierException;

public interface Node
{
  public void start() throws ApplierException;
}
