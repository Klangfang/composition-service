insert into sound(id, filename, start_position_in_ms, duration_in_ms, creatorname) values (10, 'xy.3gp', 0, 10000, 'venom')

insert into track(id) values (10)

insert into track_sounds(track_id, sounds_id) values (10, 10)

insert into composition(id, creation_date, creatorname, status) values (10, '2018-01-27', 'Riot', 'CREATED')

insert into composition_tracks(composition_id, tracks_id) values (10, 10)