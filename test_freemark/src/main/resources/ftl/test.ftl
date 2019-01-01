<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker 测试</title>
</head>
<body>
<#--这是 freemarker 注释，不会输出到文件中-->
<h1>${name}；${message}</h1>
<br>
<hr>
<br>
<#assign  name="tangpeng">
你的姓名是：${name}
<br>
<hr>
<br>
<#assign  info={"name":"tangpeng","address":"湖南省邵阳"}>
联系人:${info.name}<br>
地址：${info.address}

<#include "header.ftl">

<br>
<hr>
<#assign  ff=false>
<#if ff=true>
    不服打到你服为止
<#elseif ff=false>
     angnaiognoa
</#if>
<br>
<hr>
<#list goodsList as ba>
    ${ba_index},水果名为：${ba.name},价格为：${ba.price}<br>
</#list>
<hr>
${goodsList?size}
</body>

<#assign  str="{'name':'tp','address':'华南'}"/>
<#assign jsonObj=str?eval/>
姓名为：${jsonObj.name} <br>
地址：${jsonObj.address}

<hr>
<br>

日期为：${date?date}<br>
时间为：${date?time}<br>
日期时间为：${date?datetime}
转化格式为：${date?string('yyyy年MM月dd日 HH:ss:mm')}

<#--数字转化-->
number:${number}
<hr>

<#--空值处理-->
<#--${str!"空值默认值"}-->
${stt!}
<#if stt??>
str存在值
<#else>
str不存在值
</#if>
</html>