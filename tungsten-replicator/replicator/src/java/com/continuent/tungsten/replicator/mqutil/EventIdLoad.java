
package com.continuent.tungsten.replicator.mqutil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class EventIdLoad extends LogerEvent
{

    public static String getLogLastEventId()
    {
        String lastLine = null;
        // 使用RandomAccessFile , 从后找最后一行数据
        RandomAccessFile raf = null;
        try
        {
            File file = new File("log/event/bin.log");
            if (file.exists())
            {
                raf = new RandomAccessFile("log/event/bin.log", "r");
                long len = raf.length();

                if (len != 0L)
                {
                    long pos = len - 1;
                    while (pos > 0)
                    {
                        pos--;
                        raf.seek(pos);
                        if (raf.readByte() == '\n')
                        {
                            lastLine = raf.readLine();
                            break;
                        }
                    }
                }
            }
            return lastLine;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                raf.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String args[]) throws IOException
    {

    }
}
