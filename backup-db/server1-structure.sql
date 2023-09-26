/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : pixelcoins

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2020-12-07 22:56:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cuentas
-- ----------------------------
DROP TABLE IF EXISTS `cuentas`;
CREATE TABLE `cuentas` (
  `id` int(30) NOT NULL,
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
  `id_deuda` int(24) NOT NULL AUTO_INCREMENT,
  `deudor` varchar(24) NOT NULL,
  `acredor` varchar(24) NOT NULL,
  `pixelcoins` double(24,3) NOT NULL,
  `tiempo` int(24) NOT NULL,
  `interes` int(24) NOT NULL,
  `cuota` int(24) NOT NULL,
  `fecha` varchar(24) NOT NULL,
  PRIMARY KEY (`id_deuda`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for empleados
-- ----------------------------
DROP TABLE IF EXISTS `empleados`;
CREATE TABLE `empleados` (
  `id` int(24) NOT NULL AUTO_INCREMENT,
  `empleado` varchar(24) NOT NULL,
  `empresa` varchar(24) NOT NULL,
  `sueldo` double(24,3) NOT NULL,
  `cargo` varchar(24) NOT NULL,
  `tipo` varchar(24) NOT NULL,
  `fechaPaga` varchar(24) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for empresas
-- ----------------------------
DROP TABLE IF EXISTS `empresas`;
CREATE TABLE `empresas` (
  `id_empresa` int(24) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(24) NOT NULL,
  `owner` varchar(24) NOT NULL,
  `pixelcoins` double(24,3) NOT NULL,
  `ingresos` double(24,3) NOT NULL,
  `gastos` double(24,3) NOT NULL,
  `icono` varchar(24) NOT NULL,
  `descripcion` varchar(400) NOT NULL,
  PRIMARY KEY (`id_empresa`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for encantamientos
-- ----------------------------
DROP TABLE IF EXISTS `encantamientos`;
CREATE TABLE `encantamientos` (
  `id_encantamiento` int(24) NOT NULL AUTO_INCREMENT,
  `encantamiento` varchar(24) NOT NULL,
  `nivel` int(24) NOT NULL,
  `id_oferta` int(24) NOT NULL,
  PRIMARY KEY (`id_encantamiento`),
  KEY `id_oferta` (`id_oferta`),
  CONSTRAINT `encantamientos_ibfk_1` FOREIGN KEY (`id_oferta`) REFERENCES `ofertas` (`id_oferta`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=latin1;

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
  `pixelcoin` double(24,3) NOT NULL,
  `espacios` int(24) NOT NULL,
  `nventas` int(24) NOT NULL,
  `ingresos` double(24,3) NOT NULL,
  `gastos` double(24,3) NOT NULL,
  `beneficios` double(24,3) NOT NULL,
  `ninpagos` int(24) NOT NULL,
  `npagos` int(24) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for llamadasapi
-- ----------------------------
DROP TABLE IF EXISTS `llamadasapi`;
CREATE TABLE `llamadasapi` (
  `simbolo` varchar(24) NOT NULL,
  `precio` double(24,3) NOT NULL,
  `tipo` varchar(24) NOT NULL,
  PRIMARY KEY (`simbolo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mensajes
-- ----------------------------
DROP TABLE IF EXISTS `mensajes`;
CREATE TABLE `mensajes` (
  `id_mensaje` int(24) NOT NULL AUTO_INCREMENT,
  `destinatario` varchar(24) NOT NULL,
  `mensaje` varchar(300) NOT NULL,
  PRIMARY KEY (`id_mensaje`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for numerocuentas
-- ----------------------------
DROP TABLE IF EXISTS `numerocuentas`;
CREATE TABLE `numerocuentas` (
  `numero` int(30) NOT NULL,
  `jugador` varchar(30) NOT NULL,
  PRIMARY KEY (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for ofertas
-- ----------------------------
DROP TABLE IF EXISTS `ofertas`;
CREATE TABLE `ofertas` (
  `nombre` varchar(24) NOT NULL,
  `objeto` varchar(24) NOT NULL,
  `cantidad` int(24) NOT NULL,
  `precio` double(24,3) NOT NULL,
  `id_oferta` int(24) NOT NULL AUTO_INCREMENT,
  `durabilidad` int(24) NOT NULL,
  PRIMARY KEY (`id_oferta`)
) ENGINE=InnoDB AUTO_INCREMENT=269 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for posicionesabiertas
-- ----------------------------
DROP TABLE IF EXISTS `posicionesabiertas`;
CREATE TABLE `posicionesabiertas` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `jugador` varchar(16) NOT NULL,
  `tipo` varchar(16) NOT NULL,
  `nombre` varchar(16) NOT NULL,
  `cantidad` int(16) NOT NULL,
  `precioApertura` double(16,3) NOT NULL,
  `fechaApertura` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for posicionescerradas
-- ----------------------------
DROP TABLE IF EXISTS `posicionescerradas`;
CREATE TABLE `posicionescerradas` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `jugador` varchar(16) NOT NULL,
  `tipo` varchar(16) NOT NULL,
  `nombre` varchar(16) NOT NULL,
  `cantidad` int(16) NOT NULL,
  `precioApertura` double(16,3) NOT NULL,
  `fechaApertura` varchar(16) NOT NULL,
  `precioCierre` double(16,3) NOT NULL,
  `fechaCierre` varchar(16) NOT NULL,
  `rentabilidad` double(16,3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for solicitudes
-- ----------------------------
DROP TABLE IF EXISTS `solicitudes`;
CREATE TABLE `solicitudes` (
  `enviador` varchar(24) NOT NULL,
  `destinatario` varchar(24) NOT NULL,
  `tipo` int(24) NOT NULL,
  `id_tabla` int(24) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for transacciones
-- ----------------------------
DROP TABLE IF EXISTS `transacciones`;
CREATE TABLE `transacciones` (
  `id` int(24) NOT NULL AUTO_INCREMENT,
  `fecha` varchar(24) NOT NULL,
  `comprador` varchar(24) NOT NULL,
  `vendedor` varchar(24) NOT NULL,
  `cantidad` double(24,3) NOT NULL,
  `objeto` varchar(24) NOT NULL,
  `tipo` varchar(24) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2202 DEFAULT CHARSET=latin1;
