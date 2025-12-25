/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : pixelcoins2

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2020-12-29 22:29:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for conversacionesweb
-- ----------------------------
DROP TABLE IF EXISTS `conversacionesweb`;
CREATE TABLE `conversacionesweb` (
  `web_nombre` varchar(16) NOT NULL,
  `server_nombre` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for cuentas
-- ----------------------------
DROP TABLE IF EXISTS `cuentas`;
CREATE TABLE `cuentas` (
  `id` int(11) NOT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for deudas
-- ----------------------------
DROP TABLE IF EXISTS `deudas`;
CREATE TABLE `deudas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deudor` varchar(24) NOT NULL,
  `acredor` varchar(24) NOT NULL,
  `pixelcoins_restantes` double(24,3) NOT NULL,
  `tiempo_restante` int(11) NOT NULL,
  `interes` int(11) NOT NULL,
  `cuota` int(11) NOT NULL,
  `fecha_ultimapaga` varchar(24) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for empleados
-- ----------------------------
DROP TABLE IF EXISTS `empleados`;
CREATE TABLE `empleados` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jugador` varchar(24) NOT NULL,
  `empresa` varchar(24) NOT NULL,
  `sueldo` double(24,3) NOT NULL,
  `cargo` varchar(24) NOT NULL,
  `tipo_sueldo` varchar(24) NOT NULL,
  `fecha_ultimapaga` varchar(24) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `empresa` (`empresa`),
  CONSTRAINT `empresa-empleados` FOREIGN KEY (`empresa`) REFERENCES `empresas` (`nombre`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for empresas
-- ----------------------------
DROP TABLE IF EXISTS `empresas`;
CREATE TABLE `empresas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(24) NOT NULL,
  `owner` varchar(24) NOT NULL,
  `pixelcoins` double(24,3) NOT NULL,
  `ingresos` double(24,3) NOT NULL,
  `gastos` double(24,3) NOT NULL,
  `icono` varchar(24) NOT NULL,
  `razonCierre` varchar(16) NOT NULL,
  `descripcion` varchar(400) NOT NULL,
 PRIMARY KEY (`id`),
  KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for encantamientos
-- ----------------------------
DROP TABLE IF EXISTS `encantamientos`;
CREATE TABLE `encantamientos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `encantamiento` varchar(24) NOT NULL,
  `nivel` int(11) NOT NULL,
  `id_oferta` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_oferta` (`id_oferta`),
  CONSTRAINT `oferta-encantamientos` FOREIGN KEY (`id_oferta`) REFERENCES `ofertas` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for jugadores
-- ----------------------------
DROP TABLE IF EXISTS `jugadores`;
CREATE TABLE `jugadores` (
  `nombre` varchar(24) NOT NULL,
  `pixelcoins` double(24,3) NOT NULL,
  `nventas` int(11) NOT NULL,
  `ingresos` double(24,3) NOT NULL,
  `gastos` double(24,3) NOT NULL,
  `ninpagos` int(11) NOT NULL,
  `npagos` int(11) NOT NULL,
  `numero_cuenta` int(24) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for llamadasapi
-- ----------------------------
DROP TABLE IF EXISTS `llamadasapi`;
CREATE TABLE `llamadasapi` (
  `simbolo` varchar(24) NOT NULL,
  `precio` double(24,3) NOT NULL,
  `tipo_activo` varchar(24) NOT NULL,
  `nombre_activo` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`simbolo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mensajes
-- ----------------------------
DROP TABLE IF EXISTS `mensajes`;
CREATE TABLE `mensajes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `enviador` varchar(24) NOT NULL,
  `destinatario` varchar(24) NOT NULL,
  `mensaje` varchar(300) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=232 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for ofertas
-- ----------------------------
DROP TABLE IF EXISTS `ofertas`;
CREATE TABLE `ofertas` (
  `jugador` varchar(24) NOT NULL,
  `objeto` varchar(24) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio` double(24,3) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `durabilidad` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=794 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for posicionesabiertas
-- ----------------------------
DROP TABLE IF EXISTS `posicionesabiertas`;
CREATE TABLE `posicionesabiertas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jugador` varchar(16) NOT NULL,
  `tipo_activo` varchar(16) NOT NULL,
  `nombre_activo` varchar(16) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_apertura` double(16,3) NOT NULL,
  `fecha_apertura` varchar(16) NOT NULL,
  `tipo_posicion` varchar(10) DEFAULT 'LARGO',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=381 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for posicionescerradas
-- ----------------------------
DROP TABLE IF EXISTS `posicionescerradas`;
CREATE TABLE `posicionescerradas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jugador` varchar(16) NOT NULL,
  `tipo_activo` varchar(16) NOT NULL,
  `simbolo` varchar(16) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_apertura` double(16,3) NOT NULL,
  `fecha_apertura` varchar(16) NOT NULL,
  `precio_cierre` double(16,3) NOT NULL,
  `fecha_cierre` varchar(16) NOT NULL,
  `rentabilidad` double(16,3) NOT NULL,
  `nombre_activo` varchar(32) DEFAULT NULL,
  `tipo_posicion` varchar(10) DEFAULT 'LARGO',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=348 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for transacciones
-- ----------------------------
DROP TABLE IF EXISTS `transacciones`;
CREATE TABLE `transacciones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` varchar(24) NOT NULL,
  `comprador` varchar(24) NOT NULL,
  `vendedor` varchar(24) NOT NULL,
  `cantidad` double(24,3) NOT NULL,
  `objeto` varchar(24) NOT NULL,
  `tipo` varchar(24) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7042 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for verificacioncuentas
-- ----------------------------
DROP TABLE IF EXISTS `verificacioncuentas`;
CREATE TABLE `verificacioncuentas` (
  `jugador` varchar(32) NOT NULL,
  `numero` varchar(32) NOT NULL,
  PRIMARY KEY (`jugador`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
