<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/21
 * Time: 20:06
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";
include_once "model/History.php";

define("QUERY_ALL", "select * from " . TABLE_HISTORY);

$mysql = new MySql();
$result = $mysql->query(QUERY_ALL);
$arr = $mysql->fetchall($result);
echo History::getJsons($arr);