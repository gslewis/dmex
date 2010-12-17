<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--
Importing stylesheets must provide the following template:

    <xsl:template match='digit' mode='blank'>
-->

<xsl:output method="html"/>

<xsl:template match="problem">
     <!-- Find the maximum number of columns. -->
    <xsl:variable name="answer_cols">
        <xsl:value-of select="count(./answer/*)"/>
    </xsl:variable>
    <xsl:variable name="multiplicand_cols">
        <xsl:value-of select="count(./multiply/multiplicand/*)"/>
    </xsl:variable>
    <!-- multiplier has extra column for "times" operator -->
    <xsl:variable name="multiplier_cols">
        <xsl:value-of select="count(./multiply/multiplier/*) + 1"/>
    </xsl:variable>

    <!-- Number of columns is max of multiplicand, multiplier and answer. -->
    <xsl:variable name="cols">
        <xsl:choose>
            <xsl:when test="$answer_cols > $multiplicand_cols and $answer_cols > $multiplier_cols">
                <xsl:value-of select="$answer_cols"/>
            </xsl:when>
            <xsl:when test="$multiplicand_cols > $multiplier_cols">
                <xsl:value-of select="$multiplicand_cols"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$multiplier_cols"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <table class="problem longmult">
        <tbody>
            <xsl:apply-templates>
                <xsl:with-param name="cols" select="$cols"/>
            </xsl:apply-templates>
        </tbody>
    </table>
</xsl:template>

<xsl:template match="multiply">
    <xsl:param name="cols"></xsl:param>

    <xsl:apply-templates>
        <xsl:with-param name="cols" select="$cols"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="multiplicand">
    <xsl:param name="cols"></xsl:param>
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit)"/></xsl:variable>

    <tr>
        <xsl:if test="$pad>0">
            <td colspan="{$pad}"></td>
        </xsl:if>
        <xsl:apply-templates select="digit"/>
    </tr>
</xsl:template>

<xsl:template match="multiplier">
    <xsl:param name="cols"></xsl:param>
    <!-- multiplier row includes "times" operator so padding is 1 less -->
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit) - 1"/></xsl:variable>

    <tr>
        <!-- padding -->
        <xsl:if test="$pad>0">
            <td colspan="{$pad}"></td>
        </xsl:if>
        <!-- operator -->
        <td><xsl:text disable-output-escaping="yes">&#38;times;</xsl:text></td>
        <!-- Doesn't get converted to &times; in GAE
        <td>&#215;</td>
        -->
        <!-- digits -->
        <xsl:apply-templates select="digit"/>
    </tr>
    <tr>
        <td colspan="{$cols}" class="rule"><hr/></td>
    </tr>
</xsl:template>

<xsl:template match="working">
    <xsl:param name="cols"></xsl:param>

    <xsl:apply-templates select="row">
        <xsl:with-param name="cols" select="$cols"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="row">
    <xsl:param name="cols"></xsl:param>

    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit)"/></xsl:variable>
    <xsl:variable name="digits" select="count(./digit)"/>

    <tr class="working">
        <!-- padding -->
        <xsl:if test="$pad>0">
            <td colspan="{$pad}"></td>
        </xsl:if>

        <xsl:apply-templates select="digit" mode="blank">
            <!-- The number of trailing zeros of the working row (ie., the
            least digit of the multiplier has no trailing zeros, the 2nd least
            digit (tens) has one trailing zero, the third least digit
            (hundreds) has two trailing zeros, etc.). -->
            <xsl:with-param name="zeros" select="position() - 1"/>
            <!-- Supply the number of digits in the working row. -->
            <xsl:with-param name="length" select="$digits"/>

            <xsl:with-param name="suffix">working[<xsl:value-of select="position()"/>]</xsl:with-param>
        </xsl:apply-templates>
    </tr>
</xsl:template>

<xsl:template match="answer">
    <xsl:param name="cols"></xsl:param>
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit)"/></xsl:variable>

    <tr>
        <td colspan="{$cols}"><hr/></td>
    </tr>

    <tr class="answer lastFocusRow">
        <xsl:if test="$pad>0">
            <td colspan="{$pad}"></td>
        </xsl:if>

        <xsl:apply-templates select="digit" mode="blank">
            <xsl:with-param name="suffix">answer</xsl:with-param>
        </xsl:apply-templates>
    </tr>
</xsl:template>


<xsl:template match="digit">
    <td class="digit"><xsl:value-of select="text()"/></td>
</xsl:template>

</xsl:stylesheet>
