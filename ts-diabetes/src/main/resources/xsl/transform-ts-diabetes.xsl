<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
    xmlns:ns1="urn:local:se:intygstjanster:services:1"
    xmlns:ns2="urn:local:se:intygstjanster:services:RegisterTSBasResponder:1"
    xmlns:ns3="urn:local:se:intygstjanster:services:types:1"
    xmlns:p="urn:riv:clinicalprocess:healthcond:certificate:1"
    xmlns:p2="urn:riv:clinicalprocess:healthcond:certificate:ts-diabetes:1"
    xmlns:reg="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1">

  <xsl:output method="xml" indent="yes"/>

  <xsl:include href="xsl/transform-ts-common.xsl"/>

  <xsl:template match="ns1:diabetesIntyg | ns2:intyg">
  <reg:RegisterCertificate>
    <p:utlatande>

      <xsl:call-template name="utlatandeHeader"/>

      <xsl:apply-templates select="ns1:grundData"/>

      <xsl:call-template name="vardKontakt"/>

    <!-- AKTIVITETER begin -->
      <!-- Egenkontroll av blodsocker -->
      <xsl:if test="ns1:hypoglykemier/ns1:genomforEgenkontrollBlodsocker = 'true' or ns1:hypoglykemier/ns1:genomforEgenkontrollBlodsocker = '1'">
        <p:aktivitet>
          <p:aktivitetskod code="308113006" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
          <p:forekomst>true</p:forekomst>
        </p:aktivitet>
      </xsl:if>

      <xsl:if test="ns1:synfunktion/ns1:finnsSeparatOgonlakarintyg = 'false'">
          <!-- Synfältsprövning (Donders konfrontationsmetod) -->
          <p:aktivitet>
            <p:aktivitets-id root="1.2.752.129.2.1.2.1">
              <xsl:attribute name="extension" select="$synfaltsprovning-aktivitets-id"/>
            </p:aktivitets-id>
            <p:aktivitetskod code="86944008" codeSystem="1.2.752.116.2.1.1.1" codeSystemName="SNOMED-CT"/>
            <p2:metod code="MET1" codeSystem="b0c078c6-512a-42a5-ab42-a3380f369ac3" codeSystemName="kv_metod"/>
          </p:aktivitet>

          <!-- Prövning av ögats rörlighet -->
          <xsl:call-template name="ogatsRorlighetAktivitet"/>
      </xsl:if>

      <xsl:call-template name="synfaltsObservation"/>

      <xsl:call-template name="ogatsRorlighetObservation" />
    <!-- AKTIVITETER end -->

    <!-- REKOMMENDATIONER begin -->
      <!-- Patienten uppfyller kraven.. -->
      <p:rekommendation>
        <p:rekommendationskod code="REK8"
          codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg" />
        <xsl:for-each select="ns1:bedomning/ns1:korkortstyp">
          <p2:varde codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg">
            <xsl:attribute name="code" select="$korkortsTyp/mapping[@key = current()]/@value"/>
          </p2:varde>
        </xsl:for-each>
      </p:rekommendation>

      <!-- Specialistkompetens inom.. -->
      <xsl:if test="ns1:bedomning/ns1:behovAvLakareSpecialistKompetens">
        <p:rekommendation>
          <p:rekommendationskod code="REK9"
            codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg" />
          <p:beskrivning>
            <xsl:value-of select="ns1:bedomning/ns1:behovAvLakareSpecialistKompetens"/>
          </p:beskrivning>
        </p:rekommendation>
      </xsl:if>

      <!-- Lämplighet att inneha... -->
      <xsl:if test="ns1:bedomning/ns1:lamplighetInnehaBehorighetSpecial">
        <p:rekommendation>
          <p:rekommendationskod code="REK10"
            codeSystem="5a931b99-bda8-4f1e-8a6d-6f8a3f40a459" codeSystemName="kv_rekommendation_intyg" />
          <p2:varde>
            <xsl:value-of select="ns1:bedomning/ns1:lamplighetInnehaBehorighetSpecial"/>
          </p2:varde>
        </p:rekommendation>      
      </xsl:if>
    <!-- REKOMMENDATIONER end -->

    <!-- OBSERVATIONER begin -->

      <!-- Diabetes typ1 -->
      <p:observation>
        <p:observationskod code="E10" codeSystem="1.2.752.116.1.1.1.1.3"
          codeSystemName="ICD-10" />
        <p:observationsperiod>
          <p:from>2012</p:from>
        </p:observationsperiod>
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <!-- Diabetes typ2 -->
      <p:observation>
        <p:observationskod code="E11" codeSystem="1.2.752.116.1.1.1.1.3"
          codeSystemName="ICD-10" />
        <p:observationsperiod>
          <p:from>2012</p:from>
        </p:observationsperiod>
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <!-- Behandlingar -->
      <p:observation>
        <p:observationskod code="170746002" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="170745003" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="170747006" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:observationsperiod>
          <p:from>2012</p:from>
        </p:observationsperiod>
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="OBS10" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
          codeSystemName="kv_observationer_intyg" />
        <p:beskrivning>Hypnos</p:beskrivning>
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <!-- Hypoglykemi -->
      <p:observation>
        <p:observationskod code="OBS19" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
          codeSystemName="kv_observationer_intyg" />
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="OBS20" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
          codeSystemName="kv_observationer_intyg" />
        <p:forekomst>true</p:forekomst>
      </p:observation>
      <p:observation>
        <p:observationskod code="OBS21" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
          codeSystemName="kv_observationer_intyg" />
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="OBS22" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
          codeSystemName="kv_observationer_intyg" />
        <p:beskrivning>Beskrivning</p:beskrivning>
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="OBS23" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
          codeSystemName="kv_observationer_intyg" />
        <p:beskrivning>Beskrivning</p:beskrivning>
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <p:observation>
        <p:observationskod code="OBS24" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
          codeSystemName="kv_observationer_intyg" />
        <p:observationstid>2012-12-12</p:observationstid>
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <!-- Syn -->
      <p:observation>
        <p:observations-id root="1.2.752.129.2.1.2.1"
          extension="3" />
        <p:observationskod code="OBS25" codeSystem="335d4bed-7e1d-4f81-ae7d-b39b266ef1a3"
          codeSystemName="kv_observationer_intyg" />
        <p:forekomst>true</p:forekomst>
      </p:observation>

      <!-- Utan korrektion -->
      <!-- Höger öga -->
      <p:observation>
        <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:varde value="0.0" />
        <p2:lateralitet code="24028007" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
      </p:observation>

      <!-- Vänster öga -->
      <p:observation>
        <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:varde value="0.0" />
        <p2:lateralitet code="7771000" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
      </p:observation>

      <!-- Binokulärt -->
      <p:observation>
        <p:observationskod code="420050001" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:varde value="0.0" />
        <p2:lateralitet code="51440002" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
      </p:observation>

      <!-- Med korrektion -->
      <!-- Höger öga -->
      <p:observation>
        <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:varde value="0.0" />
        <p2:lateralitet code="24028007" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
      </p:observation>

      <!-- Vänster öga -->
      <p:observation>
        <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:varde value="0.0" />
        <p2:lateralitet code="7771000" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
      </p:observation>

      <!-- Binokulärt -->
      <p:observation>
        <p:observationskod code="397535007" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
        <p:varde value="0.0" />
        <p2:lateralitet code="51440002" codeSystem="1.2.752.116.2.1.1.1"
          codeSystemName="SNOMED-CT" />
      </p:observation>

      <!-- Dubbelseende -->
      <p:observation>
        <p:observations-id root="1.2.752.129.2.1.2.1"
          extension="4" />
        <p:observationskod code="H53.2" codeSystem="1.2.752.116.1.1.1.1.3"
          codeSystemName="ICD-10" />
        <p:forekomst>true</p:forekomst>
      </p:observation>
    <!-- OBSERVATIONER end -->

    <!-- OBSERVATIONAKTIVITETRELATION begin-->
      <p2:observationAktivitetRelation>
        <p2:observationsid root="1.2.752.129.2.1.2.1">
          <xsl:attribute name="extension" select="$synfaltsprovning-observations-id"/>
        </p2:observationsid>
        <p2:aktivitetsid root="1.2.752.129.2.1.2.1">
          <xsl:attribute name="extension" select="$synfaltsprovning-aktivitets-id"/>
        </p2:aktivitetsid>
      </p2:observationAktivitetRelation>

      <p2:observationAktivitetRelation>
        <p2:observationsid root="1.2.752.129.2.1.2.1">
          <xsl:attribute name="extension" select="$ogats-rorlighet-observations-id"/>
        </p2:observationsid>
        <p2:aktivitetsid root="1.2.752.129.2.1.2.1">
          <xsl:attribute name="extension" select="$ogats-rorlighet-aktivitets-id"/>
        </p2:aktivitetsid>
      </p2:observationAktivitetRelation>
    <!-- OBSERVATIONAKTIVITETRELATION end -->

      <xsl:for-each select="ns1:intygAvser/ns1:korkortstyp">
        <p2:intygAvser codeSystem="24c41b8d-258a-46bf-a08a-b90738b28770" codeSystemName="kv_intyget_avser">
          <xsl:attribute name="code" select="$intygAvser/mapping[@key = current()]/@value"/>
        </p2:intygAvser>
      </xsl:for-each>
      
        <p2:bilaga>
            <p2:bilagetyp code="BIL1" codeSystem="80595600-7477-4a6c-baeb-d2439e86b8bc" codeSystemName="kv_bilaga" />
            <p2:forekomst>
              <xsl:value-of select="ns1:synfunktion/ns1:finnsSeparatOgonlakarintyg"/>
            </p:forekomst>
        </p2:bilaga>
      

      <p2:utgava>
        <xsl:value-of select="ns1:utgava"/>
      </p2:utgava>
      <p2:version>
        <xsl:value-of select="ns1:version"/>
      </p2:version>

    </p:utlatande>
  </reg:RegisterCertificate>
  </xsl:template>

  <xsl:template match="ns1:grundData">
    <p:signeringsdatum>
      <xsl:value-of select="ns1:signeringsTidstampel"/>
    </p:signeringsdatum>
    <p:patient>
      <p:person-id>
        <xsl:attribute name="root">1.2.752.129.2.1.3.1</xsl:attribute>
        <xsl:attribute name="extension">
          <xsl:value-of select="ns1:patient/ns1:personId/ns3:extension"/>
        </xsl:attribute>
      </p:person-id>
      <p:fornamn>
        <xsl:value-of select="ns1:patient/ns1:fornamn"/>
      </p:fornamn>
      <p:efternamn>
        <xsl:value-of select="ns1:patient/ns1:efternamn"/>
      </p:efternamn>
      <p:postadress>
        <xsl:value-of select="ns1:patient/ns1:postadress"/>
      </p:postadress>
      <p:postnummer>
        <xsl:value-of select="ns1:patient/ns1:postnummer"/>
      </p:postnummer>
      <p:postort>
        <xsl:value-of select="ns1:patient/ns1:postort"/>
      </p:postort>
    </p:patient>
    <p:skapadAv>
      <p:personal-id>
        <xsl:attribute name="root">1.2.752.129.2.1.4.1</xsl:attribute>
        <xsl:attribute name="extension">
          <xsl:value-of select="ns1:skapadAv/ns1:personId/ns3:extension"/>
        </xsl:attribute>
      </p:personal-id>
      <p:fullstandigtNamn>
        <xsl:value-of select="ns1:skapadAv/ns1:fullstandigtNamn"/>
      </p:fullstandigtNamn>
      <xsl:for-each select="ns1:skapadAv/ns1:befattningar">
        <p:befattning>
          <xsl:value-of select="."/>
        </p:befattning>
      </xsl:for-each>
      <p:enhet>
        <p:enhets-id>
          <xsl:attribute name="root">1.2.752.129.2.1.4.1</xsl:attribute>
          <xsl:attribute name="extension">
            <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:enhetsId/ns3:extension"/>
          </xsl:attribute>
        </p:enhets-id>
        <p:enhetsnamn>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:enhetsnamn"/>
        </p:enhetsnamn>
        <p:postadress>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:postadress"/>
        </p:postadress>
        <p:postnummer>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:postnummer"/>
        </p:postnummer>
        <p:postort>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:postort"/>
        </p:postort>
        <p:telefonnummer>
          <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:telefonnummer"/>
        </p:telefonnummer>
        <p:vardgivare>
          <p:vardgivare-id>
            <xsl:attribute name="root">1.2.752.129.2.1.4.1</xsl:attribute>
            <xsl:attribute name="extension">
              <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:vardgivare/ns1:vardgivarid/ns3:extension"/>
            </xsl:attribute>
          </p:vardgivare-id>
          <p:vardgivarnamn>
            <xsl:value-of select="ns1:skapadAv/ns1:vardenhet/ns1:vardgivare/ns1:vardgivarnamn"/>
          </p:vardgivarnamn>
        </p:vardgivare>
      </p:enhet>
      <xsl:for-each select="ns1:skapadAv/ns1:specialiteter">
        <p2:specialitet code="SPEC" codeSystem="coming_soon" codeSystemName="kv_intyg_specialitet"/>
      </xsl:for-each>
    </p:skapadAv>
  </xsl:template>

  <!-- Dont output text nodes we dont transform  -->
  <xsl:template match="ns1:intygsTyp"/>

</xsl:stylesheet>
