<?php
set_time_limit ( 0 );
$prefix = C ( 'DB_PREFIX' );

$config_map ['name'] = 'SYSTEM_UPDATRE_VERSION';
$res = M ( 'config' )->where ( $config_map )->getField ( 'value' );

if ($res < 20140530) {
	 $this->error('该补丁包仅适用于5月30号发布的2.0beta版本');
	 exit;
}

unset ( $map );
$map ['name'] = 'keyword';
$model_id = M ( 'model' )->where ( $map )->getField ('id');

$sql = 'INSERT INTO '.$prefix."attribute (`name`,`title`,`field`,`type`,`value`,`remark`,`is_show`,`extra`,`model_id`,`is_must`,`status`,`update_time`,`create_time`,`validate_rule`,`validate_time`,`error_info`,`validate_type`,`auto_rule`,`auto_time`,`auto_type`) VALUES ('request_count','请求数','int(10) NOT NULL','num','0','','0','','".$model_id."','0','1','1401938983','1401938983','','3','','regex','','3','function')";
$res = M()->execute($sql);	

$sql = "UPDATE `".$prefix."model` SET `list_grid`='keyword:关键词\r\nkeyword_type|get_name_by_status:匹配类型\r\naddon:所属插件\r\naim_id:插件数据ID\r\ncTime|time_format:增加时间\r\nrequest_count:请求数\r\nid:操作:[EDIT]|编辑,[DELETE]|删除' WHERE (\"`name`='keyword'\")";
$res = M()->execute($sql);	


$sql = "UPDATE `".$prefix."attribute` SET `remark`='一旦保存后不能再修改，请确认是否正确', `is_show`='2' WHERE (\"`name`='public_id' and `title`='公众号原始id'\" )";
$res = M()->execute($sql);	


// 更新本地版本号
$res = M ( 'config' )->where ( $config_map )->setField ( 'value', 20140605 );
S ( 'DB_CONFIG_DATA', null );

$this->success ( '操作完成' );