<#macro hottoic p=1 limit=5>
  <div class="panel panel-default">
      <div class="panel-heading">一周热门帖</div>
    <@hottopic_tag p=p limit=limit>
      <table class="table">
          <tbody>
        <#list page.getContent() as topic>
        <tr>
            <td><a href="/topic/${topic.id}">${topic.title}</a></td>
            <td align="right">${topic.commentCount!0}</td>
        </tr>
        </#list>
          </tbody>
      </table>
    </@hottopic_tag>
  </div>
</#macro>