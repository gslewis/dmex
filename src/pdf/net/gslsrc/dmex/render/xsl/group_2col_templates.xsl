<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<!-- Two column layout with answers as templates in the right hand margin.

The parent template (that imports this template) must provide a "problem"
template with mode="answer":

    <xsl:template match="problem" mode="answer">

which produces content to fit in the {$answer-column-width}.
-->

<xsl:import href="layout_answers_in_margin.xsl"/>

<xsl:template match="WorkSheet" mode="layout">
    <fo:table span="none" text-align="center" table-layout="fixed" width="100%">
	<fo:table-column column-width="({$body-width}) div 2"
		number-columns-repeated="2"/>
        <fo:table-column column-width="{$spacer} * 0.2"/>
        <fo:table-column
            column-width="{$spacer} * 0.2 + {$answer-column-width}"/>

	<fo:table-body>
	    <xsl:for-each select="problem">
		<xsl:variable name="pos" select="position()"/>

		<xsl:if test="position() mod 2 != 0">
		    <fo:table-row>
			<fo:table-cell> <!-- 7.5cm -->
			    <xsl:apply-templates select=".">
                                <xsl:with-param name="number" select="$pos"/>
			    </xsl:apply-templates>
			</fo:table-cell>

			<fo:table-cell> <!-- 7.5cm -->
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
			<fo:table-cell>
			    <fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="{$spacer} * 0.2 + {$answer-column-width}"/>

				<fo:table-body>
				    <fo:table-row>
					<fo:table-cell padding-top="{$problem-line-height} div 2">
					    <xsl:apply-templates select="."
                                                mode="answer">
                                                <xsl:with-param name="number"
                                                    select="$pos"/>
					    </xsl:apply-templates>
					</fo:table-cell>
				    </fo:table-row>
				    <fo:table-row>
					<fo:table-cell padding-top="{$problem-line-height} div 2">
                                            <xsl:choose>
                                                <xsl:when test="count(following-sibling::problem)>0">
                                                    <xsl:apply-templates select="following-sibling::problem[1]" mode="answer">
                                                        <xsl:with-param name="number" select="$pos+1"/>
                                                    </xsl:apply-templates>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <fo:block></fo:block>
                                                </xsl:otherwise>
                                            </xsl:choose>
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
