<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:scs="urn:riv:insuranceprocess:healthreporting:SetCertificateStatusResponder:1">

  <xsl:include href="transform/general-transform.xslt"/>

  <xsl:template name="response">
     <scs:SetCertificateStatusResponse>
       <scs:result>
         <xsl:call-template name="result"/>
       </scs:result>
     </scs:SetCertificateStatusResponse>
   </xsl:template>

</xsl:stylesheet>