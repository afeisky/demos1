CREATE TABLE `bd_json` (
  `ids` int(11) NOT NULL AUTO_INCREMENT,
  `update_time` datetime NOT NULL,
  `filename` varchar(50) NOT NULL DEFAULT '',
  `type` varchar(10) DEFAULT NULL,
  `imei` varchar(16) DEFAULT '',
  `time` datetime DEFAULT NULL,
  `comment1` varchar(50) DEFAULT '',
  `data_ids` int(11) DEFAULT NULL,
  `json` json DEFAULT NULL,
  `filesave` json DEFAULT NULL,
  `done` int(11) DEFAULT '0',
  `done_time` datetime DEFAULT NULL,
  `json1` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ids`,`filename`)
) ENGINE=MyISAM AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
