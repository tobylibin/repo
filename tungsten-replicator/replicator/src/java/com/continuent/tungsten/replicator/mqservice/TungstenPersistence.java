
package com.continuent.tungsten.replicator.mqservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.continuent.tungsten.replicator.mqconf.TungstenDB;
import com.continuent.tungsten.replicator.mqtransform.TransformMgr;
import com.continuent.tungsten.replicator.mqutil.XMLUtils;

public class TungstenPersistence
{
    private static Logger      logger = Logger.getLogger(TungstenPersistence.class);
    private List<TransformMgr> transList;
    private String             schemaName;
 
    public TungstenPersistence(){
        
    }
    
    public TungstenPersistence(String schemaName)
    {
        this.schemaName = schemaName;
    }

    public TungstenPersistence(List<TransformMgr> transList)
    {
        this.transList = transList;
    }

    public void save()
    {
        if (transList != null && transList.size() > 0)
        {
            Connection conn = null;
            PreparedStatement ps = null;
            try
            {
                conn = TungstenDB.getConn();
                conn.setAutoCommit(false);
                // 相同的sql才能批量操作,获取批量数据
                ps = conn
                        .prepareStatement("insert into tungsten_log(schemaname,errortype,errorxml,errortime,actiontype) values(?,?,?,?,?)");
                for (TransformMgr transMgr : transList)
                {
                    ps.setString(1, transMgr.getRuleObject().getTableName());
                    ps.setInt(2, 1);
                    ps.setString(3, XMLUtils.toXml(transMgr));
                    ps.setTimestamp(4, new Timestamp(new Date().getTime()));
                    ps.setString(5, transMgr.getRuleObject().getAction());
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
                logger.error("保存数据库异常日志失败:" + e.getMessage());

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
    }

    public List<TransformMgr> getUpdateTrans()
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = TungstenDB.getConn();
            conn.setAutoCommit(false);
            // 相同的sql才能批量操作,获取批量数据
            ps = conn
                    .prepareStatement("select id,errorxml,actiontype from tungsten_log where schemaname='"
                            + schemaName
                            + "'  and actiontype !='DELETE'   limit 0,10");
            rs = ps.executeQuery();
            transList = new ArrayList<TransformMgr>();
            while (rs.next())
            {
                Long errorId = rs.getLong("id");
                TransformMgr mgr = (TransformMgr) XMLUtils.toObject(rs
                        .getString("errorxml"));
                mgr.setErrorId(errorId);
                mgr.setErrorAction(rs.getString("actiontype"));
                transList.add(mgr);
            }
            return transList;
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
            logger.error("查询数据库异常日志失败:" + e.getMessage());

        }
        finally
        {

            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

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
        return null;
    }

    public List<TransformMgr> getDeleteTrans()
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            conn = TungstenDB.getConn();
            conn.setAutoCommit(false);
            // 相同的sql才能批量操作,获取批量数据
            ps = conn
                    .prepareStatement("select id,errorxml,actiontype from tungsten_log where schemaname='"
                            + schemaName
                            + "'  and actiontype ='DELETE'   limit 0,50");
            rs = ps.executeQuery();
            transList = new ArrayList<TransformMgr>();
            while (rs.next())
            {
                Long errorId = rs.getLong("id");
                TransformMgr mgr = (TransformMgr) XMLUtils.toObject(rs
                        .getString("errorxml"));
                mgr.setErrorId(errorId);
                mgr.setErrorAction(rs.getString("actiontype"));
                transList.add(mgr);
            }
            return transList;
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
            logger.error("查询数据库异常日志失败:" + e.getMessage());

        }
        finally
        {

            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

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
        return null;
    }
    
    public void delete(List<Long> ids)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        StringBuffer sb = new StringBuffer("delete from tungsten_log where id in ");
        try
        {
            conn = TungstenDB.getConn();
            conn.setAutoCommit(false);
            String tempid = null;
            for(Long id : ids){
                if(tempid == null ){
                    tempid = id.toString();
                }else{
                    tempid = tempid+","+id.toString();
                }
            }
            sb.append("(").append(tempid).append(")");
            ps = conn
                    .prepareStatement(sb.toString());
            ps.execute();
            System.out.println(sb.toString());
            conn.commit();
           
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
            logger.error("查询数据库异常日志失败:" + e.getMessage());

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
}
