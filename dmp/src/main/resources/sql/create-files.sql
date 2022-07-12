insert into t_specialty(id, description) values ('s001', 'Specialty 1')
insert into t_specialty(id, description) values ('s002', 'Specialty 2')

insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('1', 'John', 'Smith', '0123456789', 'john.smith@doctors.com', '1 baker street', '', 'London', '', '99999', 'United Kingdom', '45')
insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('2', 'Eric', 'Martin', '0101010101', 'eric.martin@free.fr', '1 rue de la Paix', '', 'Paris', '', '75001', 'France', '0000')
insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('3', 'Jean', 'Dupont', '9999999999', 'jean.dupont@docteurs.fr', '15 rue de Vaugirard', '', 'Paris', '', '75015', 'France', '999')

insert into t_doctor(id) values ('1')
insert into t_doctor(id) values ('3')
insert into t_patient_file(id, referring_doctor_id) values ('2', '1')

insert into t_doctor_specialty(doctor_id, specialty_id) values ('1', 's001')
insert into t_doctor_specialty(doctor_id, specialty_id) values ('1', 's002')
insert into t_doctor_specialty(doctor_id, specialty_id) values ('3', 's001')
insert into t_doctor_specialty(doctor_id, specialty_id) values ('3', 's002')