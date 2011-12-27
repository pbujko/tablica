-- MySQL dump 10.13  Distrib 5.5.10, for osx10.6 (i386)
--
-- Host: localhost    Database: tablica
-- ------------------------------------------------------
-- Server version	5.5.10

--
-- Table structure for table `ad`
--


DROP TABLE IF EXISTS `ad`;
CREATE TABLE `ad` (
  `ad_id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `ad_hashed_id` varchar(50) NOT NULL,
  `ad_title` varchar(255) NOT NULL,
  `ad_atts` varchar(255),
  `ad_createDate` timestamp,
  PRIMARY KEY (`ad_id`),
  UNIQUE KEY `ad_id` (`ad_id`),
  UNIQUE KEY `ad_hashed_id` (`ad_hashed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `cat_id` varchar(255) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



--
-- Table structure for table `ad_categs`
--

DROP TABLE IF EXISTS `ad_categs`;
CREATE TABLE `ad_categs` (
  `ad_id` int(11) NOT NULL,
  `cat_id` varchar(255) NOT NULL,
  PRIMARY KEY (`ad_id`,`cat_id`),
  KEY `FK11D08417716F6A88` (`ad_id`),
  KEY `FK11D084175A3E2C50` (`cat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



-- Dump completed on 2011-12-19 11:13:00