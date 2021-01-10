create table udp_record_address
(
    id                   bigserial                                primary key,
    listening_ip         text                                     not null,
    listening_port       int                                      not null,
    destination_ip       text                                     not null,
    destination_port     int                                      not null
);

create table record_device
(
    id          bigserial                                primary key,
    device_name text                                     not null,
    is_primary  boolean                                  not null
);

create table udp_record_folder
(
    id          bigserial                                primary key,
    folder      text                                     not null
);