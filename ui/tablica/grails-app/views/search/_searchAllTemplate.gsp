<div style="float:right">
<g:form name="searchByPhrase" controller="search" action="byPhrase" method="get">
  szukaj wszędzie:<br />
  <g:textField name="q" value="${params.q}" /><br/>
  <g:render template="/search/citiesDropDownSearchAllTemplate" model="[allCities: allCities, citySelected:citySelected]" /><br />
</g:form>
</div>