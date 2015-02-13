<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:lc="urn:riv:clinicalprocess:healthcond:certificate:SendCertificateForCitizenResponder:1">

  <xsl:include href="transform/clinicalprocess-healthcond-certificate/general-transform.xslt"/>

  <xsl:template name="response">
    <lc:SendCertificateForCitizenResponse>
      <lc:result>
        <xsl:call-template name="result"/>
      </lc:result>
    </lc:SendCertificateForCitizenResponse>
  </xsl:template>

</xsl:stylesheet>