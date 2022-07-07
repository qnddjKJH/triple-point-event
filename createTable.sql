alter table review
drop
foreign key FK6cpw2nlklblpvc7hyt7ko6v3e;

drop table if exists point_history;
drop table if exists review;
drop table if exists users;


create table point_history (
   history_id CHAR(36) not null,
   type varchar(10),
   action varchar(10),
   increase_point integer,
   review_point integer,
   user_total integer,
   review_type varchar(10),
   user_id CHAR(36),
   review_id CHAR(36),
   place_id CHAR(36),
   created_at datetime,
   primary key (history_id)
) engine=INNODB;

create table review (
    review_id CHAR(36) not null,
    point integer not null,
    type varchar(10),
    content varchar(255),
    attached_photo_count integer not null,
    user_id CHAR(36),
    place_id CHAR(36),
    created_at datetime,
    modified_at datetime,
    primary key (review_id)
) engine=INNODB;

create table users (
   user_id CHAR(36) not null,
   point integer not null,
   created_at datetime,
   modified_at datetime,
   primary key (user_id)
) ENGINE=INNODB;

create index _userId on point_history (user_id);
create index _placeId on point_history (place_id);
create index _reviewId on point_history (review_id);
create index _action on point_history (ACTION);
create index _placeIdOnReview on review (place_id);

alter table review
add constraint FK6cpw2nlklblpvc7hyt7ko6v3e
foreign key (user_id)
references users (user_id);