<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
    queryBinding='xslt2' schemaVersion='ISO19757-3'>

  <iso:title>Schematron file for "Läkarintyg för sjukpenning".</iso:title>

  <iso:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
  <iso:ns prefix="rg" uri="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:2"/>
  <iso:ns prefix="gn" uri="urn:riv:clinicalprocess:healthcond:certificate:2"/>
  <iso:ns prefix="tp" uri="urn:riv:clinicalprocess:healthcond:certificate:types:2"/>

  <iso:include href='types.sch#non-empty-string-pattern'/>
  <iso:include href='types.sch#boolean-pattern'/>
  <iso:include href='types.sch#cv-pattern'/>
  <iso:include href='types.sch#date-pattern'/>
  <iso:include href='types.sch#period-pattern'/>

  <iso:pattern id="intyg">
    <iso:rule context="//rg:intyg">
      <iso:assert test="count(gn:svar[@id='27']) le 1">
        Ett 'MU' får ha högst ett 'Smittbärarpenning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='1']) ge 1 and count(gn:svar[@id='1']) le 4">
        Ett 'MU' måste ha mellan 1 och 4 'Grund för medicinskt underlag'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='28']) = 1">
        Ett 'MU' måste ha ett 'Typ av sysselsättning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='29']) le 1">
        Ett 'MU' får ha högst ett 'Nuvarande arbete'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='30']) le 1">
        Ett 'MU' får ha högst ett 'Arbetsmarknadspolitiskt program'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='6']) ge 1 and count(gn:svar[@id='6']) le 3">
        Ett 'MU' måste ha mellan 1 och 3 'Typ av diagnos'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='31']) = 1">
        Ett 'MU' måste ha ett 'Huvudsaklig grund'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='32']) ge 1 and count(gn:svar[@id='32']) le 4">
        Ett 'MU' måste ha mellan 1 och 4 'Behov av sjukskrivning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='33']) le 1">
        Ett 'MU' får ha högst ett 'Arbetstidsförläggning'
      </iso:assert>
      <iso:assert test="count(gn:svar[@id='34']) le 1">
        Ett 'MU' får ha högst ett 'Arbetsresor'
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q27">
    <iso:rule context="//gn:svar[@id='27']">
      <iso:assert test="count(gn:delsvar[@id='27.1']) = 1">
        'Smittbärarpenning' måste ha ett 'Om smittbärarpenning'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q27">
    <iso:rule context="//gn:delsvar[@id='27.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1">
    <iso:rule context="//gn:svar[@id='1']">
      <iso:assert test="count(gn:delsvar[@id='1.1']) = 1">
        'Grund för medicinskt underlag (MU)' måste ha ett 'Typ av grund för MU'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='1.2']) = 1">
        'Grund för medicinskt underlag (MU)' måste ha ett 'Datum som grund för MU'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='1.3']) le 1">
        'Grund för medicinskt underlag (MU)' får ha högst ett 'Vilken annan grund finns för MU'.
      </iso:assert>
      <iso:assert test="not(normalize-space(preceding-sibling::gn:svar[@id='1']/gn:delsvar[@id='1.1']/tp:cv/tp:code) = normalize-space(gn:delsvar[@id='1.1']/tp:cv/tp:code))">
        Samma 'Typ av grund för MU' kan inte användas flera gånger i samma 'MU'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.1">
    <iso:rule context="//gn:delsvar[@id='1.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0001'"/>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^[1235]$')">
        'Typ av grund för MU' kan ha ett av värdena 1, 2, 3 eller 5.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.2">
    <iso:rule context="//gn:delsvar[@id='1.2']">
      <iso:extends rule="date"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.3">
    <iso:rule context="//gn:delsvar[@id='1.3']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q1.1-1.3">
    <iso:rule context="//gn:delsvar[@id='1.1']/tp:cv/tp:code[normalize-space(.) = '5']">
      <iso:assert test="../../../gn:delsvar[@id='1.3']">
        Om 'Typ av grund för MU' är 'Annat' så måste 'Vilken annan grund finns för MU' anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q28">
    <iso:rule context="//gn:svar[@id='28']">
      <iso:assert test="count(gn:delsvar[@id='28.1']) = 1">
        'Typ av sysselsättning' måste ha ett 'Typ av sysselsättning'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q28.1">
    <iso:rule context="//gn:delsvar[@id='28.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0002'"/>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^[1-5]$')">
        'Typ av sysselsättning' kan ha ett av värdena 1, 2, 3, 4 eller 5.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q28.1-q29">
    <iso:rule context="//gn:delsvar[@id='28.1']/tp:cv/tp:code[normalize-space(.)='1']">
      <iso:assert test="count(../../../../gn:svar[@id='29']) = 1">
        Om 'Typ av sysselsättning' besvarats med 1, måste 'Nuvarande arbete' besvaras
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q28.1-q30">
    <iso:rule context="//gn:delsvar[@id='28.1']/tp:cv/tp:code[normalize-space(.)='5']">
      <iso:assert test="count(../../../../gn:svar[@id='30']) = 1">
        Om 'Typ av sysselsättning' besvarats med 5, måste 'Arbetsmarknadspolitiskt program' besvaras
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q29">
    <iso:rule context="//gn:svar[@id='29']">
      <iso:assert test="count(gn:delsvar[@id='29.1']) = 1">
        'Nuvarande arbete' måste ha ett 'Yrke och arbetsuppgifter'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q29.1">
    <iso:rule context="//gn:delsvar[@id='29.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30">
    <iso:rule context="//gn:svar[@id='30']">
      <iso:assert test="count(gn:delsvar[@id='30.1']) = 1">
        'Arbetsmarknadspolitiskt program' måste ha ett 'Aktiviteter i arbetsmarknadspolitiskt program'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.1">
    <iso:rule context="//gn:delsvar[@id='30.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6">
    <iso:rule context="//gn:svar[@id='6']">
      <iso:assert test="count(gn:delsvar[@id='6.1']) = 1">
        'Typ av diagnos' måste ha en 'Diagnostext'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='6.2']) = 1">
        'Typ av diagnos' måste ha en 'Diagnoskod ICD-10'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6.1">
    <iso:rule context="//gn:delsvar[@id='6.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q6.2">
    <iso:rule context="//gn:delsvar[@id='6.2']"> 
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'ICD-10-SE'"/>
      <iso:assert test="matches(tp:cv/tp:code,'[A-Za-z]\d{2}.*')"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30">
    <iso:rule context="//gn:svar[@id='30']">
      <iso:assert test="count(gn:delsvar[@id='30.1']) = 1">
        'Huvudsaklig grund' måste ha ett 'Beskrivning'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q30.1">
    <iso:rule context="//gn:delsvar[@id='30.1']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q32">
    <iso:rule context="//gn:svar[@id='32']">
      <iso:assert test="count(gn:delsvar[@id='32.1']) = 1">'Sjukskrivning' måste ha en 'grad'.</iso:assert>
      <iso:assert test="count(gn:delsvar[@id='32.2']) = 1">'Sjukskrivning' måste ha en 'period'.</iso:assert>
      <iso:let name="cstart" value="normalize-space(gn:delsvar[@id='32.2']/tp:datePeriod/tp:start)"/>
      <iso:let name="cend" value="normalize-space(gn:delsvar[@id='32.2']/tp:datePeriod/tp:end)"/>
      <iso:assert test="not(preceding-sibling::gn:svar[@id='32']/gn:delsvar[@id='32.2']/tp:datePeriod/tp:start[normalize-space(.) lt $cend and normalize-space(../tp:end) gt $cstart])">
        Två 'perioder' kan inte vara överlappande.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q32.1">
    <iso:rule context="//gn:delsvar[@id='32.1']">
      <iso:extends rule="cv"/>
      <iso:assert test="tp:cv/tp:codeSystem = 'KV_FKMU_0003'"/>
      <iso:assert test="matches(normalize-space(tp:cv/tp:code), '^[1-4]$')">
        'Sjukskrivningsnivå' kan ha ett av värdena 1, 2, 3 eller 4.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q32.2">
    <iso:rule context="//gn:delsvar[@id='32.2']">
      <iso:extends rule="period"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q32.1-33">
    <iso:rule context="//gn:delsvar[@id='32.1']/tp:cv/tp:code[matches(normalize-space(.), '^[2-4]$')]">
      <iso:assert test="count(../../../../gn:svar[@id='33']) = 1">
        Om 'Sjukskrivningsnivå' är 2, 3 eller 4 så måste 'Arbetstidsförläggning' anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q33">
    <iso:rule context="//gn:svar[@id='33']">
      <iso:assert test="count(gn:delsvar[@id='33.1']) = 1">
        'Arbetstidsförläggning' måste ha ett 'Om arbetstidsförläggning'.
      </iso:assert>
      <iso:assert test="count(gn:delsvar[@id='33.2']) le 1">
        'Arbetstidsförläggning' får ha högst ett 'Motivering arbetstidsförläggning'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q33.1">
    <iso:rule context="//gn:delsvar[@id='33.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q33.2">
    <iso:rule context="//gn:delsvar[@id='33.2']">
      <iso:extends rule="non-empty-string"/>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q33.1-33.2">
    <iso:rule context="//gn:delsvar[@id='33.1' and (normalize-space(.) = '1' or normalize-space(.) = 'true')]">
      <iso:assert test="../gn:delsvar[@id='33.2']">
        Om 'Om arbetstidsförläggning' är 'true' så måste 'Motivering arbetstidsförläggning' anges.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q34">
    <iso:rule context="//gn:svar[@id='34']">
      <iso:assert test="count(gn:delsvar[@id='34.1']) = 1">
        'Arbetsresor' måste ha ett 'Om arbetsresor'.
      </iso:assert>
    </iso:rule>
  </iso:pattern>

  <iso:pattern id="q34.1">
    <iso:rule context="//gn:delsvar[@id='34.1']">
      <iso:extends rule="boolean"/>
    </iso:rule>
  </iso:pattern>

</iso:schema>
