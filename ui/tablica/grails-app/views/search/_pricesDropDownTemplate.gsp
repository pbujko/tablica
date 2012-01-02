<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<g:select name="cenaOd" noSelection="['':'--Cena od--']"
          onChange="document.forms['catAttsSelect'].submit();" 
          value="${params.priceMin}" 
          from="${['1000', '2000', '3000', '4000']}" />

<g:select name="cenaDo" noSelection="['':'--Cena do--']"
          onChange="document.forms['catAttsSelect'].submit();" 
          value="${params.priceMax}" 
          from="${['1000', '2000', '3000', '4000']}" />

