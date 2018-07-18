<#macro setting_menu setting_menu_tab>
<div class="panel panel-default">
  <div class="list-group">
    <a href="/user/setting/profile" class="list-group-item <#if setting_menu_tab == 'profile'>active</#if>">个人设置</a>
    <a href="/user/setting/changeAvatar" class="list-group-item <#if setting_menu_tab == 'changeAvatar'>active</#if>">修改头像</a>
    <a href="/user/setting/changePassword" class="list-group-item <#if setting_menu_tab == 'changePassword'>active</#if>">修改密码</a>
  </div>
</div>
</#macro>
