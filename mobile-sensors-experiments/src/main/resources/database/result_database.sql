/*
SQLyog Community Edition- MySQL GUI v8.12 
MySQL - 5.1.37-1ubuntu5 : Database - experiments
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`experiments` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `experiments`;

/*Table structure for table `experiment_group` */

DROP TABLE IF EXISTS `experiment_group`;

CREATE TABLE `experiment_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Table structure for table `experiment_result` */

DROP TABLE IF EXISTS `experiment_result`;

CREATE TABLE `experiment_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `communication_range` double DEFAULT NULL,
  `communication_prob` double DEFAULT NULL,
  `sensor_class` text,
  `experiment_set_id` int(11) NOT NULL,
  `average_rms` double DEFAULT NULL,
  `message_count` int(11) DEFAULT NULL,
  `average_sensor_pairs_in_range` int(11) DEFAULT NULL,
  `starttime` bigint(11) DEFAULT NULL,
  `endtime` bigint(11) DEFAULT NULL,
  `lengthscale` double DEFAULT NULL,
  `timescale` double DEFAULT NULL,
  `expanded_partial_nodes` int(11) DEFAULT NULL,
  `total_partial_nodes` int(11) DEFAULT NULL,
  `function_calls` int(11) DEFAULT NULL,
  `cache_misses` int(11) DEFAULT NULL,
  `bounded_ms_tree_value` double DEFAULT NULL,
  `bounded_ms_factor_graph_value` double DEFAULT NULL,
  `bounded_ms_optimal_value` double DEFAULT NULL,
  `bounded_ms_upper_bound` double DEFAULT NULL,
  `message_size` double DEFAULT NULL,
  `bounded_ms_approx_ratio` double DEFAULT NULL,
  `sensor_count` int(11) DEFAULT NULL,
  `capturetime` double DEFAULT NULL,
  `patrollingloss` double DEFAULT NULL,
  `average_field_value` double DEFAULT NULL,
  `average_entropy` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1191 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Table structure for table `experiment_set` */

DROP TABLE IF EXISTS `experiment_set`;

CREATE TABLE `experiment_set` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` tinytext,
  `experiment_group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
