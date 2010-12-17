<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
		xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<!-- Multi-column layout with empty right margin.  Used to generate blank
     problem sheet or completed answer sheet.  Defaults to two columns but
     this can be overridden by setting the "body-column-count" variable. -->

<xsl:import href="a4.xsl"/>
<xsl:import href="layout_templates.xsl"/>

<!-- not strictly necessary as we now use select="problems" -->
<xsl:strip-space elements="WorkSheet"/>

<xsl:variable name="body-width"><xsl:value-of select="$page-width"/> - <xsl:value-of select="$page-margin-right"/> - <xsl:value-of select="$page-margin-left"/></xsl:variable>

<xsl:output method="xml" indent="yes" omit-xml-declaration="no"/>

<xsl:template match="WorkSheet">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<fo:layout-master-set>
	    <fo:simple-page-master master-name="simple"
				   page-height="{$page-height}"
				   page-width="{$page-width}"
				   margin-top="{$page-margin-top}"
				   margin-bottom="{$page-margin-bottom}"
				   margin-left="{$page-margin-left}"
				   margin-right="{$page-margin-right}">
		<fo:region-body margin-top="{$body-margin-top}"
				margin-bottom="{$body-margin-bottom}"
				column-count="{$body-column-count}"
				column-gap="{$body-column-gap}"/>
		<fo:region-before extent="{$before-extent}"/>
		<fo:region-after extent="{$after-extent}"/>
	    </fo:simple-page-master>
	</fo:layout-master-set>

	<fo:page-sequence master-reference="simple">
	    <xsl:call-template name="FOOTER">
                <xsl:with-param name="width" select="$body-width"/>
                <xsl:with-param name="sheet" select="."/>
            </xsl:call-template>

	    <xsl:call-template name="HEADER">
                <xsl:with-param name="width" select="$body-width"/>
                <xsl:with-param name="title" select="@title"/>
            </xsl:call-template>

	    <!-- Body -->
	    <fo:flow flow-name="xsl-region-body">
		<xsl:apply-templates select="name_date"/>
		<xsl:apply-templates select="prompt"/>
		<xsl:apply-templates select="problem"/>
	    </fo:flow>
	</fo:page-sequence>
    </fo:root>
</xsl:template>

</xsl:stylesheet>
