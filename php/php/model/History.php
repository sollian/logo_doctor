<?php

/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/21
 * Time: 20:00
 */
class History
{
    public $id;
    public $userName;
    public $logoId;
    public $img;
    public $read;
    public $processing;
    public $createTime;

    function __construct($id, $userName, $logoId, $img, $read, $processing, $createTime)
    {
        $this->id = $id;
        $this->userName = $userName;
        $this->logoId = $logoId;
        $this->img = $img;
        $this->read = $read;
        $this->processing = $processing;
        $this->createTime = $createTime;
    }

    function getInsertSql($table)
    {
        return "INSERT INTO " . $table . " (`userName`,`img`) VALUE ('" . $this->userName . "','" . $this->img . "')";
    }

    public static function getJsons($arr)
    {
        if (!$arr) {
            return null;
        }
        $historys = History::getHistorys($arr);
        if ($historys) {
            return json_encode($historys, JSON_UNESCAPED_UNICODE);
        } else {
            return "[]";
        }
    }

    public static function getJson($value)
    {
        if (!$value) {
            return null;
        }
        $history = History::getHistory($value);
        if ($history) {
            return json_encode($history, JSON_UNESCAPED_UNICODE);
        } else {
            return null;
        }
    }

    public static function getHistorys($arr)
    {
        if (!$arr) {
            return null;
        }
        foreach ($arr as $value) {
            $historys[] = History::getHistory($value);
        }
        if (isset($historys)) {
            return $historys;
        } else {
            return null;
        }
    }

    public static function getHistory($value)
    {
        if ($value) {
            return @new History($value["id"], $value["userName"], $value["logoId"], $value["img"], $value["isRead"],
                $value["isProcessing"], $value["createTime"]);
        } else {
            return null;
        }
    }

}