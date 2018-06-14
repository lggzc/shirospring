<%@ page language="java" pageEncoding="UTF-8"%>  
  
<html>  
<body>  
<div style="color:red; font-size:22px;">${error_msg}</div>  
  
<form action="login" method="GET">  
    姓名：<input type="text" name="username"/><br/>  
    密码：<input type="text" name="password"/><br/>  
    <input type="submit" value="确认"/>  
</form>  
</body>  
</html>  