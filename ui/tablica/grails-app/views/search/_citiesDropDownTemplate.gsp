<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<select name="city" onChange="document.forms['catAttsSelect'].submit();">
  <g:each in="${allCities}">
    <option value="${it.id}" style="padding-left: ${it.depth * 5}px"  <g:if test="${it.code==citySelected}">selected="selected"</g:if>>${it.label}</option>
  </g:each>
</select>
