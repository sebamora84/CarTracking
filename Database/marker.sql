CREATE TABLE `marker`  (
  `internal_id` int(10) NOT NULL AUTO_INCREMENT,
  `id` int(10) NOT NULL,
  `device_id` int(10) NOT NULL,
  `unit_id` int(10) NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `accuracy` double NOT NULL,
  `type` varchar(30) NOT NULL,
  `timestamp` datetime NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `markers`
--
ALTER TABLE `marker`
  ADD PRIMARY KEY (`internal_id`),
  ADD UNIQUE KEY `id_1` (`internal_id`),
  ADD UNIQUE KEY `id_2` (`id`);
