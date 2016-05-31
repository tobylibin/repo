/**
 * Tungsten Scale-Out Stack
 * Copyright (C) 2007-2011 Continuent Inc.
 * Contact: tungsten@continuent.org
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
 *
 * Initial developer(s): Robert Hodges
 * Contributor(s): Linas Virbalas
 */

package com.continuent.tungsten.replicator.mqservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.continuent.tungsten.replicator.mqconf.DBManager;
import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.continuent.tungsten.replicator.mqtransform.TransformMgr;

public class MQOracleApplier
{
    private static Logger      logger = Logger.getLogger(MQOracleApplier.class);

    private List<TransformMgr> transList;

    public MQOracleApplier(List<TransformMgr> transList)
    {
        this.transList = transList;
    }

    public void applier() throws ApplierException
    {

        Connection conn = DBManager.getConn();
        PreparedStatement ps = null;
        try
        {
            conn.setAutoCommit(false);
            // 相同的sql才能批量操作,获取批量数据
            TransformMgr transFirst = transList.get(0);
            String sql = transFirst.getPreparestatementSql();
            ps = conn.prepareStatement(sql);
            for (TransformMgr transMgr : transList)
            {
                transMgr.setParam(ps);
                ps.addBatch();

            }

            ps.executeBatch();
            conn.commit();
            ps.clearBatch();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                conn.rollback();
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            throw new ApplierException("", "Applier加载数据库失败:", e);

        }
        finally
        {

            if (ps != null)
            {
                try
                {
                    ps.clearBatch();
                    ps.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private Long getFixLenthString()
    {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, 6);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        String num = fixLenthString.substring(1, 6 + 1);
        return Long.valueOf(num.replace(".", ""));
    }

    public static void main(String args[])
    {

    }

}