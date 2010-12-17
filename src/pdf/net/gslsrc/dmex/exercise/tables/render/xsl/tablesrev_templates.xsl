<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<xsl:template match="problem">
    <xsl:param name="number" select="position()"/>

    <fo:table span="none" table-layout="fixed" width="100%"
        line-height="{$problem-line-height} * 1.85"
        space-after="{$problem-space-after} * 0.8">
        <fo:table-column column-width="{$problem-number-width}"/>
        <fo:table-column column-width="{$problem-spacer-width}"/>
        <fo:table-column column-width="{$problem-field-width}"
            number-columns-repeated="5"/>

        <fo:table-body>
            <fo:table-row>
                <fo:table-cell display-align="center">
                    <fo:block font-size="{$font-size-small}"
                        font-family="serif"
                        font-style="italic"
                        line-height="{$problem-line-height}"
                        text-align="end">
                        <xsl:value-of select="$number"/>.
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell><fo:block></fo:block></fo:table-cell>
                <xsl:apply-templates/>
            </fo:table-row>
        </fo:table-body>
    </fo:table>
</xsl:template>

<xsl:template match="equals">
    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-large}"
                  line-height="{$problem-line-height}"
                  text-align="center">=</fo:block>
    </fo:table-cell>
</xsl:template>

<xsl:template match="multiply|divide">
    <xsl:apply-templates select="term[1]"/>

    <xsl:call-template name="OPERATOR">
        <xsl:with-param name="type">
            <xsl:choose>
                <xsl:when test="name()='multiply'">*</xsl:when>
                <xsl:when test="name()='divide'">/</xsl:when>
            </xsl:choose>
        </xsl:with-param>
    </xsl:call-template>

    <xsl:apply-templates select="term[2]"/>
</xsl:template>

<xsl:template match="term[child::numerator or child::denominator]">
    <fo:table-cell>
        <fo:table span="none" table-layout="fixed" width="100%">
            <fo:table-column column-width="10mm"/>

            <fo:table-body>
                <fo:table-row>
                    <xsl:apply-templates select="numerator"/>
                </fo:table-row>
                <fo:table-row keep-with-previous="always">
                    <fo:table-cell>
                        <fo:block text-align="start"
                            line-height="{$problem-line-height} div 4"
                            padding-bottom="{$problem-line-height} div 4">
                            <fo:leader leader-pattern="rule"
                                leader-length="{$problem-field-width}"
                                rule-thickness="0.5mm"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row keep-with-previous="always">
                    <xsl:apply-templates select="denominator"/>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </fo:table-cell>
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
    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-large}"
                  line-height="{$problem-line-height}"
                  text-align="center">
            <xsl:value-of select="text()"/>
        </fo:block>
    </fo:table-cell>
</xsl:template>

<xsl:template name="BlankField">
    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-large}"
                  line-height="{$problem-line-height}" text-align="center"
                  border-width="0.5mm" border-style="solid" color="gray">
            <xsl:choose>
                <xsl:when test="$show-answers='true' or ancestor::WorkSheet[@type]='answer'">
                    <xsl:value-of select="text()"/>
                </xsl:when>
                <xsl:otherwise>
                    <!-- Invisible '?' -->
                    <fo:character character="?" color="white"/>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
    </fo:table-cell>
</xsl:template>


<xsl:template name="OPERATOR">
    <xsl:param name="type">?</xsl:param>

    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-large}"
                  line-height="{$problem-line-height}"
                  text-align="center">
            <xsl:choose>
                <xsl:when test="$type='*'">x</xsl:when>
                <xsl:when test="$type='/'">&#247;</xsl:when>
                <xsl:otherwise><xsl:value-of select="$type"/></xsl:otherwise>
            </xsl:choose>
        </fo:block>
    </fo:table-cell>
</xsl:template>

</xsl:stylesheet>
