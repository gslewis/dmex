<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="tablesrev_html_templates.xsl"/>

<xsl:template name="BlankField">
    <td class="blank reveal"><xsl:value-of select="text()"/></td>
</xsl:template>

</xsl:stylesheet>
