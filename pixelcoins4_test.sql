-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 10, 2022 at 01:11 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pixelcoins4_test`
--

-- --------------------------------------------------------

--
-- Table structure for table `accionistasserver`
--

CREATE TABLE `accionistasserver` (
  `accionistaServerId` varchar(36) NOT NULL,
  `nombreAccionista` varchar(16) NOT NULL,
  `tipoAccionista` varchar(7) NOT NULL,
  `empresa` varchar(16) NOT NULL,
  `cantidad` int(7) NOT NULL,
  `precioApertura` double(12,2) NOT NULL,
  `fechaApertura` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `activosinfo`
--

CREATE TABLE `activosinfo` (
  `nombreActivo` varchar(32) NOT NULL,
  `precio` double(12,2) NOT NULL,
  `tipoActivo` varchar(15) NOT NULL,
  `nombreActivoLargo` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `cuentasweb`
--

CREATE TABLE `cuentasweb` (
  `cuentaWebId` varchar(36) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `deudas`
--

CREATE TABLE `deudas` (
  `deudaId` varchar(36) NOT NULL,
  `deudor` varchar(16) NOT NULL,
  `acredor` varchar(16) NOT NULL,
  `pixelcoinsRestantes` double(12,2) NOT NULL,
  `tiempoRestante` int(3) NOT NULL,
  `interes` int(4) NOT NULL,
  `cuota` double(6,2) NOT NULL,
  `fechaUltimapaga` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `empleados`
--

CREATE TABLE `empleados` (
  `empleadoId` varchar(36) NOT NULL,
  `nombre` varchar(16) NOT NULL,
  `empresa` varchar(16) NOT NULL,
  `sueldo` double(12,2) NOT NULL,
  `cargo` varchar(16) NOT NULL,
  `tipoSueldo` varchar(8) NOT NULL,
  `fechaUltimaPaga` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `empresas`
--

CREATE TABLE `empresas` (
  `empresaId` varchar(36) NOT NULL,
  `nombre` varchar(16) NOT NULL,
  `owner` varchar(16) NOT NULL,
  `pixelcoins` double(12,2) NOT NULL,
  `ingresos` double(12,2) NOT NULL,
  `gastos` double(12,2) NOT NULL,
  `icono` varchar(34) NOT NULL,
  `descripcion` varchar(32) NOT NULL,
  `cotizada` int(1) NOT NULL,
  `accionesTotales` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `jugadores`
--

CREATE TABLE `jugadores` (
  `jugadorId` varchar(36) NOT NULL,
  `nombre` varchar(16) NOT NULL,
  `pixelcoins` double(12,2) NOT NULL,
  `nVentas` int(6) NOT NULL,
  `ingresos` double(12,2) NOT NULL,
  `gastos` double(12,2) NOT NULL,
  `nInpagosDeuda` int(6) NOT NULL,
  `nPagosDeuda` int(6) NOT NULL,
  `numeroVerificacionCuenta` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- --------------------------------------------------------

--
-- Table structure for table `mensajes`
--

CREATE TABLE `mensajes` (
  `mensajeId` varchar(36) NOT NULL,
  `enviador` varchar(16) NOT NULL,
  `destinatario` varchar(16) NOT NULL,
  `mensaje` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mensajes`
--

-- --------------------------------------------------------

--
-- Table structure for table `ofertasaccionesserver`
--

CREATE TABLE `ofertasaccionesserver` (
  `ofertaAccionServerId` varchar(36) NOT NULL,
  `nombreOfertante` varchar(16) NOT NULL,
  `empresa` varchar(16) NOT NULL,
  `precio` double(12,2) NOT NULL,
  `cantidad` int(7) NOT NULL,
  `fecha` varchar(12) NOT NULL,
  `tipoOfertante` varchar(7) NOT NULL,
  `precioApertura` double(12,2) NOT NULL,
  `accionistaEmpresaServerId` varchar(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `ordenespremarket`
--

CREATE TABLE `ordenespremarket` (
  `orderPremarketId` varchar(36) NOT NULL,
  `jugador` varchar(16) NOT NULL,
  `nombreActivo` varchar(32) NOT NULL,
  `cantidad` int(7) NOT NULL,
  `tipoAccion` varchar(12) NOT NULL,
  `posicionAbiertaId` varchar(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `posicionesabiertas`
--

CREATE TABLE `posicionesabiertas` (
  `posicionAbiertaId` varchar(36) NOT NULL,
  `jugador` varchar(16) NOT NULL,
  `tipoActivo` varchar(15) NOT NULL,
  `nombreActivo` varchar(32) NOT NULL,
  `cantidad` int(7) NOT NULL,
  `precioApertura` double(12,2) NOT NULL,
  `fechaApertura` varchar(12) NOT NULL,
  `tipoPosicion` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `posicionesabiertas`
--
-- --------------------------------------------------------

--
-- Table structure for table `posicionescerradas`
--

CREATE TABLE `posicionescerradas` (
  `posicionCerradaId` varchar(36) NOT NULL,
  `jugador` varchar(16) NOT NULL,
  `tipoActivo` varchar(15) NOT NULL,
  `nombreActivo` varchar(32) NOT NULL,
  `cantidad` int(7) NOT NULL,
  `precioApertura` double(12,2) NOT NULL,
  `fechaApertura` varchar(12) NOT NULL,
  `precioCierre` double(12,2) NOT NULL,
  `fechaCierre` varchar(12) NOT NULL,
  `tipoPosicion` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tienda`
--

CREATE TABLE `tienda` (
  `tiendaObjetoId` varchar(36) NOT NULL,
  `jugador` varchar(16) NOT NULL,
  `objeto` varchar(34) NOT NULL,
  `cantidad` int(2) NOT NULL,
  `precio` double(12,2) NOT NULL,
  `durabilidad` int(2) NOT NULL,
  `encantamientos` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `transacciones`
--

CREATE TABLE `transacciones` (
  `transaccionId` varchar(36) NOT NULL,
  `fecha` varchar(12) NOT NULL,
  `comprador` varchar(16) NOT NULL,
  `vendedor` varchar(16) NOT NULL,
  `cantidad` double(12,2) NOT NULL,
  `objeto` varchar(32) NOT NULL,
  `tipo` varchar(29) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accionistasserver`
--
ALTER TABLE `accionistasserver`
  ADD PRIMARY KEY (`accionEmpresaServerId`);

--
-- Indexes for table `activosinfo`
--
ALTER TABLE `activosinfo`
  ADD PRIMARY KEY (`nombreActivo`);

--
-- Indexes for table `cuentasweb`
--
ALTER TABLE `cuentasweb`
  ADD PRIMARY KEY (`cuentaWebId`);

--
-- Indexes for table `deudas`
--
ALTER TABLE `deudas`
  ADD PRIMARY KEY (`deudaId`);

--
-- Indexes for table `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`empleadoId`);

--
-- Indexes for table `empresas`
--
ALTER TABLE `empresas`
  ADD PRIMARY KEY (`empresaId`);

--
-- Indexes for table `jugadores`
--
ALTER TABLE `jugadores`
  ADD PRIMARY KEY (`jugadorId`);

--
-- Indexes for table `mensajes`
--
ALTER TABLE `mensajes`
  ADD PRIMARY KEY (`mensajeId`);

--
-- Indexes for table `ofertasaccionesserver`
--
ALTER TABLE `ofertasaccionesserver`
  ADD PRIMARY KEY (`ofertaAccionServerId`);

--
-- Indexes for table `ordenespremarket`
--
ALTER TABLE `ordenespremarket`
  ADD PRIMARY KEY (`orderPremarketId`);

--
-- Indexes for table `posicionesabiertas`
--
ALTER TABLE `posicionesabiertas`
  ADD PRIMARY KEY (`posicionAbiertaId`);

--
-- Indexes for table `posicionescerradas`
--
ALTER TABLE `posicionescerradas`
  ADD PRIMARY KEY (`posicionCerradaId`);

--
-- Indexes for table `tienda`
--
ALTER TABLE `tienda`
  ADD PRIMARY KEY (`tiendaObjetoId`);

--
-- Indexes for table `transacciones`
--
ALTER TABLE `transacciones`
  ADD PRIMARY KEY (`transaccionId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
