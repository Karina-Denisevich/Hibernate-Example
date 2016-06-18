USE music;
insert into `group` (GROUP_NAME) values('Metallica'), ('Rihanna');
insert into `genre` (GENRE_NAME) values('rock'), ('pop'), ('metal');
insert into `group_genre` (GROUP_ID, GENRE_ID) values('1', '1'), ('1', '3'), ('2', '2');
