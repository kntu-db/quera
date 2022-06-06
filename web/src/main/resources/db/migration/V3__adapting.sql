alter table class rename to ClassRoom;
alter table classparticipation rename column class to classroom;
alter table problemset rename column class to classroom;

alter table submit rename column final to isFinal;