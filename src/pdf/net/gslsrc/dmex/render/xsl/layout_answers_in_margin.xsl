<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<!--
Page layout for answers in right-hand margin.

This requires that the owner stylesheet provides a "WorkSheet" template in
"layout" mode:

    <xsl:template match="WorkSheet" mode="layout">

that handles the layout of the problems within the region-body space provided
by this layout.
-->

<xsl:import href="a4.xsl"/>
<xsl:import href="layout_templates.xsl"/>

<xsl:variable name="body-width"><xsl:value-of select="$page-width"/> - <xsl:value-of select="$page-margin-right"/> - <xsl:value-of select="$page-margin-left"/> - <xsl:value-of select="$spacer"/></xsl:variable>

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
		   margin-right="{$page-margin-right} - {$answer-column-width}">
		<fo:region-body margin-top="{$body-margin-top}"
				margin-bottom="{$body-margin-bottom}"/>
		<fo:region-before extent="{$before-extent}"/>
		<fo:region-after extent="{$after-extent}"/>
	    </fo:simple-page-master>

	    <fo:page-sequence-master master-name="single">
		<fo:repeatable-page-master-reference master-reference="simple"/>
	    </fo:page-sequence-master>
	</fo:layout-master-set>

	<fo:page-sequence master-reference="single">
	    <!-- Footer -->
	    <xsl:call-template name="FOOTER">
		<xsl:with-param name="width" select="$body-width"/>
		<xsl:with-param name="sheet" select="."/>
	    </xsl:call-template>

	    <!-- Header -->
	    <xsl:call-template name="HEADER">
		<xsl:with-param name="width" select="$body-width"/>
		<xsl:with-param name="title" select="@title"/>
	    </xsl:call-template>

	    <!-- Body -->
	    <fo:flow flow-name="xsl-region-body">
		<xsl:apply-templates select="name_date"/>
		<xsl:apply-templates select="prompt"/>
		<xsl:apply-templates select="." mode="layout"/>
	    </fo:flow>
	</fo:page-sequence>
    </fo:root>
</xsl:template>

</xsl:stylesheet>
