<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
		xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<xsl:template match="problem">
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
    <xsl:variable name="has_hints">
        <xsl:value-of select="count(./hints)>0"/>
    </xsl:variable>
    <xsl:variable name="hints_side">
	<xsl:value-of select="$has_hints='true' and count(./dividend/*)&lt;4"/>
    </xsl:variable>
    <xsl:variable name="hints_below">
	<xsl:value-of select="$has_hints='true' and $hints_side='false'"/>
    </xsl:variable>

    <fo:table line-height="{$problem-line-height} * 1.85"
        space-after.optimum="{$problem-space-after} * 1.8" span="none"
        border-collapse="separate" table-layout="fixed" width="100%"
        keep-together.within-column="always">
        <fo:table-column column-width="{$problem-number-width}"/>
        <fo:table-column column-width="{$problem-spacer-width}"/>
	<fo:table-column
            column-width="{$divwidth} * {$problem-field-width} * 0.35"/>
        <fo:table-column column-width="{$problem-field-width} div 2"/>
        <fo:table-column column-width="{$problem-field-width}"
            number-columns-repeated="{$cols}"/>
	<xsl:if test="$hints_side='true'">
	    <fo:table-column column-width="{$problem-spacer-width}"/>
	    <fo:table-column column-width="{$problem-field-width} * 1.5"/>
	</xsl:if>

        <fo:table-body>
	    <!-- Quotient row -->
	    <fo:table-row>
		<!-- Problem number -->
		<fo:table-cell display-align="center">
		    <fo:block font-size="{$font-size-small}"
                        font-family="serif" font-style="italic"
                        line-height="{$problem-line-height}" text-align="end">
			<xsl:value-of select="$number"/>.
		    </fo:block>
		</fo:table-cell>
                <!-- Spacer -->
                <fo:table-cell><fo:block></fo:block></fo:table-cell>
		<!-- Divisor column -->
                <fo:table-cell><fo:block></fo:block></fo:table-cell>
		<!-- 'Bracket' column -->
                <fo:table-cell><fo:block></fo:block></fo:table-cell>

		<xsl:apply-templates select="quotient">
                    <xsl:with-param name="cols" select="$cols"/>
		</xsl:apply-templates>

		<xsl:if test="$hints_side='true'">
		    <xsl:apply-templates select="hints" mode="side">
                        <xsl:with-param name="rows" select="$rows"/>
		    </xsl:apply-templates>
		</xsl:if>
	    </fo:table-row>

	    <!-- Division rule row -->
	    <fo:table-row keep-with-previous="always">
		<!-- Problem number, spacer and Divisor columns -->
		<fo:table-cell number-columns-spanned="3">
                    <fo:block></fo:block>
                </fo:table-cell>
                <!-- Division rule -->
		<fo:table-cell number-columns-spanned="1 + {$cols}">
		    <fo:block line-height="{$problem-line-height} div 4"
                        space-after="{$problem-space-after} div 4">
			<fo:leader leader-pattern="rule"
                            leader-length="(0.5 + {$cols}) * {$problem-field-width}"
                            rule-thickness="0.5mm"/>
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
		    <fo:block font-size="{$font-size-large}"
                        line-height="{$problem-line-height}"
                        text-align="end">
			<xsl:value-of select="./divisor/text()"/>
		    </fo:block>
		</fo:table-cell>
		<fo:table-cell>
		    <fo:block font-size="{$font-size-small} * 3"
                        line-height="{$problem-line-height}"
                        text-align="start">)</fo:block>
		</fo:table-cell>
		<xsl:apply-templates select="dividend/*"/>
	    </fo:table-row>

            <xsl:apply-templates select="working/row">
                <xsl:with-param name="cols" select="$cols"/>
	    </xsl:apply-templates>

	    <xsl:if test="$hints_below='true'">
		<fo:table-row keep-with-previous="always">
		    <fo:table-cell number-columns-spanned="4 + {$cols}">
			<fo:block line-height="{$problem-line-height} div 4"
                            space-after="{$problem-space-after} div 4"
                            margin-bottom="{$problem-space-after} div 4"/>
		    </fo:table-cell>
		</fo:table-row>

		<fo:table-row keep-with-previous="always">
		    <!-- Problem number and spacer columns -->
		    <fo:table-cell number-columns-spanned="2">
                        <fo:block></fo:block>
                    </fo:table-cell>

                    <fo:table-cell number-columns-spanned="2 + {$cols}">
			<xsl:apply-templates select="hints" mode="below">
			    <xsl:with-param name="width">
				<xsl:value-of
				    select="$divwidth * 5 + 5 + $cols * 10"/>
			    </xsl:with-param>
			</xsl:apply-templates>
		    </fo:table-cell>
		</fo:table-row>
	    </xsl:if>
        </fo:table-body>
    </fo:table>
</xsl:template>

<xsl:template match="hints" mode="side">
    <xsl:param name="rows">1</xsl:param>

    <fo:table-cell number-rows-spanned="{$rows}">
        <fo:block></fo:block>
    </fo:table-cell>
    <fo:table-cell number-rows-spanned="{$rows}">
	<fo:table table-layout="fixed" width="100%">
	    <fo:table-column column-width="{$problem-field-width} * 1.5"/>

	    <fo:table-body>
		<xsl:for-each select="hint">
		    <fo:table-row>
			<fo:table-cell>
			    <fo:block font-size="{$font-size-normal} div 2"
                                line-height="{$font-size-small}"
                                space-after.optimum="0pt"
                                text-align="start">
				<xsl:value-of select="text()"/>
			    </fo:block>
			</fo:table-cell>
		    </fo:table-row>
		</xsl:for-each>
	    </fo:table-body>
	</fo:table>
    </fo:table-cell>
</xsl:template>

<xsl:template match="hints" mode="below">
    <xsl:param name="width">40</xsl:param>

    <fo:table table-layout="fixed" width="100%">
	<fo:table-column column-width="{$width} div 4 * 1mm"
            number-columns-repeated="4"/>

	<fo:table-body>
	    <fo:table-row>
		<fo:table-cell><xsl:apply-templates select="./hint[1]"/></fo:table-cell>
		<fo:table-cell><xsl:apply-templates select="./hint[2]"/></fo:table-cell>
		<fo:table-cell><xsl:apply-templates select="./hint[3]"/></fo:table-cell>
		<fo:table-cell><xsl:apply-templates select="./hint[4]"/></fo:table-cell>
	    </fo:table-row>
	    <fo:table-row>
		<fo:table-cell><xsl:apply-templates select="./hint[5]"/></fo:table-cell>
		<fo:table-cell><xsl:apply-templates select="./hint[6]"/></fo:table-cell>
		<fo:table-cell><xsl:apply-templates select="./hint[7]"/></fo:table-cell>
		<fo:table-cell><xsl:apply-templates select="./hint[8]"/></fo:table-cell>
	    </fo:table-row>
	</fo:table-body>
    </fo:table>
</xsl:template>

<xsl:template match="hint">
    <fo:block font-size="{$font-size-normal} div 2"
        line-height="{$font-size-small}" space-after.optimum="0pt"
        text-align="start">
	<xsl:value-of select="text()"/>
    </fo:block>
</xsl:template>

<xsl:template match="row">
    <xsl:param name="cols"></xsl:param>

    <xsl:apply-templates select="bigend">
        <xsl:with-param name="cols" select="$cols"/>
    </xsl:apply-templates>
    <xsl:apply-templates select="subend">
        <xsl:with-param name="cols" select="$cols"/>
        <xsl:with-param name="remainder"
            select="count(./following-sibling::row)=0"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="bigend">
    <xsl:param name="cols"></xsl:param>

    <xsl:variable name="length">
        <xsl:value-of select="count(./digit)"/>
    </xsl:variable>
    <xsl:variable name="pad">
	<xsl:value-of select="$cols - $length - @shift"/>
    </xsl:variable>

    <fo:table-row keep-with-previous="always">
	<!-- Problem number, spacer, Divisor and 'Bracket' columns -->
	<fo:table-cell number-columns-spanned="4">
            <fo:block></fo:block>
        </fo:table-cell>
	<xsl:if test="$pad>0">
	    <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>

	<xsl:apply-templates select="./digit" mode="blank"/>

	<xsl:if test="@shift>0">
	    <fo:table-cell number-columns-spanned="{@shift}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>
    </fo:table-row>

    <!-- Row for rule beneath the bigend -->
    <fo:table-row keep-with-previous="always">
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
	    <fo:block line-height="{$problem-line-height} div 4"
                space-after="{$problem-space-after} div 4">
		<fo:leader leader-pattern="rule"
                    leader-length="{$length} * {$problem-field-width}"
                    rule-thickness="0.5mm"/>
	    </fo:block>
	</fo:table-cell>
	<xsl:if test="@shift>0">
	    <fo:table-cell number-columns-spanned="{@shift}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>
    </fo:table-row>
</xsl:template>

<xsl:template match="subend">
    <xsl:param name="cols"></xsl:param>
    <xsl:param name="remainder">false</xsl:param>

    <xsl:variable name="length">
	<xsl:value-of select="count(./digit)"/>
    </xsl:variable>
    <xsl:variable name="pad">
	<xsl:value-of select="$cols - $length - @shift"/>
    </xsl:variable>

    <fo:table-row keep-with-previous="always">
	<!-- Problem number and spacer columns -->
	<fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>

	<xsl:choose>
	    <xsl:when test="$remainder='true'">
		<fo:table-cell number-columns-spanned="2 + {$pad}">
		    <fo:block font-size="{$font-size-tiny}" font-style="italic"
                        line-height="{$problem-line-height}"
                        text-align="end" margin-right="2pt">Remainder</fo:block>
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

	<xsl:apply-templates select="./digit" mode="blank"/>

	<xsl:if test="@shift>0">
	    <fo:table-cell number-columns-spanned="{@shift}">
                <fo:block></fo:block>
            </fo:table-cell>
	</xsl:if>
    </fo:table-row>
</xsl:template>

<xsl:template match="quotient">
    <xsl:param name="cols"></xsl:param>
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit)"/></xsl:variable>

    <xsl:if test="$pad>0">
	<fo:table-cell number-columns-spanned="{$pad}">
            <fo:block></fo:block>
        </fo:table-cell>
    </xsl:if>

    <xsl:apply-templates select="digit" mode="blank"/>
</xsl:template>

<xsl:template match="digit">
    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-large}"
            line-height="{$problem-line-height}" text-align="center">
            <xsl:value-of select="text()"/>
        </fo:block>
    </fo:table-cell>
</xsl:template>

<xsl:template match="digit" mode="blank">
    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-large}"
            line-height="{$problem-line-height}" text-align="center"
            border-width="0.5mm" border-style="solid"
            border-color="gray" padding-left="-2pt" padding-right="-2pt"
            margin-top="{$problem-space-after} div 6">
            <xsl:choose>
                <xsl:when test="$show-answers='true' or ancestor::WorkSheet[@type]='answer'">
                    <fo:block color="gray">
                        <xsl:value-of select="text()"/>
                    </fo:block>
                </xsl:when>
                <xsl:otherwise>
                    <fo:character character="?" color="white"/>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
    </fo:table-cell>
</xsl:template>

</xsl:stylesheet>
