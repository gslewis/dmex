<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<xsl:import href="group_2col_templates.xsl"/>
<xsl:import href="longmult_templates.xsl"/>

<xsl:variable name="show-answers">false</xsl:variable>

<xsl:template match="problem" mode="answer">
    <xsl:param name="number" select="position()"/>

    <fo:table line-height="{$problem-line-height} div 3"
        space-after.optimum="2pt" span="none"
        table-layout="fixed" width="100%">
	<fo:table-column column-width="{$problem-field-width} * 0.5"/>
        <fo:table-column column-width="{$problem-field-width} * 0.1"/>
        <fo:table-column column-width="{$problem-field-width} * 0.3"
            number-columns-repeated="7"/>

        <fo:table-body>
	    <xsl:apply-templates mode="answer">
                <xsl:with-param name="number" select="$number"/>
	    </xsl:apply-templates>
        </fo:table-body>
    </fo:table>
</xsl:template>

<xsl:template match="multiply" mode="answer">
    <xsl:param name="number">??</xsl:param>

    <xsl:apply-templates mode="answer">
        <xsl:with-param name="number" select="$number"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="multiplicand" mode="answer">
    <xsl:param name="number">??</xsl:param>

    <xsl:variable name="pad"><xsl:value-of select="7 - count(./digit)"/></xsl:variable>

    <fo:table-row>
	<fo:table-cell display-align="center">
	    <fo:block font-size="{$font-size-normal} div 2" font-family="serif"
                font-style="italic" text-align="end">
		<xsl:value-of select="$number"/>.
	    </fo:block>
	</fo:table-cell>
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
	<xsl:if test="$pad>0">
	    <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>

	<xsl:for-each select="digit">
	    <fo:table-cell>
		<fo:block font-size="{$font-size-normal} div 2"
                    text-align="center">
		    <xsl:value-of select="text()"/>
		</fo:block>
	    </fo:table-cell>
	</xsl:for-each>
    </fo:table-row>
</xsl:template>

<xsl:template match="multiplier" mode="answer">
    <xsl:variable name="pad"><xsl:value-of select="7 - count(./digit) - 1"/></xsl:variable>

    <fo:table-row keep-with-previous="always">
        <!-- number & spacer -->
	<fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
        <!-- "times" operator -->
        <fo:table-cell>
            <fo:block font-size="{$font-size-normal} div 2"
                text-align="center">x</fo:block>
        </fo:table-cell>
        <!-- padding -->
	<xsl:if test="$pad>0">
	    <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>
        <!-- digits -->
	<xsl:for-each select="digit">
	    <fo:table-cell>
		<fo:block font-size="{$font-size-normal} div 2"
                    text-align="center">
                    <xsl:value-of select="text()"/>
		</fo:block>
	    </fo:table-cell>
	</xsl:for-each>
    </fo:table-row>

    <fo:table-row keep-with-previous="always">
	<fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
	<fo:table-cell number-columns-spanned="7">
	    <fo:block line-height="{$problem-line-height} div 6"
                space-after="{$problem-line-height} div 6">
		<fo:leader leader-pattern="rule" rule-thickness="0.1mm"
                    leader-length="{$problem-field-width} * 2.1"/>
	    </fo:block>
	</fo:table-cell>
    </fo:table-row>
</xsl:template>

<xsl:template match="row" mode="answer">
    <xsl:variable name="pad"><xsl:value-of select="7 - count(./digit)"/></xsl:variable>

    <fo:table-row keep-with-previous="always">
	<fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
	<xsl:if test="$pad>0">
	    <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>
	<xsl:for-each select="digit">
	    <fo:table-cell>
		<fo:block font-size="{$font-size-normal} div 2"
                    text-align="center" margin-top="2pt">
		    <xsl:value-of select="text()"/>
		</fo:block>
	    </fo:table-cell>
	</xsl:for-each>
    </fo:table-row>
</xsl:template>

<xsl:template match="answer" mode="answer">
    <xsl:variable name="pad"><xsl:value-of select="7 - count(./digit)"/></xsl:variable>

    <fo:table-row keep-with-previous="always">
	<fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
	<fo:table-cell number-columns-spanned="7">
	    <fo:block line-height="{$problem-line-height} div 6"
                space-after="{$problem-line-height} div 6">
		<fo:leader leader-pattern="rule" rule-thickness="0.1mm"
                    leader-length="{$problem-field-width} * 2.1"/>
	    </fo:block>
	</fo:table-cell>
    </fo:table-row>

    <fo:table-row keep-with-previous="always">
	<fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
	<xsl:if test="$pad>0">
	    <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>

	<xsl:for-each select="digit">
	    <fo:table-cell>
		<fo:block font-size="{$font-size-normal} div 2"
                    text-align="center" margin-top="2pt">
		    <xsl:value-of select="text()"/>
		</fo:block>
	    </fo:table-cell>
	</xsl:for-each>
    </fo:table-row>
</xsl:template>

</xsl:stylesheet>
