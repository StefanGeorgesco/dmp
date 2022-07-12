use dmp;

insert into t_user (id, username, role, password, security_code) values ('D001', 'user', 'ROLE_DOCTOR', '$2a$12$7/n1myGRPalYXRCUbsrXz.vhtJCYTfi8j1dBlX1m/ECFosXD6jcMa', '');
insert into t_user (id, username, role, password, security_code) values ('D002', 'doc', 'ROLE_DOCTOR', '$2a$12$rbh2MVQ3zMmeE4a.vHQcJOlBQ7uLLE9fpcy3G.l.vfT96WhlF/51m', '');
insert into t_user (id, username, role, password, security_code) values ('P001', 'jean', 'ROLE_PATIENT', '$2a$12$nGajrCywpBYm9xLtu.lAbuTTNCFX3rrHaisz87P.fw2BXCF8E/gD2', '');
insert into t_user (id, username, role, password, security_code) values ('P002', 'utilisateur', 'ROLE_PATIENT', '$2a$12$tClFvDjq0BaRSxi8/iird.BdMGV99a88Bun39z1yc29A9Qg0u40bm', '');
insert into t_user (id, username, role, password, security_code) values ('A001', 'admin', 'ROLE_ADMIN', '$2a$12$yjbMztwlMm0yEIVbM8ybu.nJ6kQPipI6ViV/8GbU8TWgIwSvRbAQa', '');

insert into t_specialty(id, description) values ('S001', 'Specialty 1');
insert into t_specialty(id, description) values ('S002', 'Specialty 2');
insert into t_specialty(id, description) values ('S003', 'Specialty 3');

insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('D001', 'John', 'Smith', '0123456789', 'john.smith@doctors.com', '1 baker street', '', 'London', '', '99999', 'United Kingdom', '45');
insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('D002', 'Jean', 'Dupont', '9999999999', 'jean.dupont@docteurs.fr', '15 rue de Vaugirard', '', 'Paris', '', '75015', 'France', '999');
insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('P001', 'Jean', 'Martin', '0101010101', 'jean.martin@free.fr', '1 rue de la Paix', '', 'Paris', '', '75001', 'France', '0000');
insert into t_file(id, firstname, lastname, phone, email, street1, street2, city, state, zipcode, country, security_code) values ('P002', 'Paul', 'Dubois', '5555555555', 'paul.dubois@orange.fr', '11 rue de la Convention', '', 'Paris', '', '75015', 'France', '1111');

insert into t_doctor(id) values ('D001');
insert into t_doctor(id) values ('D002');
insert into t_patient_file(id, referring_doctor_id) values ('P001', 'D001');
insert into t_patient_file(id, referring_doctor_id) values ('P002', 'D002');

insert into t_doctor_specialty(doctor_id, specialty_id) values ('D001', 'S001');
insert into t_doctor_specialty(doctor_id, specialty_id) values ('D001', 'S002');
insert into t_doctor_specialty(doctor_id, specialty_id) values ('D001', 'S003');
insert into t_doctor_specialty(doctor_id, specialty_id) values ('D002', 'S002');

