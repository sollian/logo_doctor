-- phpMyAdmin SQL Dump
-- version 4.4.14.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2015-09-18 16:03:37
-- 服务器版本： 5.6.26-log
-- PHP Version: 5.4.45

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `logodoctor`
--

-- --------------------------------------------------------

--
-- 表的结构 `logo`
--

CREATE TABLE IF NOT EXISTS `logo` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `img` text NOT NULL,
  `extra` text,
  `description` text NOT NULL,
  `category` text NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `logo`
--

INSERT INTO `logo` (`id`, `name`, `img`, `extra`, `description`, `category`) VALUES
(1, '奥迪', '/img/logo/car/audi.jpg', '德系', '奥迪是德国历史最悠久的汽车制造商之一。从1932年起，奥迪开始采用四环徽标，它象征着奥迪与小奇迹（DKW）、霍希（Horch）和漫游者（Wanderer）在克姆尼茨理合并为汽车联盟公司。现为大众汽车公司的子公司，总部设在德国的英戈尔施塔特，主要产品有A1、A2、A3、A4、A5、A6L、A7、A8L、Q3、Q5、Q7、R8、TT以及RS性能系列等等。奥迪轿车的标志代表着合并前的四家公司。这些公司曾经是自行车、摩托车及小客车的生产厂家。由于该公司原是由4家公司合并而成，因此每一环都是其中一个公司的象征。', '汽车'),
(2, '宝骏', '/img/logo/car/baojun.jpg', '国产', '宝骏，是上汽通用五菱2010年创建的自主汽车品牌。上汽通用五菱正式发布的新乘用车品牌“宝骏汽车”，标志这个中国微车领头羊开始正式进军方兴未艾的轿车市场。宝骏品牌是上汽通用五菱积二十多年造车经验，融八年合资运作精华，充分集成上汽、通用、五菱三方股东方的优势，着力打造的适合全球新兴市场的乘用车品牌。宝骏品牌源由：“骏”的本义是良驹，宝骏即人们最心爱的良驹。', '汽车');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `logo`
--
ALTER TABLE `logo`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `logo`
--
ALTER TABLE `logo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
