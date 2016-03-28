<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/24
 * Time: 14:02
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";
include_once "model/History.php";
include_once "model/Logo.php";

define("QUERY_STATE", "SELECT * FROM " . TABLE_HISTORY . " WHERE `id` IN");

if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["ids"])) {
        $ids = $_GET["ids"];
    }
}

if (!isset($ids)) {
    echo "请求参数错误";
} else {
    $mysql = new MySql();
    $result = $mysql->query(QUERY_STATE . "(" . $ids . ")");
    $arr = $mysql->fetchall($result);
    $histories = History::getHistorys($arr);
    if ($histories) {
        foreach ($histories as $history) {
            if (!$history->logoId) {
                /**
                 * 查看是否处理完毕
                 */
                $imgPath = $history->img;
                $filePath = ".." . $imgPath . ".txt";
                if (is_file($filePath)) {
                    /**
                     * 处理完毕，更新数据库
                     */
                    $file = fopen($filePath, "r");
                    if ($file) {
                        $logoName = fgets($file);
                        if ($logoName) {
                            $logoName = trim(substr($logoName, 2));
                            //根据logoName获取logoId
                            $sql = "select * from " . TABLE_LOGO . " where `img`='$logoName' OR `moreImg` LIKE '$logoName'";
                            $result = $mysql->query($sql);
                            $value = $mysql->fetcharray($result);
                            $logo = Logo::getLogo($value);
                        } else {
                            $logo = new Logo(-1, null, null, null, null, null);
                        }
                        if ($logo) {
                            $sql = "UPDATE `" . TABLE_HISTORY . "` SET `logoId`=$logo->id,`isProcessing`=0 WHERE `id`=$history->id";
                            $mysql->query($sql);
                            $affetctRows = $mysql->affectedrows();
                            if ($affetctRows > 0) {
                                $history->logoId = $logo->id;
                                $history->processing = 0;
                            }
                        }
                    }
                    fclose($file);
                    /**
                     * 删除文件
                     */
                    chmod($filePath, 511);
//                    unlink($filePath);
                }
            }


            if ($history->logoId) {
                $count[] = $history->id;
            }
        }
    }
    if (isset($count)) {
        echo implode(",", $count);
    } else {
        echo "";
    }
}