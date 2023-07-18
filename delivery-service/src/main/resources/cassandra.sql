CREATE KEYSPACE delivery_1_0
    WITH REPLICATION = {
        'class' : 'NetworkTopologyStrategy',
        'datacenter1' : 1
        } ;
