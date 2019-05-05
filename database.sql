CREATE DATABASE notas;

USE notas;

CREATE USER 'notas'@'localhost' IDENTIFIED BY 'P@55word';

GRANT ALL PRIVILEGES ON notas.* TO 'notas'@'localhost';

CREATE TABLE usuario (
    idUsuario int NOT NULL auto_increment,
    nombre varchar(255) NOT NULL,
    telefono varchar(10) NOT NULL,
    tiempoRegistro DATETIME NOT NULL,
    tokenAcceso varchar(4) NOT NULL,
    idEstatus varchar(1) NOT NULL,
    tiempoValidacion DATETIME,
    password varchar(64) NOT NULL,
    PRIMARY KEY (idUsuario)
);

CREATE TABLE notas (
  idNota int NOT NULL auto_increment,
  idUsuario int NOT NULL,
  titulo varchar(255) NOT NULL,
  contenido text,
  tiempoCreacion DATETIME,
  PRIMARY KEY (idNota, idUsuario),
  FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)
);

