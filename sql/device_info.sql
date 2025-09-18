create table device_info
(
    id            int auto_increment
        primary key,
    model         varchar(30) null,
    model_version varchar(30) null
)
    comment '设备数据';