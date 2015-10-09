<?php

/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/18
 * Time: 20:16
 */
define("SERVER", "localhost");
define("USER", "root");
define("PASSWORD", "root");
define("DATABASE", "logodoctor");
define("CHARSET", "utf8");

class mysql
{
    private $server = SERVER; //服务器名
    private $user = USER; //数据库用户名
    private $password = PASSWORD; //数据库密码
    private $database = DATABASE; //数据库名
    private $link; //mysql连接标识符
    private $charset = CHARSET; //数据库编码,默认为utf8

    function __construct()
    {
        $this->connect();
    }

    function connect()
    {
        $this->link = mysql_connect($this->server, $this->user, $this->password) or die($this->error("数据库服务器连接出错!"));
        mysql_select_db($this->database, $this->link) or die($this->error("数据库连接出错!"));
        mysql_query("set names '$this->charset'");
        mysql_query("SET CHARACTER SET '$this->charset'");
        mysql_query("SET CHARACTER_SET_RESULTS='$this->charset'");
    }

    function query($sql)
    {
        $result = mysql_query($sql, $this->link);
        if (!$result) {
            $this->error($sql . "语句执行失败!");
            return false;
        } else {
            return $result;
        }
    }


    function fetcharray($result)
    {
        return mysql_fetch_array($result);
    }

    function fetchall($result)
    {
        while ($row = mysql_fetch_array($result)) {
            $arr[] = $row;
        }
        mysql_free_result($result);
        if (isset($arr)) {
            return $arr;
        } else {
            return null;
        }
    }

    function numrows($result)
    {
        return mysql_num_rows($result);
    }

    function numfields($result)
    {
        return mysql_num_fields($result);
    }

    function affectedrows()
    {
        return mysql_affected_rows($this->link);
    }

    function version()
    {
        return mysql_get_server_info();
    }

    function insertid()
    {
        return mysql_insert_id($this->link);
    }


    function checksql($db_string, $querytype = 'select')
    {
        $clean = '';
        $old_pos = 0;
        $pos = -1;
        //如果是普通查询语句，直接过滤一些特殊语法
        if ($querytype == 'select') {
            $notallow1 = "[^0-9a-z@._-]{1,}(union|sleep|benchmark|load_file|outfile)[^0-9a-z@.-]{1,}";
            //$notallow2 = "--|
        }
    }

    function close()
    {
        mysql_close($this->link);
    }

    function error($err_msg = "")
    {
        if ($err_msg == "") {
        } else {
            echo $err_msg;
        }
        return null;
    }

    function __destruct()
    {
        $this->close();
    }
}