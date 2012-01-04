
<g:each in="${atts}">

  <p>
    <label for="att|${it.id}">${it.label}</label>
    <g:select name="att|${it.id}"
            from="${it.choices}"
            optionKey="id"
            optionValue="label"
            value="label"
            noSelection="['':'--'+it.label+'--']"
            />
  </p>
</g:each>