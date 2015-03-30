<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		  <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
		  <script type="text/javascript" src="../js/jquery.form.js"></script>
	</head>
	<body>
	详细参见Supplies类{<br>
	<br>
	CREATE TABLE `supplies` (<br>
  `id` int(7) unsigned NOT NULL AUTO_INCREMENT,<br>
  `name` varchar(64) DEFAULT NULL,<br>
  `supplies_type` enum('video','image','info') DEFAULT 'video' COMMENT '类型',<br>
  `user_id` varchar(64) DEFAULT NULL COMMENT '物料上传人',<br>
  `file_path` varchar(128) DEFAULT NULL,<br>
  `info_context` varchar(2048) DEFAULT NULL COMMENT 'info内容',<br>
  `stats` enum('upload','first_pass','final_pass','refuse') DEFAULT NULL,<br>
  `oper_fristuser` varchar(64) DEFAULT NULL COMMENT '初审用户',<br>
  `oper_fristcomment` varchar(128) DEFAULT NULL,<br>
  `oper_finaluser` varchar(64) DEFAULT NULL COMMENT '终审用户',<br>
  `oper_finalcomment` varchar(64) DEFAULT NULL,<br>
  `seq_number` varchar(32) DEFAULT NULL COMMENT '北广回填 报审号',<br>
  `car_numer` varchar(64) DEFAULT NULL COMMENT '北广回填 磁带号',<br>
  `response_cid` varchar(64) DEFAULT NULL COMMENT '北广回填 内容编号',<br>
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间 ',<br>
  `edit_time` datetime DEFAULT NULL COMMENT '最后操作时间 ',<br>
  PRIMARY KEY (`id`)<br>
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8<br>
	<br>
	
	}<br>
	下面是列表数据
	    <#list list as item>
	     ${item.name}<br>
	     </#list> 
	     
	     ${pageNum}
	</body>
</html>
