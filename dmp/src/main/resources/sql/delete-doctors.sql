delete from t_file where id in (select id from t_doctor)