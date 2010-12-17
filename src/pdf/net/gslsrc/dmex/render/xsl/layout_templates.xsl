<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
		xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<xsl:template name="FOOTER">
    <xsl:param name="width"/>
    <xsl:param name="sheet"/>

    <fo:static-content flow-name="xsl-region-after">
	<fo:block>
	    <fo:leader leader-pattern="rule"
		       leader-length="{$width}"
		       rule-thickness="0.5mm"/>
	</fo:block>
	<fo:block>
	    <fo:inline font-size="{$font-size-normal}"
		       font-weight="bold">
		Page <fo:page-number/>
	    </fo:inline>
	    <fo:inline font-style="italic">
		<xsl:value-of select="$sheet/@title"/>
	    </fo:inline>
	    <xsl:if test="$sheet/@name!=''">
		<fo:inline>
		    <xsl:text> - </xsl:text>
		    <xsl:value-of select="$sheet/@name"/>
		</fo:inline>
	    </xsl:if>
	</fo:block>
    </fo:static-content>
</xsl:template>

<xsl:template name="HEADER">
    <xsl:param name="width"/>
    <xsl:param name="title"/>

    <fo:static-content flow-name="xsl-region-before">
	<fo:block>
            <fo:table table-layout="fixed" width="100%">
                <fo:table-column column-width="{$header-column-left}"/>
                <fo:table-column column-width="{$width} - {$header-column-right} - {$header-column-left}"/>
                <fo:table-column column-width="{$header-column-right}"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell display-align="center">
                            <fo:block text-align="start"
                                    font-size="{$font-size-normal}">
                                DaisyMaths
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align="center"
                                    display-align="center"
                                    font-size="{$font-size-title}">
                                <xsl:value-of select="$title"/>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell display-align="center">
                            <fo:block font-size="{$font-size-small}"
                                      text-align="end">
                                <xsl:value-of select="$sponsor-text"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
	</fo:block>
	<fo:block>
	    <fo:leader leader-pattern="rule"
		       leader-length="{$body-width}"
		       rule-thickness="0.5mm"/>
	</fo:block>
    </fo:static-content>
</xsl:template>

<xsl:template match="prompt">
    <fo:block font-size="{$font-size-large}"
              font-style="italic"
              line-height="{$problem-line-height}"
              padding-top="3pt"
              space-after.optimum="{$problem-space-after}"
              span="all">
        <xsl:value-of select="text()"/>
    </fo:block>
</xsl:template>

</xsl:stylesheet>
