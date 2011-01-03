<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--
Templates to be imported into a parent template.
Parent must supply the following templates:

    <xsl:template match='digit' mode='blank'>
    <xsl:template match='hints'>
-->

<xsl:output method="html"/>

<xsl:template match="problem">
    <xsl:variable name="rows">
        <xsl:value-of select="3 * count(./working/row) - 1 + 3"/>
    </xsl:variable>
    <xsl:variable name="cols">
	<xsl:value-of select="count(./dividend/*)"/>
    </xsl:variable>

    <table class="problem longdiv">
        <tbody>
            <tr class="quotient">
                <xsl:apply-templates select="quotient">
                    <xsl:with-param name="cols" select="$cols"/>
                </xsl:apply-templates>
            </tr>
            <tr class="rule">
                <td colspan="1"></td>
                <td colspan="{$cols + 1}">
                    <hr/>
                </td>
            </tr>
            <tr class="divisor">
                <xsl:apply-templates select="divisor"/>
                <xsl:apply-templates select="dividend"/>
            </tr>
            <xsl:apply-templates select="working">
                <xsl:with-param name="cols" select="$cols"/>
            </xsl:apply-templates>
        </tbody>
    </table>

    <xsl:apply-templates select="hints"/>
</xsl:template>

<xsl:template match="quotient">
    <xsl:param name="cols"></xsl:param>
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit)"/></xsl:variable>

    <td colspan="{$pad + 2}"></td>

    <xsl:apply-templates select="digit" mode="blank">
        <xsl:with-param name="suffix">answer</xsl:with-param>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="divisor">
    <td class="divisor"><xsl:value-of select="text()"/></td>
    <td class="bracket">)</td>
</xsl:template>

<xsl:template match="dividend">
    <xsl:apply-templates select="digit">
        <xsl:with-param name="class">dividend</xsl:with-param>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="working">
    <xsl:param name="cols"></xsl:param>

    <xsl:apply-templates select="row">
        <xsl:with-param name="cols" select="$cols"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="row">
    <xsl:param name="cols"></xsl:param>

    <xsl:variable name="suffix">working[<xsl:value-of select="position()"/>]</xsl:variable>

    <xsl:apply-templates select="bigend">
        <xsl:with-param name="cols" select="$cols"/>
        <xsl:with-param name="suffix" select="$suffix"/>
    </xsl:apply-templates>
    <xsl:apply-templates select="subend">
        <xsl:with-param name="cols" select="$cols"/>
        <xsl:with-param name="remainder"
            select="count(./following-sibling::row)=0"/>
        <xsl:with-param name="suffix" select="$suffix"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="bigend">
    <xsl:param name="cols"></xsl:param>
    <xsl:param name="suffix"></xsl:param>

    <xsl:variable name="length">
        <xsl:value-of select="count(./digit)"/>
    </xsl:variable>
    <xsl:variable name="pad">
	<xsl:value-of select="$cols - $length - @shift"/>
    </xsl:variable>

    <tr class="bigend">
        <td colspan="{$pad + 2}"></td>

	<xsl:apply-templates select="./digit" mode="blank">
            <xsl:with-param name="suffix"><xsl:value-of select="$suffix"/>[1]</xsl:with-param>
            <xsl:with-param name="class">bigend</xsl:with-param>
        </xsl:apply-templates>

	<xsl:if test="@shift>0">
            <td colspan="{@shift}"></td>
	</xsl:if>
    </tr>
    <tr class="rule">
        <td colspan="{$pad + 2}"></td>
        <td colspan="{$length}">
            <hr/>
        </td>
	<xsl:if test="@shift>0">
            <td colspan="{@shift}"></td>
	</xsl:if>
    </tr>
</xsl:template>

<xsl:template match="subend">
    <xsl:param name="cols"></xsl:param>
    <xsl:param name="remainder">false</xsl:param>
    <xsl:param name="suffix"></xsl:param>

    <xsl:variable name="length">
	<xsl:value-of select="count(./digit)"/>
    </xsl:variable>
    <xsl:variable name="pad">
	<xsl:value-of select="$cols - $length - @shift"/>
    </xsl:variable>

    <tr class="subend">
        <td colspan="{$pad + 2}">
            <xsl:if test="$remainder='true'">
                <xsl:attribute name="class">remainder</xsl:attribute>
                <span class="remainder">Remainder</span>
            </xsl:if>
        </td>

	<xsl:apply-templates select="./digit" mode="blank">
            <xsl:with-param name="suffix"><xsl:value-of select="$suffix"/>[2]</xsl:with-param>
            <xsl:with-param name="class">subend</xsl:with-param>
        </xsl:apply-templates>

	<xsl:if test="@shift>0">
            <td colspan="{@shift}"></td>
	</xsl:if>
    </tr>
</xsl:template>

<xsl:template match="digit">
    <xsl:param name="class"></xsl:param>

    <td class="digit">
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="$class!=''">digit <xsl:value-of select="$class"/></xsl:when>
                <xsl:otherwise>digit</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <xsl:value-of select="text()"/>
    </td>
</xsl:template>

</xsl:stylesheet>
