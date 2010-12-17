<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:import href="longdiv_html_templates.xsl"/>

<xsl:template match="digit" mode="blank">
    <xsl:param name="suffix"></xsl:param>
    <xsl:param name="class"></xsl:param>

    <xsl:variable name="errorClass">
        <xsl:if test="@correct='false'">error</xsl:if>
    </xsl:variable>

    <xsl:variable name="cc">
        <xsl:value-of select="normalize-space(concat('blank ', $class, ' ', $errorClass))"/>
    </xsl:variable>

    <td>
        <xsl:attribute name="class">
            <xsl:value-of select="$cc"/>
        </xsl:attribute>

        <input class="problemFocus" type="text" size="1" maxlength="1">
            <xsl:attribute name="id">focus_<xsl:value-of select="@step"/></xsl:attribute>
            <xsl:attribute name="name">longdiv_<xsl:value-of select="$suffix"/>[<xsl:value-of select="position()"/>]</xsl:attribute>
            <xsl:attribute name="value">
                <xsl:choose>
                    <xsl:when test="@correct='true'"><xsl:value-of select="text()"/></xsl:when>
                    <xsl:otherwise></xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
        </input>
    </td>
</xsl:template>

<xsl:template match="hints">
    <div class="hintsBlock">
        <input type="button" onclick="toggleHints(this);" value="Show hints"/>
        <div class="hintsContainer">
            <ul class="hints">
                <xsl:apply-templates select="hint"/>
            </ul>
        </div>
    </div>
</xsl:template>

<xsl:template match="hint">
    <li class="hint"><xsl:value-of select="text()"/></li>
</xsl:template>

</xsl:stylesheet>
