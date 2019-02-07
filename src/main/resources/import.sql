insert into sound(id, filename, title, creatorname, start_position_in_ms, duration_in_ms) values (10, 'xy.3gp', 'FirstSound', 'Venom', 0, 10000)

insert into track(id) values (10)

insert into track_sounds(track_id, sounds_id) values (10, 10)

insert into composition(id, title, creatorname, creation_date, status) values (10, 'FirstComposition', 'Riot', '2018-01-27', 'CREATED')

insert into composition_tracks(composition_id, tracks_id) values (10, 10)