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
                $pathArr = explode(".", $imgPath);
                $filePath = ".." . $pathArr[0] . ".txt";
                if (is_file($filePath)) {
                    /**
                     * 处理完毕，更新数据库
                     */
                    $file = fopen($filePath, "r");
                    if ($file) {
                        $logoId = fgets($file);
                        if ($logoId) {
                            $sql = "UPDATE `" . TABLE_HISTORY . "` SET `logoId`=$logoId,`isProcessing`=0 WHERE `id`=$history->id";
                            $mysql->query($sql);
                            $affetctRows = $mysql->affectedrows();
                            if ($affetctRows > 0) {
                                $history->logoId = $logoId;
                                $history->processing = 0;
                            }
                        }
                    }
                    fclose($file);
                    /**
                     * 删除文件
                     */
                    chmod($filePath, 511);
                    unlink($filePath);
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