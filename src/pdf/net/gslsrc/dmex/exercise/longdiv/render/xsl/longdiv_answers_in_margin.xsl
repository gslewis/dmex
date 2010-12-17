<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<xsl:import href="group_2col_templates.xsl"/>
<xsl:import href="longdiv_templates.xsl"/>

<xsl:variable name="show-answers">false</xsl:variable>

<xsl:template match="problem" mode="answer">
    <xsl:param name="number" select="position()"/>

    <xsl:variable name="rows">
        <xsl:value-of select="3 * count(./working/row) - 1 + 3"/>
    </xsl:variable>
    <xsl:variable name="cols">
	<xsl:value-of select="count(./dividend/*)"/>
    </xsl:variable>
    <xsl:variable name="divwidth">
	<xsl:value-of select="string-length(./divisor/text())"/>
    </xsl:variable>

    <fo:table line-height="{$problem-line-height} div 3"
        space-after.optimum="2pt" span="none" border-collapse="separate"
        table-layout="fixed" width="100%"
        keep-together.within-page="always">
        <fo:table-column column-width="{$problem-field-width} div 2"/>
        <fo:table-column column-width="{$problem-spacer-width} div 2"/>
	<fo:table-column
            column-width="{$divwidth} * {$problem-field-width} * 0.12"/>
        <fo:table-column column-width="{$problem-field-width} * 0.12"/>
        <fo:table-column column-width="{$problem-field-width} * 0.2"
            number-columns-repeated="{$cols}"/>

        <fo:table-body>

	    <!-- Quotient row -->
	    <fo:table-row>
		<!-- Problem number -->
		<fo:table-cell display-align="center">
		    <fo:block font-size="{$font-size-normal} div 2"
                        font-family="serif" font-style="italic"
                        text-align="end">
			<xsl:value-of select="$number"/>.
		    </fo:block>
		</fo:table-cell>
                 <!-- Spacer -->
                <fo:table-cell><fo:block></fo:block></fo:table-cell>
                <!-- Divisor column -->
                <fo:table-cell><fo:block></fo:block></fo:table-cell>
                <!-- 'Bracket' column -->
                <fo:table-cell><fo:block></fo:block></fo:table-cell>

		<xsl:apply-templates select="quotient" mode="answer">
                    <xsl:with-param name="cols" select="$cols"/>
		</xsl:apply-templates>
	    </fo:table-row>

	    <!-- Division rule row -->
	    <fo:table-row keep-with-previous="always">
		<!-- Problem number, spacer and Divisor columns -->
		<fo:table-cell number-columns-spanned="3">
                    <fo:block></fo:block>
                </fo:table-cell>
		<fo:table-cell number-columns-spanned="1 + {$cols}">
		    <fo:block line-height="{$problem-line-height} div 10"
                        margin-bottom="{$problem-line-height} div 10">
			<fo:leader leader-pattern="rule"
                            leader-length="(0.11 + {$cols} * 0.2) * {$problem-field-width}"
                            rule-thickness="0.25mm"/>
		    </fo:block>
		</fo:table-cell>
	    </fo:table-row>

	    <!-- Divisor, 'Bracket' and Dividend row -->
	    <fo:table-row keep-with-previous="always">
		<!-- Problem number and spacer columns -->
		<fo:table-cell number-columns-spanned="2">
                    <fo:block></fo:block>
                </fo:table-cell>

		<fo:table-cell display-align="center">
		    <fo:block font-size="{$font-size-normal} div 2"
                        text-align="end">
			<xsl:value-of select="./divisor/text()"/>
		    </fo:block>
		</fo:table-cell>
		<fo:table-cell>
		    <fo:block font-size="{$font-size-small}"
                        line-height="{$problem-line-height} div 3"
                        text-align="start">)</fo:block>
		</fo:table-cell>
		<xsl:apply-templates select="dividend/*" mode="answer"/>
	    </fo:table-row>

            <xsl:apply-templates select="working/row" mode="answer">
                <xsl:with-param name="cols" select="$cols"/>
	    </xsl:apply-templates>
        </fo:table-body>
    </fo:table>
</xsl:template>

<xsl:template match="row" mode="answer">
    <xsl:param name="cols"></xsl:param>

    <xsl:apply-templates select="bigend" mode="answer">
        <xsl:with-param name="cols" select="$cols"/>
    </xsl:apply-templates>
    <xsl:apply-templates select="subend" mode="answer">
        <xsl:with-param name="cols" select="$cols"/>
        <xsl:with-param name="remainder"
            select="count(./following-sibling::row)=0"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="bigend" mode="answer">
    <xsl:param name="cols"></xsl:param>

    <xsl:variable name="length">
        <xsl:value-of select="count(./digit)"/>
    </xsl:variable>
    <xsl:variable name="pad">
	<xsl:value-of select="$cols - $length - @shift"/>
    </xsl:variable>

    <fo:table-row>
	<!-- Problem number, spacer, Divisor and 'Bracket' columns -->
	<fo:table-cell number-columns-spanned="4">
            <fo:block></fo:block>
        </fo:table-cell>
	<xsl:if test="$pad>0">
	    <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>

	<xsl:apply-templates select="./digit" mode="answer"/>
        <!-- XXX do I need to add @shift empty cells here? -->
    </fo:table-row>

    <fo:table-row>
	<!-- Problem number, spacer, Divisor and 'Bracket' columns -->
	<fo:table-cell number-columns-spanned="4">
            <fo:block></fo:block>
        </fo:table-cell>

	<xsl:if test="$pad>0">
	    <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>

	<fo:table-cell number-columns-spanned="{$length}">
	    <fo:block line-height="1pt" margin-bottom="5pt" margin-top="-2pt">
		<fo:leader leader-pattern="rule" rule-thickness="0.25mm"
                    leader-length="{$length} * {$problem-field-width} * 0.2"/>
	    </fo:block>
	</fo:table-cell>
    </fo:table-row>

    <!--
    <xsl:apply-templates select="following-sibling::SubEnd[1]" mode="answer">
	<xsl:with-param name="cols">
	    <xsl:value-of select="$cols"/>
	</xsl:with-param>
    </xsl:apply-templates>
    -->

</xsl:template>


<xsl:template match="subend" mode="answer">
    <xsl:param name="cols"></xsl:param>
    <xsl:param name="remainder">false</xsl:param>

    <xsl:variable name="length">
	<xsl:value-of select="count(./digit)"/>
    </xsl:variable>
    <xsl:variable name="pad">
	<xsl:value-of select="$cols - $length - @shift"/>
    </xsl:variable>

    <fo:table-row keep-with-previous="always">
	<!-- Problem number, spacer, Divisor and 'Bracket' columns -->
	<fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>

	<xsl:choose>
	    <xsl:when test="$remainder='true'">
		<fo:table-cell number-columns-spanned="2 + {$pad}">
		    <fo:block font-size="{$font-size-small} div 2"
                        font-style="italic" text-align="end"
                        margin-right="2pt">Rem</fo:block>
		</fo:table-cell>
	    </xsl:when>
	    <xsl:otherwise>
		<fo:table-cell number-columns-spanned="2">
                    <fo:block></fo:block>
                </fo:table-cell>
		<xsl:if test="$pad>0">
		    <fo:table-cell number-columns-spanned="{$pad}">
                        <fo:block></fo:block>
                    </fo:table-cell>
		</xsl:if>
	    </xsl:otherwise>
	</xsl:choose>

	<xsl:apply-templates select="./digit" mode="answer"/>
        <!-- XXX @shift goes here? -->
    </fo:table-row>

    <!--
    <xsl:if test="count(./following-sibling::BigEnd)>0">
	<fo:table-row keep-with-previous="always">
	    <fo:table-cell number-columns-spanned="4 + {$cols}">
		<fo:block line-height="3pt" space-after="3pt"/>
	    </fo:table-cell>
	</fo:table-row>
    </xsl:if>
    -->
</xsl:template>

<xsl:template match="quotient" mode="answer">
    <xsl:param name="cols"></xsl:param>

    <xsl:variable name="pad">
        <xsl:value-of select="$cols - count(./digit)"/>
    </xsl:variable>

    <xsl:if test="$pad>0">
	<fo:table-cell number-columns-spanned="{$pad}">
            <fo:block></fo:block>
        </fo:table-cell>
    </xsl:if>

    <xsl:apply-templates select="digit" mode="answer"/>
</xsl:template>

<xsl:template match="digit" mode="answer">
    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-normal} div 2" text-align="center">
            <xsl:value-of select="text()"/>
        </fo:block>
    </fo:table-cell>
</xsl:template>

</xsl:stylesheet>
