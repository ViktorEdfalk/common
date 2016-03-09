<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rc="urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificate:2:rivtabp21">

  <xsl:include href="transform/general-certificate-transform.xslt"/>

  <xsl:template name="response">
     <rc:RegisterCertificateResponse>
       <rc:resultat>
         <xsl:call-template name="result"/>
       </rc:resultat>
     </rc:RegisterCertificateResponse>
   </xsl:template>

</xsl:stylesheet>
