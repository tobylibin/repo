
package com.continuent.tungsten.replicator.mqconf;

import com.continuent.tungsten.common.config.TungstenProperties;
import com.continuent.tungsten.replicator.ReplicatorException;
import com.continuent.tungsten.replicator.applier.DummyApplier;
import com.continuent.tungsten.replicator.conf.ReplicatorConf;
import com.continuent.tungsten.replicator.conf.ReplicatorMonitor;
import com.continuent.tungsten.replicator.conf.ReplicatorRuntime;
import com.continuent.tungsten.replicator.datasource.AliasDataSource;
import com.continuent.tungsten.replicator.extractor.mysql.MySQLExtractor;
import com.continuent.tungsten.replicator.management.MockOpenReplicatorContext;
import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.continuent.tungsten.replicator.pipeline.SingleThreadStageTask;

public class RuntimeProperties
{
    public  TungstenProperties getConfig() throws ApplierException   
    {
        TungstenProperties conf=new TungstenProperties();
        conf.setString(ReplicatorConf.SERVICE_NAME,Propertiesmgr.getValue(PropertiesAttribute.pnode_service_name));
        conf.setString(ReplicatorConf.ROLE,ReplicatorConf.ROLE_MASTER);
        conf.setString(ReplicatorConf.PIPELINES,"master");
        conf.setString(ReplicatorConf.PIPELINE_ROOT + ".master","extract");
        conf.setString(ReplicatorConf.STAGE_ROOT + ".extract",SingleThreadStageTask.class.toString());
        conf.setString(ReplicatorConf.STAGE_ROOT + ".extract.extractor","mysql");
        conf.setString(ReplicatorConf.STAGE_ROOT + ".extract.applier","dummy");
        conf.setString(ReplicatorConf.APPLIER_ROOT + ".dummy",DummyApplier.class.getName());
        conf.setString(ReplicatorConf.EXTRACTOR_ROOT + ".mysql",MySQLExtractor.class.getName());
        //
        //设置日志监听目录和名称
        conf.setString(ReplicatorConf.EXTRACTOR_ROOT + ".mysql.binlog_dir",
                Propertiesmgr.getValue(PropertiesAttribute.pnode_db_binlog_dir));
        conf.setString(ReplicatorConf.EXTRACTOR_ROOT
                + ".mysql.binlog_file_pattern", Propertiesmgr.getValue(PropertiesAttribute.pnode_db_binlog_file_pattern));
        //
        conf.setString("replicator.service.datasource","com.continuent.tungsten.replicator.datasource.DataSourceService");
        conf.setString("replicator.datasources","global,extractor");
        conf.setString("replicator.pipeline.master.services","datasource");
        conf.setString("replicator.datasource.extractor",AliasDataSource.class.getName());
        conf.setString("replicator.datasource.extractor.dataSource","global");
        conf.setString("replicator.datasource.global","com.continuent.tungsten.replicator.datasource.SqlDataSource");
        //
        conf.setString("replicator.datasource.global.connectionSpec","com.continuent.tungsten.replicator.datasource.SqlConnectionSpecMySQL");
        conf.setString("replicator.datasource.global.connectionSpec.host",Propertiesmgr.getValue(PropertiesAttribute.pnode_db_host));
        conf.setString("replicator.datasource.global.connectionSpec.user",Propertiesmgr.getValue(PropertiesAttribute.pnode_db_user));
        conf.setString("replicator.datasource.global.connectionSpec.password",Propertiesmgr.getValue(PropertiesAttribute.pnode_db_password)); 
        conf.setString("replicator.datasource.global.connectionSpec.port",Propertiesmgr.getValue(PropertiesAttribute.pnode_db_port));
        conf.setString("replicator.datasource.global.connectionSpec.schema",Propertiesmgr.getValue(PropertiesAttribute.pnode_db_instance));
        ReplicatorRuntime runtime=new ReplicatorRuntime(conf,new MockOpenReplicatorContext(),ReplicatorMonitor.getInstance());
        try
        {
            runtime.configure();
            try
            {
                runtime.prepare();
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (ReplicatorException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        runtime.release();

        return conf;
    }
    public static void main(String args[]) throws ApplierException{
        new RuntimeProperties().getConfig();
    }
}
