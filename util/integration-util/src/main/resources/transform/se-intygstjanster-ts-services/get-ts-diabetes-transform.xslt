<?xml version="1.0"?>
<!--
  ~ Copyright (C) 2016 Inera AB (http://www.inera.se)
  ~
  ~ This file is part of sklintyg (https://github.com/sklintyg).
  ~
  ~ sklintyg is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ sklintyg is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -->

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gtb="urn:local:se:intygstjanster:services:GetTSDiabetesResponder:1">

  <xsl:include href="transform/se-intygstjanster-ts-services/general-transform.xslt"/>

  <xsl:template name="response">
     <gtb:GetTSBasResponse>
       <gtb:resultat>
         <xsl:call-template name="result"/>
       </gtb:resultat>
     </gtb:GetTSBasResponse>
   </xsl:template>

</xsl:stylesheet>
