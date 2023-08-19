CREATE KEYSPACE pricing_1_0
    WITH REPLICATION = {
        'class' : 'NetworkTopologyStrategy',
        'datacenter1' : 1
        };


TRUNCATE pricing_1_0.definitepriceentity;