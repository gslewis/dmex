<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<!-- Two column layout with answers as values in the right hand margin. -->

<xsl:import href="layout_answers_in_margin.xsl"/>

<xsl:template match="WorkSheet" mode="layout">
    <fo:table line-height="{$problem-line-height} * 1.85"
	      space-after="{$problem-space-after} * 0.8"
              span="none" text-align="center"
              table-layout="fixed" width="100%">
	<fo:table-column column-width="({$body-width}) div 2"
		number-columns-repeated="2"/>
	<fo:table-column column-width="{$spacer} * 0.6"/>
        <fo:table-column
            column-width="{$spacer} * 0.4 + {$answer-column-width}"/>

	<fo:table-body>
	    <xsl:for-each select="problem">
		<xsl:variable name="pos">
		    <xsl:value-of select="position()"/>
		</xsl:variable>

		<xsl:if test="position() mod 2 != 0">
		    <xsl:variable name="a1">
			<xsl:value-of select=".//*[@blank='true']/text()"/>
		    </xsl:variable>
		    <fo:table-row>
			<fo:table-cell display-align="center">
			    <xsl:apply-templates select=".">
                                <xsl:with-param name="number" select="$pos"/>
			    </xsl:apply-templates>
			</fo:table-cell>
			<fo:table-cell display-align="center">
                            <xsl:choose>
                                <xsl:when test="count(following-sibling::problem)>0">
                                    <xsl:apply-templates
                                        select="following-sibling::problem[1]">
                                        <xsl:with-param name="number"
                                            select="$pos+1"/>
                                    </xsl:apply-templates>
                                </xsl:when>
                                <xsl:otherwise>
                                    <fo:block></fo:block>
                                </xsl:otherwise>
                            </xsl:choose>
			</fo:table-cell>
                        <fo:table-cell><fo:block></fo:block></fo:table-cell>
			<fo:table-cell display-align="center">
			    <fo:table table-layout="fixed" width="100%">
				<fo:table-column
                                    column-width="{$answer-column-width} * 0.55"
                                    number-columns-repeated="2"/>
				<fo:table-body>
				    <fo:table-row>
					<fo:table-cell>
					    <fo:block font-size="{$font-size-tiny}">
						<fo:inline font-style="italic"
							   font-family="serif">
						<xsl:value-of select="$pos"/>
						</fo:inline>.
						<xsl:value-of select="$a1"/>
					    </fo:block>
					</fo:table-cell>
					<fo:table-cell>
					    <fo:block font-size="{$font-size-tiny}">
						<xsl:if test="count(following-sibling::problem)>0">
						    <xsl:text> </xsl:text>
						    <fo:inline font-style="italic" font-family="serif">
							<xsl:value-of select="$pos+1"/>
						    </fo:inline>.
						    <xsl:value-of select="following-sibling::problem[1]//*[@blank='true']/text()"/>
						</xsl:if>
					    </fo:block>
					</fo:table-cell>
				    </fo:table-row>
				</fo:table-body>
			    </fo:table>
			</fo:table-cell>
		    </fo:table-row>
		</xsl:if>
	    </xsl:for-each>
	</fo:table-body>
    </fo:table>
</xsl:template>

</xsl:stylesheet>
