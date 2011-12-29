
<g:form name="searchByPhrase" controller="search" action="byPhrase" method="get">
  szukaj we wszystkich ogloszeniach: <g:textField name="q" value="${params.q}" />
  <g:render template="/search/citiesDropDownSearchAllTemplate" model="[allCities: allCities, citySelected:citySelected]" /><br />
</g:form>
