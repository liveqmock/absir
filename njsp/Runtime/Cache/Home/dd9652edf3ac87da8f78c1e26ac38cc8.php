<?php if (!defined('THINK_PATH')) exit();?><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>WeiPHP-简洁而强大的开源微信公众平台开发框架 weiphp.cn</title>
<meta content="遵循Apache2开源协议,免费提供使用,微信功能插件化开发,多公众号管理,配置简单" name="keywords"/>
<meta content="weiphp 简洁而强大的开源微信公众平台开发框架微信功能插件化开发,多公众号管理,配置简单" name="description"/>
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link type="text/css" rel="stylesheet" href="/njsp/Public/Home/css/about.css?v=<?php echo SITE_VERSION;?>" />
<link type="text/css" rel="stylesheet" href="/njsp/Public/Home/css/forum.css?v=<?php echo SITE_VERSION;?>" />
<script type="text/javascript" src="/njsp/Public/static/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="/njsp/Public/static/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/njsp/Public/Home/js/admin_common.js?v=<?php echo SITE_VERSION;?>"></script>
</head>
<body>
	<div class="head">
    	<div class="wrap">
        	<h1 class="fl"><a class="logo" href="<?php echo SITE_URL;?>" title="返回首页">首家开源的微信公众平台开发框架微信功能插件化开发,多公众号管理,配置简单</a></h1>
            <div class="nav">
            	<a <?php if(ACTION_NAME == 'index' and CONTROLLER_NAME == 'Index'): ?>class="cur"<?php endif; ?> href="<?php echo U('home/index/index');?>">首页</a>
                <!--<a <?php if(ACTION_NAME == 'index' and CONTROLLER_NAME == 'Forum'): ?>class="cur"<?php endif; ?> href="<?php echo U('home/forum/index');?>">讨论区</a>-->
                <!--<a <?php if(ACTION_NAME == 'store' and CONTROLLER_NAME == 'Forum'): ?>class="cur"<?php endif; ?> href="<?php echo U('home/forum/store');?>">插件商店</a>-->
                
                <a <?php if(ACTION_NAME == 'help'): ?>class="cur"<?php endif; ?> href="<?php echo U('home/index/help');?>">帮助中心</a>
                <a href="<?php echo U('home/index/main');?>">管理中心</a>
                <a href="http://www.weiphp.cn/wiki" target="_blank">二次开发手册</a>
                <a href="http://bbs.weiphp.cn" target="_blank">论坛</a>
            </div>
        </div>
    </div>

    <div class="wrap">
    	<div class="content" style="min-height:400px">
    		<h5>关于我们</h5>
			<p>weiphp是一个开源，高效，简洁的微信开发平台，它是基于oneThink这个简单而强大的内容管理框架实现的。</p>
            <p>旨在帮助开发者快速实现微信公众账号的个性化功能。</p>
            <h5>联系我们</h5>
            <p>email:&nbsp;weiphp@weiphp.cn</p>
            <p>官方QQ群:&nbsp;8322255</p>
		</div>
    </div>
    <div class="footer">
    	<div class="wrap foot_wrap">
        	<div class="foot_div">
            	<h6>关于我们</h6>
                <a href="<?php echo U('about');?>">关于我们</a><br/>
                <a href="<?php echo U('about');?>">联系方式</a><br/>
				<!--<a href="#">友情链接</a><br/>-->
  				<a href="<?php echo U('license');?>">授权协议</a>
                
            </div>
        	<div class="foot_div">
            	<h6>帮助</h6>
                <a href="http://bbs.weiphp.cn" target="_blank">官方论坛</a><br/>
                <!--<a href="javasctipt:;" target="_blank">官方微博</a><br/>-->
                <a href="javascript:;">官方QQ交流群：8322255</a>
				
            </div>
            <div class="getqrcode">
            	<img src="/njsp/Public/Home/images/getqrcode.jpg"/>
                <p>微信扫码左侧二维码<br/>并加关注WeiPHP官方微信公众号<br/>体验WeiPHP的最新功能</p>
            </div>
            <div class="foot_logo"></div>
        </div>
        <p class="copyright"><?php echo C('COPYRIGHT');?> <?php echo C('WEB_SITE_ICP');?></p>
    </div>
<!-- 页面footer钩子，一般用于加载插件JS文件和JS代码 -->
<?php echo hook('pageFooter', 'widget');?>    
</body>
</html>