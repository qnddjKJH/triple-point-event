drop table if exists point_history;

create table point_history (
   history_id CHAR(36) not null,
   type VARCHAR(10),
   action VARCHAR(10),
   is_first_review BIT NOT NULL,
   current_point INTEGER,
   increase_point INTEGER,
   user_id CHAR(36),
   place_id CHAR(36),
   review_id CHAR(36),
   created_at datetime,
   primary key (history_id)
) engine=INNODB;
 
create index _userId on point_history (user_id);
create index _placeId on point_history (place_id);
create index _reviewId on point_history (review_id);
create index _action on point_history (ACTION);