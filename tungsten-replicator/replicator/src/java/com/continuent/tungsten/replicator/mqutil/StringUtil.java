package com.continuent.tungsten.replicator.mqutil;

public class StringUtil
{
  public static boolean notEmpty(String value){
      if(value != null && value.trim() !=""){
          return true;
      }else{
          return false;
      }
  }
  
  public static boolean empty(String value){
      if(value == null || value.trim() ==""){
          return true;
      }else{
          return false;
      }
  }
}
