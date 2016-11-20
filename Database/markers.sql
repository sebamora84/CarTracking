CREATE TABLE IF NOT EXISTS `markers` (
  `internal_id` int(10) NOT NULL AUTO_INCREMENT,
  `id` int(10) NOT NULL,
  `unit_id` int(10) NOT NULL,
  `lat` float(10,6) NOT NULL,
  `lng` float(10,6) NOT NULL,
  `accuracy` float(10,6) NOT NULL,
  `type` varchar(30) NOT NULL,
  `timestamp` datetime NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`internal_id`),
  UNIQUE KEY `id_1` (`internal_id`),
  UNIQUE KEY `id_2` (`id`),
)