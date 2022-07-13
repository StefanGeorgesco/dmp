insert into t_specialty(id, description) values ('S001', 'Specialty 1')
insert into t_specialty(id, description) values ('S002', 'Specialty 2')

insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('D001', 'John', 'Smith', '0123456789', 'john.smith@doctors.com', '1 baker street', '', 'London', '', '99999', 'United Kingdom', '45')
insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('P001', 'Eric', 'Martin', '0101010101', 'eric.martin@free.fr', '1 rue de la Paix', '', 'Paris', '', '75001', 'France', '0000')
insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('D002', 'Jean', 'Dupont', '9999999999', 'jean.dupont@docteurs.fr', '15 rue de Vaugirard', '', 'Paris', '', '75015', 'France', '999')

insert into t_doctor(id) values ('D001')
insert into t_doctor(id) values ('D002')
insert into t_patient_file(id, referring_doctor_id) values ('P001', 'D001')

insert into t_doctor_specialty(doctor_id, specialty_id) values ('D001', 'S001')
insert into t_doctor_specialty(doctor_id, specialty_id) values ('D001', 'S002')
insert into t_doctor_specialty(doctor_id, specialty_id) values ('D002', 'S001')
insert into t_doctor_specialty(doctor_id, specialty_id) values ('D002', 'S002')