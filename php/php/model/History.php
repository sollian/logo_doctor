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
    public $logoId;
    public $img;
    public $isRead;
    public $isProcessing;
    public $createTime;

    function __construct($id, $logoId, $img, $isRead, $isProcessing, $createTime)
    {
        $this->id = $id;
        $this->logoId = $logoId;
        $this->img = $img;
        $this->isRead = $isRead;
        $this->isProcessing = $isProcessing;
        $this->createTime = $createTime;
    }

    function toString()
    {
        return $this->id . "," . $this->logoId . "," . $this->img . "," . $this->createTime;
    }

    public static function getJsons($arr)
    {
        $historys = History::getHistorys($arr);
        if ($historys) {
            return json_encode($historys, JSON_UNESCAPED_UNICODE);
        } else {
            return "[]";
        }
    }

    public static function getJson($value)
    {
        $history = History::getHistory($value);
        if ($history) {
            return json_encode($history, JSON_UNESCAPED_UNICODE);
        } else {
            return null;
        }
    }

    public static function getHistorys($arr)
    {
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
            return new History($value["id"], $value["logoId"], $value["img"], $value["isRead"],
                $value["isProcessing"], $value["createTime"]);
        } else {
            return null;
        }
    }

}