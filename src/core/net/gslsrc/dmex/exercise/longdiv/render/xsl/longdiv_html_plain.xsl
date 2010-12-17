<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="longdiv_html_templates.xsl"/>

<xsl:template match="digit" mode="blank">
    <xsl:param name="suffix"></xsl:param>
    <xsl:param name="class"></xsl:param>

    <xsl:variable name="cc">
        <xsl:value-of
            select="normalize-space(concat('blank reveal ', $class))"/>
    </xsl:variable>

    <td>
        <xsl:attribute name="class">
            <xsl:value-of select="$cc"/>
        </xsl:attribute>

        <xsl:value-of select="text()"/>
    </td>
</xsl:template>

<xsl:template match="hints">
</xsl:template>

</xsl:stylesheet>
