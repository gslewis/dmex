<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="longmult_html_templates.xsl"/>

<xsl:template match="digit" mode="blank">
    <!-- The number of trailing zeros to reveal. -->
    <xsl:param name="zeros">0</xsl:param>
    <!-- The length of the value to which this digit belongs. -->
    <xsl:param name="length">999</xsl:param>

    <td>
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="$length - position() &lt; $zeros">blank reveal zero</xsl:when>
                <xsl:otherwise>blank reveal</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <xsl:value-of select="text()"/>
    </td>
</xsl:template>

</xsl:stylesheet>
