<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->
<p>
  <label for="category">Podkategoria</label>
<g:select name="category" 
          from="${cats}" 
          optionKey="id"
          optionValue="label"
          noSelection="['':'--Wybierz podkategorię--']"/>
</p>