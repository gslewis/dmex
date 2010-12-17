<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- Importing stylesheets must implement the 'BlankField' template. -->

<xsl:output method="html"/>

<xsl:template match="problem">
    <table class="problem tablesrev">
        <tbody>
            <tr>
                <xsl:apply-templates/>
            </tr>
        </tbody>
    </table>
</xsl:template>

<xsl:template match="equals">
    <td class="equals">=</td>
</xsl:template>

<xsl:template match="multiply|divide">
    <xsl:apply-templates select="term[1]"/>
    <td class="operator">
        <xsl:choose>
            <xsl:when test="name()='multiply'">
                <xsl:text disable-output-escaping="yes">&#38;times;</xsl:text>
            </xsl:when>
            <xsl:when test="name()='divide'">
                <xsl:text disable-output-escaping="yes">&#38;divide;</xsl:text>
            </xsl:when>
            <!-- The following does not work on GAE so we need to construct
            the &times; and &divide; entities manually.
            <xsl:when test="name()='multiply'">&#215;</xsl:when>
            <xsl:when test="name()='divide'">&#247;</xsl:when>
            -->
        </xsl:choose>
    </td>
    <xsl:apply-templates select="term[2]"/>
</xsl:template>

<xsl:template match="term[child::numerator or child::denominator]">
    <td>
        <table>
            <tbody>
                <tr><xsl:apply-templates select="numerator"/></tr>
                <tr><td class="rule"><hr/></td></tr>
                <tr><xsl:apply-templates select="denominator"/></tr>
            </tbody>
        </table>
    </td>
</xsl:template>

<xsl:template match="term|answer|numerator|denominator">
    <xsl:choose>
        <xsl:when test="@blank='true'">
            <xsl:call-template name="BlankField"/>
        </xsl:when>
        <xsl:otherwise>
            <xsl:call-template name="DigitField"/>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template name="DigitField">
    <td class="digit"><xsl:value-of select="text()"/></td>
</xsl:template>

</xsl:stylesheet>
