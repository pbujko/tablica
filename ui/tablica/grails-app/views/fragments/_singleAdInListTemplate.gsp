
<div class="singleAdInList">
  <g:if test="${ad.cover}">
    <g:link mapping="adDetails" params="[id:ad.id, niewazneCo:ad.title.replace('/','')]">
      <img src="${createLink(controller:'adImage', action:'thumbnail', id:ad.cover)}" alt="${ad.title}" width="50"/>
    </g:link>
  </g:if>

  <g:link mapping="adDetails" params="[id:ad.id, niewazneCo:ad.title.replace('/','')]">${ad.title}</g:link>

  <g:if test="${ad.price}">
    <span class="price">
      <g:formatNumber number='${Integer.parseInt(ad.price)}' type="number" /> EUR
    </span>
  </g:if>
  <span class="city">${ad.city.label}</span>
</div>
