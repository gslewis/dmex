<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<xsl:template match="problem">
    <xsl:param name="number" select="position()"/>

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

    <fo:table span="none" table-layout="fixed" width="100%"
        line-height="{$problem-line-height} * 1.85"
        space-after.optimum="{$problem-space-after} * 0.8"
        border-collapse="separate">
	<fo:table-column column-width="{$problem-number-width}"/>
        <fo:table-column column-width="{$problem-spacer-width}"/>
        <fo:table-column column-width="{$problem-field-width}"
            number-columns-repeated="{$cols}"/>

        <fo:table-body>
            <xsl:apply-templates>
                <xsl:with-param name="number" select="$number"/>
                <xsl:with-param name="cols" select="$cols"/>
            </xsl:apply-templates>
        </fo:table-body>
    </fo:table>
</xsl:template>

<xsl:template match="multiply">
    <xsl:param name="number">??</xsl:param>
    <xsl:param name="cols"></xsl:param>

    <xsl:apply-templates>
        <xsl:with-param name="number" select="$number"/>
        <xsl:with-param name="cols" select="$cols"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="working">
    <xsl:param name="cols"></xsl:param>

    <xsl:apply-templates select="row">
        <xsl:with-param name="cols" select="$cols"/>
    </xsl:apply-templates>
</xsl:template>

<xsl:template match="multiplicand">
    <xsl:param name="number">??</xsl:param>
    <xsl:param name="cols"></xsl:param>
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit)"/></xsl:variable>

    <fo:table-row>
        <fo:table-cell display-align="center">
            <fo:block font-size="{$font-size-small}" font-family="serif"
                font-style="italic" line-height="{$problem-line-height}"
                text-align="end">
                <xsl:value-of select="$number"/>.
            </fo:block>
        </fo:table-cell>
        <!-- spacer -->
        <fo:table-cell><fo:block></fo:block></fo:table-cell>
        <!-- padding -->
        <xsl:if test="$pad>0">
            <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
        </xsl:if>
        <!-- digits -->
        <xsl:apply-templates select="digit"/>
    </fo:table-row>
</xsl:template>

<xsl:template match="multiplier">
    <xsl:param name="cols"></xsl:param>
    <!-- multiplier row includes "times" operator so padding is 1 less -->
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit) - 1"/></xsl:variable>

    <fo:table-row keep-with-previous="always">
        <!-- number & spacer -->
        <fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
        <!-- padding -->
        <xsl:if test="$pad>0">
            <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
        </xsl:if>
        <!-- operator -->
        <fo:table-cell display-align="center">
            <fo:block font-size="{$font-size-large}"
                line-height="{$problem-line-height}"
                text-align="center">x</fo:block>
        </fo:table-cell>
        <!-- digits -->
        <xsl:apply-templates/>
    </fo:table-row>
    <fo:table-row keep-with-previous="always">
        <fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
        <fo:table-cell number-columns-spanned="{$cols}">
            <fo:block line-height="{$problem-line-height} div 4"
		      space-after="{$problem-space-after} div 4">
                <fo:leader leader-pattern="rule"
                           leader-length="{$cols} * {$problem-field-width}"
                           rule-thickness="0.5mm"/>
            </fo:block>
        </fo:table-cell>
    </fo:table-row>
</xsl:template>

<xsl:template match="row">
    <xsl:param name="cols"></xsl:param>
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit)"/></xsl:variable>
    <xsl:variable name="digits" select="count(./digit)"/>

    <fo:table-row keep-with-previous="always">
        <!-- number and spacer -->
        <fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
        <!-- padding -->
        <xsl:if test="$pad>0">
            <fo:table-cell number-columns-spanned="{$pad}">
                <fo:block></fo:block>
            </fo:table-cell>
        </xsl:if>

        <xsl:apply-templates select="digit" mode="blank">
            <!-- The number of trailing zeros of the working row (ie., the
            least digit of the multiplier has no trailing zeros, the 2nd least
            digit (tens) has one trailing zero, the third least digit
            (hundreds) has two trailing zeros, etc.). -->
            <xsl:with-param name="zeros" select="position() - 1"/>
            <!-- Supply the number of digits in the working row. -->
            <xsl:with-param name="length" select="$digits"/>
        </xsl:apply-templates>
    </fo:table-row>

    <fo:table-row keep-with-previous="always">
        <fo:table-cell number-columns-spanned="2 + {$cols}">
            <fo:block line-height="{$problem-line-height} div 4"
		      space-after="{$problem-space-after} div 4"/>
        </fo:table-cell>
    </fo:table-row>
</xsl:template>

<xsl:template match="answer">
    <xsl:param name="cols"></xsl:param>
    <xsl:variable name="pad"><xsl:value-of select="$cols - count(./digit)"/></xsl:variable>

    <fo:table-row keep-with-previous="always">
        <fo:table-cell number-columns-spanned="2">
            <fo:block></fo:block>
        </fo:table-cell>
        <fo:table-cell number-columns-spanned="{$cols}">
            <fo:block line-height="{$problem-line-height} div 4"
                space-after="{$problem-space-after} div 4">
                <fo:leader leader-pattern="rule"
                    leader-length="{$cols} * {$problem-field-width}"
                    rule-thickness="0.5mm"/>
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

        <xsl:apply-templates select="digit" mode="blank"/>
    </fo:table-row>
</xsl:template>

<!-- Displays a single digit.  Used for the multiplicand and multiplier. -->
<xsl:template match="digit">
    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-large}"
            line-height="{$problem-line-height}"
            text-align="center">
            <xsl:value-of select="text()"/>
        </fo:block>
    </fo:table-cell>
</xsl:template>

<!-- Displays a digit box.  Used for working rows and the answer.  The box may
be blank or contain a digit.  It will contain a digit if answers are being
shown or if the digit is a trailing zero of a working row. -->
<xsl:template match="digit" mode="blank">
    <!-- The number of trailing zeros to reveal. -->
    <xsl:param name="zeros">0</xsl:param>
    <!-- The length of the value to which this digit belongs. -->
    <xsl:param name="length">999</xsl:param>

    <fo:table-cell display-align="center">
        <fo:block font-size="{$font-size-large}"
            line-height="{$problem-line-height}" text-align="center"
            border-width="0.5mm" border-style="solid" border-color="gray"
            padding-left="-2pt" padding-right="-2pt"
            margin-top="{$problem-space-after} div 5">
            <xsl:choose>
                <xsl:when test="$length - position() &lt; $zeros">
                    <xsl:value-of select="text()"/>
                </xsl:when>
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
