
<p>
  <label for="category">Podkategoria</label>
<g:select name="category" 
          from="${cats}" 
          optionKey="id"
          optionValue="label"
          noSelection="['':'--Wybierz podkategorię--']"/>
</p>