
package com.continuent.tungsten.replicator.mqconf;

public class PropertiesAttribute
{
    // 源节点
    public static final String pnode_service_name           = "pnode.service.name";
    public static final String pnode_db_type                = "pnode.db.type";
    public static final String pnode_db_host                = "pnode.db.host";
    public static final String pnode_db_port                = "pnode.db.port";
    public static final String pnode_db_user                = "pnode.db.user";
    public static final String pnode_db_password            = "pnode.db.password";
    public static final String pnode_db_binlog_dir          = "pnode.db.binlog_dir";
    public static final String pnode_db_binlog_file_pattern = "pnode.db.binlog_file_pattern";
    public static final String pnode_db_instance            = "pnode.db.instance";
    // 目标节点
    public static final String snode_service_name           = "snode.service.name";
    public static final String snode_db_type                = "snode.db.type";
    public static final String snode_db_host                = "snode.db.host";
    public static final String snode_db_port                = "snode.db.port";
    public static final String snode_db_user                = "snode.db.user";
    public static final String snode_db_password            = "snode.db.password";
    public static final String snode_db_instance            = "snode.db.instance";
    public static final String snode_db_minpoolsize         = "snode.db.minpoolsize";
    public static final String snode_db_increament          = "snode.db.increament";
    public static final String snode_db_maxpoolsize         = "snode.db.maxpoolsize";

    public static final String snode_db_batchnum            = "snode.db.batchnum";
    // mq服务
    public static final String middle_mq_address                 = "middle.mq.address";
    public static final String middle_mq_queue_create_type  = "middle.mq.queue.create.type";
    public static final String middle_mq_queues             = "middle.mq.queues";
    //tungsten
    public static final String tungsten_db_host                = "tungsten.db.host";
    public static final String tungsten_db_port                = "tungsten.db.port";
    public static final String tungsten_db_user                = "tungsten.db.user";
    public static final String tungsten_db_password            = "tungsten.db.password";

}
