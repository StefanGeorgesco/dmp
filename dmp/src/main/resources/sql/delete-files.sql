delete from t_file where id in (select id from t_patient_file)

delete from t_file where id in (select id from t_doctor)
