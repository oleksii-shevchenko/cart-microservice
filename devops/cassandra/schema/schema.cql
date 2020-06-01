CREATE KEYSPACE IF NOT EXISTS  cart_keyspace
    WITH REPLICATION = {
        'class' : 'SimpleStrategy', 'replication_factor' : 3
    }
    AND DURABLE_WRITES =  true;

USE cart_keyspace;

CREATE TABLE IF NOT EXISTS user_binding(
    user_id BIGINT,
    cart_id BIGINT,
    PRIMARY KEY ((user_id), cart_id))
    WITH default_time_to_live = 2592000 // 30 days
    AND compaction = {
        'class' : 'TimeWindowCompactionStrategy',
        'compaction_window_unit' : 'DAYS',
        'compaction_window_size' : 1
    };

CREATE TABLE IF NOT EXISTS cart(
    cart_id BIGINT,
    item_id TEXT,
    number INT,
    PRIMARY KEY ((cart_id), item_id))
    WITH default_time_to_live = 3456000; // 40 days
