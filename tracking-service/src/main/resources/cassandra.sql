CREATE KEYSPACE tracking_1_0
    WITH REPLICATION = {
        'class' : 'NetworkTopologyStrategy',
        'datacenter1' : 1
        } ;
